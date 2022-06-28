package com.shawcxx.unpack.v1.service;

import com.shawcxx.unpack.BaseUnpackBO;
import com.shawcxx.unpack.BaseUnpackReturnUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Chen jl
 * @date 2022/6/19 21:01
 * @description
 **/
@Service("v1Cmd1000Service")
@Slf4j
public class Cmd1000Service {

    private static final String RETURN_CMD = "1001";


    public String unpack(BaseUnpackBO baseUnpackBO) {

        //获取注册台数
        return BaseUnpackReturnUtil.getUnpackReturnData(RETURN_CMD, baseUnpackBO.getRouteId(), "00", "00");
    }
}
