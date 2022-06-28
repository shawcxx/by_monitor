package com.shawcxx.modules.device.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSONObject;
import com.shawcxx.modules.device.dao.DeviceRecordDAO;
import com.shawcxx.modules.device.domain.DeviceRecordDO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Chen jl
 * @date 2022/6/24 19:35
 * @description
 **/
@Service
public class DeviceRecordService {
    @Resource
    private DeviceRecordDAO deviceRecordDAO;
    @Resource
    private MongoTemplate mongoTemplate;

    public List<JSONObject> deviceRecord(String deviceNo, Integer type) {
        DateTime startTime;
        DateTime endTime = DateUtil.date();

        if (type == 1) {
            startTime = DateUtil.beginOfDay(endTime);
        } else {
            startTime = DateUtil.beginOfDay(DateUtil.offsetDay(endTime, -7));
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("deviceNo").is(deviceNo));
        query.addCriteria(Criteria.where("deviceTime").gte(startTime).lte(endTime));
        Sort sort = Sort.by(Sort.Direction.ASC, "deviceTime");
        query.with(sort);
        List<DeviceRecordDO> dataList = mongoTemplate.find(query, DeviceRecordDO.class);
        ArrayList<JSONObject> list = new ArrayList<>();
        HashMap<Long, JSONObject> map = new HashMap<>();
        for (DeviceRecordDO deviceRecordDO : dataList) {
            if (deviceRecordDO.getDeviceTime() != null) {
                JSONObject obj = map.get(deviceRecordDO.getDeviceTime().getTime());
                if (obj == null) {
                    obj = new JSONObject();
                    obj.put("voltage", NumberUtil.decimalFormat("0.00", deviceRecordDO.getVoltage()));
                    obj.put("power", NumberUtil.decimalFormat("0.00", deviceRecordDO.getPower()));
                    obj.put("energy", NumberUtil.decimalFormat("0.00", deviceRecordDO.getEnergy()));
                    obj.put("temperature", NumberUtil.decimalFormat("0.00", deviceRecordDO.getTemperature()));
                    obj.put("gridVoltage", NumberUtil.decimalFormat("0.00", deviceRecordDO.getGridVoltage()));
                    obj.put("gridFreq", NumberUtil.decimalFormat("0.00", deviceRecordDO.getGridFreq()));
                    obj.put("time", DateUtil.format(deviceRecordDO.getDeviceTime(), "yyyy-MM-dd HH:mm:ss"));
                    map.put(deviceRecordDO.getDeviceTime().getTime(), obj);
                    list.add(obj);
                } else {
                    obj.put("voltage", NumberUtil.decimalFormat("0.00", obj.getDoubleValue("voltage") + deviceRecordDO.getVoltage()));
                    obj.put("power", NumberUtil.decimalFormat("0.00", obj.getDoubleValue("power") + deviceRecordDO.getPower()));
                    obj.put("energy", NumberUtil.decimalFormat("0.00", obj.getDoubleValue("energy") + deviceRecordDO.getEnergy()));
                    obj.put("temperature", NumberUtil.decimalFormat("0.00", obj.getDoubleValue("temperature") + deviceRecordDO.getTemperature()));
                    obj.put("gridVoltage", NumberUtil.decimalFormat("0.00", obj.getDoubleValue("gridVoltage") + deviceRecordDO.getGridVoltage()));
                    obj.put("gridFreq", NumberUtil.decimalFormat("0.00", obj.getDoubleValue("gridFreq") + deviceRecordDO.getGridFreq()));
                }
            }
        }
        return list;
    }
}
