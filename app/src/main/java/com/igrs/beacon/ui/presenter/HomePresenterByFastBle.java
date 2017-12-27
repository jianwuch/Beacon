package com.igrs.beacon.ui.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.igrs.beacon.MyApplication;
import com.igrs.beacon.moudle.data.iBeacon;
import com.igrs.beacon.ui.contract.MainPageContract;
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

public class HomePresenterByFastBle extends MainPageContract.IHomePresenter {
    public static final int TYPE_RSSI = 0;//默认过滤类型
    public static final int TYPE_NAME = 1;
    private static final String TAG = HomePresenterByFastBle.class.getSimpleName();
    public List<iBeacon> getmDatas() {
        return mDatas;
    }
    private BleManager bleManager;
    private List<iBeacon> mDatas;
    private int mFilterType;
    @Override
    public void setFilter(int type) {
        mFilterType = type;
    }

    @Override
    public void start() {
        mDatas = new ArrayList<>();
        mView.showDataFromPresenter(mDatas);
        bleManager = BleManager.getInstance();
        bleManager.init(MyApplication.getInstance());
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
//                .setServiceUuids(serviceUuids)      // 只扫描指定的服务的设备，可选
//                .setDeviceName(true, names)   // 只扫描指定广播名的设备，可选
//                .setDeviceMac(mac)                  // 只扫描指定mac的设备，可选
//                .setAutoConnect(isAutoConnect)      // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(10000)              // 扫描超时时间，可选，默认10秒
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
        scanBeacon();
    }

    @Override
    public void scanBeacon() {
        searchDevice();
    }

    private void searchDevice() {
        if (!BleManager.getInstance().isSupportBle()) {
            mView.showToast("不支持ble");
        }

        bleManager.enableBluetooth();

        mView.showLoading(true);
        bleManager.scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {

            }

            @Override
            public void onScanning(BleDevice result) {
                SearchResult bleDevice = new SearchResult(result.getDevice(), result.getRssi(), result.getScanRecord());
                iBeacon ibeacon = iBeacon.fromScanData(bleDevice);
                Log.d(TAG, Thread.currentThread().getName());
                if (ibeacon != null) {
                    addDevice(ibeacon);
                }
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                mView.showLoading(false);
            }
        });
    }

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

        mDatas.add(device);
        //排序，过滤在此操作
        if (mFilterType == TYPE_RSSI) {
            Collections.sort(mDatas, new Comparator<iBeacon>() {
                @Override
                public int compare(iBeacon h1, iBeacon h2) {
                    return h2.rssi - h1.rssi;
                }
            });
        } else {
            Collections.sort(mDatas, new Comparator<iBeacon>() {
                @Override
                public int compare(iBeacon h1, iBeacon h2) {
                    return (h2.name.compareTo(h1.name));
                }
            });
        }
        mView.newBeacon();
    }
}
