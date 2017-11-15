package com.igrs.beacon;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.igrs.beacon.base.BaseActivity;
import com.igrs.beacon.moudle.data.BleBeacon;
import com.igrs.beacon.ui.adapter.ScanBleAdapter;
import com.igrs.beacon.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements ActionMode.Callback {
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.by_rssi)
    RadioButton byRssi;
    @BindView(R.id.by_name)
    RadioButton byName;
    @BindView(R.id.radio)
    RadioGroup radio;
    @BindView(R.id.batch_type)
    View batchType;

    private ScanBleAdapter mAdapter;
    private List<BleBeacon> mDatas;
    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolBar);

        initRecycleView();
        loadData();
        initEvent();
    }

    private void loadData() {
        for (int i = 0; i < 10; i++) {
            mDatas.add(new BleBeacon());
        }
    }

    private void initRecycleView() {
        mDatas = new ArrayList<>();
        mAdapter = new ScanBleAdapter(R.layout.scan_device_info, mDatas);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);
        recycleView.addItemDecoration(
                new DividerItemDecoration(MainActivity.this, layoutManager.getOrientation()));
        recycleView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_page_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_batch:
                ToastUtil.ToastShort(MainActivity.this, "batch");
                break;
            case R.id.action_search:
                ToastUtil.ToastShort(MainActivity.this, "二维码扫描");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initEvent() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtil.ToastShort(MainActivity.this, "点击了——:" + position);
            }
        });
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtil.ToastShort(MainActivity.this, "长按这一项了：" + position);
                if (actionMode == null) {
                    actionMode = startSupportActionMode(MainActivity.this);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        if (actionMode == null) {
            actionMode = mode;
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.choose_action_mode, menu);
            showBatchAnim(true);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_choose_all:
                ToastUtil.ToastShort(MainActivity.this, "全选");
                return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
        ToastUtil.ToastShort(MainActivity.this, "ActionMode消失");
        showBatchAnim(false);

//        positionSet.clear();
//        adapter.notifyDataSetChanged();
    }

    private void showBatchAnim(boolean toShow) {
        ObjectAnimator animator = null;
        if (toShow) {
            animator = ObjectAnimator.ofFloat(batchType, "translationY",
                    0,-batchType.getHeight());
        } else {
            animator = ObjectAnimator.ofFloat(batchType, "translationY",
                     -batchType.getHeight(), 0);
        }
        animator.start();
    }
}
