package com.shawcxx.unpack.v2.service;

import cn.hutool.core.util.NumberUtil;
import com.shawcxx.common.util.MyHexUtil;
import com.shawcxx.unpack.BaseUnpackBO;
import com.shawcxx.unpack.BaseUnpackReturnUtil;
import org.springframework.stereotype.Service;

/**
 * @author Chen jl
 * @date 2022/7/1 20:57
 * @description
 **/
@Service("v2Cmd01Service")
public class Cmd01Service {
    private static final String RETURN_CMD = "01";

    public String unpack(BaseUnpackBO baseUnpackBO) {
        String data = baseUnpackBO.getContent();
        int contentLength = MyHexUtil.getHexInt(data, 1, 1);
        String secret = MyHexUtil.getHex(data, 2, 10);
        String hardwareVersion = MyHexUtil.getHex(data, 12, 2);
        String softwareVersion = MyHexUtil.getHex(data, 14, 2);
        String routeType = MyHexUtil.getHexIntStr(data, 16, 2);


        return BaseUnpackReturnUtil.getV2UnpackReturnData("00", RETURN_CMD, baseUnpackBO.getRouteId(), "01");
    }
}
