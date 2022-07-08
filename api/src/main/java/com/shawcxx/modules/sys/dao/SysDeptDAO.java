package com.shawcxx.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shawcxx.modules.sys.domain.SysDeptDO;
import com.shawcxx.modules.sys.dto.SysDeptDTO;
import com.shawcxx.modules.sys.form.SysDeptRequestForm;
import org.apache.ibatis.annotations.Param;

/**
 * (SysDept)表数据库访问层
 *
 * @author cjl
 * @since 2022-07-05 19:59:03
 */
public interface SysDeptDAO extends BaseMapper<SysDeptDO> {

    Page<SysDeptDTO> deptList(Page page, @Param("form") SysDeptRequestForm form);
}
