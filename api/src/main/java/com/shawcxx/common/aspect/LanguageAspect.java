package com.shawcxx.common.aspect;

import cn.hutool.core.util.StrUtil;
import com.shawcxx.common.base.MyResult;
import com.shawcxx.common.myi18n.I18nUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @author cjl
 * @date 2022/7/17 17:16
 * @description
 */
@Aspect
@Component
public class LanguageAspect {
    @Resource
    private I18nUtils i18nUtils;

    @Pointcut("execution(* com.shawcxx.modules.*.controller.*.*(..)))")
    public void annotationLangCut() {

    }

    @Pointcut("execution(* com.shawcxx.common.exception.MyExceptionHandler.handleException(..)))")
    public void annotationLangCut2() {

    }

    @AfterReturning(pointcut = "annotationLangCut2()", returning = "obj")
    public void pointCut2(JoinPoint point, Object obj) {
        this.cut(point, obj);
    }

    @AfterReturning(pointcut = "annotationLangCut()", returning = "obj")
    public void pointCut(JoinPoint point, Object obj) {
        this.cut(point, obj);
    }

    private void cut(JoinPoint point, Object obj) {
        Object resultObject = obj;
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            //从获取RequestAttributes中获取HttpServletRequest的信息
            HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
            String langFlag = request.getHeader("lang");
            if (null != langFlag) {
                MyResult r = (MyResult) obj;
                String msg = r.get("msg").toString().trim();
                if (StrUtil.isNotBlank(msg)) {
                    if ("CN".equals(langFlag)) {
                        Locale locale = Locale.CHINA;
                        msg = i18nUtils.getKey(msg, locale);
                    } else if ("EN".equals(langFlag)) {
                        Locale locale = Locale.US;
                        msg = i18nUtils.getKey(msg, locale);
                    } else {
                        msg = i18nUtils.getKey(msg);
                    }
                }
                r.put("msg", msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //返回原值
            obj = resultObject;
        }
    }
}
