package com.shawcxx.modules.device.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

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
    private String deviceNo;
    private Long deviceType;
    private Date createTime;
    private Date updateTime;
    private Date deviceTime;
    private String dtuId;
    private String hardwareVersion;
    private String softwareVersion;
}
