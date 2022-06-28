package com.shawcxx.unpack.v1.service;

import com.shawcxx.unpack.BaseUnpackBO;
import com.shawcxx.unpack.BaseUnpackReturnUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Chen jl
 * @date 2022/6/20 20:43
 * @description
 **/
@Service("v1Cmd2000Service")
@Slf4j
public class Cmd2000Service {
    private static final String RETURN_CMD = "2001";

    public String unpack(BaseUnpackBO baseUnpackBO) {

        //todo 解析
        String content = baseUnpackBO.getContent();


        return BaseUnpackReturnUtil.getUnpackReturnData(RETURN_CMD, baseUnpackBO.getRouteId());
    }
}
