package com.shawcxx.unpack.v1.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shawcxx.modules.device.domain.DeviceDO;
import com.shawcxx.modules.device.domain.DeviceRecordDO;
import com.shawcxx.modules.device.service.DeviceService;
import com.shawcxx.unpack.BaseUnpackBO;
import com.shawcxx.unpack.BaseUnpackReturnUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Chen jl
 * @date 2022/6/19 21:01
 * @description
 **/
@Service("v1Cmd1000Service")
@Slf4j
public class Cmd1000Service {
    @Resource
    private DeviceService deviceService;
    private static final String RETURN_CMD = "1001";
    private static final String DEFAULT_MSG = "0000";


    public String unpack(BaseUnpackBO baseUnpackBO) {

        //获取注册台数
        String msg;
        String emuId = baseUnpackBO.getRouteId();
        LambdaQueryWrapper<DeviceDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeviceDO::getEmuId, emuId);
        List<DeviceDO> list = deviceService.list(queryWrapper);
        if (CollUtil.isEmpty(list)) {
            msg = DEFAULT_MSG;
        } else {
            int total = list.size();
            if (total > 200) {
                total = 200;
            }
            String s2 = "";
            int i = 0;
            List<DeviceDO> collect = list.stream().limit(200).collect(Collectors.toList());
            for (DeviceDO deviceDO : collect) {
                String index = StrUtil.padPre(Long.toHexString(i + 1), 4, '0');
                String deviceNo = deviceDO.getDeviceNo();
                i++;
                s2 += index + StrUtil.padPre(deviceNo, 8, '0');
            }
            String s1 = StrUtil.padPre(Long.toHexString(total), 4, '0');
            msg = s1 + s2;
        }
        return BaseUnpackReturnUtil.getV1UnpackReturnData(RETURN_CMD, baseUnpackBO.getRouteId(), msg);
    }
}
