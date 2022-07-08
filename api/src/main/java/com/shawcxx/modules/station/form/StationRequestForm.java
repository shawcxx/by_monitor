package com.shawcxx.modules.station.form;

import com.shawcxx.common.base.BasePageForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @author cjl
 * @date 2022/7/2 15:48
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StationRequestForm extends BasePageForm {
    /**
     * 电站状态 1 正常 2报警
     */
    private Integer stationStatus;
    /**
     * 电站名称
     */
    private String stationName;

    private String stationId;
    private Integer type;
    private String item;
    private String startTime;
    private String endTime;
    private List<Long> deptList;
    private String username;
    private Long userId;
    private Boolean queryData;
}
