package com.igrs.beacon.model.data;

import android.bluetooth.BluetoothDevice;
import com.inuker.bluetooth.library.search.SearchResult;

/**
 * Created by jove.chen on 2017/11/10.
 */

public class BleBeacon extends SearchResult {
    private String major;
    private String minor;

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public BleBeacon(BluetoothDevice bluetoothDevice, int riis, byte[] scanRecord) {
        super(bluetoothDevice, riis, scanRecord);
    }
}
