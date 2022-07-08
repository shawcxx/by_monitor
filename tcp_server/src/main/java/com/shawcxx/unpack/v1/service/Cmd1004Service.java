package com.shawcxx.unpack.v1.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.shawcxx.common.util.MyHexUtil;
import com.shawcxx.modules.device.domain.DeviceDO;
import com.shawcxx.modules.device.domain.DeviceRecordDO;
import com.shawcxx.modules.device.service.DeviceLastTimeService;
import com.shawcxx.modules.device.service.DeviceRecordService;
import com.shawcxx.modules.device.service.DeviceService;
import com.shawcxx.modules.statistic.service.EnergyService;
import com.shawcxx.unpack.BaseUnpackBO;
import com.shawcxx.unpack.BaseUnpackReturnUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chen jl
 * @date 2022/6/20 20:43
 * @description
 **/
@Service("v1Cmd1004Service")
@Slf4j

public class Cmd1004Service {
    private static final String RETURN_CMD = "1005";
    @Resource
    private DeviceLastTimeService deviceLastTimeService;
    @Resource
    private DeviceRecordService deviceRecordService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private EnergyService energyService;

    public String unpack(BaseUnpackBO baseUnpackBO) {
        String content = baseUnpackBO.getContent();
        DateTime deviceTime = DateUtil.date();
//        LocalDateTime time = MyHexUtil.getDate(content, 1, 6);
        List<DeviceRecordDO> dataList = new ArrayList<>();
        int start = 12;
        while (start < content.length()) {
            String data = StrUtil.sub(content, start, start + 64);
            start += 64;
            //出厂编号
            String deviceId = MyHexUtil.getHex(data, 1, 4);
            if ("00000000".equals(deviceId)) {
                continue;
            }
            //原边软件版本
            String midVersion = NumberUtil.decimalFormat("0.0", MyHexUtil.getHexLong(data, 5, 1) / 10.0);
            //副边软件版本
            String sideVersion = NumberUtil.decimalFormat("0.0", MyHexUtil.getHexLong(data, 6, 1) / 10.0);
            //电压
            Double voltage = Math.round(MyHexUtil.getHexLong(data, 7, 2) * 64 / 32768.0 * 100) / 100.0;
            //功率
            double power = Math.round(MyHexUtil.getHexLong(data, 9, 2) * 1024 / 32768.0 * 100) / 100.0;
            //发电量
            double energy = Math.round(MyHexUtil.getHexLong(data, 11, 4) * 4 / 32768.0 * 100) / 100.0;
            //温度
            double temperature = Math.round((MyHexUtil.getHexLong(data, 15, 2) * 256 / 32768.0 - 40) * 100) / 100.0;
            //并网电压
            double gridVoltage = Math.round(MyHexUtil.getHexLong(data, 17, 2) * 512 / 32768.0 * 100) / 100.0;
            //并网频率
            double gridFreq = Math.round(MyHexUtil.getHexLong(data, 19, 2) * 128 / 32768.0 * 100) / 100.0;

            //原边故障 (从bit0到bit15)
            int[] midError = MyHexUtil.hexToIntArray(MyHexUtil.getHex(data, 21, 2));
            //副边故障
            int[] sideError = MyHexUtil.hexToIntArray(MyHexUtil.getHex(data, 23, 2));

            DeviceDO deviceDO = deviceService.getDevice(deviceId);
            if (deviceDO == null) {
                continue;
            }
            deviceDO.setSoftwareVersion(midVersion);
            deviceDO.setDeviceTime(deviceTime);
            deviceDO.setEmuId(baseUnpackBO.getRouteId());
            deviceLastTimeService.data2Domain(deviceDO);

            DeviceRecordDO deviceRecordDO = new DeviceRecordDO();
            deviceRecordDO.setStationId(deviceDO.getStationId());
            deviceRecordDO.setDeviceNo(deviceId);
            deviceRecordDO.setVoltage(voltage);
            deviceRecordDO.setPower(power);
            deviceRecordDO.setTemperature(temperature);
            deviceRecordDO.setGridVoltage(gridVoltage);
            deviceRecordDO.setGridFreq(gridFreq);
            deviceRecordDO.setEnergy(energy);
            deviceRecordDO.setDeviceTime(deviceTime);
            deviceRecordDO.setRouteId(baseUnpackBO.getRouteId());
            deviceRecordDO.setDeviceType(deviceDO.getDeviceType());
            dataList.add(deviceRecordDO);

            //发电量处理
            energyService.saveEnergy(deviceId, energy, deviceTime, 1);
        }
        DeviceDO routeDevice = deviceService.getDevice(baseUnpackBO.getRouteId());
        if (routeDevice != null) {
            routeDevice.setDeviceTime(deviceTime);
            deviceLastTimeService.data2Domain(routeDevice);
        }
        if (CollUtil.isNotEmpty(dataList)) {
            try {
                deviceRecordService.saveAll(dataList);
            } catch (Exception e) {
                log.warn("数据入库失败", e);
            }
        }

        return BaseUnpackReturnUtil.getV1UnpackReturnData(RETURN_CMD, baseUnpackBO.getRouteId(), "00", "00");
    }


}
