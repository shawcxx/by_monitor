package com.shawcxx.modules.sys.domain;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * (SysUserDept)表实体类
 *
 * @author cjl
 * @since 2022-07-06 14:49:41
 */
@Data
@TableName("sys_user_dept")
public class SysUserDeptDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long deptId;
}
