package com.igrs.beacon.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.igrs.beacon.R;
import com.igrs.beacon.base.BaseViewHolderWithImgLevel;
import com.igrs.beacon.moudle.data.BaseCheckable;
import com.igrs.beacon.moudle.data.BeaconWithCheckable;
import com.igrs.beacon.moudle.data.BleBeacon;
import java.util.List;

/**
 * Created by jove.chen on 2017/11/10.
 */

public class ScanBleAdapter extends BaseQuickAdapter<BeaconWithCheckable, BaseViewHolderWithImgLevel> {
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
    public ScanBleAdapter(@LayoutRes int layoutResId, @Nullable List<BeaconWithCheckable> data) {
        super(layoutResId, data);
    }

    public ScanBleAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    public void setChecked(int position) {
        if (chooseMode) {
            BeaconWithCheckable bleBeacon = mData.get(position);
            if (bleBeacon.isChecked()) {
                bleBeacon.setChecked(false);
            } else {
                bleBeacon.setChecked(true);
            }
            notifyDataSetChanged();
        }
    }

    @Override
    protected void convert(BaseViewHolderWithImgLevel helper, BeaconWithCheckable item) {
        CheckBox checkBox = helper.getView(R.id.checkbox);
        if (chooseMode) {
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(item.isChecked());
        } else {
            checkBox.setVisibility(View.GONE);
        }
    }
}
