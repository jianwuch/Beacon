package com.igrs.beacon.config;

/**
 * Created by jianw on 17-12-5.
 */

public class AppConstans {
    public interface SharedPreferencesKey {
        String ENABLE_UUID = "enableUUID";
        String ENABLE_MAJOR = "enableMajor";
        String ENABLE_MINOR = "enableMinor";

        String FILTER_UUID = "uuid";

        String MAJOR_FROM = "major_from";
        String MAJOR_TO = "major_to";

        String MINOR_FROM = "minor_from";
        String MINOR_TO = "minor_to";
    }

    public interface UUID_STR {
        String SERVER_UUID = "0000fff1-0000-1000-8000-00805f9b34fb";
        String CHA_WRITE_UUID = "0000fff1-0000-1000-8000-00805f9b34fb";
        String CHA_READ_UUID = "0000fff2-0000-1000-8000-00805f9b34fb";
    }

    public interface RegAD {
        String PASSWORD = "01";
        String UUID = "02";
        String MAJOR = "03";
        String MINOR = "04";
        String TX_POWER = "05";
        String BLE_NAME = "06";
        String BAT = "07";
        String INTERVAL = "08";//间隔
    }
}
