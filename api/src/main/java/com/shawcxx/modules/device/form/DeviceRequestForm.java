package com.shawcxx.modules.device.form;

import com.shawcxx.common.base.BasePageForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Chen jl
 * @date 2022/6/24 19:38
 * @description
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceRequestForm extends BasePageForm {
    private Long deviceId;
    private String stationId;
    private String deviceNo;
    private String softwareVersion;
    private Integer deviceType;
    private Integer type;
    private String startTime;
    private String endTime;
    private List<Long> deptList;
    private Long userId;
}
