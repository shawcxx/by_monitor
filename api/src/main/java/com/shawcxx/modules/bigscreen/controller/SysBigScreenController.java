package com.shawcxx.modules.bigscreen.controller;

import com.shawcxx.common.base.MyResult;
import com.shawcxx.modules.bigscreen.dto.BigScreenDTO;
import com.shawcxx.modules.bigscreen.service.BigScreenService;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author cjl
 * @date 2022/7/5 17:33
 * @description
 */
@RestController
@RequestMapping("bigScreen")
public class SysBigScreenController {
    @Resource
    private BigScreenService bigScreenService;

    /**
     * 数据大屏
     *
     * @return
     */
    @PostMapping("userBigScreen")
    public MyResult userBigScreen() {
        BigScreenDTO dto = bigScreenService.userBigScreen();
        return MyResult.data(dto);
    }

}
