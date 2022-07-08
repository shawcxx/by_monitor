package com.shawcxx.common.constant;

import com.shawcxx.modules.device.domain.DeviceDO;
import org.smartboot.socket.transport.AioSession;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dj
 * @date 2021-12-23 10:40
 * @description
 **/
public class SysConstant {

    /**
     * 解析包文件
     */
    public static File UNPACK_JAR_FILE = null;

    public static Class<?> UNPACK_CLASS = null;
    /**
     * 连接的AioSession维护
     * AioSession的map
     */
    public static Map<String, AioSession> SESSION_MAP = new ConcurrentHashMap<>();

    public static Map<String, DeviceDO> DEVICE_MAP = new ConcurrentHashMap<>();
}
