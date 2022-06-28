package com.shawcxx.common.util;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

/**
 * @author: dj
 * @create: 2019-08-07 10:21
 * @description:
 */
@Slf4j
public class CommonUtil {

    public static int getUnpackJarVersion2Int(File file) {
        int versionNum = 0;
        String version = getUnpackJarVersion(file);
        try {
            versionNum = version2Int(version);
        } catch (Exception e) {
            log.warn("jar包版本号错误", e);
        }
        return versionNum;
    }

    public static int version2Int(String version) throws RuntimeException {
        version = StrUtil.subBefore(version, ".jar", true);
        return Integer.parseInt(version.replace(".", ""));
    }

    public static String getUnpackJarVersion(File file) throws RuntimeException {
        String fileName = file.getName();
        return StrUtil.subAfter(fileName, "-", true);
    }

    public static String getInstructionType(String content) {
        String instructionType = null;
        try {
            List<String> array = StrUtil.split(content, "|");
            instructionType = array.get(4);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StrUtil.upperFirst(instructionType);
    }

}
