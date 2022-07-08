package com.shawcxx.modules.device.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shawcxx.common.base.SysUserBO;
import com.shawcxx.modules.device.domain.DeviceDO;
import com.shawcxx.modules.device.dto.DeviceDTO;
import com.shawcxx.modules.device.form.DeviceRequestForm;
import org.apache.ibatis.annotations.Param;

/**
 * @author Chen jl
 * @date 2022/6/24 17:58
 * @description
 **/
public interface DeviceDAO extends BaseMapper<DeviceDO> {
    Page<DeviceDTO> deviceList(Page page, @Param("form") DeviceRequestForm form);

    JSONObject userDevice(@Param("user") SysUserBO user);
}
