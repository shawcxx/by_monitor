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
@Service("v1Cmd1006Service")
@Slf4j
public class Cmd1006Service {
    private static final String RETURN_CMD = "1007";

    public String unpack(BaseUnpackBO baseUnpackBO) {

        //todo 解析
        String content = baseUnpackBO.getContent();
        return BaseUnpackReturnUtil.getUnpackReturnData(RETURN_CMD, baseUnpackBO.getRouteId(),"01000010104400000000000000");
    }
}
