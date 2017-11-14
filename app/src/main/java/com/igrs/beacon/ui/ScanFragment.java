package com.igrs.beacon.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import com.igrs.beacon.R;
import com.igrs.beacon.base.BaseListFragment;
import com.igrs.beacon.moudle.data.BleBeacon;
import com.igrs.beacon.ui.adapter.ScanBleAdapter;

/**
 * Created by jove.chen on 2017/11/10.
 */

public class ScanFragment extends BaseListFragment<BleBeacon> {

    @Override
    protected RecyclerView.Adapter initAdapter() {
        return new ScanBleAdapter(R.layout.scan_device_info, mDatas);
    }

    @Override
    protected SwipeRefreshLayout.OnRefreshListener initRefreshListener() {
        return null;
    }

    @Override
    protected void loadData() {
        //测试代码
        for (int i = 0; i < 10; i++) {
            mDatas.add(new BleBeacon());
        }
        notifyAdapter();
    }
}
