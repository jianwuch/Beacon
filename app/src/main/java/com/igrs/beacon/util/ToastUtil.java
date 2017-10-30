package com.igrs.beacon.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;
public class ToastUtil {

    public static Toast gToast = null;

    /**
     * 弹出Toast提示，Toast.LENGTH_SHORT
     */
    public static void ToastShort(Context context, String msg) {
        if (null == context) {//异步回调有可能context为空
            return;
        }
        cleanToast();
        gToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        gToast.show();
    }

    public static void ToastShort(Context context, int msgTxtId) {
        if (null != context) {
            ToastShort(context.getApplicationContext(),
                    context.getApplicationContext().getResources().getString(msgTxtId));
        }
    }

    /**
     * 清除显示的TOAST
     */
    public static void cleanToast() {
        if (null != gToast) {
            gToast.cancel();
            gToast = null;
        }
    }
}
