package com.shawcxx.modules.station.form;

import com.shawcxx.common.validate.Edit;
import com.shawcxx.common.validate.Insert;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author cjl
 * @date 2022/7/2 15:59
 * @description
 */
@Data
public class StationForm {
    /**
     * 电站id
     */
    private String stationId;

    /**
     * 电站名称
     */
    @NotBlank(message = "电站名称不能为空",groups = {Insert.class, Edit.class})
    @Length(max = 100, message = "电站名称长度不能超过100位",groups = {Insert.class, Edit.class})
    private String stationName;

    /**
     * 电站类型
     */
    @NotBlank(message = "电站类型不能为空",groups = {Insert.class, Edit.class})
    private String stationType;

    /**
     * 装机容量
     */
    @NotNull(message = "装机容量不能为空",groups = {Insert.class, Edit.class})
    private Double stationCapacity;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 地址
     */
    private String address;
    private String timeZone;
    private String country;
    private String location;
    @NotNull(message = "安装商不能为空", groups = Insert.class)
    private Long deptId;
    @NotBlank(message = "业主信息不能为空", groups = Insert.class)
    private String username;
}
