package com.igrs.beacon.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.igrs.beacon.R;
import com.igrs.beacon.base.BaseActivity;
import com.igrs.beacon.moudle.data.UUIDBean;
import com.igrs.beacon.ui.adapter.UUIDListAdapter;
import com.igrs.beacon.util.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import greendao.GreenDaoHelper;

/**
 * Created by jianw on 17-12-3.
 */

public class UUIDManagerActivity extends BaseActivity {

    public static final String UUID_KEY = "uuid";
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    private List<UUIDBean> mDatas;

    private UUIDListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_uuid_manager);
        ButterKnife.bind(this);

        initToolBar(toolBar, true, "UUDI白名单");
        initRecyclerView();
        initEvent();
        loadDbData();
    }

    private void loadDbData() {
        mDatas = GreenDaoHelper.getDaoSession().getUUIDBeanDao().loadAll();
        mAdapter.replaceData(mDatas);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mAdapter = new UUIDListAdapter(R.layout.item_uuid_manager, mDatas);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setHasFixedSize(true);
        recycleView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
        recycleView.setAdapter(mAdapter);
    }

    private void initEvent() {
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra(UUID_KEY, mDatas.get(position).uuid);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.uuid_manager_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                ToastUtil.ToastShort(UUIDManagerActivity.this, "添加");
                View view = getLayoutInflater().inflate(R.layout.dialog_add_uuid, null, false);
                final EditText name = ((EditText) view.findViewById(R.id.name));
                final EditText uuid = ((EditText) view.findViewById(R.id.uuid));
                AlertDialog alertDialog = new AlertDialog.Builder(UUIDManagerActivity.this)
                        .setTitle("增加白名单")
                        .setView(view)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String nameStr = name.getText().toString().trim();
                                String uuidStr = uuid.getText().toString().trim();
                                if (TextUtils.isEmpty(nameStr) || TextUtils.isEmpty(uuidStr)) {
                                    ToastUtil.ToastShort(UUIDManagerActivity.this, "两项必填");
                                    return;
                                }
                                UUIDBean uuid1 = new UUIDBean(nameStr, uuidStr);
                                GreenDaoHelper.getDaoSession().getUUIDBeanDao().insert(uuid1);
                                mDatas.add(uuid1);
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
