package com.shawcxx;

import cn.hutool.core.util.StrUtil;
import cn.hutool.socket.SocketUtil;
import com.alibaba.fastjson.JSON;
import com.shawcxx.modules.device.domain.DeviceRecordDO;
import com.shawcxx.modules.device.service.DeviceRecordService;
import com.shawcxx.unpack.BaseUnpackBO;
import com.shawcxx.unpack.BaseUnpackService;
import com.shawcxx.unpack.v1.service.Cmd1000Service;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.net.Socket;
import java.net.SocketException;

/**
 * @author Chen jl
 * @date 2022/6/12 21:54
 * @description
 **/
@SpringBootTest
public class MyTask {
    @Resource
    private Cmd1000Service v1Cmd1000Service;

    @Test
    public void test() {
        BaseUnpackBO baseUnpackBO = new BaseUnpackBO();
        baseUnpackBO.setRouteId("90999121");
        String s = v1Cmd1000Service.unpack(baseUnpackBO);
        System.out.println(s);
    }
}
