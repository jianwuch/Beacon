package com.igrs.beacon;

import com.clj.fastble.utils.HexUtil;
import com.igrs.beacon.util.HexIntUtil;

/**
 * Created by jianw on 18-2-28.
 */

public class TestHex {
    public static void main(String[] arg) {

        int a = 4391;
        byte[] aList = HexIntUtil.decimalToHexBytes(a);

        System.out.print(HexIntUtil.lowByte2int(aList));
    }
}
