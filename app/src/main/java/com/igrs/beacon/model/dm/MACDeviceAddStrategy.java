package com.igrs.beacon.model.dm;

import com.igrs.beacon.model.data.ScanLeDevice;
import java.util.List;

/**
 * Created by jove.chen on 2017/11/3.
 * 相同mac adress被认为是相同的设备
 *
 */

public class MACDeviceAddStrategy implements DeviceAddStrategy {
    @Override
    public boolean add(List<ScanLeDevice> mScaneds, ScanLeDevice device) {
        for (int i = 0; i < mScaneds.size(); i++) {

            ScanLeDevice scanLeDevice = mScaneds.get(i);
            if (scanLeDevice.getBluetoothDevice()
                    .getAddress()
                    .equals(device.getBluetoothDevice().getAddress())) {
                return false;
            }
        }
        mScaneds.add(device);
        return true;
    }
}
