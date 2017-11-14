package com.igrs.beacon.base;

import android.graphics.Bitmap;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by jove.chen on 2017/11/14.
 */

public class BaseViewHolderWithImgLevel extends BaseViewHolder {
    public BaseViewHolderWithImgLevel(View view) {
        super(view);
    }

    public BaseViewHolder setImageLevel(@IdRes int viewId, int value) {
        ImageView view = getView(viewId);
        view.setImageLevel(value);
        return this;
    }
}
