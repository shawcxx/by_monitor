package com.shawcxx.modules.sys.dto;

import lombok.Data;

/**
 * @author cjl
 * @date 2022/7/6 15:02
 * @description
 */
@Data
public class SysDeptDTO {
    /**
     * id
     */
    private Long deptId;

    private String deptName;

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
}
