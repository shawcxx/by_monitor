package com.shawcxx.modules.device.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shawcxx.modules.device.dao.DeviceDAO;
import com.shawcxx.modules.device.domain.DeviceDO;
import com.shawcxx.modules.device.dto.DeviceDTO;
import com.shawcxx.modules.device.form.DeviceRequestForm;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * @author Chen jl
 * @date 2022/6/24 19:35
 * @description
 **/
@Service
public class DeviceService extends ServiceImpl<DeviceDAO, DeviceDO> {
    public Page<DeviceDTO> devicePage(DeviceRequestForm form) {
        Page<DeviceDO> page = this.page(new Page<>(form.getCurrent(), form.getSize()));
        Page<DeviceDTO> rPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        rPage.setRecords(page.getRecords().stream().map(o -> {
            DeviceDTO deviceDTO = new DeviceDTO();
            BeanUtil.copyProperties(o, deviceDTO);
            return deviceDTO;
        }).collect(Collectors.toList()));
        return rPage;
    }
}
