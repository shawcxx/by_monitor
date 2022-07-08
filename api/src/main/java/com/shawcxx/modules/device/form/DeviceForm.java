package com.shawcxx.modules.device.form;

import lombok.Data;

import java.util.List;

/**
 * @author cjl
 * @date 2022/7/5 9:45
 * @description
 */
@Data
public class DeviceForm {
    private List<String> deviceList;
    private String stationId;
    private String emuId;

}
