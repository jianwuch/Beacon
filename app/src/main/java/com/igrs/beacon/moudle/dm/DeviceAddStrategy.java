package com.igrs.beacon.moudle.dm;

import com.igrs.beacon.moudle.data.ScanLeDevice;
import java.util.List;

/**
 * Created by jove.chen on 2017/11/3.
 * 设备添加的策略
 */

public interface DeviceAddStrategy {

    //当前的设备是否需要添加
    boolean add(List<ScanLeDevice> mScaneds, ScanLeDevice device);
}
