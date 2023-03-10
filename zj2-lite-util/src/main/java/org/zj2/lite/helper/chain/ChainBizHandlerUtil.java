package org.zj2.lite.helper.chain;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.zj2.lite.common.util.BeanUtil;
import org.zj2.lite.helper.handler.BizFunc;
import org.zj2.lite.spring.SpringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 业务处理器标识
 * <br>CreateDate 三月 27,2022
 *
 * @author peijie.ye
 */
@Slf4j
public class ChainBizHandlerUtil {
    private static final ConcurrentLinkedQueue<ChainBizFuncExecutor> EXECUTORS = new ConcurrentLinkedQueue<>();
    private static final ThreadLocal<ChainBizContext> CHAIN_CONTEXT_TL = new ThreadLocal<>();

    static {
        scanExecutors(ChainBizFuncExecutors.class);
    }

    static ChainBizContext startChain(ChainBizHandler<?> handler, Object ctx) {
        ChainBizContext parent = CHAIN_CONTEXT_TL.get();
        ChainBizContext chainBizContext = new ChainBizContext(parent, handler, ctx);
        CHAIN_CONTEXT_TL.set(chainBizContext);//NOSONAR
        return chainBizContext;
    }

    static void completeChain(ChainBizContext context) {
        if(context == null || context.parent() == null) {
            CHAIN_CONTEXT_TL.remove();
        } else {
            CHAIN_CONTEXT_TL.set(context.parent());
        }
    }

    public static ChainBizContext currentChainContext() {
        return CHAIN_CONTEXT_TL.get();
    }

    public static void scanExecutors(Class<?> type) {
        for(Field field : FieldUtils.getAllFields(type)) {
            if(Modifier.isStatic(field.getModifiers()) && ChainBizFuncExecutor.class.isAssignableFrom(field.getType())) {
                try {
                    ChainBizFuncExecutor executor = (ChainBizFuncExecutor)FieldUtils.readStaticField(field, true);
                    registerExecutor(executor);
                } catch(Throwable ignored) {//NOSONAR
                }
            }
        }
    }

    public static void registerExecutor(ChainBizFuncExecutor executor) {
        if(executor != null) { EXECUTORS.add(executor); }
    }

    static ChainBizFuncExecutor getExecutor(BizFunc handler) {
        for(ChainBizFuncExecutor executor : EXECUTORS) {
            if(executor != null && executor.supports(handler)) {
                return executor;
            }
        }
        return null;
    }

    public static <T extends BizFunc> T getHandler(Class<T> type) {
        try {
            return SpringUtil.getBean(type);
        } catch(NoSuchBeanDefinitionException e) {
            return BeanUtil.newInstance(type);
        }
    }

    public static <T extends BizFunc> Collection<T> getHandlers(Class<T> type) {
        try {
            Map<String, T> beans = SpringUtil.getBeansOfType(type);
            return beans.values();
        } catch(Exception e) {
            return Collections.emptyList();
        }
    }
}
