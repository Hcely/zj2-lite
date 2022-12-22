package org.zj2.lite.sign;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 *  SignBuilder
 *
 * @author peijie.ye
 * @date 2022/11/27 23:33
 */

public class SignBuilder {
    @Getter
    private Map<String, Object> params;
    @Getter
    private String prefix;
    @Getter
    private String suffix;
    //
    private Function<SignBuilder, String> toStringFunc;
    private Sign  signFunc;

    public SignBuilder put(String key, Object value) {
        if (params == null) {params = new LinkedHashMap<>();}
        params.put(key == null ? "" : key, value);
        return this;
    }

    public SignBuilder prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public SignBuilder suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public SignBuilder toStringFunc(Function<SignBuilder, String> toStringFunc) {
        this.toStringFunc = toStringFunc;
        return this;
    }

    public SignBuilder signFunc(Sign signFunc) {//NOSONAR
        this.signFunc = signFunc;
        return this;
    }

    public String sign() {
        String value = toStringFunc == null ? StandardToStringFunc.INSTANCE.apply(this) : toStringFunc.apply(this);
        return signFunc == null ? Md5Sign.INSTANCE.sign(value) : signFunc.sign(value);
    }
}
