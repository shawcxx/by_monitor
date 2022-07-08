package com.shawcxx.modules.device.dao;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shawcxx.common.base.SysUserBO;
import com.shawcxx.modules.device.domain.DeviceEnergyStatisticDO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * (TDeviceEnergyStatistic)表数据库访问层
 *
 * @author cjl
 * @since 2022-07-03 14:53:30
 */
public interface DeviceEnergyStatisticDAO extends BaseMapper<DeviceEnergyStatisticDO> {

    double getUserEnergy(@Param("user") SysUserBO user);

    List<DeviceEnergyStatisticDO> getUserEnergyTrend(@Param("user") SysUserBO user, @Param("statisticType") Integer statisticType, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
