package com.shawcxx.modules.device.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author Chen jl
 * @date 2022/6/24 17:43
 * @description
 **/
@Data
public class DeviceDTO {
    private Long deviceId;
    private String deviceNo;
    //    private Integer deviceStatus;
    private String powerStation;
    private Long dtuId;
    private String version;
    private Integer deviceType;
    private String hardwareVersion;
    private String softwareVersion;
    private Date deviceTime;
}
