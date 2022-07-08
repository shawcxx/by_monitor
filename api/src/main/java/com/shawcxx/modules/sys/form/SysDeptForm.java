package com.shawcxx.modules.sys.form;

import com.shawcxx.common.validate.Mobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author cjl
 * @date 2022/7/6 15:02
 * @description
 */
@Data
public class SysDeptForm {
    @NotBlank(message = "部门名称不能为空")
    @Length(max = 50, message = "部门名称过长")
    private String deptName;
    @Length(max = 255, message = "地址过长")
    private String address;
    @Length(max = 20, message = "联系人过长")
    private String manager;
    @Mobile
    private String phone;

    @NotNull(message = "上级不能为空")
    private Long parentId;
}
