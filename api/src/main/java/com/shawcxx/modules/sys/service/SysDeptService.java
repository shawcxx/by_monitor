package com.shawcxx.modules.sys.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shawcxx.common.exception.MyException;
import com.shawcxx.common.utils.MyUserUtil;
import com.shawcxx.modules.sys.dao.SysDeptDAO;
import com.shawcxx.modules.sys.domain.SysDeptDO;
import com.shawcxx.modules.sys.domain.SysUserDO;
import com.shawcxx.modules.sys.domain.SysUserDeptDO;
import com.shawcxx.modules.sys.dto.SysDeptDTO;
import com.shawcxx.modules.sys.form.SysDeptForm;
import com.shawcxx.modules.sys.form.SysDeptRequestForm;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (SysDept)表服务接口
 *
 * @author cjl
 * @since 2022-07-05 19:59:03
 */
@Service
public class SysDeptService extends ServiceImpl<SysDeptDAO, SysDeptDO> {

    @Resource
    private SysUserDeptService sysUserDeptService;

    @Resource
    private SysUserService sysUserService;

    public Page<SysDeptDTO> deptList(SysDeptRequestForm form) {
        List<Long> deptList = MyUserUtil.getUser().getDeptList();
        form.setDeptList(deptList);
        return baseMapper.deptList(new Page<>(form.getCurrent(), form.getSize()), form);
    }

    public void addDept(SysDeptForm form) {
        SysDeptDO sysDeptDO = new SysDeptDO();
        SysDeptDO parent = this.getById(form.getParentId());
        if (parent == null) {
            throw new MyException("上级不存在");
        }
        sysDeptDO.setDeptName(form.getDeptName());
        sysDeptDO.setAddress(form.getAddress());
        sysDeptDO.setManager(form.getManager());
        sysDeptDO.setPhone(form.getPhone());
        String deptTree = parent.getDeptTree();
        sysDeptDO.setParentId(form.getParentId());
        this.save(sysDeptDO);
        sysDeptDO.setDeptTree(deptTree + "," + sysDeptDO.getDeptId());
        this.updateById(sysDeptDO);
    }

    public void delete(Long deptId) {
        this.removeById(deptId);
        sysUserDeptService.remove(new LambdaQueryWrapper<SysUserDeptDO>().eq(SysUserDeptDO::getDeptId, deptId));
    }

    public void deleteUser(SysDeptRequestForm form) {
        Long deptId = form.getDeptId();
        Long userId = form.getUserId();
        if (deptId == null || userId == null) {
            throw new MyException("请求参数错误");
        }
        sysUserDeptService.remove(new LambdaQueryWrapper<SysUserDeptDO>().eq(SysUserDeptDO::getUserId, userId).eq(SysUserDeptDO::getDeptId, deptId));
    }

    public void addUser(SysDeptRequestForm form) {
        String username = form.getUsername();
        Long deptId = form.getDeptId();
        if (deptId == null || StrUtil.isBlank(username)) {
            throw new MyException("请求参数错误");
        }
        SysUserDO user = sysUserService.getOne(new LambdaQueryWrapper<SysUserDO>().eq(SysUserDO::getUsername, username));
        if (user == null) {
            throw new MyException("用户不存在,请先创建用户");
        }
        LambdaQueryWrapper<SysUserDeptDO> queryWrapper = new LambdaQueryWrapper<SysUserDeptDO>().eq(SysUserDeptDO::getUserId, user.getUserId()).eq(SysUserDeptDO::getDeptId, deptId);
        SysUserDeptDO one = sysUserDeptService.getOne(queryWrapper);
        if (one == null) {
            SysUserDeptDO sysUserDeptDO = new SysUserDeptDO();
            sysUserDeptDO.setDeptId(deptId);
            sysUserDeptDO.setUserId(user.getUserId());
            sysUserDeptService.save(sysUserDeptDO);
        }
    }

    public void editDept(SysDeptForm form) {
        if (form.getDeptId() == null) {
            throw new MyException("请求参数错误");
        }
        if (form.getParentId().equals(form.getDeptId())) {
            throw new MyException("上级不能选择自己");
        }
        SysDeptDO sysDeptDO = new SysDeptDO();
        SysDeptDO parent = this.getById(form.getParentId());
        if (parent == null) {
            throw new MyException("上级不存在");
        }
        sysDeptDO.setDeptName(form.getDeptName());
        sysDeptDO.setAddress(form.getAddress());
        sysDeptDO.setManager(form.getManager());
        sysDeptDO.setPhone(form.getPhone());
        String deptTree = parent.getDeptTree();
        sysDeptDO.setParentId(form.getParentId());
        sysDeptDO.setDeptId(form.getDeptId());
        sysDeptDO.setDeptTree(deptTree + "," + sysDeptDO.getDeptId());
        this.updateById(sysDeptDO);
    }
}
