package com.shawcxx.modules.station.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shawcxx.common.base.MyResult;
import com.shawcxx.common.validate.Edit;
import com.shawcxx.common.validate.Insert;
import com.shawcxx.modules.device.dto.DeviceDTO;
import com.shawcxx.modules.station.dto.StationDTO;
import com.shawcxx.modules.station.form.StationForm;
import com.shawcxx.modules.station.form.StationRequestForm;
import com.shawcxx.modules.station.service.StationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * (Station)表控制层
 *
 * @author cjl
 * @since 2022-07-02 13:55:18
 */
@RestController
@RequestMapping("station")
public class StationController {
    @Resource
    private StationService stationService;

    /**
     * 电站列表
     *
     * @param form
     * @return
     */
    @PostMapping("list")
    public MyResult list(@RequestBody StationRequestForm form) {
        Page<StationDTO> page = stationService.stationList(form);
        return MyResult.data(page);
    }


    /**
     * 电站详情
     *
     * @param stationId
     * @return
     */
    @PostMapping("info")
    public MyResult info(@RequestParam String stationId) {
        StationDTO stationDTO = stationService.stationInfo(stationId);
        return MyResult.data(stationDTO);
    }

    /**
     * 创建电站
     *
     * @param stationForm
     * @return
     */
    @PostMapping("createStation")
    public MyResult createStation(@RequestBody @Validated(value = Insert.class) StationForm stationForm) {
        StationDTO stationDTO = stationService.createStation(stationForm);
        return MyResult.data(stationDTO);
    }

    /**
     * 编辑电站
     *
     * @param stationForm
     * @return
     */
    @PostMapping("modifyStation")
    public MyResult modifyStation(@RequestBody @Validated(value = Edit.class) StationForm stationForm) {
        stationService.modifyStation(stationForm);
        return MyResult.ok();
    }


    /**
     * 删除电站
     *
     * @param stationId
     * @return
     */
    @PostMapping("deleteStation")
    public MyResult deleteStation(@RequestParam String stationId) {
        stationService.deleteStation(stationId);
        return MyResult.ok();
    }


    /**
     * 电站功率/发电量历史数据
     *
     * @return type 1 日 2周 3月 4 年 5总
     */
    @PostMapping("historicalData")
    public MyResult historyData(@RequestBody StationRequestForm form) {
        List<JSONObject> list = stationService.historyData(form);
        return MyResult.data(list);
    }

    @PostMapping("deviceList")
    public MyResult deviceList(@RequestParam String stationId, @RequestParam Integer type) {
        List<DeviceDTO> list = stationService.deviceList(stationId, type);
        return MyResult.data(list);
    }


    @PostMapping("linkStation")
    public MyResult linkStation(@RequestBody StationRequestForm form) {
        stationService.linkStation(form);
        return MyResult.ok();
    }

    /**
     * 数据导出
     *
     * @param form
     * @param response
     * @return
     */
    @PostMapping("dataExport")
    public MyResult dataExport(@RequestBody StationRequestForm form, HttpServletResponse response) {
        stationService.dataExport(form, response);
        return MyResult.ok();
    }
}
