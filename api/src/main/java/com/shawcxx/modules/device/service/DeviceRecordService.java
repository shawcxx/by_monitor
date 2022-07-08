package com.shawcxx.modules.device.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSONObject;
import com.shawcxx.modules.device.dao.DeviceRecordDAO;
import com.shawcxx.modules.device.domain.DeviceRecordDO;
import com.shawcxx.modules.device.form.DeviceRequestForm;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
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
                    obj.put("voltage", deviceRecordDO.getVoltage());
                    obj.put("power", deviceRecordDO.getPower());
                    obj.put("temperature", deviceRecordDO.getTemperature());
                    obj.put("gridVoltage", deviceRecordDO.getGridVoltage());
                    obj.put("gridFreq", deviceRecordDO.getGridFreq());
                    obj.put("time", DateUtil.format(deviceRecordDO.getDeviceTime(), "yyyy-MM-dd HH:mm:ss"));
                    map.put(deviceRecordDO.getDeviceTime().getTime(), obj);
                    list.add(obj);
                } else {
                    obj.put("voltage", obj.getDoubleValue("voltage") + deviceRecordDO.getVoltage());
                    obj.put("power", obj.getDoubleValue("power") + deviceRecordDO.getPower());
                    obj.put("temperature", obj.getDoubleValue("temperature") + deviceRecordDO.getTemperature());
                    obj.put("gridVoltage", obj.getDoubleValue("gridVoltage") + deviceRecordDO.getGridVoltage());
                    obj.put("gridFreq", obj.getDoubleValue("gridFreq") + deviceRecordDO.getGridFreq());
                }
            }
        }
        return list;
    }

    public double getStationLastPower(String stationId) {
        double r = 0.0;
        Criteria criteria = Criteria.where("stationId").is(stationId);
        criteria.and("deviceTime").gte(DateUtil.offsetMinute(DateUtil.date(), -30));
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.project("deviceNo", "deviceType", "power", "deviceTime"),
                Aggregation.sort(Sort.Direction.DESC, "deviceTime"),
                Aggregation.group("deviceNo", "deviceType").first("deviceTime").as("deviceTime").first("power").as("power")
        );
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(aggregation, DeviceRecordDO.class, JSONObject.class);
        if (CollUtil.isNotEmpty(results.getMappedResults())) {
            r = results.getMappedResults().stream().mapToDouble(o -> o.getDoubleValue("power")).sum();
        }
        return r;
    }

    public double getStationLastPower(List<String> stationList) {
        double r = 0.0;
        Criteria criteria = Criteria.where("stationId").in(stationList);
        criteria.and("deviceTime").gte(DateUtil.offsetMinute(DateUtil.date(), -30));
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.project("deviceNo", "deviceType", "power", "deviceTime"),
                Aggregation.sort(Sort.Direction.DESC, "deviceTime"),
                Aggregation.group("deviceNo", "deviceType").first("deviceTime").as("deviceTime").first("power").as("power")
        );
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(aggregation, DeviceRecordDO.class, JSONObject.class);
        if (CollUtil.isNotEmpty(results.getMappedResults())) {
            r = results.getMappedResults().stream().mapToDouble(o -> o.getDoubleValue("power")).sum();
        }
        return r;
    }

    public Double getDeviceLastPower(String deviceNo) {
        double r = 0.0;
        Criteria criteria = Criteria.where("deviceNo").is(deviceNo);
        criteria.and("deviceTime").gte(DateUtil.offsetMinute(DateUtil.date(), -30));
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.project("deviceNo", "deviceType", "power", "deviceTime"),
                Aggregation.sort(Sort.Direction.DESC, "deviceTime"),
                Aggregation.group("deviceNo", "deviceType").first("deviceTime").as("deviceTime").first("power").as("power")
        );
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(aggregation, DeviceRecordDO.class, JSONObject.class);
        if (CollUtil.isNotEmpty(results.getMappedResults())) {
            r = results.getMappedResults().stream().mapToDouble(o -> o.getDoubleValue("power")).sum();
        }
        return r;
    }


    public List<JSONObject> stationPowerData(String stationId, Integer type, String startTime, String endTime) {
        List<JSONObject> list = new ArrayList<>();
        Criteria criteria = Criteria.where("stationId").is(stationId);
        criteria.and("deviceTime").gte(DateUtil.parse(startTime)).lte(DateUtil.parse(endTime));
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.sort(Sort.Direction.ASC, "deviceTime"),
                Aggregation.group("deviceTime").sum("power").as("power")
        );
        AggregationResults<JSONObject> results = mongoTemplate.aggregate(aggregation, DeviceRecordDO.class, JSONObject.class);
        if (CollUtil.isNotEmpty(results.getMappedResults())) {

            for (JSONObject mappedResult : results.getMappedResults()) {
                Date time = mappedResult.getDate("_id");
                Double value = mappedResult.getDoubleValue("power");
                JSONObject object = new JSONObject();
                object.put("time", time);
                object.put("value", value);
                list.add(object);
            }
        }
        return list;
    }


}
