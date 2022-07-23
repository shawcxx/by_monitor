package com.shawcxx;

import cn.hutool.core.util.StrUtil;
import com.shawcxx.modules.device.domain.DeviceDO;
import com.shawcxx.modules.device.service.DeviceService;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author cjl
 * @date 2022/7/4 17:59
 * @description
 */
@SpringBootTest
public class Test {
    @Resource
    private DeviceService deviceService;

    @org.junit.jupiter.api.Test
    public void test() {
        String s = "900";
        for (int i = 0; i < 10000; i++) {
            String p = StrUtil.padPre(i + "", 5, '0');
            DeviceDO deviceDO = new DeviceDO();
            deviceDO.setDeviceNo(s + p);
            deviceDO.setStationId("1");
            deviceDO.setDeviceType(2);
        }
    }
}
