package com.igrs.beacon;

/**
 * Created by jianw on 18-2-28.
 */

public class TestHex {
    public static void main(String[] arg) {
        System.out.print(decToHex(1000));
    }

    public static String decToHex(int dec) {
        String hex = "";
        while(dec != 0) {
            String h = Integer.toString(dec & 0xff, 16);
            if((h.length() & 0x01) == 1)
                h = '0' + h;
            hex = hex + h;
            dec = dec >> 8;
        }
        return hex;
    }
}
