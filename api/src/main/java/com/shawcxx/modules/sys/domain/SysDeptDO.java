package com.shawcxx.modules.sys.domain;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * (SysDept)表实体类
 *
 * @author cjl
 * @since 2022-07-05 19:59:03
 */
@Data
@TableName("sys_dept")
public class SysDeptDO {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long deptId;

    private String deptName;

    private String deptTree;

    /**
     * 地址
     */
    private String address;

    /**
     * 联系人
     */
    private String manager;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 上级
     */
    private Long parentId;
}
