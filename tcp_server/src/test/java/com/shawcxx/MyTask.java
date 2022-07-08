package com.shawcxx;

import cn.hutool.core.util.StrUtil;
import cn.hutool.socket.SocketUtil;
import com.alibaba.fastjson.JSON;
import com.shawcxx.modules.device.domain.DeviceRecordDO;
import com.shawcxx.modules.device.service.DeviceRecordService;
import com.shawcxx.unpack.BaseUnpackService;
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
    private DeviceRecordService deviceRecordService;

    @Test
    public void test() {
        DeviceRecordDO lastData = deviceRecordService.getLastData("11860034", 1);
        System.out.println(JSON.toJSONString(lastData));
    }
}
