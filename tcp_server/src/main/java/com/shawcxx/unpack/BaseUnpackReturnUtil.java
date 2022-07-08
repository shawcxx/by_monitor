package com.shawcxx.unpack;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import com.shawcxx.common.util.MyHexUtil;

/**
 * @author Chen jl
 * @date 2022/6/19 21:19
 * @description
 **/
public class BaseUnpackReturnUtil {
    public static String getV1UnpackReturnData(String CMD, String deviceId, String... args) {
        int length = 0;
        //起始字符
        String protocol = "68";
        length++;

        //数据域长
        length += 2;

        //起始字符
        String protocol2 = "68";
        length++;

        //控制码
        length += 2;

        //采集器ID
        length += 4;
        String id = StrUtil.padPre(Long.toHexString(Long.parseLong(deviceId)) + "", 8, '0');

        //预留
        length += 4;
        String reverse = "00000000";

        StringBuilder data = new StringBuilder();
        for (String arg : args) {
            length += arg.length() / 2;
            data.append(arg);
        }

        //cs
        length++;

        //end
        length++;

        String pre = protocol + StrUtil.padPre(HexUtil.toHex(length), 4, '0') + protocol2 + CMD + id + reverse + data.toString();

        String cs = getCheckSum1(pre);

        String end = "16";

        return (pre + cs + end).toUpperCase();
    }

    public static String getV2UnpackReturnData(String serverCmd, String cmd, String deviceId, String... args) {
        //起始字符
        String protocol = "6464";


        //消息包索引
        String sn = "8000";


        //产生时间
        String time = MyHexUtil.getDateHex();

        //网关信息
        String id = StrUtil.padPre(Long.toHexString(Long.parseLong(deviceId)) + "", 8, '0');

        int length = 0;
        StringBuilder data = new StringBuilder();
        for (String arg : args) {
            data.append(arg);
            length += arg.length() / 2;
        }
        String result = sn + time + id + serverCmd + cmd + StrUtil.padPre(HexUtil.toHex(length), 2, '0') + data;
        //cs

        //end
        String cs = getCheckSum2(result);

        String end = "2323";

        return (protocol + result + cs + end).toUpperCase();
    }

    private static String getCheckSum1(String pre) {
        long total = 0;
        for (String s : StrUtil.split(pre, 2)) {
            long i = Long.parseLong(s, 16);

            total += i;

        }
        total += 85;
        /**
         * 用256求余最大是255，即16进制的FF
         */
        int mod = (int) (total % 256);
        String hex = StrUtil.padPre(Integer.toHexString(mod), 2, '0').toUpperCase();
        return hex;
    }

    private static String getCheckSum2(String pre) {
        long total = 0;
        for (String s : StrUtil.split(pre, 2)) {
            long i = Long.parseLong(s, 16);

            total += i;

        }
        /**
         * 用256求余最大是255，即16进制的FF
         */
        int mod = (int) (total % 256);
        String hex = StrUtil.padPre(Integer.toHexString(mod), 2, '0').toUpperCase();
        return hex;
    }

    public static void main(String[] args) {
        //A5
        String data = StrUtil.replace("00 01 FF FF FF FF FF FF 89 AB CD EF 00 01 01 01"," ","");
        System.out.println(getCheckSum2(data));
    }


}
