package com.shawcxx.modules.device.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
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
            throw new MyException("???????????????");
        }


        LambdaQueryWrapper<DeviceDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeviceDO::getDeviceNo, emuId);
        DeviceDO emu = this.getOne(queryWrapper);
        if (emu == null) {
            emu = new DeviceDO();
            emu.setDeviceNo(emuId);
            emu.setStationId(stationId);
            emu.setDeviceType(2);
            this.save(emu);
        }

        List<String> deviceList = form.getDeviceList();
        int i = 0;
        for (String deviceNo : deviceList) {
            //??????????????????????????????
            queryWrapper.clear();
            queryWrapper.eq(DeviceDO::getDeviceNo, deviceNo);
            DeviceDO one = this.getOne(queryWrapper);
            if (one != null && !one.getStationId().equals(stationId)) {
                throw new MyException("??????:" + deviceNo + "????????????");
            } else {
                one = new DeviceDO();
                one.setDeviceNo(deviceNo);
                one.setStationId(stationId);
            }
            one.setSort(i);
            one.setDeviceType(1);
            one.setEmuId(emuId);
            list.add(one);
            i++;
        }
        this.saveOrUpdateBatch(list);

    }

    public List<JSONObject> energyHistory(DeviceRequestForm form) {
        return energyStatisticService.energyData(null, form.getType(), form.getStartTime(), form.getEndTime(), form.getDeviceId());
    }

    public void deleteDevice(Long deviceId) {

        DeviceDO deviceDO = this.getById(deviceId);
        if (deviceDO != null) {
            if (deviceDO.getDeviceType() == 2) {
                List<DeviceDO> list = this.list(new LambdaQueryWrapper<DeviceDO>().eq(DeviceDO::getEmuId, deviceDO.getDeviceNo()));
                if (CollUtil.isNotEmpty(list)) {
                    throw new MyException("EMU????????????MI,????????????MI");
                }
            }
            this.removeById(deviceId);
        }
    }

    public JSONObject getUserDevice() {
        SysUserBO user = MyUserUtil.getUser();
        return baseMapper.userDevice(user);
    }
}
