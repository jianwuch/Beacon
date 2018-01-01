package com.igrs.beacon.moudle.dm;

import com.igrs.beacon.moudle.data.iBeacon;

import java.util.List;

/**
 * Created by jianw on 18-1-1.
 */

public class DeviceBatchBiz {
    private List<iBeacon> mDevices;
    private List<iBeacon> mFiledDevices;//批量失败的设备
    private List mFiledArguments;//批量失败的设备应该设置的参数保存在这里

    public void setDevices(List mdatas) {
        this.mDevices = mdatas;

        //开始遍历修改

    }
}
