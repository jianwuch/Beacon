package com.igrs.beacon.moudle.data;

import com.igrs.beacon.util.iBeaconUtil;

/**
 * Created by jove.chen on 2017/11/16.
 */

public class BeaconWithCheckable extends iBeacon {
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public BeaconWithCheckable() {
    }
}
