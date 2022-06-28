package com.shawcxx.modules.device.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shawcxx.modules.device.dao.DeviceDAO;
import com.shawcxx.modules.device.domain.DeviceDO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Chen jl
 * @date 2022/6/24 17:59
 * @description
 **/
@Service
public class DeviceService extends ServiceImpl<DeviceDAO, DeviceDO> {

    public void updateByDeviceNO(DeviceDO deviceDO) {
        LambdaUpdateWrapper<DeviceDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(StrUtil.isNotBlank(deviceDO.getDtuId()), DeviceDO::getDtuId, deviceDO.getDtuId());
        updateWrapper.set(StrUtil.isNotBlank(deviceDO.getSoftwareVersion()), DeviceDO::getSoftwareVersion, deviceDO.getSoftwareVersion());
        updateWrapper.set(deviceDO.getDeviceTime() != null, DeviceDO::getDeviceTime, deviceDO.getDeviceTime());
        updateWrapper.eq(DeviceDO::getDeviceNo, deviceDO.getDeviceNo());
        this.update(updateWrapper);
    }
}
