package com.shawcxx.modules.station.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shawcxx.modules.station.domain.StationDO;
import com.shawcxx.modules.station.dto.StationDTO;
import com.shawcxx.modules.station.form.StationRequestForm;
import org.apache.ibatis.annotations.Param;

/**
 * (Station)表数据库访问层
 *
 * @author cjl
 * @since 2022-07-02 13:55:16
 */
public interface StationDAO extends BaseMapper<StationDO> {

    Page<StationDTO> stationList(Page page, @Param("form") StationRequestForm form);

    StationDTO stationInfo(@Param("stationId") String stationId);
}
