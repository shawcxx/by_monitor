package com.shawcxx.modules.station.domain;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * (UserStation)表实体类
 *
 * @author cjl
 * @since 2022-07-07 20:52:31
 */
@Data
@TableName("t_user_station")
public class UserStationDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String stationId;
}
