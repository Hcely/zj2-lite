package org.zj2.lite.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zj2.lite.helper.chain.ChainBizContext;
import org.zj2.lite.helper.chain.ChainBizHandler;
import org.zj2.lite.helper.handler.BizContextConverter;
import org.zj2.lite.helper.handler.BizFunc;
import org.zj2.lite.helper.handler.BizHandler;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * BizHelperReference
 * <br>CreateDate 一月 11,2022
 * @author peijie.ye
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Repeatable(BizReference.Multi.class)
public @interface BizReference {
    Class<? extends BizContextConverter> converter() default NoneContextConverter.class;

    Class<? extends BizFunc>[] value() default {};

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Component
    @interface Multi {
        BizReference[] value() default {};
    }

    @SuppressWarnings("all")
    final class Helper {
        public static BizHandler createHandler(Class<?> type) {
            Multi multi = type.getAnnotation(Multi.class);
            if (multi != null) {return createHandler(type, multi);}
            BizReference reference = type.getAnnotation(BizReference.class);
            if (reference != null) {return createHandler(type, reference);}
            return BizHandler.EMPTY_HANDLER;
        }

        private static BizHandler createHandler(Class<?> type, Multi reference) {
            BizReference[] references = reference.value();
            if (references.length == 0) {return BizHandler.EMPTY_HANDLER;}
            if (references.length == 1) {return createHandler(type, references[0]);}
            String handlerName = type.getSimpleName();
            Logger logger = LoggerFactory.getLogger(type);
            ChainBizHandler.Builder<?> builder = ChainBizHandler.builder();
            builder.handlerName(handlerName);
            builder.logger(logger);
            for (BizReference ref : references) {
                if (reference.value().length > 0) {builder.addHandler(createHandler(handlerName, logger, ref));}
            }
            return builder.build();
        }

        private static BizHandler createHandler(Class<?> type, BizReference reference) {
            if (reference.value().length == 0) {return BizHandler.EMPTY_HANDLER;}
            return createHandler(type.getSimpleName(), LoggerFactory.getLogger(type), reference);
        }

        private static BizHandler createHandler(String name, Logger logger, BizReference reference) {
            ChainBizHandler.Builder<?> builder = ChainBizHandler.builder();
            builder.handlerName(name);
            builder.logger(logger);
            if (reference.converter() != NoneContextConverter.class) {builder.addHandler(reference.converter());}
            for (Class<? extends BizFunc> type : reference.value()) {builder.addHandler(type);}
            return builder.build();
        }
    }

    final class NoneContextConverter implements BizContextConverter {
        private NoneContextConverter() {
        }

        @Override
        public Object convert(ChainBizContext chainContext) {
            return null;
        }
    }
}
