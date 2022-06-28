package com.shawcxx.modules.device.service;

import com.shawcxx.modules.device.domain.DeviceDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.*;

/**
 * @author: dj
 * @create: 2020-09-15 14:31
 * @description:
 */
@Slf4j
@Service
public class DeviceLastTimeService {

    public static Set<DeviceDO> DEV_LAST_TIME_SET = new HashSet<>();

    public static Map<String, Long> DEV_LAST_TIME_MAP = new HashMap<>();
    @Resource
    private DeviceService deviceService;

    public void data2Domain(DeviceDO deviceDO) {
        try {
            Date deviceTime = deviceDO.getDeviceTime();
            if (deviceTime != null) {
                long devTime = deviceTime.getTime();
                this.setDevice(deviceDO, devTime);
            }
        } catch (Exception e) {
            log.warn("k2入缓存失败", e);
        }
    }


    private void setDevice(DeviceDO deviceDO, long devTime) {
        String deviceNO = deviceDO.getDeviceNo();
        Long time = DEV_LAST_TIME_MAP.get(deviceNO);
        if (null != time) {
            if (devTime > time) {
                this.setSetAndMap(deviceDO, deviceNO, devTime);
            }
        } else {
            this.setSetAndMap(deviceDO, deviceNO, devTime);
        }
    }

    private void setSetAndMap(DeviceDO deviceDO, String devSysId, long devTime) {
        DEV_LAST_TIME_SET.add(deviceDO);
        DEV_LAST_TIME_MAP.put(devSysId, devTime);
    }

    @Scheduled(fixedDelay = 1000 * 60 * 5)
    @PreDestroy
    public void saveLastTime() {
        try {
            Long start = System.currentTimeMillis();
            Set<DeviceDO> set = DeviceLastTimeService.DEV_LAST_TIME_SET;
            DeviceLastTimeService.DEV_LAST_TIME_SET = new HashSet<>();
            if (0 < set.size()) {
                for (DeviceDO deviceDO : set) {
                    deviceService.updateByDeviceNO(deviceDO);
                }
            }
            //花费时间
            Long end = System.currentTimeMillis();
            //存储数据量
            log.info("存储数量:{},花费时间:{}", set.size(), end - start);
        } catch (Exception e) {
            log.warn("设备最后数据时间错误", e);
        }
    }

}
