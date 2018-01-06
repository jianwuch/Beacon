package com.igrs.beacon.model.data;

/**
 * Created by jove.chen on 2018/1/5.
 */

public class BatchConfig {
    public int majorFrom;
    public int majorStepLength;

    public int minorFrom;
    public int minorStepLength;

    public boolean txPowerEnable;
    public boolean nameEnable;
    public boolean batEnable;
    public boolean intervalEnable;

    public String bleName;
    public int bat;
    public int txPower;
    public int interval;
}
