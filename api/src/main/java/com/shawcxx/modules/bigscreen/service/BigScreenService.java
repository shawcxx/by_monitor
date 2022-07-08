package com.shawcxx.modules.bigscreen.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shawcxx.common.base.MyResult;
import com.shawcxx.common.base.SysUserBO;
import com.shawcxx.common.utils.MyUserUtil;
import com.shawcxx.modules.bigscreen.dto.BigScreenDTO;
import com.shawcxx.modules.device.domain.DeviceDO;
import com.shawcxx.modules.device.domain.DeviceEnergyStatisticDO;
import com.shawcxx.modules.device.service.DeviceEnergyStatisticService;
import com.shawcxx.modules.device.service.DeviceRecordService;
import com.shawcxx.modules.device.service.DeviceService;
import com.shawcxx.modules.station.dto.StationDTO;
import com.shawcxx.modules.station.form.StationRequestForm;
import com.shawcxx.modules.station.service.StationService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cjl
 * @date 2022/7/8 10:45
 * @description
 */
@Service
public class BigScreenService {
    @Resource
    private StationService stationService;

    @Resource
    private DeviceEnergyStatisticService deviceEnergyStatisticService;

    @Resource
    private DeviceService deviceService;

    @Resource
    private DeviceRecordService deviceRecordService;

    public BigScreenDTO userBigScreen() {
        SysUserBO user = MyUserUtil.getUser();
        BigScreenDTO bigScreenDTO = new BigScreenDTO();
        StationRequestForm stationRequestForm = new StationRequestForm();
        stationRequestForm.setCurrent(1);
        stationRequestForm.setSize(9999);
        stationRequestForm.setQueryData(false);
        Page<StationDTO> page = stationService.stationList(stationRequestForm);
        List<StationDTO> stationList = page.getRecords();

        if (CollUtil.isNotEmpty(stationList)) {
            double energy = deviceEnergyStatisticService.getUserEnergy();
            bigScreenDTO.setEnergy(energy);

            bigScreenDTO.setAlarm(null);

            bigScreenDTO.setDevice(deviceService.getUserDevice());

            double power = deviceRecordService.getStationLastPower(stationList.stream().map(StationDTO::getStationId).collect(Collectors.toList()));
            bigScreenDTO.setPower(power);

            JSONObject stationInfo = new JSONObject();
            stationInfo.put("totalCapacity", stationList.stream().filter(stationDTO -> stationDTO.getStationCapacity() != null).mapToDouble(StationDTO::getStationCapacity).sum());
            stationInfo.put("inUseCapacity", stationList.stream().filter(stationDTO -> stationDTO.getInUse() == 1).filter(stationDTO -> stationDTO.getStationCapacity() != null).mapToDouble(StationDTO::getStationCapacity).sum());
            stationInfo.put("unUseCapacity", stationList.stream().filter(stationDTO -> stationDTO.getInUse() == 0).filter(stationDTO -> stationDTO.getStationCapacity() != null).mapToDouble(StationDTO::getStationCapacity).sum());
            stationInfo.put("totalStation", stationList.size());
            stationInfo.put("alarm", stationList.stream().filter(stationDTO -> stationDTO.getStationStatus() == 2).count());
            stationInfo.put("normal", stationList.stream().filter(stationDTO -> stationDTO.getStationStatus() == 0).count());
            stationInfo.put("offline", stationList.stream().filter(stationDTO -> stationDTO.getStationStatus() == 1).count());
            stationInfo.put("build", stationList.stream().filter(stationDTO -> stationDTO.getStationStatus() == -1).count());
            bigScreenDTO.setStationInfo(stationInfo);


            bigScreenDTO.setMonthEnergyTrend(deviceEnergyStatisticService.getUserEnergyTrend(1));
            bigScreenDTO.setYearEnergyTrend(deviceEnergyStatisticService.getUserEnergyTrend(2));
            bigScreenDTO.setStationList(stationList);
        }
        return bigScreenDTO;
    }
}
