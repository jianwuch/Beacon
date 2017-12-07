package com.igrs.beacon.moudle.data;

/**
 * Created by jove.chen on 2017/12/6.
 */

public class FilterConfig {
    public boolean enableUUID;
    public boolean enableMajor;
    public boolean enableMinor;

    public String filterUUID;

    //0-65532范围
    public int majorFrom;
    public int majorTo;

    //0-65532范围
    public int minorFrom;
    public int minorTo;
}
