package com.shawcxx.common.myi18n;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * @author cjl
 * @date 2022/7/17 17:24
 * @description
 */
@Component
public class I18nUtils {
    @Resource
    private LocaleMessage localeMessage;

    /**
     * 获取key
     *
     * @param key
     * @return
     */
    public String getKey(String key) {
        String name = localeMessage.getMessage(key);
        return name;
    }

    /**
     * 获取指定哪个配置文件下的key
     *
     * @param key
     * @param local
     * @return
     */
    public String getKey(String key, Locale local) {
        String name = localeMessage.getMessage(key, local);
        return name;
    }
}
