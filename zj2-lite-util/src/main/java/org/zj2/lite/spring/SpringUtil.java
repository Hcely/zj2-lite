package org.zj2.lite.spring;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.zj2.lite.common.util.CollUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 *  SpringUtil
 *
 * @author peijie.ye
 * @date 2022/11/19 23:20
 */
@Component
public class SpringUtil implements ApplicationContextAware {
    private static final Map<Integer, Object> SPRING_SELF_MAP = new HashMap<>(512);
    private static ApplicationContext context;

    private static void setContext(ApplicationContext context) {
        SpringUtil.context = context;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        setContext(applicationContext);
    }

    public static <T> T self(T instance) {
        Integer identifyCode = System.identityHashCode(instance);
        Object result = SPRING_SELF_MAP.get(identifyCode);
        if (result == null) {
            synchronized (SPRING_SELF_MAP) {
                result = SPRING_SELF_MAP.computeIfAbsent(identifyCode, i -> self0(instance));
            }
        }
        //noinspection unchecked
        return (T) result;
    }

    private static Object self0(Object instance) {
        if (AopUtils.isAopProxy(instance)) {return instance;}
        try {
            Map<String, ?> beans = SpringUtil.getBeansOfType(instance.getClass());
            if (CollUtil.isEmpty(beans)) {
                return instance;
            } else if (CollUtil.size(beans) == 1) {
                return CollUtil.getFirst(beans);
            } else {
                for (Object bean : beans.values()) {
                    if (getAopTargetObject(bean) == instance) {return bean;}
                }
            }
        } catch (Throwable e) {//NOSONAR
        }
        return instance;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> aClass) throws BeansException {
        return context.getBeansOfType(aClass);
    }

    public static Object getBean(String s) throws BeansException {
        return context.getBean(s);
    }

    public static <T> T getBean(Class<T> aClass) throws BeansException {
        return context.getBean(aClass);
    }

    public static Class<?> getAopTargetClass(Object proxy) {
        if (!AopUtils.isAopProxy(proxy)) {return proxy.getClass();}
        return AopUtils.getTargetClass(proxy);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAopTargetObject(T proxy) {
        if (!AopUtils.isAopProxy(proxy)) {return proxy;}
        try {
            if (AopUtils.isJdkDynamicProxy(proxy)) {
                return (T) getJdkDynamicProxyTargetObject(proxy);
            } else if (AopUtils.isCglibProxy(proxy)) {
                return (T) getCglibProxyTargetObject(proxy);
            }
        } catch (Exception ignored) {
            // nothing
        }
        return proxy;
    }

    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field cglibField = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        cglibField.trySetAccessible();
        Object dynamicAdvisedInterceptor = cglibField.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.trySetAccessible();
        return ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
    }

    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field jdkDynamicField = proxy.getClass().getSuperclass().getDeclaredField("jdkDynamicField");
        jdkDynamicField.trySetAccessible();
        AopProxy aopProxy = (AopProxy) jdkDynamicField.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.trySetAccessible();
        return ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
    }
}
