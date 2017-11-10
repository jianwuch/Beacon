package com.igrs.beacon.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import com.igrs.beacon.R;

/**
 * Created by jove.chen on 2017/10/30.
 */

public class DialogManager {
    private Activity activity;
    private Dialog mDialog;
    private boolean isInitSuccess = true;

    public DialogManager(Context context) {
        if (context instanceof Activity) {
            activity = (Activity) context;
            isInitSuccess = true;
        } else {
            isInitSuccess = false;
        }
    }

    private View loadingView;

    public void showDialog(int layoutId, boolean flag) {
        if (!isInitSuccess || activity == null || activity.isFinishing()) {
            return;
        }
        if (null == mDialog) {
            mDialog = new Dialog(activity, android.R.style.Theme_Panel);
            loadingView = LayoutInflater.from(activity).inflate(layoutId, null);
            mDialog.setCanceledOnTouchOutside(flag);
            mDialog.setContentView(loadingView);
        }
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void showProgress(){
        showDialog(R.layout.dialog_loading, true);
    }

    public void dissmissDialog() {
        if (null != mDialog && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
