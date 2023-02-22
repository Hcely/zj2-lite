package org.zj2.lite.common.entity.key;

import org.zj2.lite.common.util.StrUtil;

/**
 * FormatKey
 *
 * @author peijie.ye
 * @date 2023/2/22 10:32
 */
public class FormatKey {
    private final String keyFormat;

    public FormatKey(String keyFormat) {
        this.keyFormat = keyFormat;
    }

    public String get() {
        return keyFormat;
    }

    public String get(Object arg) {
        return StrUtil.format(keyFormat, arg);
    }

    public String get(Object arg0, Object arg1) {
        return StrUtil.format(keyFormat, arg0, arg1);
    }

    public String get(Object arg0, Object arg1, Object arg2) {
        return StrUtil.format(keyFormat, arg0, arg1, arg2);
    }

    public String get(Object arg0, Object arg1, Object arg2, Object arg3) {
        return StrUtil.format(keyFormat, arg0, arg1, arg2, arg3);
    }

    public String get(Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) {
        return StrUtil.format(keyFormat, arg0, arg1, arg2, arg3, arg4);
    }
}
