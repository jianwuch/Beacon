package com.igrs.beacon.ui.model.ble;

import com.igrs.beacon.MyApplication;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.BluetoothContext;

public class ClientManager {

    private static BluetoothClient mClient;

    public static BluetoothClient getClient() {
        if (mClient == null) {
            synchronized (ClientManager.class) {
                if (mClient == null) {
                    mClient = new BluetoothClient(BluetoothContext.get());
                }
            }
        }
        return mClient;
    }
}
