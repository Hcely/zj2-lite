package org.zj2.lite.session.entity;

import java.util.Map;

/**
 *
 * <br>CreateDate 七月 01,2022
 * @author peijie.ye
 */
public class SessionAttr implements Map.Entry<String, Object> {
    private final String key;
    private Object value;

    public SessionAttr(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public Object setValue(Object value) {
        Object tmp = this.value;
        this.value = value;
        return tmp;
    }
}
