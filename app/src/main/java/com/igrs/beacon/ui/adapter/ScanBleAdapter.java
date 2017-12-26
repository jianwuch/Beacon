package com.igrs.beacon.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.igrs.beacon.R;
import com.igrs.beacon.base.BaseViewHolderWithImgLevel;
import com.igrs.beacon.moudle.data.iBeacon;

import java.util.List;

import butterknife.BindView;

/**
 * Created by jove.chen on 2017/11/10.
 */

public class ScanBleAdapter extends BaseQuickAdapter<iBeacon, BaseViewHolderWithImgLevel> {
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

    public ScanBleAdapter(@LayoutRes int layoutResId, @Nullable List<iBeacon> data) {
        super(layoutResId, data);
    }

    public ScanBleAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    public void setChecked(int position) {
        if (chooseMode) {
            iBeacon bleBeacon = mData.get(position);
            if (bleBeacon.isChecked()) {
                bleBeacon.setChecked(false);
            } else {
                bleBeacon.setChecked(true);
            }
            notifyDataSetChanged();
        }
    }

    @Override
    protected void convert(BaseViewHolderWithImgLevel helper, iBeacon item) {
        CheckBox checkBox = helper.getView(R.id.checkbox);
        if (chooseMode) {
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(item.isChecked());
        } else {
            checkBox.setVisibility(View.GONE);
        }
        helper.setText(R.id.mac, String.format("MAC:%1$s", item.bluetoothAddress))
                .
                        setText(R.id.major, String.format("Major:%1$d", item.major))
                .setText(R.id.minor, String.format("Minor:%1$d", item.minor))
                .setText(R.id.uuid, String.format("UUIDBean:%1$s", item.proximityUuid))
                .setText(R.id.power, item.txPower+"")
                .setText(R.id.rssi, item.rssi+"");

        helper.setImageLevel(R.id.img_battery, item.txPower).setImageLevel(R.id.img_rssi, item.rssi);
    }
}
