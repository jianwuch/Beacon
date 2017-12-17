package com.igrs.beacon.util;

import android.util.Log;

/**
 * Created by jianw on 17-12-17.
 */

public class LogUtil{
    private static final String TAG_MSG = "cjw_beacon";
    public static void d(String msg) {
        Log.d(TAG_MSG, msg);
    }

    public static void e(String msg) {
        Log.e(TAG_MSG, msg);
    }
}
