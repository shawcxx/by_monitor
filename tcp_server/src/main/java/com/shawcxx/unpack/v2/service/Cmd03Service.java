package com.shawcxx.unpack.v2.service;

import com.shawcxx.common.util.MyHexUtil;
import com.shawcxx.unpack.BaseUnpackBO;
import com.shawcxx.unpack.BaseUnpackReturnUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chen jl
 * @date 2022/7/1 21:27
 * @description
 **/
@Service("v2Cmd03Service")
public class Cmd03Service {
    private static final String RETURN_CMD = "03";

    public String unpack(BaseUnpackBO baseUnpackBO) {
        String data = baseUnpackBO.getContent();
        int contentLength = MyHexUtil.getHexInt(data, 1, 2);
        int deviceNumber = MyHexUtil.getHexInt(data, 3, 1);
        int start = 4;
        int length = 23;
        System.out.println(deviceNumber);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < deviceNumber; i++) {
            String hex = MyHexUtil.getHex(data, start, length);
            System.out.println(hex);
            list.add(hex);
            start += length;
        }
        System.out.println(list.size());
        System.out.println(MyHexUtil.getHex(data, start, 3));
        return BaseUnpackReturnUtil.getV2UnpackReturnData("00", RETURN_CMD, baseUnpackBO.getRouteId(), "01");
    }
}
