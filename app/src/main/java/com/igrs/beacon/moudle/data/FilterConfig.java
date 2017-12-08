package com.igrs.beacon.moudle.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.igrs.beacon.config.AppConstans;
import com.igrs.beacon.util.SharedPreferencesUtils;

/**
 * Created by jove.chen on 2017/12/6.
 */

public class FilterConfig {
    public boolean enableUUID;
    public boolean enableMajor;
    public boolean enableMinor;

    public String filterUUID;

    //0-65532范围
    public int majorFrom;
    public int majorTo;

    //0-65532范围
    public int minorFrom;
    public int minorTo;

    public FilterConfig(Context context) {
        this.enableUUID = (boolean) SharedPreferencesUtils.getParam(context,
                AppConstans.SharedPreferencesKey.ENABLE_UUID, false);
        this.enableMajor = (boolean) SharedPreferencesUtils.getParam(context,
                AppConstans.SharedPreferencesKey.ENABLE_MAJOR, false);
        this.enableMinor = (boolean) SharedPreferencesUtils.getParam(context,
                AppConstans.SharedPreferencesKey.ENABLE_MINOR, false);
        this.filterUUID = (String) SharedPreferencesUtils.getParam(context,
                AppConstans.SharedPreferencesKey.FILTER_UUID, "");
        this.majorFrom = (int) SharedPreferencesUtils.getParam(context,
                AppConstans.SharedPreferencesKey.MAJOR_FROM, 0);
        this.majorTo = (int) SharedPreferencesUtils.getParam(context,
                AppConstans.SharedPreferencesKey.MAJOR_TO, 0);
        this.minorFrom = (int) SharedPreferencesUtils.getParam(context,
                AppConstans.SharedPreferencesKey.MINOR_FROM, 0);
        this.minorTo = (int) SharedPreferencesUtils.getParam(context,
                AppConstans.SharedPreferencesKey.MINOR_TO, 0);
    }

    public FilterConfig() {
    }
}
