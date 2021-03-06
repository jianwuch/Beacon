package com.igrs.beacon.config;

/**
 * Created by jianw on 17-12-5.
 */

public class AppConstans {

    public static String DEFAULT_PASSWORD = "147852369000";
    public static final String[] BLE_TX_POWER_LIST = {
            "-21db", "-18db", "-16db", "-12db", "-9db", "-6db", "-3db", "0db", "1db", "2db", "3db",
            "4db", "5db"
    };

    public static final String[] BLE_TX_POWER_STRING = {
            "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0a", "0b", "0c"
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
