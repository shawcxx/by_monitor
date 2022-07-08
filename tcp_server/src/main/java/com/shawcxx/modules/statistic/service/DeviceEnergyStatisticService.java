package com.shawcxx.modules.statistic.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shawcxx.modules.statistic.dao.DeviceEnergyStatisticDAO;
import com.shawcxx.modules.statistic.domain.DeviceEnergyStatisticDO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (TDeviceEnergyStatistic)表服务接口
 *
 * @author cjl
 * @since 2022-07-03 14:53:30
 */
@Service
public class DeviceEnergyStatisticService extends ServiceImpl<DeviceEnergyStatisticDAO, DeviceEnergyStatisticDO> {

}
