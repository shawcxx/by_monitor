package com.shawcxx.modules.station.dto;

import cn.hutool.json.JSONObject;
import lombok.Data;

import java.util.Date;

/**
 * @author cjl
 * @date 2022/7/2 15:50
 * @description
 */
@Data
public class StationDTO {
    private String stationId;
    private String stationName;
    private String address;
    private String stationType;
    private Double stationCapacity;
    private String owner;
    private Double power;
    private Double energy;
    private Object energyTrend;
    private Object alarmInfo;
    /**
     * -1 未使用,0 正常,1 离线,2 报警
     */
    private Integer stationStatus = 0;
    private Date updateTime;
    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;
    private String timeZone;
    private String country;
    private String location;
    private String installer;
    private Integer inUse;

}
