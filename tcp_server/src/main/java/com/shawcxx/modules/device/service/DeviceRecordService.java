package com.shawcxx.modules.device.service;

import cn.hutool.core.collection.CollUtil;
import com.shawcxx.modules.device.dao.DeviceRecordDAO;
import com.shawcxx.modules.device.domain.DeviceRecordDO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Chen jl
 * @date 2022/6/24 18:28
 * @description
 **/
@Service
@Slf4j
public class DeviceRecordService {
    @Resource
    private DeviceRecordDAO deviceRecordDAO;
    @Resource
    private MongoTemplate mongoTemplate;
    private static final ConcurrentLinkedQueue<DeviceRecordDO> queue = new ConcurrentLinkedQueue<>();

    public void saveAll(List<DeviceRecordDO> dataList) {
        for (DeviceRecordDO deviceRecordDO : dataList) {
            queue.offer(deviceRecordDO);
        }
    }

    @Scheduled(fixedDelay = 1000 * 5)
    @PreDestroy
    public void saveData() {
        int limit = 0;
        List<DeviceRecordDO> list = new ArrayList<>();
        while (!queue.isEmpty()) {
            DeviceRecordDO deviceRecordDO = queue.poll();
            if (deviceRecordDO == null) {
                break;
            }
            list.add(deviceRecordDO);
            limit++;
            if (limit >= 10000) {
                saveData(list);
                list.clear();
                limit = 0;
            }
        }
        if (CollUtil.isNotEmpty(list)) {
            saveData(list);
        }
    }

    private void saveData(List<DeviceRecordDO> list) {
        Long start = System.currentTimeMillis();
        deviceRecordDAO.saveAll(list);
        Long end = System.currentTimeMillis();
        log.info("存储数量:{},花费时间:{}", list.size(), end - start);
    }

    public DeviceRecordDO getLastData(String deviceNo, Integer deviceType) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deviceNo").is(deviceNo).and("deviceType").is(deviceType));
        query.with(Sort.by(Sort.Direction.DESC, "deviceTime"));
        query.limit(1);
        DeviceRecordDO one = mongoTemplate.findOne(query, DeviceRecordDO.class);
        return one;
    }
}
