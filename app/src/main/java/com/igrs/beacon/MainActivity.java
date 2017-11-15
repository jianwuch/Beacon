package com.igrs.beacon;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import butterknife.BindView;
import com.igrs.beacon.base.BaseActivity;
import com.igrs.beacon.moudle.data.BleBeacon;
import com.igrs.beacon.ui.adapter.ScanBleAdapter;
import com.igrs.beacon.util.ToastUtil;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    @BindView(R.id.recycle_view) RecyclerView recycleView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tool_bar) Toolbar toolBar;
    @BindView(R.id.by_rssi) RadioButton byRssi;
    @BindView(R.id.by_name) RadioButton byName;
    @BindView(R.id.radio) RadioGroup radio;

    private ScanBleAdapter mAdapter;
    private List<BleBeacon> mDatas;

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
    }
}
