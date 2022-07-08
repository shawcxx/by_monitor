package com.shawcxx.modules.sys.form;

import com.shawcxx.common.base.BasePageForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author cjl
 * @date 2022/7/5 20:00
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDeptRequestForm extends BasePageForm {
    private Long deptId;
    private Long userId;
    private String username;
    private String deptName;
    private List<Long> deptList;
}
