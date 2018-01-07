package com.igrs.beacon.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.igrs.beacon.R;
import com.igrs.beacon.base.BaseViewHolderWithImgLevel;
import com.igrs.beacon.model.data.iBeacon;

import java.util.ArrayList;
import java.util.List;

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
                .setText(R.id.power, item.txPower + "")
                .setText(R.id.rssi, item.rssi + "")
        .setText(R.id.name, String.format("Name:%1$s", item.name));

        helper.setImageLevel(R.id.img_battery, getPowerLevel(item.txPower))
                .setImageLevel(R.id.img_rssi, getRssiLevel(item.rssi));
    }

    private int getPowerLevel(int txPower) {
        //-59,-69,-79,-89,-99
        if (txPower >= -59) {
            return 4;
        } else if (txPower >= -69) {
            return 3;
        } else if (txPower >= -79) {
            return 2;
        } else if (txPower >= -89) {
            return 1;
        } else {
            return 0;
        }
    }

    private int getRssiLevel(int rssi) {
        //-59,-69,-79,-89
        if (rssi >= -59) {
            return 3;
        } else if (rssi >= -69) {
            return 2;
        } else if (rssi >= -79) {
            return 1;
        } else {
            return 0;
        }
    }

    public List<iBeacon> getSelectedDatas() {
        List<iBeacon> beacons = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isChecked()) {
                beacons.add(mData.get(i));
            }
        }

        return beacons;
    }
}
