package com.shawcxx.modules.sys.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shawcxx.common.base.MyResult;
import com.shawcxx.modules.sys.dto.SysDeptDTO;
import com.shawcxx.modules.sys.form.SysDeptForm;
import com.shawcxx.modules.sys.form.SysDeptRequestForm;
import com.shawcxx.modules.sys.service.SysDeptService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (SysDept)表控制层
 *
 * @author cjl
 * @since 2022-07-05 19:59:03
 */
@RestController
@RequestMapping("sys/dept")
public class SysDeptController {
    @Resource
    private SysDeptService sysDeptService;


    @PostMapping("list")
    public MyResult deptList(@RequestBody SysDeptRequestForm form) {
        Page<SysDeptDTO> page = sysDeptService.deptList(form);
        return MyResult.data(page);
    }

    @PostMapping("addDept")
    public MyResult addDept(@RequestBody @Validated SysDeptForm form) {
        sysDeptService.addDept(form);
        return MyResult.ok();
    }


    @PostMapping("delete")
    public MyResult delete(@RequestParam Long deptId) {
        sysDeptService.delete(deptId);
        return MyResult.ok();
    }

    @PostMapping("deleteUser")
    public MyResult deleteUser(@RequestBody SysDeptRequestForm form) {
        sysDeptService.deleteUser(form);
        return MyResult.ok();
    }

    @PostMapping("addUser")
    public MyResult addUser(@RequestBody SysDeptRequestForm form) {
        sysDeptService.addUser(form);
        return MyResult.ok();
    }

}
