package com.igrs.beacon.config;

import android.content.Context;

import com.igrs.beacon.moudle.data.FilterConfig;
import com.igrs.beacon.util.SharedPreferencesUtils;

/**
 * Created by jianw on 17-12-7.
 */

public class FilterManager {
    private static FilterConfig filterConfig;
    private static Context mContext;

    /**
     * 最好是application的 context
     *
     * @param context
     */
    public static void init(Context context) {
        if (filterConfig != null) {
            return;
        }
        mContext = context;
        filterConfig = new FilterConfig(context);
    }

    //获取配置
    public static FilterConfig getFilterConfig() {
        return filterConfig;
    }

    //保存新的配置
    public static void saveNewConfig(FilterConfig config) {
        filterConfig = config;

        //保存sharepreferances
        SharedPreferencesUtils.setParam(mContext, AppConstans.SharedPreferencesKey.ENABLE_UUID, config.enableUUID);
        SharedPreferencesUtils.setParam(mContext, AppConstans.SharedPreferencesKey.ENABLE_MAJOR, config.enableMajor);
        SharedPreferencesUtils.setParam(mContext, AppConstans.SharedPreferencesKey.ENABLE_MINOR, config.enableMinor);

        SharedPreferencesUtils.setParam(mContext, AppConstans.SharedPreferencesKey.FILTER_UUID, config.filterUUID);

        SharedPreferencesUtils.setParam(mContext, AppConstans.SharedPreferencesKey.MAJOR_FROM, config.majorFrom);
        SharedPreferencesUtils.setParam(mContext, AppConstans.SharedPreferencesKey.MAJOR_TO, config.majorTo);
        SharedPreferencesUtils.setParam(mContext, AppConstans.SharedPreferencesKey.MINOR_FROM, config.minorFrom);
        SharedPreferencesUtils.setParam(mContext, AppConstans.SharedPreferencesKey.MINOR_TO, config.minorTo);
    }

    private FilterManager() {
    }
}
