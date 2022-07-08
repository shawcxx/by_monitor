package com.shawcxx.modules.device.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author Chen jl
 * @date 2022/6/24 18:17
 * @description
 **/
@Data
@Document("device_record")
public class DeviceRecordDO {
    private String id;
    private String deviceNo;
    private Double voltage;
    private Double power;
    private Double temperature;
    private Double gridVoltage;
    private Double gridFreq;
    private Date deviceTime;
    private String routeId;
    private Double energy;
    private Integer deviceType;
    private String stationId;
}
