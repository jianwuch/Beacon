package com.igrs.beacon;

import android.animation.ObjectAnimator;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnClick;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.igrs.beacon.moudle.data.iBeacon;
import com.igrs.beacon.ui.ConfigurationActivity;
import com.igrs.beacon.ui.CustomScanActivity;
import com.igrs.beacon.ui.FilterActivity;
import com.igrs.beacon.ui.UUIDManagerActivity;
import com.igrs.beacon.ui.adapter.ScanBleAdapter;
import com.igrs.beacon.ui.basemvp.BaseMvpActivity;
import com.igrs.beacon.ui.contract.MainPageContract;
import com.igrs.beacon.ui.presenter.HomePresenterByFastBle;
import com.igrs.beacon.util.ToastUtil;
import java.util.List;

public class MainActivity extends BaseMvpActivity<List<iBeacon>, HomePresenterByFastBle>
        implements ActionMode.Callback, MainPageContract.IHomeView {
    @BindView(R.id.recycle_view) RecyclerView recycleView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tool_bar) Toolbar toolBar;
    @BindView(R.id.by_rssi) RadioButton byRssi;
    @BindView(R.id.by_name) RadioButton byName;
    @BindView(R.id.radio) RadioGroup radio;
    @BindView(R.id.batch_type) View batchType;
    @BindView(R.id.type_default) TextView typeDefault;
    @BindView(R.id.type_password) TextView typePassword;
    @BindView(R.id.type_name) TextView typeName;

    private ScanBleAdapter mAdapter;
    private ActionMode actionMode;
    private static final int START_CODE_UUID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolBar);

        initRecycleView();
        loadData();
        initEvent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult intentResult =
                IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(this, "内容为空", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "扫描成功", Toast.LENGTH_LONG).show();
                // ScanResult 为 获取到的字符串
                String ScanResult = intentResult.getContents();
                Toast.makeText(this, ScanResult, Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == START_CODE_UUID) {
            switch (resultCode) {
                case RESULT_CANCELED:
                    ToastUtil.ToastShort(this, "取消");
                    break;
                case RESULT_OK:
                    String uuid = data.getStringExtra(UUIDManagerActivity.UUID_KEY);
                    ToastUtil.ToastShort(this, uuid);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public HomePresenterByFastBle initPresenter() {
        return new HomePresenterByFastBle();
    }

    private void loadData() {
        presenter.start();
    }

    private void initRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);
        recycleView.addItemDecoration(
                new DividerItemDecoration(MainActivity.this, layoutManager.getOrientation()));
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
                customScan();
                break;
            case R.id.action_setting:
                ToastUtil.ToastShort(MainActivity.this, "设置过滤");
                //                startActivityForResult(new Intent(MainActivity.this, UUIDManagerActivity.class), START_CODE_UUID);
                startActivity(new Intent(this, FilterActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        if (actionMode == null) {
            showActionMode(true, mode, menu);
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
        showActionMode(false, mode, null);
    }

    private void showBatchAnim(boolean toShow) {
        ObjectAnimator animator = null;
        if (toShow) {
            animator = ObjectAnimator.ofFloat(batchType, "translationY", 0, -batchType.getHeight());
        } else {
            animator = ObjectAnimator.ofFloat(batchType, "translationY", -batchType.getHeight(), 0);
        }
        animator.start();
    }

    private void initEvent() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mAdapter.getChooseMode()) {

                    //多选模式
                    mAdapter.setChecked(position);
                } else {

                    //跳转模式
                    ConfigurationActivity.show(MainActivity.this,
                            mAdapter.getItem(position).bleDevice);
                }
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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.scanBeacon();
            }
        });

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.by_rssi:
                        presenter.setFilter(HomePresenterByFastBle.TYPE_RSSI);
                        ToastUtil.ToastShort(MainActivity.this, "信号强度过滤");
                        break;
                    case R.id.by_name:
                        ToastUtil.ToastShort(MainActivity.this, "名称过滤");
                        presenter.setFilter(HomePresenterByFastBle.TYPE_NAME);
                        break;
                }
            }
        });
    }

    @OnClick({ R.id.type_default, R.id.type_name, R.id.type_password })
    public void gotoEdit(View view) {

        //// TODO: 2017/11/16 判断数据是否选择
        switch (view.getId()) {
            case R.id.type_default:
                ToastUtil.ToastShort(MainActivity.this, "默认");
                break;
            case R.id.type_name:
                ToastUtil.ToastShort(MainActivity.this, "名称");
                break;
            case R.id.type_password:
                ToastUtil.ToastShort(MainActivity.this, "密码");
                break;
        }
        showActionMode(false, actionMode, null);
    }

    private void showActionMode(boolean isEditMode, ActionMode mode, Menu menu) {
        if (isEditMode) {
            actionMode = mode;
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.choose_action_mode, menu);
            mAdapter.setChooseMode(true);
        } else {
            actionMode.finish();
            actionMode = null;
            mAdapter.setChooseMode(false);
        }
        showBatchAnim(isEditMode);
    }

    @Override
    public void newBeacon() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showDataFromPresenter(List<iBeacon> data) {
        if (mAdapter == null) {
            mAdapter = new ScanBleAdapter(R.layout.item_scan_device_info, data);
            recycleView.setAdapter(mAdapter);
        }

        if (mAdapter.getData() == null) {
            mAdapter.setNewData(data);
        }
    }

    @Override
    public void loadMorePage() {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void showLoading(boolean isShow) {
        super.showLoading(isShow);
        if (swipeRefreshLayout != null && !isShow) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    // 你也可以使用简单的扫描功能，但是一般扫描的样式和行为都是可以自定义的，这里就写关于自定义的代码了
    // 你可以把这个方法作为一个点击事件
    public void customScan() {
        new IntentIntegrator(this).setOrientationLocked(false)
                .setCaptureActivity(CustomScanActivity.class) // 设置自定义的activity是CustomActivity
                .initiateScan(); // 初始化扫描
    }
}
