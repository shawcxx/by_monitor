package com.shawcxx.modules.device.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shawcxx.common.constant.SysConstant;
import com.shawcxx.modules.device.dao.DeviceDAO;
import com.shawcxx.modules.device.domain.DeviceDO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author Chen jl
 * @date 2022/6/24 17:59
 * @description
 **/
@Service
public class DeviceService extends ServiceImpl<DeviceDAO, DeviceDO> {

    public void updateByDeviceNo(DeviceDO deviceDO) {
        LambdaUpdateWrapper<DeviceDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(StrUtil.isNotBlank(deviceDO.getEmuId()), DeviceDO::getEmuId, deviceDO.getEmuId());
        updateWrapper.set(StrUtil.isNotBlank(deviceDO.getSoftwareVersion()), DeviceDO::getSoftwareVersion, deviceDO.getSoftwareVersion());
        updateWrapper.set(deviceDO.getDeviceTime() != null, DeviceDO::getDeviceTime, deviceDO.getDeviceTime());
        updateWrapper.eq(DeviceDO::getDeviceNo, deviceDO.getDeviceNo());
        this.update(updateWrapper);
    }


    @Scheduled(fixedDelay = 5 * 60 * 1000)
    public void deviceList() {
        HashMap<String, DeviceDO> map = new HashMap<>();
        List<DeviceDO> list = this.list();
        for (DeviceDO deviceDO : list) {
            map.put(deviceDO.getDeviceNo(), deviceDO);
        }
        SysConstant.DEVICE_MAP = map;
    }

    public DeviceDO getDevice(String deviceNo) {
        DeviceDO deviceDO = SysConstant.DEVICE_MAP.get(deviceNo);
        if (deviceDO == null) {
            deviceDO = this.getOne(new LambdaUpdateWrapper<DeviceDO>().eq(DeviceDO::getDeviceNo, deviceNo));
            if (deviceDO != null) {
                SysConstant.DEVICE_MAP.put(deviceNo, deviceDO);
            }
        }
        return deviceDO;
    }
}
