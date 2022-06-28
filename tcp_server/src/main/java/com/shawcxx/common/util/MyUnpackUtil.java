package com.shawcxx.common.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author dj
 * @date 2021-12-21 09:14
 * @description
 **/
@Slf4j
public class MyUnpackUtil {

    /**
     * 16进制字符串转byte
     */
    public static byte[] hex2Bytes(String str) {
        if (str == null || "".equals(str.trim())) {
            return new byte[0];
        }
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }

    public static int[] hex2IntArray(String str) {
        if (str == null || "".equals(str.trim())) {
            return new int[0];
        }
        int[] intArray = new int[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            intArray[i] = Integer.parseInt(subStr, 16);
        }
        return intArray;
    }

    /**
     * byteBuffer 转16进制字符串
     */
    public static String readBuffer2Hex(ByteBuffer byteBuffer) {
        String data = null;
        int len = byteBuffer.limit() - byteBuffer.position();
        byte[] bytes = new byte[len];
        if (!byteBuffer.isReadOnly()) {
            byteBuffer.get(bytes);
            StringBuilder sb = new StringBuilder(bytes.length);
            String sTemp;
            for (byte b : bytes) {
                sTemp = Integer.toHexString(0xFF & b);
                if (sTemp.length() < 2) {
                    sb.append(0);
                }
                sb.append(sTemp.toUpperCase());
            }
            data = sb.toString();
        }
        return data;
    }

    /**
     * 补零
     */
    public static String strReplenishZero(String str, int num) {
        str = str == null ? "" : str;
        int strLength = str.length();
        if (num != strLength) {
            while (true) {
                strLength = str.length();
                if (num > strLength) {
                    str = "0" + str;
                } else {
                    break;
                }
            }
        }
        return str;
    }

    public static String getTimeBcd(String data) {
        String date = DateUtil.now();
        try {
            DateTime dateTime = DateUtil.parse(data);
            date = DatePattern.NORM_DATETIME_FORMAT.format(dateTime);
        } catch (Exception e) {
            log.warn("设备时间解析错误:{}", data, e);
        }
        return date;
    }

    /**
     * btye转字符串
     */
    public static String byte2Str(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(b);
        }
        return sb.toString();
    }

    public static String getCs(String data) {
        String[] array = StrUtil.split(data, 2);
        int all = 0;
        for (String str : array) {
            BigInteger num = new BigInteger(str, 16);
            all = all + num.intValue();
        }
        int m = all % 256;
        return String.format("%02X", m);
    }

    public static byte[] string2Ascii2byte(String value) {
        char[] chars = value.toCharArray();
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        return StandardCharsets.UTF_8.encode(cb).array();
    }

    //public static byte[] hexStr2byte(String value) {
    //    String[] array = StrUtil.split(value, 2);
    //    byte[] bytes = new byte[array.length / 2];
    //    for (String str : array) {
    //
    //    }
    //    return bytes;
    //}

    public static void main(String[] args) {
        //String data = "69001129E23230323131323031313535353431202034474D3135364D4330343445010108693CB6E3A712110100";
        //System.out.println(String.format("%02X", stringToAscii(data)));
        //String cs = getCs(data);
        //System.out.println(cs);
    }


}
