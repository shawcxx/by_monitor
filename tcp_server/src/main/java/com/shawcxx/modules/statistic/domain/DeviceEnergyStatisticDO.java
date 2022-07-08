package com.shawcxx.modules.statistic.domain;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * (TDeviceEnergyStatistic)表实体类
 *
 * @author cjl
 * @since 2022-07-03 14:53:29
 */
@Data
@TableName("t_device_energy_statistic")
public class DeviceEnergyStatisticDO {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 统计时间
     */
    private String statisticTime;

    /**
     * 电站id
     */
    private String stationId;

    /**
     * 电量
     */
    private Double energyValue;

    /**
     * 统计类型(1 日 2 月 3年)
     */
    private Integer statisticType;


    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 设备类型
     */
    private Integer deviceType;
}
