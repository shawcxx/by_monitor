package com.shawcxx.modules.device.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shawcxx.common.base.MyResult;
import com.shawcxx.modules.device.dto.DeviceDTO;
import com.shawcxx.modules.device.form.DeviceRequestForm;
import com.shawcxx.modules.device.service.DeviceRecordService;
import com.shawcxx.modules.device.service.DeviceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Chen jl
 * @date 2022/6/24 19:34
 * @description
 **/
@RestController
@RequestMapping("device")
public class DeviceController {
    @Resource
    private DeviceService deviceService;
    @Resource
    private DeviceRecordService deviceRecordService;

    @PostMapping("list")
    public MyResult deviceList(@RequestBody DeviceRequestForm form) {
        Page<DeviceDTO> page = deviceService.devicePage(form);
        return MyResult.data(page);
    }

    @PostMapping("record")
    public MyResult deviceRecord(@RequestParam String deviceNo, @RequestParam Integer type) {
        List<JSONObject> list = deviceRecordService.deviceRecord(deviceNo, type);
        return MyResult.data(list);
    }
}
