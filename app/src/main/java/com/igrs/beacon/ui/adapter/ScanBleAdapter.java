package com.igrs.beacon.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.igrs.beacon.R;
import com.igrs.beacon.base.BaseViewHolderWithImgLevel;
import com.igrs.beacon.moudle.data.BleBeacon;
import java.util.List;

import static android.media.CamcorderProfile.get;

/**
 * Created by jove.chen on 2017/11/10.
 */

public class ScanBleAdapter extends BaseQuickAdapter<BleBeacon, BaseViewHolderWithImgLevel> {
    private boolean chooseMode;
    public void setChooseMode(boolean chooseMode) {
        this.chooseMode = chooseMode;
        if (!chooseMode) {
            for (int i = 0; i < mData.size(); i++) {
                mData.get(i).setChecked(false);
            }
        }
        notifyDataSetChanged();
    }

    public boolean getChooseMode() {
        return chooseMode;
    }
    public ScanBleAdapter(@LayoutRes int layoutResId, @Nullable List<BleBeacon> data) {
        super(layoutResId, data);
    }

    public void setChecked(int position) {
        if (chooseMode) {
            BleBeacon bleBeacon = mData.get(position);
            if (bleBeacon.isChecked()) {
                bleBeacon.setChecked(false);
            } else {
                bleBeacon.setChecked(true);
            }
            notifyDataSetChanged();
        }
    }

    @Override
    protected void convert(BaseViewHolderWithImgLevel helper, BleBeacon item) {
        CheckBox checkBox = helper.getView(R.id.checkbox);
        if (chooseMode) {
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(item.isChecked());
        } else {
            checkBox.setVisibility(View.GONE);
        }
    }
}
