package com.igrs.beacon.model.data;

import java.util.UUID;

/**
 * Created by jianw on 17-12-3.
 */

public class GattAttribute {
    public interface BleService{
        //写数据
        UUID uuid1 = UUID.fromString("0000FFF1-0000-1000-8000-00805f9b34fb");

        //读数据
        UUID uuid2 = UUID.fromString("0000FFF2-0000-1000-8000-00805f9b34fb");
    }
}
