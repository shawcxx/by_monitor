package com.shawcxx.unpack;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Chen jl
 * @date 2022/6/14 22:40
 * @description
 **/
@Data
public class BaseUnpackBO {
    private String protocol;
    private String sn;
    private LocalDateTime deviceTime;
    private String routeId;
    private String cmd;
    private String content;
    private String cs;
    private String end;
}
