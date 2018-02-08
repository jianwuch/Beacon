package com.igrs.beacon.model;

import android.text.TextUtils;
import com.igrs.beacon.model.data.BatchConfig;

/**
 * Created by jove.chen on 2018/2/6.
 */

public class QRScanMgr {
    private static final String UUID = "UUID:";
    private static final String MAJOR = "Major:";
    private static final String MINOR = "Minor:";

    //解析数据
    public static BatchConfig parseQRResult(String result) {
        if (TextUtils.isEmpty(result)) {
            return null;
        }
        String[] configs = result.split("\n");
        BatchConfig batchConfig = new BatchConfig();
        if (null != configs && configs.length > 0) {
            for (int i = 0; i < configs.length; i++) {
                parseItem(batchConfig, configs[i]);
            }
        } else {
            return null;
        }
        return batchConfig;
    }

    public static void parseItem(BatchConfig config, String item) {
        if (item.startsWith(UUID)) {
            config.uuidEnable = true;
            config.uuid = item.substring(UUID.length() + 1, item.length());
        } else if (item.startsWith(MAJOR)) {
            config.majorFrom =
                    Integer.parseInt(item.substring(MAJOR.length() + 1, item.length()));
            config.majorStepLength = 0;
        } else if (item.startsWith(MINOR)) {
            config.minorFrom =
                    Integer.parseInt(item.substring(MINOR.length() + 1, item.length()));
            config.minorStepLength = 0;
        }
    }
}
