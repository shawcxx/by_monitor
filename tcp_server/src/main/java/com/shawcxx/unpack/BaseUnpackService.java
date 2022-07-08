package com.shawcxx.unpack;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.shawcxx.common.constant.SysConstant;
import com.shawcxx.common.util.MyHexUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @author Chen jl
 * @date 2022/6/14 22:31
 * @description
 **/
@Service
@Slf4j
public class BaseUnpackService {


    public String unpack(String data) {
        if (StrUtil.isBlank(data)) {
            return null;
        }
        String result = null;
        try {
            String returnData = null;
            String protocol = MyHexUtil.getHex(data, 1, 1);
            if ("68".equals(protocol)) {
                returnData = unpackV1(data);
            } else if ("64".equals(protocol)) {
                returnData = unpackV2(data);
            }
            return returnData;
        } catch (Exception e) {
            log.warn("协议解析错误", e);
        }
        if (result != null) {
            //todo 保存收到的数据和返回的数据
        }
        return result;
    }

    private String unpackV1(String data) {
        String protocol = MyHexUtil.getHex(data, 1, 1);
        BaseUnpackBO baseUnpackBO = new BaseUnpackBO();

        int dataLength = MyHexUtil.getHexInt(data, 2, 2);
        if (dataLength * 2 != data.length()) {
            return null;
        }
        String protocol2 = MyHexUtil.getHex(data, 4, 1);
        String cmd = MyHexUtil.getHex(data, 5, 2);
        String routeId = MyHexUtil.getHex(data, 7, 4);


        //保留字段
//            String reverse = MyHexUtil.getRouteId(data, 11, 4);

        int contentLength = dataLength - 16;
        String content = MyHexUtil.getHex(data, 15, contentLength);
        String cs = MyHexUtil.getHex(data, 15 + contentLength, 1);
        String end = MyHexUtil.getHex(data, 16 + contentLength, 1);
        baseUnpackBO.setProtocol(protocol);
        baseUnpackBO.setRouteId(routeId);
        baseUnpackBO.setCmd(cmd);
        baseUnpackBO.setContent(content);
        baseUnpackBO.setCs(cs);
        baseUnpackBO.setEnd(end);

        Object bean = SpringUtil.getBean("v1Cmd" + cmd + "Service");
        if (bean != null) {
            return ReflectUtil.invoke(bean, "unpack", baseUnpackBO);
        }
        return null;
    }

    private String unpackV2(String data) {
        String protocol = MyHexUtil.getHex(data, 1, 1);
        BaseUnpackBO baseUnpackBO = new BaseUnpackBO();
        String sn = MyHexUtil.getHexIntStr(data, 3, 2);
//        LocalDateTime deviceTime = MyHexUtil.getDate(data, 5, 6);
        String routeId = MyHexUtil.getHex(data, 11, 4);
        String cmd = MyHexUtil.getHex(data, 15, 1);
        String content = StrUtil.sub(data, 15 * 2, data.length());
        baseUnpackBO.setProtocol(protocol);
        baseUnpackBO.setRouteId(routeId);
        baseUnpackBO.setCmd(cmd);
        baseUnpackBO.setContent(content);
//        baseUnpackBO.setDeviceTime(deviceTime);
        Object bean = SpringUtil.getBean("v2Cmd" + cmd + "Service");
        if (bean != null) {
            return ReflectUtil.invoke(bean, "unpack", baseUnpackBO);
        }
        return null;
    }

    public static void main(String[] args) {
        BaseUnpackService baseUnpackService = new BaseUnpackService();
    }
}

