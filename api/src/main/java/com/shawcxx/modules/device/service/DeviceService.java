package com.shawcxx.modules.device.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shawcxx.common.base.SysUserBO;
import com.shawcxx.common.exception.MyException;
import com.shawcxx.common.utils.MyUserUtil;
import com.shawcxx.modules.device.dao.DeviceDAO;
import com.shawcxx.modules.device.domain.DeviceDO;
import com.shawcxx.modules.device.dto.DeviceDTO;
import com.shawcxx.modules.device.form.DeviceForm;
import com.shawcxx.modules.device.form.DeviceRequestForm;
import com.shawcxx.modules.station.dao.StationDAO;
import com.shawcxx.modules.station.domain.StationDO;
import com.shawcxx.modules.station.service.StationService;
import com.shawcxx.modules.sys.domain.SysUserDO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Chen jl
 * @date 2022/6/24 19:35
 * @description
 **/
@Service
public class DeviceService extends ServiceImpl<DeviceDAO, DeviceDO> {
    @Resource
    private DeviceEnergyStatisticService energyStatisticService;
    @Resource
    private DeviceRecordService deviceRecordService;
    @Resource
    private StationDAO stationDAO;

    public Page<DeviceDTO> devicePage(DeviceRequestForm form) {
        SysUserBO user = MyUserUtil.getUser();
        form.setDeptList(user.getDeptList());
        form.setUserId(user.getUserId());
        Page<DeviceDTO> page = baseMapper.deviceList(new Page<>(form.getCurrent(), form.getSize()), form);
        for (DeviceDTO record : page.getRecords()) {
            record.setPower(deviceRecordService.getDeviceLastPower(record.getDeviceNo()));
            record.setEnergy(energyStatisticService.getDeviceDayEnergy(record.getDeviceId(), DateUtil.date()));
            record.setEnergyTrend(energyStatisticService.energyData(null, 1, DateUtil.now(), DateUtil.now(), record.getDeviceId()));
        }
        return page;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveDevice(DeviceForm form) {
        List<DeviceDO> list = new ArrayList<>();
        String emuId = form.getEmuId();
        String stationId = form.getStationId();
        StationDO stationDO = stationDAO.selectById(stationId);
        if (stationDO == null) {
            throw new MyException("电站不存在");
        }
        List<String> deviceList = form.getDeviceList();
        for (String deviceNo : deviceList) {
            //判断设备是否已被报装
            LambdaQueryWrapper<DeviceDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DeviceDO::getDeviceNo, deviceNo);
            DeviceDO one = this.getOne(queryWrapper);
            if (one != null) {
                throw new MyException("设备:" + deviceNo + "已被使用");
            }
            DeviceDO deviceDO = new DeviceDO();
            deviceDO.setDeviceNo(deviceNo);
            deviceDO.setEmuId(emuId);
            deviceDO.setStationId(stationId);
            list.add(deviceDO);
        }
        this.saveOrUpdateBatch(list);

    }

    public List<JSONObject> energyHistory(DeviceRequestForm form) {
        return energyStatisticService.energyData(null, form.getType(), form.getStartTime(), form.getEndTime(), form.getDeviceId());
    }

    public void deleteDevice(Long deviceId) {
        this.removeById(deviceId);
    }

    public JSONObject getUserDevice() {
        SysUserBO user = MyUserUtil.getUser();
        return baseMapper.userDevice(user);
    }
}
