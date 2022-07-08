package com.shawcxx.unpack.v2.service;

import com.shawcxx.common.util.MyHexUtil;
import com.shawcxx.unpack.BaseUnpackBO;
import com.shawcxx.unpack.BaseUnpackReturnUtil;
import org.springframework.stereotype.Service;

/**
 * @author Chen jl
 * @date 2022/7/1 21:27
 * @description
 **/
@Service("v2Cmd02Service")
public class Cmd02Service {
    private static final String RETURN_CMD = "02";

    public String unpack(BaseUnpackBO baseUnpackBO) {
        return BaseUnpackReturnUtil.getV2UnpackReturnData("00", RETURN_CMD, baseUnpackBO.getRouteId(), "01");
    }
}
