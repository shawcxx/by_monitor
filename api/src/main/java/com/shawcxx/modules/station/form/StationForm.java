package com.shawcxx.modules.station.form;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

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
    @NotBlank(message = "电站名称不能为空")
    @Length(max = 100, message = "电站名称长度不能超过100位")
    private String stationName;

    /**
     * 电站类型
     */
    @NotBlank(message = "电站类型不能为空")
    private String stationType;

    /**
     * 装机容量
     */
    @NotNull(message = "装机容量不能为空")
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
    @NotNull(message = "安装商不能为空")
    private Long deptId;
    @NotBlank(message = "业主信息不能为空")
    private String username;
}
