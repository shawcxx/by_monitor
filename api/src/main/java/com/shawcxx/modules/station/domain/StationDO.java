package com.shawcxx.modules.station.domain;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * (Station)表实体类
 *
 * @author cjl
 * @since 2022-07-02 13:55:15
 */
@Data
@TableName("t_station")
public class StationDO {

    /**
     * 电站id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String stationId;

    /**
     * 电站名称
     */
    private String stationName;

    /**
     * 电站类型
     */
    private String stationType;

    /**
     * 装机容量
     */
    private String stationCapacity;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 地址
     */
    private String address;

    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Integer delFlag;

    /**
     * 更新用户
     */
    private Long updateUserId;

    private Long userId;

    private String timeZone;

    private String country;

    private String location;

    private Long deptId;

}
