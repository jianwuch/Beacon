package com.igrs.beacon.config;

/**
 * Created by jianw on 17-12-5.
 */

public class AppConstans {
    public interface SharedPreferencesKey{
        String ENABLE_UUID = "enableUUID";
        String ENABLE_MAJOR = "enableMajor";
        String ENABLE_MINOR = "enableMinor";

        String FILTER_UUID = "uuid";

        String MAJOR_FROM = "major_from";
        String MAJOR_TO = "major_to";

        String MINOR_FROM = "minor_from";
        String MINOR_TO = "minor_to";
    }
}
