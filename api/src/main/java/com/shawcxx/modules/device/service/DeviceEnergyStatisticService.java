package com.shawcxx.modules.device.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shawcxx.common.base.SysUserBO;
import com.shawcxx.common.utils.MyUserUtil;
import com.shawcxx.modules.device.dao.DeviceEnergyStatisticDAO;
import com.shawcxx.modules.device.domain.DeviceEnergyStatisticDO;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * (TDeviceEnergyStatistic)表服务接口
 *
 * @author cjl
 * @since 2022-07-03 14:53:30
 */
@Service
public class DeviceEnergyStatisticService extends ServiceImpl<DeviceEnergyStatisticDAO, DeviceEnergyStatisticDO> {

    public Double getStationDayEnergy(String stationId, Date date) {
        double energy = 0.00;
        String statisticTime = DateUtil.format(date, "yyyy-MM-dd");
        LambdaQueryWrapper<DeviceEnergyStatisticDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeviceEnergyStatisticDO::getStatisticTime, statisticTime);
        queryWrapper.eq(DeviceEnergyStatisticDO::getStationId, stationId);
        queryWrapper.eq(DeviceEnergyStatisticDO::getStatisticType, 1);
        List<DeviceEnergyStatisticDO> list = this.list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            energy = list.stream().mapToDouble(DeviceEnergyStatisticDO::getEnergyValue).sum();
        }
        return energy;
    }

    public Double getDeviceDayEnergy(Long deviceId, DateTime date) {
        double energy = 0.00;
        String statisticTime = DateUtil.format(date, "yyyy-MM-dd");
        LambdaQueryWrapper<DeviceEnergyStatisticDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeviceEnergyStatisticDO::getStatisticTime, statisticTime);
        queryWrapper.eq(DeviceEnergyStatisticDO::getDeviceId, deviceId);
        queryWrapper.eq(DeviceEnergyStatisticDO::getStatisticType, 1);
        List<DeviceEnergyStatisticDO> list = this.list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            energy = list.stream().mapToDouble(DeviceEnergyStatisticDO::getEnergyValue).sum();
        }
        return energy;
    }

    public JSONArray getEnergyDayTrend(String stationId, DateTime date) {
        JSONArray r = new JSONArray();
        String statisticTime = DateUtil.format(date, "yyyy-MM-dd");
        LambdaQueryWrapper<DeviceEnergyStatisticDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(DeviceEnergyStatisticDO::getStatisticTime, statisticTime);
        queryWrapper.eq(DeviceEnergyStatisticDO::getStationId, stationId);
        queryWrapper.eq(DeviceEnergyStatisticDO::getStatisticType, 0);
        List<DeviceEnergyStatisticDO> list = this.list(queryWrapper);
        HashMap<Integer, JSONObject> map = new HashMap<>();
        for (int i = 0; i < 24; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("time", i);
            jsonObject.put("value", 0.00);
            map.put(i, jsonObject);
            r.add(jsonObject);
        }
        for (DeviceEnergyStatisticDO statisticDO : list) {
            DateTime time = DateUtil.parse(statisticDO.getStatisticTime(), "yyyy-MM-dd HH");
            int hour = DateUtil.hour(time, true);
            JSONObject object = map.get(hour);
            object.put("value", object.getDouble("value") + statisticDO.getEnergyValue());
        }
        return r;
    }

    public List<JSONObject> energyData(String stationId, Integer type, String startTime, String endTime, Long deviceId) {
        List<JSONObject> list = new ArrayList<>();
        HashMap<String, JSONObject> map = new HashMap<>();
        String format;
        DateTime start;
        DateTime end;
        DateTime temp;
        int statisticType;
        switch (type) {
            //日
            case 1:
                format = "yyyy-MM-dd HH";
                start = DateUtil.beginOfDay(DateUtil.parse(startTime, "yyyy-MM-dd"));
                end = DateUtil.endOfDay(DateUtil.parse(startTime, "yyyy-MM-dd"));
                statisticType = 0;
                for (int i = 0; i < 24; i++) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("time", i);
                    jsonObject.put("value", 0.00);
                    map.put(i + "", jsonObject);
                    list.add(jsonObject);
                }
                break;
            //周
            case 2:
                format = "yyyy-MM-dd";
                statisticType = 1;
                end = DateUtil.endOfDay(DateUtil.parse(endTime, "yyyy-MM-dd"));
                start = DateUtil.beginOfMonth(DateUtil.offsetDay(end, -6));
                for (int i = 6; i >= 0; i--) {
                    String time = DateUtil.format(DateUtil.offsetDay(end, -i), format);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("time", time);
                    jsonObject.put("value", 0.00);
                    map.put(time, jsonObject);
                    list.add(jsonObject);
                }
                break;
            //月
            case 3:
                format = "yyyy-MM-dd";
                statisticType = 1;
                end = DateUtil.endOfMonth(DateUtil.parse(endTime, "yyyy-MM"));
                start = DateUtil.beginOfMonth(end);
                temp = start;

                for (; temp.before(end); temp = DateUtil.offsetDay(temp, 1)) {
                    String time = DateUtil.format(temp, format);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("time", time);
                    jsonObject.put("value", 0.00);
                    map.put(time, jsonObject);
                    list.add(jsonObject);
                }
                break;
            //年
            case 4:
                format = "yyyy-MM";
                statisticType = 2;
                end = DateUtil.endOfYear(DateUtil.parse(endTime, "yyyy"));
                start = DateUtil.beginOfYear(end);
                for (int i = 11; i >= 0; i--) {
                    String time = DateUtil.format(DateUtil.offsetMonth(end, -i), format);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("time", time);
                    jsonObject.put("value", 0.00);
                    map.put(time, jsonObject);
                    list.add(jsonObject);
                }
                break;
            //总
            case 5:
                format = "yyyy";
                statisticType = 3;
                start = DateUtil.beginOfYear(DateUtil.parse(startTime, "yyyy"));
                end = DateUtil.endOfYear(DateUtil.parse(endTime, "yyyy"));
                temp = start;
                for (; temp.before(end); temp = DateUtil.offset(temp, DateField.YEAR, 1)) {
                    String time = DateUtil.format(temp, format);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("time", time);
                    jsonObject.put("value", 0.00);
                    map.put(time, jsonObject);
                    list.add(jsonObject);
                }
                break;
            default:
                return list;
        }

        LambdaQueryWrapper<DeviceEnergyStatisticDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(DeviceEnergyStatisticDO::getStatisticTime, start.offset(DateField.SECOND, -1));
        queryWrapper.le(DeviceEnergyStatisticDO::getStatisticTime, end);
        queryWrapper.eq(StrUtil.isNotBlank(stationId), DeviceEnergyStatisticDO::getStationId, stationId);
        queryWrapper.eq(deviceId != null, DeviceEnergyStatisticDO::getDeviceId, deviceId);
        queryWrapper.eq(DeviceEnergyStatisticDO::getStatisticType, statisticType);
        List<DeviceEnergyStatisticDO> energyList = this.list(queryWrapper);
        for (DeviceEnergyStatisticDO statisticDO : energyList) {
            if (type == 1) {
                DateTime time = DateUtil.parse(statisticDO.getStatisticTime(), format);
                int hour = DateUtil.hour(time, true);
                JSONObject object = map.get(hour + "");
                object.put("value", object.getDouble("value") + statisticDO.getEnergyValue());
            } else {
                JSONObject object = map.get(statisticDO.getStatisticTime());
                object.put("value", object.getDouble("value") + statisticDO.getEnergyValue());
            }
        }
        return list;
    }

    public double getUserEnergy() {
        SysUserBO user = MyUserUtil.getUser();
        return baseMapper.getUserEnergy(user);
    }

    public List<JSONObject> getUserEnergyTrend(int statisticType) {
        List<JSONObject> list = new ArrayList<>();
        HashMap<String, JSONObject> map = new HashMap<>();
        String format;
        String parse;
        DateTime startTime;
        DateTime endTime;
        switch (statisticType) {
            case 1:
                startTime = DateUtil.beginOfMonth(DateUtil.date());
                endTime = DateUtil.endOfMonth(DateUtil.date());
                format = "dd";
                parse = "yyyy-MM-dd";
                for (int i = 1; i <= DateUtil.endOfMonth(DateUtil.date()).dayOfMonth(); i++) {
                    String time = StrUtil.padPre(i + "", 2, '0');
                    JSONObject data = new JSONObject();
                    data.put("time", time);
                    data.put("value", 0.0);
                    map.put(time + "", data);
                    list.add(data);
                }
                break;
            case 2:
                startTime = DateUtil.beginOfYear(DateUtil.date());
                endTime = DateUtil.endOfYear(DateUtil.date());
                format = "MM";
                parse = "yyyy-MM";
                for (int i = 1; i <= 12; i++) {
                    String time = StrUtil.padPre(i + "", 2, '0');
                    JSONObject data = new JSONObject();
                    data.put("time", time);
                    data.put("value", 0.0);
                    map.put(time + "", data);
                    list.add(data);
                }
                break;
            default:
                return list;
        }
        SysUserBO user = MyUserUtil.getUser();
        List<DeviceEnergyStatisticDO> energyList = baseMapper.getUserEnergyTrend(user, statisticType, startTime.offset(DateField.SECOND, -1), endTime);
        for (DeviceEnergyStatisticDO deviceEnergyStatisticDO : energyList) {
            JSONObject object = map.get(DateUtil.parse(deviceEnergyStatisticDO.getStatisticTime(), parse).toString(format));
            object.put("value", object.getDouble("value") + deviceEnergyStatisticDO.getEnergyValue());
        }
        return list;
    }

    public JSONObject stationEnergyStatistic(String stationId) {
        JSONObject energyStatistic = new JSONObject();
        DateTime now = DateUtil.date();
        LambdaQueryWrapper<DeviceEnergyStatisticDO> q = new LambdaQueryWrapper<>();
        q.eq(DeviceEnergyStatisticDO::getStatisticType, 1);
        q.eq(DeviceEnergyStatisticDO::getStationId, stationId);
        q.eq(DeviceEnergyStatisticDO::getStatisticTime, DateUtil.format(now, "yyyy-MM-dd"));
        List<DeviceEnergyStatisticDO> list = this.list(q);
        double sum = list.stream().mapToDouble(DeviceEnergyStatisticDO::getEnergyValue).sum();
        energyStatistic.put("1", sum);

        q.clear();
        q.eq(DeviceEnergyStatisticDO::getStatisticType, 2);
        q.eq(DeviceEnergyStatisticDO::getStationId, stationId);
        q.eq(DeviceEnergyStatisticDO::getStatisticTime, DateUtil.format(now, "yyyy-MM"));
        list = this.list(q);
        sum = list.stream().mapToDouble(DeviceEnergyStatisticDO::getEnergyValue).sum();
        energyStatistic.put("2", sum);

        q.clear();
        q.eq(DeviceEnergyStatisticDO::getStatisticType, 3);
        q.eq(DeviceEnergyStatisticDO::getStationId, stationId);
        q.eq(DeviceEnergyStatisticDO::getStatisticTime, DateUtil.format(now, "yyyy"));
        list = this.list(q);
        sum = list.stream().mapToDouble(DeviceEnergyStatisticDO::getEnergyValue).sum();
        energyStatistic.put("3", sum);

        q.clear();
        q.eq(DeviceEnergyStatisticDO::getStatisticType, 3);
        q.eq(DeviceEnergyStatisticDO::getStationId, stationId);
        list = this.list(q);
        sum = list.stream().mapToDouble(DeviceEnergyStatisticDO::getEnergyValue).sum();
        energyStatistic.put("4", sum);

        return energyStatistic;
    }
}
