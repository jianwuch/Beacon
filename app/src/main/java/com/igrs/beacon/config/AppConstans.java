package com.igrs.beacon.config;

/**
 * Created by jianw on 17-12-5.
 */

public class AppConstans {

    public static final String DEFAULT_PASSWORD = "147852369000";
    public static final String[] BLE_TX_POWER_LIST = {
            "0x570900:-21db", "0x570901:18db", "0x570902:-16db", "0x570903:-12db", "0x570904:-9db",
            "0x570905:-6db", "0x570906:-3db", "0x570907:0db", "0x570908:1db", "0x570909:2db",
            "0x57090a:3db", "0x57090b:4db", "0x57090c:5db"
    };

    public static final int[] BLE_TX_POWER_int = {
            -21, -18, -16, -12, -9, -6, -3, 0, 1, 2, 3, 4, 5
    };

    public interface SharedPreferencesKey {
        String ENABLE_UUID = "enableUUID";
        String ENABLE_MAJOR = "enableMajor";
        String ENABLE_MINOR = "enableMinor";

        String FILTER_UUID = "uuid";

        String MAJOR_FROM = "major_from";
        String MAJOR_TO = "major_to";

        String MINOR_FROM = "minor_from";
        String MINOR_TO = "minor_to";

        String PASS_WORD = "password";
    }

    public interface UUID_STR {
        String SERVER_UUID = "0000fff0-0000-1000-8000-00805f9b34fb";
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
        String BLE_TX_POWER = "09";
        String CHANGE_PASS_WORD = "0A";
    }
}
