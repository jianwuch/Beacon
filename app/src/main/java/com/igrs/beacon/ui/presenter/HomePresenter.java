package com.igrs.beacon.ui.presenter;

import com.igrs.beacon.R;
import com.igrs.beacon.moudle.data.BleBeacon;
import com.igrs.beacon.ui.contract.MainPageContract;
import com.igrs.beacon.ui.model.ble.ClientManager;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jove.chen on 2017/11/16.
 */

public class HomePresenter extends MainPageContract.IHomePresenter {
    public List<BleBeacon> getmDatas() {
        return mDatas;
    }

    private List<BleBeacon> mDatas;
    @Override
    public void start() {
        mDatas = new ArrayList<>();
        scanBeacon();
    }

    @Override
    public void setFilter() {

    }

    @Override
    public void scanBeacon() {
        searchDevice();
    }

    private void searchDevice() {
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(5000, 2).build();

        ClientManager.getClient().search(request, mSearchResponse);
    }

    private final SearchResponse mSearchResponse = new SearchResponse() {
        @Override
        public void onSearchStarted() {
            mDatas.clear();
            mView.showLoading(true);
        }

        @Override
        public void onDeviceFounded(SearchResult device) {
            //            BluetoothLog.w("MainActivity.onDeviceFounded " + device.device.getAddress());
/*            if (!mDevices.contains(device)) {
                mDevices.add(device);
                mAdapter.setDataList(mDevices);

                //                Beacon beacon = new Beacon(device.scanRecord);
                //                BluetoothLog.v(String.format("beacon for %s\n%s", device.getAddress(), beacon.toString()));

                //                BeaconItem beaconItem = null;
                //                BeaconParser beaconParser = new BeaconParser(beaconItem);
                //                int firstByte = beaconParser.readByte(); // 读取第1个字节
                //                int secondByte = beaconParser.readByte(); // 读取第2个字节
                //                int productId = beaconParser.readShort(); // 读取第3,4个字节
                //                boolean bit1 = beaconParser.getBit(firstByte, 0); // 获取第1字节的第1bit
                //                boolean bit2 = beaconParser.getBit(firstByte, 1); // 获取第1字节的第2bit
                //                beaconParser.setPosition(0); // 将读取起点设置到第1字节处
            }

            if (mDevices.size() > 0) {
                mRefreshLayout.showState(AppConstants.LIST);
            }*/
        }

        @Override
        public void onSearchStopped() {
            BluetoothLog.w("MainActivity.onSearchStopped");
            mView.showLoading(false);
        }

        @Override
        public void onSearchCanceled() {
            BluetoothLog.w("MainActivity.onSearchCanceled");
        }
    };
}
