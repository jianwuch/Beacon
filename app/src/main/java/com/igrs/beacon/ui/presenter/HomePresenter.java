package com.igrs.beacon.ui.presenter;

import android.util.Log;

import com.igrs.beacon.moudle.data.BeaconWithCheckable;
import com.igrs.beacon.moudle.data.iBeacon;
import com.igrs.beacon.ui.contract.MainPageContract;
import com.igrs.beacon.ui.model.ble.ClientManager;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jove.chen on 2017/11/16.
 */

public class HomePresenter extends MainPageContract.IHomePresenter {
    private static final String TAG = HomePresenter.class.getSimpleName();
    public List<iBeacon> getmDatas() {
        return mDatas;
    }

    private List<iBeacon> mDatas;
    @Override
    public void start() {
        mDatas = new ArrayList<>();
        mView.showDataFromPresenter(mDatas);
        scanBeacon();
    }

    @Override
    public void setFilter(int type) {

    }

    @Override
    public void chooseAll() {

    }

    @Override
    public void beginBatch(int type) {

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
            iBeacon ibeacon = iBeacon.fromScanData(device);
            Log.d(TAG, Thread.currentThread().getName());
            if (ibeacon != null) {
                addDevice(ibeacon);
            }
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

    private void addDevice(iBeacon device) { //更新beacon信息
        if(device==null) {
            Log.d("DeviceScanActivity ", "device==null ");
            return;
        }

        for(int i=0;i<mDatas.size();i++){//相同mac地址丢弃
            String btAddress = mDatas.get(i).bluetoothAddress;
            if(btAddress.equals(device.bluetoothAddress)){
//                mDatas.add(i+1, device);
//                mDatas.remove(i);
//                break;
                return;
            }
        }

        //todo
        //排序，过滤在此操作
        Collections.sort(mDatas, new Comparator<iBeacon>() {
            @Override
            public int compare(iBeacon h1, iBeacon h2) {
                return h2.rssi - h1.rssi;
            }
        });
        mDatas.add(device);
        mView.newBeacon();
    }
}
