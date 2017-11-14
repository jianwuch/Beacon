package com.igrs.beacon.moudle.data;

import android.bluetooth.BluetoothDevice;

/**
 * Created by jove.chen on 2017/11/10.
 */

public class BleBeacon extends ScanLeDevice {
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

    public BleBeacon() {
    }
}
