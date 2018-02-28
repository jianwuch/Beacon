package com.igrs.beacon.util;

import com.clj.fastble.utils.HexUtil;

/**
 * Created by jove.chen on 2017/12/25.
 */

public class HexIntUtil {
    /* byte[]->int */
    public final static int getInt(byte[] buf, boolean asc) {
        if (buf == null) {
            throw new IllegalArgumentException("byte array is null!");
        }
        if (buf.length > 4) {
            throw new IllegalArgumentException("byte array size > 4 !");
        }
        int r = 0;
        if (asc) {
            for (int i = buf.length - 1; i >= 0; i--) {
                r <<= 8;
                r |= (buf[i] & 0x000000ff);
            }
        } else {
            for (int i = 0; i < buf.length; i++) {
                r <<= 8;
                r |= (buf[i] & 0x000000ff);
            }
        }
        return r;
    }

    /**
     * 十进制转16进制byte
     */
    public static byte[] decimalToHexBytes(int decimal) {
        String hexString = decimalToHex(decimal);
        return HexUtil.hexStringToBytes(hexString);
    }

    public static String decimalToHex(int decimal) {
        String hex = "";
        while (decimal != 0) {
            int hexValue = decimal % 16;
            hex = toHexChar(hexValue) + hex;
            decimal = decimal / 16;
        }
        return hex;
    }

    //将0~15的十进制数转换成0~F的十六进制数
    public static char toHexChar(int hexValue) {
        if (hexValue <= 9 && hexValue >= 0) {
            return (char) (hexValue + '0');
        } else {
            return (char) (hexValue - 10 + 'A');
        }
    }

    /**
     * 转成两个字节的string
     */
    public static String decimalTo2ByteHex(int decimal) {
        if (decimal > 65535) return "";
        String result = decimalToHex(decimal);
        int length = result.length();
        switch (length) {
            case 0:
                return "0000";
            case 1:
                return "000" + result;

            case 2:
                return "00" + result;

            case 3:
                return "0" + result;
            case 4:
                return result;
            default:
                return "0000";
        }
    }

    public static String decimalTo1ByteHex(int decimal) {
        if (decimal > 128) return "00";
        String result = decimalToHex(decimal);
        int length = result.length();
        switch (length) {
            case 0:
                return "00";
            case 1:
                return "0" + result;
            default:
                return "00";
        }
    }


    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。 和bytesToInt（）配套使用
     * @param value
     *            要转换的int值
     * @return byte数组
     */
    public static byte[] intToBytes( int value )
    {
        byte[] src = new byte[4];
        src[3] =  (byte) ((value>>24) & 0xFF);
        src[2] =  (byte) ((value>>16) & 0xFF);
        src[1] =  (byte) ((value>>8) & 0xFF);
        src[0] =  (byte) (value & 0xFF);
        return src;
    }
    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。  和bytesToInt2（）配套使用
     */
    public static byte[] intToBytes2(int value)
    {
        byte[] src = new byte[4];
        src[0] = (byte) ((value>>24) & 0xFF);
        src[1] = (byte) ((value>>16)& 0xFF);
        src[2] = (byte) ((value>>8)&0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

    //int转一个字节
    public static byte intTo1Byte(int value) {
        byte src = (byte) ((value) & 0xFF);
        return src;
    }

    //切換低位在前
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
