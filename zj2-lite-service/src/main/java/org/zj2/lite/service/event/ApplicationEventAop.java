package org.zj2.lite.service.event;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * <br>CreateDate 三月 26,2022
 *
 * @author peijie.ye
 */
@Component
@Aspect
class ApplicationEventAop {
    @Pointcut("this(org.springframework.context.ApplicationListener)&&execution(* onApplicationEvent(..))")
    void pointcutApplicationListener() {
        //NOTHING
    }

    @Pointcut("@annotation(org.springframework.context.event.EventListener)")
    void pointcutEventListener() {
        //NOTHING
    }

    @Around("pointcutApplicationListener()||pointcutEventListener()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {// NOSONAR
        final boolean ignoreError = EventPublishManager.isIgnoreError();
        try {
            return joinPoint.proceed();
        } catch(Throwable e) {// NOSONAR
            EventPublishManager.logger.error("事件处理异常", e);
            if(ignoreError) {
                return null;
            } else {
                throw e;
            }
        }
    }
}
