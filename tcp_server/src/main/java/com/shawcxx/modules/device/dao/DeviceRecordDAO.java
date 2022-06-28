package com.shawcxx.modules.device.dao;

import com.shawcxx.modules.device.domain.DeviceRecordDO;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Chen jl
 * @date 2022/6/24 18:23
 * @description
 **/
public interface DeviceRecordDAO extends MongoRepository<DeviceRecordDO, String> {
}
