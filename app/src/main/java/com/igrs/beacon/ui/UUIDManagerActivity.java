package com.igrs.beacon.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
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
import com.igrs.beacon.model.data.UUIDBean;
import com.igrs.beacon.ui.adapter.UUIDListAdapter;
import com.igrs.beacon.util.ToastUtil;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import greendao.GreenDaoHelper;

/**
 * Created by jianw on 17-12-3.
 */

public class UUIDManagerActivity extends BaseActivity {

    public static final int REQUEST_RESULT = 101;
    public static final String UUID_KEY = "uuid";
    @BindView(R.id.recycle_view) RecyclerView recycleView;
    @BindView(R.id.tool_bar) Toolbar toolBar;
    private TextInputLayout nameLayout, uuidLayout;
    private List<UUIDBean> mDatas;

    private UUIDListAdapter mAdapter;

    public static void show(Activity context) {
        context.startActivityForResult(new Intent(context, UUIDManagerActivity.class),
                REQUEST_RESULT);
    }

    public static String getResult(Context context, int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESULT) {
            switch (resultCode) {
                case RESULT_CANCELED:
                    ToastUtil.ToastShort(context, "未选择uuid");
                    return null;
                case RESULT_OK:
                    return data.getStringExtra(UUIDManagerActivity.UUID_KEY);
                default:
                    break;
            }
        }
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_uuid_manager);
        ButterKnife.bind(this);

        initToolBar(toolBar, true, "UUID管理");
        toolBar.setSubtitle("Tip:长按可以删除单条记录");
        initRecyclerView();
        initEvent();
        loadDbData();
    }

    private void loadDbData() {
        mDatas = GreenDaoHelper.getDaoSession().getUUIDBeanDao().loadAll();
        mAdapter.replaceData(mDatas);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new UUIDListAdapter(R.layout.item_uuid_manager);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setHasFixedSize(true);
        recycleView.addItemDecoration(
                new DividerItemDecoration(this, layoutManager.getOrientation()));
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
                intent.putExtra(UUID_KEY, mAdapter.getData().get(position).uuid);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

                //数据库中删除
                GreenDaoHelper.getDaoSession()
                        .getUUIDBeanDao()
                        .delete(mAdapter.getData().get(position));
                mAdapter.remove(position);
                return true;
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
                nameLayout = view.findViewById(R.id.input_layout_name);
                uuidLayout = view.findViewById(R.id.input_layout_uuid);
                final AlertDialog alertDialog =
                        new AlertDialog.Builder(UUIDManagerActivity.this).setTitle("增加白名单")
                                .setView(view)
                                .setPositiveButton("添加", null)
                                .create();
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String nameStr = name.getText().toString().trim();
                                String uuidStr = uuid.getText().toString().trim();
                                if (checkUUID(nameStr, uuidStr)) {
                                    UUIDBean uuid1 = new UUIDBean(null, nameStr, uuidStr);
                                    GreenDaoHelper.getDaoSession().getUUIDBeanDao().insert(uuid1);
                                    mAdapter.addData(uuid1);
                                    alertDialog.dismiss();
                                }
                            }
                        });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkUUID(String name, String uuid) {
        if (TextUtils.isEmpty(name)) {
            nameLayout.setError("必填");
            return false;
        }

        if (TextUtils.isEmpty(uuid)) {
            uuidLayout.setError("必填");
            return false;
        }
        List<UUIDBean> data = mAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).name.equals(name)) {
                nameLayout.setError("名称已存在");
                return false;
            }
        }

        if (!isUUID(uuid)) {
            uuidLayout.setError("格式不对");
            return false;
        }
        return true;
    }

    private boolean isUUID(String uuid) {
        String reg = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
        return Pattern.matches(reg, uuid);
    }
}
