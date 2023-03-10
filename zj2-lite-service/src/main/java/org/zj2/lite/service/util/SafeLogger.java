package org.zj2.lite.service.util;

import org.slf4j.Logger;
import org.slf4j.Marker;
import org.springframework.beans.BeanUtils;

/**
 * SaleLog
 *
 * @author peijie.ye
 * @date 2022/12/2 17:18
 */
class SafeLogger implements Logger {
    private final Logger logger;

    SafeLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        logger.trace(msg);
    }

    @Override
    public void trace(String format, Object arg) {
        logger.trace(format, safeObject(arg));
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        logger.trace(format, safeObject(arg1), safeObject(arg2));
    }

    @Override
    public void trace(String format, Object... arguments) {
        logger.trace(format, safeObjects(arguments));
    }

    @Override
    public void trace(String msg, Throwable t) {
        logger.trace(msg, t);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return logger.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String msg) {
        logger.trace(marker, msg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        logger.trace(marker, format, safeObject(arg));
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        logger.trace(marker, format, safeObject(arg1), safeObject(arg2));
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        logger.trace(marker, format, safeObjects(argArray));
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        logger.trace(marker, msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        logger.debug(msg);
    }

    @Override
    public void debug(String format, Object arg) {
        logger.debug(format, safeObject(arg));
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        logger.debug(format, safeObject(arg1), safeObject(arg2));
    }

    @Override
    public void debug(String format, Object... arguments) {
        logger.debug(format, safeObjects(arguments));
    }

    @Override
    public void debug(String msg, Throwable t) {
        logger.debug(msg, t);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return logger.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String msg) {
        logger.debug(marker, msg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        logger.debug(marker, format, safeObject(arg));
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        logger.debug(marker, format, safeObject(arg1), safeObject(arg2));
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        logger.debug(marker, format, safeObjects(arguments));
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        logger.debug(marker, msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        logger.info(msg);
    }

    @Override
    public void info(String format, Object arg) {
        logger.info(format, safeObject(arg));
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        logger.info(format, safeObject(arg1), safeObject(arg2));
    }

    @Override
    public void info(String format, Object... arguments) {
        logger.info(format, safeObjects(arguments));
    }

    @Override
    public void info(String msg, Throwable t) {
        logger.info(msg, t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return logger.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String msg) {
        logger.info(marker, msg);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        logger.info(marker, format, safeObject(arg));
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        logger.info(marker, format, safeObject(arg1), safeObject(arg2));
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        logger.info(marker, format, safeObjects(arguments));
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        logger.info(marker, msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        logger.warn(msg);
    }

    @Override
    public void warn(String format, Object arg) {
        logger.warn(format, safeObject(arg));
    }

    @Override
    public void warn(String format, Object... arguments) {
        logger.warn(format, safeObjects(arguments));
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        logger.warn(format, safeObject(arg1), safeObject(arg2));
    }

    @Override
    public void warn(String msg, Throwable t) {
        logger.warn(msg, t);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return logger.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String msg) {
        logger.warn(marker, msg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        logger.warn(marker, format, safeObject(arg));
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        logger.warn(marker, format, safeObject(arg1), safeObject(arg2));
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        logger.warn(marker, format, safeObjects(arguments));
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        logger.warn(marker, msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        logger.error(msg);
    }

    @Override
    public void error(String format, Object arg) {
        logger.error(format, safeObject(arg));
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        logger.error(format, safeObject(arg1), safeObject(arg2));
    }

    @Override
    public void error(String format, Object... arguments) {
        logger.error(format, safeObjects(arguments));
    }

    @Override
    public void error(String msg, Throwable t) {
        logger.error(msg, t);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return logger.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String msg) {
        logger.error(marker, msg);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        logger.error(marker, format, safeObject(arg));
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        logger.error(marker, format, safeObject(arg1), safeObject(arg2));
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        logger.error(marker, format, safeObjects(arguments));
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        logger.error(marker, msg, t);
    }

    private static Object safeObject(Object obj) {
        if(obj == null) { return null; }
        return BeanUtils.isSimpleValueType(obj.getClass()) ? obj : SafeLogUtil.toJSONStr(obj);
    }

    private static Object[] safeObjects(Object[] objs) {
        if(objs == null || objs.length == 0) { return objs; }
        for(int i = 0, len = objs.length; i < len; ++i) {
            objs[i] = safeObject(objs[i]);
        }
        return objs;
    }
}
