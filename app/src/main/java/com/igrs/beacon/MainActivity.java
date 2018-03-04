package com.igrs.beacon;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.igrs.beacon.model.QRScanMgr;
import com.igrs.beacon.model.data.BatchConfig;
import com.igrs.beacon.model.data.FilterConfig;
import com.igrs.beacon.model.data.iBeacon;
import com.igrs.beacon.model.dm.DeviceBatchBiz;
import com.igrs.beacon.ui.BatchConfigationActivty;
import com.igrs.beacon.ui.ConfigurationActivity;
import com.igrs.beacon.ui.CustomScanActivity;
import com.igrs.beacon.ui.FilterActivity;
import com.igrs.beacon.ui.adapter.ScanBleAdapter;
import com.igrs.beacon.ui.basemvp.BaseMvpActivity;
import com.igrs.beacon.ui.contract.MainPageContract;
import com.igrs.beacon.ui.presenter.HomePresenterByFastBle;
import com.igrs.beacon.util.LogUtil;
import com.igrs.beacon.util.ToastUtil;
import com.igrs.beacon.widget.NeedWriteInfo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseMvpActivity<List<iBeacon>, HomePresenterByFastBle>
        implements ActionMode.Callback, MainPageContract.IHomeView {
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
    @BindView(R.id.type_default)
    TextView typeDefault;
    @BindView(R.id.type_password)
    TextView typePassword;
    @BindView(R.id.type_name)
    TextView typeName;

    private ScanBleAdapter mAdapter;
    private ActionMode actionMode;
    private DeviceBatchBiz mDeviceBatchBiz;
    private static final int START_CODE_UUID = 0;
    private BatchConfig mConfig;
    private NeedWriteInfo infoView;

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
                String scanResult = intentResult.getContents();
                LogUtil.d(scanResult);

                mConfig = QRScanMgr.parseQRResult(scanResult);
                if (null != mConfig) {
                    showQRResult(scanResult);
                } else {
                    ToastUtil.ToastShort(this, "扫描的数据有误");
                }
            }
        } else if (requestCode == FilterActivity.REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_CANCELED:
                    break;
                case RESULT_OK:
                    FilterConfig config = (FilterConfig) data.getSerializableExtra(
                            FilterActivity.INTENT_RESULT_KEY);
                    presenter.setFilterConfig(config);
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
                if (actionMode == null) {
                    actionMode = startSupportActionMode(MainActivity.this);
                }
                break;
            case R.id.action_search:
                customScan();
                break;
            case R.id.action_setting:
                startActivityForResult(new Intent(this, FilterActivity.class),
                        FilterActivity.REQUEST_CODE);
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
                presenter.chooseAll();
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
                if (mConfig != null) {

                    //二维码扫描设置参数
                    List<iBeacon> list = new ArrayList<>();
                    list.add(mAdapter.getData().get(position));
                    if (mDeviceBatchBiz == null) {
                        mDeviceBatchBiz = new DeviceBatchBiz();
                    }
                    mDeviceBatchBiz.beginBatch(list, mConfig);
                    mDeviceBatchBiz.setProcessChangedLinstener(
                            new DeviceBatchBiz.ProcessChangedLinstener() {
                                @Override
                                public void onChanged(int process, String processStr) {

                                }

                                @Override
                                public void onFinished() {
                                    ToastUtil.ToastShort(MainActivity.this, "二维码修改成功");
                                    infoView.removeSelf();
                                }

                                @Override
                                public void onFailed(String errorStr) {
                                    ToastUtil.ToastShort(MainActivity.this, "二维码修改失败:" + errorStr);
                                    infoView.removeSelf();
                                }
                            });
                } else {
                    if (mAdapter.getChooseMode()) {

                        //多选模式
                        mAdapter.setChecked(position);
                    } else {

                        //跳转模式
                        ConfigurationActivity.show(MainActivity.this,
                                mAdapter.getItem(position).bleDevice);
                    }
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
                        presenter.setSort(HomePresenterByFastBle.TYPE_RSSI);
                        ToastUtil.ToastShort(MainActivity.this, "信号强度过滤");
                        break;
                    case R.id.by_name:
                        ToastUtil.ToastShort(MainActivity.this, "名称过滤");
                        presenter.setSort(HomePresenterByFastBle.TYPE_NAME);
                        break;
                }
            }
        });
    }

    @OnClick({R.id.type_default, R.id.type_name, R.id.type_password})
    public void gotoEdit(View view) {

        //// TODO: 2017/11/16 判断数据是否选择
        switch (view.getId()) {
            case R.id.type_default:
                List<iBeacon> mDatas = mAdapter.getSelectedDatas();
                if (null != mDatas && mDatas.size() > 0) {
                    BatchConfigationActivty.show(this, mDatas);
                } else {
                    ToastUtil.ToastShort(this, "未选择设备");
                }
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
    public void notifyDataSet() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
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

    private void showQRResult(String result) {
        ViewGroup vp = findViewById(Window.ID_ANDROID_CONTENT);
        if (null != infoView) {
            vp.removeView(infoView);
        }
        FrameLayout.LayoutParams lp =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM;
        infoView = new NeedWriteInfo(this);
        infoView.setReult(result);
        infoView.setId(R.id.main_qr_layout_id);
        vp.addView(infoView, lp);

        infoView.setCancelListener(new NeedWriteInfo.CancelListener() {
            @Override
            public void onCancel() {
                mConfig = null;
            }
        });
    }
}
