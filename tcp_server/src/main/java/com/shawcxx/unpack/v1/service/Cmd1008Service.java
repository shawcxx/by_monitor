package com.shawcxx.unpack.v1.service;

import com.shawcxx.unpack.BaseUnpackBO;
import com.shawcxx.unpack.BaseUnpackReturnUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Chen jl
 * @date 2022/6/20 20:43
 * @description
 **/
@Service("v1Cmd1008Service")
@Slf4j
public class Cmd1008Service {
    private static final String RETURN_CMD = "1009";

    @Resource
    private Cmd1004Service cmd1004Service;

    public String unpack(BaseUnpackBO baseUnpackBO) {
        cmd1004Service.unpack(baseUnpackBO);
        return BaseUnpackReturnUtil.getV1UnpackReturnData(RETURN_CMD, baseUnpackBO.getRouteId(), "00", "00");
    }
}
