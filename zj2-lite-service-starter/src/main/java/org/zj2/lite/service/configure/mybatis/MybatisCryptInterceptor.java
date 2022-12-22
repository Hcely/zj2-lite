package org.zj2.lite.service.configure.mybatis;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;
import org.zj2.lite.common.annotation.CryptProperty;
import org.zj2.lite.common.util.PropertyUtil;
import org.zj2.lite.util.CryptUtil;

import java.util.function.Function;

/**
 *  MybatisCryptIn
 *
 * @author peijie.ye
 * @date 2022/11/24 14:44
 */
@Component
@Intercepts({//
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})})
public class MybatisCryptInterceptor implements Interceptor {
    private static final PropertyCryptHandler ENCRYPT_HANDLER = new PropertyCryptHandler(true);
    private static final PropertyCryptHandler DECRYPT_HANDLER = new PropertyCryptHandler(false);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        boolean isUpdate = args.length == 2;
        if (isUpdate) {
            Object value = invocation.getArgs()[1];
            PropertyUtil.scanProperties(value, ENCRYPT_HANDLER);
            try {
                return invocation.proceed();
            } finally {
                PropertyUtil.scanProperties(value, DECRYPT_HANDLER);
            }
        } else {
            Object value = invocation.proceed();
            PropertyUtil.scanProperties(value, DECRYPT_HANDLER);
            return value;
        }
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor || target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    private static final class PropertyCryptHandler
            implements Function<PropertyUtil.BeanPropertyContext, PropertyUtil.PropScanMode> {
        private final boolean isEncrypt;

        private PropertyCryptHandler(boolean isEncrypt) {
            this.isEncrypt = isEncrypt;
        }

        @Override
        public PropertyUtil.PropScanMode apply(PropertyUtil.BeanPropertyContext context) {
            if (!context.isSimplePropertyType()) {
                return PropertyUtil.PropScanMode.DEEP;
            }
            Object propValue = context.propertyValue();
            if (propValue instanceof String && context.propertyAnnotation(CryptProperty.class) != null) {
                context.propertyValue(CryptUtil.crypt(isEncrypt, propValue.toString()));
            }
            return PropertyUtil.PropScanMode.NOT_DEEP;
        }
    }
}
