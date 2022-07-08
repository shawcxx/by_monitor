package com.shawcxx;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shawcxx.modules.sys.dao.SysUserDAO;
import com.shawcxx.modules.sys.dto.SysUserDTO;
import com.shawcxx.modules.sys.form.SysUserQueryForm;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author cjl
 * @date 2022/7/4 17:59
 * @description
 */
@SpringBootTest
public class Test {
    @Resource
    private SysUserDAO sysUserDAO;

    @org.junit.jupiter.api.Test
    public void test() {
        SysUserQueryForm form = new SysUserQueryForm();
        form.setDeptId(0l);
        Page<SysUserDTO> sysUserDTOPage = sysUserDAO.userList(new Page(1, 10), form);
        System.out.println(JSON.toJSONString(sysUserDTOPage));
        form.setDeptId(null);
        sysUserDTOPage = sysUserDAO.userList(new Page(1, 10), form);
        System.out.println(JSON.toJSONString(sysUserDTOPage));
    }
}
