package com.shawcxx.modules.device.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Chen jl
 * @date 2022/6/24 17:43
 * @description
 **/
@Data
public class DeviceDTO {
    private Long deviceId;
    private String deviceNo;
    // 1:正常 2:离线 3:故障 4:报警
    private Integer deviceStatus = 1;
    private String stationName;
    private Long dtuId;
    private String version;
    private Integer deviceType;
    private String hardwareVersion;
    private String softwareVersion;
    private Date deviceTime;
    private List<DeviceDTO> list;
    private Double power;
    private Double energy;
    private Object energyTrend;
}
