package com.shawcxx.unpack.v1.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.URLEncoder;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shawcxx.common.util.MyHexUtil;
import com.shawcxx.modules.device.dao.DeviceRecordDAO;
import com.shawcxx.modules.device.domain.DeviceDO;
import com.shawcxx.modules.device.domain.DeviceRecordDO;
import com.shawcxx.modules.device.service.DeviceLastTimeService;
import com.shawcxx.modules.device.service.DeviceRecordService;
import com.shawcxx.unpack.BaseUnpackBO;
import com.shawcxx.unpack.BaseUnpackReturnUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chen jl
 * @date 2022/6/20 20:43
 * @description
 **/
@Service("v1Cmd1004Service")
@Slf4j

//F71F0B0E2211 11860033 01 0B 4D22 450C0009E2C038733AE2320900000000000000000000000011860034010B4D2A44FF000B812C34993A79320C00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000009916
public class Cmd1004Service {
    private static final String RETURN_CMD = "1005";
    @Resource
    private DeviceLastTimeService deviceLastTimeService;
    @Resource
    private DeviceRecordService deviceRecordService;

    public String unpack(BaseUnpackBO baseUnpackBO) {
        //todo 解析
        String content = baseUnpackBO.getContent();
        DateTime deviceTime = DateUtil.date();
//        LocalDateTime time = MyHexUtil.getDate(content, 1, 6);
        List<DeviceRecordDO> dataList = new ArrayList();
        int start = 12;
        while (start < content.length()) {
            String data = StrUtil.sub(content, start, start + 64);
            start += 64;
            //出厂编号
            Long deviceId = MyHexUtil.getHexLong(data, 1, 4);
            if (deviceId == 0) {
                continue;
            }
            //原边软件版本
            String midVersion = NumberUtil.decimalFormat("0.0", MyHexUtil.getHexLong(data, 5, 1) / 10.0);
            //副边软件版本
            String sideVersion = NumberUtil.decimalFormat("0.0", MyHexUtil.getHexLong(data, 6, 1) / 10.0);
            //电压
            Double voltage = MyHexUtil.getHexLong(data, 7, 2) * 64 / 32768.0;
            //功率
            double power = MyHexUtil.getHexLong(data, 9, 2) * 512 / 32768.0;
            //发电量
            double energy = MyHexUtil.getHexLong(data, 11, 4) * 4 / 32768.0;
            //温度
            double temperature = MyHexUtil.getHexLong(data, 15, 2) * 256 / 32768.0 - 40;
            //并网电压
            double gridVoltage = MyHexUtil.getHexLong(data, 17, 2) * 512 / 32768.0;
            //并网频率
            double gridFreq = MyHexUtil.getHexLong(data, 19, 2) * 128 / 32768.0;

            //原边故障 (从bit0到bit15)
            int[] midError = MyHexUtil.hexToIntArray(MyHexUtil.getHex(data, 21, 2));
            //副边故障
            int[] sideError = MyHexUtil.hexToIntArray(MyHexUtil.getHex(data, 23, 2));
            DeviceDO deviceDO = new DeviceDO();
            deviceDO.setDeviceNo(deviceId + "");
            deviceDO.setSoftwareVersion(midVersion);
            deviceDO.setDeviceTime(deviceTime);
            deviceDO.setDtuId(baseUnpackBO.getRouteId());
            deviceLastTimeService.data2Domain(deviceDO);

            DeviceRecordDO deviceRecordDO = new DeviceRecordDO();
            deviceRecordDO.setDeviceNo(deviceId + "");
            deviceRecordDO.setVoltage(voltage);
            deviceRecordDO.setPower(power);
            deviceRecordDO.setEnergy(energy);
            deviceRecordDO.setTemperature(temperature);
            deviceRecordDO.setGridVoltage(gridVoltage);
            deviceRecordDO.setGridFreq(gridFreq);
            deviceRecordDO.setDeviceTime(deviceTime);
            deviceRecordDO.setRouteId(baseUnpackBO.getRouteId());
            deviceRecordDO.setDeviceType(1);
            dataList.add(deviceRecordDO);
        }

        if (CollUtil.isNotEmpty(dataList)) {
            try {
                deviceRecordService.saveAll(dataList);
            } catch (Exception e) {
                log.warn("数据入库失败", e);
            }
        }

        return BaseUnpackReturnUtil.getUnpackReturnData(RETURN_CMD, baseUnpackBO.getRouteId(), "00", "00");
    }

}
