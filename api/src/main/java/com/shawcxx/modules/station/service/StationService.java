package com.shawcxx.modules.station.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shawcxx.common.base.MyResult;
import com.shawcxx.common.base.SysUserBO;
import com.shawcxx.common.exception.MyException;
import com.shawcxx.common.utils.MyUserUtil;
import com.shawcxx.modules.device.dao.DeviceDAO;
import com.shawcxx.modules.device.domain.DeviceDO;
import com.shawcxx.modules.device.dto.DeviceDTO;
import com.shawcxx.modules.device.service.DeviceRecordService;
import com.shawcxx.modules.device.service.DeviceService;
import com.shawcxx.modules.station.dao.StationDAO;
import com.shawcxx.modules.station.domain.StationDO;
import com.shawcxx.modules.station.domain.UserStationDO;
import com.shawcxx.modules.station.dto.StationDTO;
import com.shawcxx.modules.station.form.StationForm;
import com.shawcxx.modules.station.form.StationRequestForm;
import com.shawcxx.modules.device.service.DeviceEnergyStatisticService;
import com.shawcxx.modules.sys.dao.SysUserDAO;
import com.shawcxx.modules.sys.domain.SysDeptDO;
import com.shawcxx.modules.sys.domain.SysUserDO;
import com.shawcxx.modules.sys.service.SysDeptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * (Station)表服务接口
 *
 * @author cjl
 * @since 2022-07-02 13:55:18
 */
@Service
public class StationService extends ServiceImpl<StationDAO, StationDO> {
    @Resource
    private DeviceEnergyStatisticService deviceEnergyStatisticService;
    @Resource
    private DeviceDAO deviceDAO;
    @Resource
    private DeviceRecordService deviceRecordService;
    @Resource
    private SysUserDAO sysUserDAO;
    @Resource
    private UserStationService userStationService;
    @Resource
    private SysDeptService sysDeptService;

    public Page<StationDTO> stationList(StationRequestForm form) {
        SysUserBO user = MyUserUtil.getUser();
        form.setDeptList(user.getDeptList());
        form.setUserId(user.getUserId());
        Page<StationDTO> page = baseMapper.stationList(new Page<>(form.getCurrent(), form.getSize()), form);
        for (StationDTO record : page.getRecords()) {
            record.setAlarmInfo(null);
            if (form.getQueryData()) {
                record.setEnergy(deviceEnergyStatisticService.getStationDayEnergy(record.getStationId(), DateUtil.date()));
                record.setPower(deviceRecordService.getStationLastPower(record.getStationId()));
                record.setEnergyTrend(deviceEnergyStatisticService.energyData(record.getStationId(), 1, DateUtil.now(), DateUtil.now(), null));
            }
        }
        return page;
    }

    public StationDTO stationInfo(String stationId) {
        StationDTO stationDTO = baseMapper.stationInfo(stationId);
        return stationDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public StationDTO createStation(StationForm form) {
        StationDO stationDO = new StationDO();
        LambdaQueryWrapper<StationDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StationDO::getStationName, form.getStationName());
        StationDO one = this.getOne(queryWrapper);
        if (one != null) {
            throw new MyException("电站名称已被使用");
        }
        BeanUtil.copyProperties(form, stationDO, "stationId");
        SysDeptDO dept = sysDeptService.getById(form.getDeptId());
        if (dept == null) {
            throw new MyException("安装商不能为空");
        }
        stationDO.setDeptId(dept.getDeptId());
        SysUserDO userDO = sysUserDAO.selectOne(new LambdaQueryWrapper<SysUserDO>().eq(SysUserDO::getUsername, form.getUsername()));
        if (userDO == null) {
            throw new MyException("业主账户不存在");
        }
        stationDO.setUserId(userDO.getUserId());
        this.save(stationDO);
        StationDTO stationDTO = new StationDTO();
        BeanUtil.copyProperties(stationDO, stationDTO);


        return stationDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public void modifyStation(StationForm form) {
        LambdaQueryWrapper<StationDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StationDO::getStationName, form.getStationName());
        StationDO one = this.getOne(queryWrapper);
        if (StrUtil.isBlank(form.getStationId())) {
            throw new MyException("请求参数错误");
        }
        if (one != null && !one.getStationId().equals(form.getStationId())) {
            throw new MyException("电站名称已被使用");
        }
        StationDO stationDO = this.getById(form.getStationId());
        BeanUtil.copyProperties(form, stationDO, "stationId");
        stationDO.setUpdateUserId(MyUserUtil.getUser().getUserId());
        this.updateById(stationDO);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteStation(String stationId) {
        this.removeById(stationId);
        // 删除设备
        deviceDAO.delete(new LambdaQueryWrapper<DeviceDO>().eq(DeviceDO::getStationId, stationId));
    }

    public List<JSONObject> historyData(StationRequestForm form) {
        String item = form.getItem();
        String stationId = form.getStationId();
        if (StrUtil.isBlank(stationId)) {
            throw new MyException("请求参数错误");
        }

        List<JSONObject> list = new ArrayList<>();
        if ("power".equals(item)) {
            list = deviceRecordService.stationPowerData(form.getStationId(), form.getType(), form.getStartTime(), form.getEndTime());
        }
        if ("energy".equals(item)) {
            list = deviceEnergyStatisticService.energyData(form.getStationId(), form.getType(), form.getStartTime(), form.getEndTime(), null);
        }

        return list;
    }

    public void dataExport(StationRequestForm form, HttpServletResponse response) {

    }


    public List<DeviceDTO> deviceList(String stationId, Integer type) {
        List<DeviceDTO> list = new ArrayList<>();
        if (type == 1) {
            List<DeviceDO> deviceList = deviceDAO.selectList(new LambdaQueryWrapper<DeviceDO>().eq(DeviceDO::getStationId, stationId));
            Map<String, DeviceDTO> routeList = deviceList.stream().filter(o -> o.getDeviceType() == 2).map(o -> {
                DeviceDTO deviceDTO = new DeviceDTO();
                BeanUtil.copyProperties(o, deviceDTO);
                return deviceDTO;
            }).collect(Collectors.toMap(DeviceDTO::getDeviceNo, o -> o));
            for (DeviceDO deviceDO : deviceList) {
                if (deviceDO.getDeviceType() == 1) {
                    DeviceDTO deviceDTO = routeList.get(deviceDO.getDeviceNo());
                    list.add(deviceDTO);
                } else {
                    DeviceDTO deviceDTO = routeList.get(deviceDO.getEmuId());
                    DeviceDTO child = new DeviceDTO();
                    if (CollUtil.isEmpty(deviceDTO.getList())) {
                        deviceDTO.setList(new ArrayList<>());
                        deviceDTO.getList().add(child);
                    }
                }
            }
        } else if (type == 2) {
            List<DeviceDO> deviceList = deviceDAO.selectList(new LambdaQueryWrapper<DeviceDO>().eq(DeviceDO::getStationId, stationId).eq(DeviceDO::getDeviceType, 1));
            for (DeviceDO deviceDO : deviceList) {
                DeviceDTO deviceDTO = new DeviceDTO();
                BeanUtil.copyProperties(deviceDO, deviceDTO);
                list.add(deviceDTO);
            }
        }
        return list;
    }

    public void linkStation(StationRequestForm form) {
        String username = form.getUsername();
        String stationId = form.getStationId();
        if (StrUtil.hasBlank(username, stationId)) {
            throw new MyException("请求参数错误");
        }
        SysUserDO userDO = sysUserDAO.selectOne(new LambdaQueryWrapper<SysUserDO>().eq(SysUserDO::getUsername, username));
        if (userDO == null) {
            throw new MyException("用户不存在");
        }
        UserStationDO one = userStationService.getOne(new LambdaQueryWrapper<UserStationDO>().eq(UserStationDO::getUserId, userDO.getUserId()).eq(UserStationDO::getStationId, stationId));
        if (one == null) {
            UserStationDO userStationDO = new UserStationDO();
            userStationDO.setStationId(stationId);
            userStationDO.setUserId(userDO.getUserId());
            userStationService.save(userStationDO);
        }
    }
}
