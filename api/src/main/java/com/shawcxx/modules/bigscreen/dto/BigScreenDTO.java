package com.shawcxx.modules.bigscreen.dto;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.shawcxx.modules.station.dto.StationDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cjl
 * @date 2022/7/8 10:45
 * @description
 */
@Data
public class BigScreenDTO {
    private JSONObject stationInfo;
    private List<StationDTO> stationList;
    private List<JSONObject> monthEnergyTrend;
    private List<JSONObject> yearEnergyTrend;
    private Double power;
    private Double energy;
    private JSONObject alarm;
    private JSONObject device;

    public BigScreenDTO() {
        this.power = 0.0;
        this.energy = 0.0;

        JSONObject stationInfo = new JSONObject();
        stationInfo.put("totalCapacity", 0);
        stationInfo.put("inUseCapacity", 0);
        stationInfo.put("unUseCapacity", 0);
        stationInfo.put("totalStation", 0);
        stationInfo.put("alarm", 0);
        stationInfo.put("normal", 0);
        stationInfo.put("offline", 0);
        stationInfo.put("build", 0);
        this.stationInfo = stationInfo;

        this.stationList = new ArrayList<>();

        List<JSONObject> monthEnergyTrend = new ArrayList<>();
        for (int i = 1; i <= DateUtil.endOfMonth(DateUtil.date()).dayOfMonth(); i++) {
            String time = StrUtil.padPre(i + "", 2, '0');

            JSONObject data = new JSONObject();
            data.put("time", time);
            data.put("value", 0.0);
            monthEnergyTrend.add(data);
        }
        this.monthEnergyTrend = monthEnergyTrend;

        List<JSONObject> yearEnergyTrend = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            JSONObject data = new JSONObject();
            String time = StrUtil.padPre(i + "", 2, '0');
            data.put("time", time);
            data.put("value", 0.0);
            yearEnergyTrend.add(data);
        }
        this.yearEnergyTrend = yearEnergyTrend;

        this.alarm = new JSONObject();

        JSONObject device = new JSONObject();
        device.put("total", 0);
        device.put("mi", 0);
        device.put("emu", 0);
        this.device = device;
    }
}
