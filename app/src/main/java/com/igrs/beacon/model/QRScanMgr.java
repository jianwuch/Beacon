package com.igrs.beacon.model;

import android.text.TextUtils;
import com.igrs.beacon.model.data.BatchConfig;

/**
 * Created by jove.chen on 2018/2/6.
 */

public class QRScanMgr {

    //解析数据
    public static BatchConfig parseQRResult(String result) {
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        String[] configs = result.split("\n");
        BatchConfig batchConfig = new BatchConfig();
        return batchConfig;
    }
}
