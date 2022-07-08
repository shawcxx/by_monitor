package com.shawcxx.modules.device.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 * @author Chen jl
 * @date 2022/6/24 17:55
 * @description
 **/
@Data
@TableName("t_device")

public class DeviceDO {
    @TableId(type = IdType.AUTO)
    private Long deviceId;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String deviceNo;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Integer deviceType;
    private Date deviceTime;
    private String emuId;
    private String hardwareVersion;
    private String softwareVersion;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String stationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeviceDO that = (DeviceDO) o;
        return Objects.equals(deviceNo, that.deviceNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceNo);
    }
}
