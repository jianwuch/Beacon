package com.igrs.beacon.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.igrs.beacon.R;
import com.igrs.beacon.base.BaseViewHolderWithImgLevel;
import com.igrs.beacon.moudle.data.BleBeacon;
import java.util.List;

/**
 * Created by jove.chen on 2017/11/10.
 */

public class ScanBleAdapter extends BaseQuickAdapter<BleBeacon, BaseViewHolderWithImgLevel> {

    public ScanBleAdapter(@LayoutRes int layoutResId, @Nullable List<BleBeacon> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolderWithImgLevel helper, BleBeacon item) {

    }
}
