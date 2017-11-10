package com.igrs.beacon.moudle.data;

import android.bluetooth.BluetoothDevice;
import java.util.Arrays;

/**
 * Created by jove.chen on 2017/10/31.
 */

public class ScanLeDevice {
    private BluetoothDevice bluetoothDevice;
    private int riis;
    private byte[] scanRecord;

    public ScanLeDevice(BluetoothDevice bluetoothDevice, int riis, byte[] scanRecord) {
        this.bluetoothDevice = bluetoothDevice;
        this.riis = riis;
        this.scanRecord = scanRecord;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public int getRiis() {
        return riis;
    }

    public void setRiis(int riis) {
        this.riis = riis;
    }

    public byte[] getScanRecord() {
        return scanRecord;
    }

    public void setScanRecord(byte[] scanRecord) {
        this.scanRecord = scanRecord;
    }

    @Override
    public String toString() {
        return "ScanLeDevice{"
                + "bluetoothDevice="
                + bluetoothDevice +"\n"
                + ", riis="
                + riis +"db" + "\n"
                + ", scanRecord="
                + Arrays.toString(scanRecord) + "\n"
                + '}';
    }
}
