package com.shawcxx.common.util;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static jdk.nashorn.internal.objects.NativeString.substring;

/**
 * @author dj
 * @date 2021-12-21 09:37
 * @description hex解析包
 **/
public class MyHexUtil {

    /**
     * 16进制转10进制Long
     */
    public static long getHexLong(String hexData, int start, int hexLength) {
        return Long.parseLong(getHex(hexData, start, hexLength), 16);
    }

    public static String getHexIntStr(String hexData, int start, int hexLength) {
        return Long.parseLong(getHex(hexData, start, hexLength), 16) + "";
    }

    public static int getHexInt(String hexData, int start, int hexLength) {
        return Integer.parseInt(getHex(hexData, start, hexLength), 16);
    }

    public static String getHex(String hexData, int start, int hexLength) {
        return hexData.substring((start - 1) * 2, (start - 1 + hexLength) * 2);
    }

    public static int getHexLowInt(String hexData, int start, int hexLength) {
        String hex = hexData.substring((start - 1) * 2, (start - 1 + hexLength) * 2);
        return lowHex2Int(hex);
    }

    /**
     * 16进制转2进制
     * 从右到左
     */
    public static int[] hexToIntArray(String numStr16es) {
        List<String> numStr16List = getHexDataList(numStr16es, 1);
        int[] str = new int[4 * numStr16List.size()];
        int i = 0;
        for (String numStr16 : numStr16List) {
            String result = Integer.toBinaryString(Integer.parseInt(numStr16, 16));
            String num2Str = MyUnpackUtil.strReplenishZero(result, 4);
            List<String> num2StrList = getHexDataList(num2Str, 1);
            for (String num2 : num2StrList) {
                str[i] = Integer.parseInt(num2);
                i++;
            }
        }
        return ArrayUtil.reverse(str);
    }
    public static List<String> getHexDataList(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        return getStrList(inputString, length, size);
    }

    private static List<String> getStrList(String inputString, int length,
                                           int size) {
        List<String> list = new ArrayList<>();
        for (int index = 0; index < size; index++) {
            String childStr = substring(inputString, index * length,
                    (index + 1) * length);
            list.add(childStr);
        }
        return list;
    }

    public static String lowInt2Hex(int num, int length) {
        String content = String.format("%02X", num);
        if (2 == length) {
            String data = String.format("%04X", num);
            String data1 = StrUtil.sub(data, 0, 2);
            String data2 = StrUtil.sub(data, 2, 4);
            content = data2 + data1;
        } else if (3 == length) {
            String data = String.format("%06X", num);
            String data1 = StrUtil.sub(data, 0, 2);
            String data2 = StrUtil.sub(data, 2, 4);
            String data3 = StrUtil.sub(data, 4, 6);
            content = data3 + data2 + data1;
        } else if (4 == length) {
            String data = String.format("%08X", num);
            String data1 = StrUtil.sub(data, 0, 2);
            String data2 = StrUtil.sub(data, 2, 4);
            String data3 = StrUtil.sub(data, 4, 6);
            String data4 = StrUtil.sub(data, 6, 8);
            content = data4 + data3 + data2 + data1;
        }
        return content;
    }

    public static Integer lowHex2Int(String data) {
        int num = 0;
        String content = null;
        int length = data.length();
        if (4 == length) {
            String data1 = StrUtil.sub(data, 0, 2);
            String data2 = StrUtil.sub(data, 2, 4);
            content = data2 + data1;
        } else if (6 == length) {
            String data1 = StrUtil.sub(data, 0, 2);
            String data2 = StrUtil.sub(data, 2, 4);
            String data3 = StrUtil.sub(data, 4, 6);
            content = data3 + data2 + data1;
        } else if (8 == length) {
            String data1 = StrUtil.sub(data, 0, 2);
            String data2 = StrUtil.sub(data, 2, 4);
            String data3 = StrUtil.sub(data, 4, 6);
            String data4 = StrUtil.sub(data, 6, 8);
            content = data4 + data3 + data2 + data1;
        }
        if (null != content) {
            num = Integer.parseInt(content, 16);
        }
        return num;
    }

    public static String num2Ascii(String num) {
        byte[] bytes = num.getBytes();
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }



    public static LocalDateTime getDate(String data, int start, int hexLength) {
        String date = MyHexUtil.getHex(data, start, hexLength);
        int year = MyHexUtil.getHexInt(date, 1, 1);
        int month = MyHexUtil.getHexInt(date, 2, 1);
        int day = MyHexUtil.getHexInt(date, 3, 1);
        int hour = MyHexUtil.getHexInt(date, 4, 1);
        int minute = MyHexUtil.getHexInt(date, 5, 1);
        int second = MyHexUtil.getHexInt(date, 6, 1);
        return LocalDateTime.of(LocalDate.of(2000 + year, month, day), LocalTime.of(hour, minute, second));
    }

    public static String getRouteId(String data, int start, int hexLength) {
        return null;
    }

    public static void main(String[] args) {
        //int num = 1700;
        //String data = String.format("%04X", num);
        //String data1 = StrUtil.sub(data, 0, 2);
        //String data2 = StrUtil.sub(data, 2, 4);
        //data = data2 + data1;
        //System.out.println(data);

        String data = "1609100f101e";
        System.out.println(getDate(data, 1, 6));
    }
}
