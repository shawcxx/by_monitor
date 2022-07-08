package com.shawcxx.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shawcxx.modules.sys.domain.SysRoleDO;
import com.shawcxx.modules.sys.domain.SysUserDO;
import com.shawcxx.modules.sys.dto.SysUserDTO;
import com.shawcxx.modules.sys.form.SysUserQueryForm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cjl
 * @create 2020/3/3
 */
@Repository
public interface SysUserDAO extends BaseMapper<SysUserDO> {


    List<String> queryPerms(@Param("userId") Long userId);

    List<SysRoleDO> queryRoles(@Param("userId") Long userId);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> queryAllMenuId(@Param("userId") Long userId);

    Page<SysUserDTO> userList(Page page, @Param("form") SysUserQueryForm form);
}
