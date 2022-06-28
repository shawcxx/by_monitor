package com.shawcxx.modules.sys.dto;

import cn.hutool.core.bean.BeanUtil;
import com.shawcxx.modules.sys.domain.SysRoleDO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author cjl
 * @create 2020.4.8
 */
@Data
public class SysRoleDTO {
    private Long roleId;

    private String roleName;
    private String roleCode;
    private String remark;

    private Date createTime;

    private List<Long> menuIdList;


    public SysRoleDTO(SysRoleDO sysRoleDO) {
        BeanUtil.copyProperties(sysRoleDO, this);
    }

}
