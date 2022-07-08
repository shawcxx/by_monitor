package com.shawcxx.modules.statistic.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shawcxx.modules.device.domain.DeviceDO;
import com.shawcxx.modules.device.domain.DeviceRecordDO;
import com.shawcxx.modules.device.service.DeviceRecordService;
import com.shawcxx.modules.device.service.DeviceService;
import com.shawcxx.modules.statistic.domain.DeviceEnergyStatisticDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Chen jl
 * @date 2022/7/2 13:03
 * @description 发电量处理服务
 **/
@Service
@Slf4j
public class EnergyService {

    @Resource
    private DeviceService deviceService;

    @Resource
    private DeviceEnergyStatisticService deviceEnergyStatisticService;

    @Resource
    private DeviceRecordService deviceRecordService;
    private final Map<String, Double> lastEnergyMap = new ConcurrentHashMap<>();

    private String[] dateFormat = {"yyyy-MM-dd HH", "yyyy-MM-dd", "yyyy-MM", "yyyy"};

    /**
     * 发电量每小时的增量更新
     *
     * @param deviceNo   设备编号
     * @param energy     当前发电量
     * @param time       时间
     * @param deviceType 设备类型
     */
    @Async
    public void saveEnergy(String deviceNo, Double energy, Date time, Integer deviceType) {
        DeviceDO device = deviceService.getDevice(deviceNo);
        if (device != null && StrUtil.isNotBlank(device.getStationId())) {
            String key = deviceNo + "-" + deviceType;
            Double value = lastEnergyMap.get(key);
            if (value == null) {
                DeviceRecordDO deviceRecordDO = deviceRecordService.getLastData(deviceNo, deviceType);
                if (deviceRecordDO != null) {
                    value = deviceRecordDO.getEnergy();
                }
            }
            if (value != null) {
                try {
                    double inc = energy - value;
                    if (inc > 0) {
                        List<DeviceEnergyStatisticDO> list = new ArrayList<>();
                        for (int i = 0; i < 4; i++) {
                            String statisticTime = DateUtil.format(time, dateFormat[i]);
                            LambdaQueryWrapper<DeviceEnergyStatisticDO> queryWrapper = new LambdaQueryWrapper<>();
                            queryWrapper.eq(DeviceEnergyStatisticDO::getDeviceId, device.getDeviceId());
                            queryWrapper.eq(DeviceEnergyStatisticDO::getDeviceType, deviceType);
                            queryWrapper.eq(DeviceEnergyStatisticDO::getStatisticTime, statisticTime);
                            DeviceEnergyStatisticDO statisticDO = deviceEnergyStatisticService.getOne(queryWrapper);
                            if (statisticDO == null) {
                                statisticDO = new DeviceEnergyStatisticDO();
                                statisticDO.setDeviceType(deviceType);
                                statisticDO.setDeviceId(device.getDeviceId());
                                statisticDO.setStatisticTime(statisticTime);
                                statisticDO.setEnergyValue(0.0);
                                statisticDO.setStationId(device.getStationId());
                                statisticDO.setStatisticType(i);
                            }
                            statisticDO.setEnergyValue(statisticDO.getEnergyValue() + inc);
                            list.add(statisticDO);
                        }
                        if (CollUtil.isNotEmpty(list)) {
                            deviceEnergyStatisticService.saveOrUpdateBatch(list);
                        }
                    }
                } catch (Exception e) {
                    log.error("发电量保存失败", e);
                }
            }
            lastEnergyMap.put(key, energy);
        }
    }


}
