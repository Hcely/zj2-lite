package org.zj2.lite.service.entity.event;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

/**
 * BasicEvent
 * <br>CreateDate 一月 11,2022
 * @author peijie.ye
 * @since 1.0
 */
public class BaseEvent<T> extends ApplicationEvent {
    private static final long serialVersionUID = -8755213378344053481L;

    private static String nextEventId() {
        return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
    }

    protected String eventId;
    protected long eventTime;
    protected String tag;
    protected T source;//NOSONAR
    private transient Object mqEvent;

    protected BaseEvent() {
        super(Boolean.TRUE);
    }

    protected BaseEvent(String tag, T source) {
        super(Boolean.TRUE);
        this.eventId = nextEventId();
        this.eventTime = System.currentTimeMillis();
        this.source = source;
        this.tag = tag;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }

    @Override
    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public int hashCode() {//NOSONAR
        int hash = bizHashCode();
        return hash == -1 ? super.hashCode() : hash;
    }

    protected int bizHashCode() {
        return -1;
    }

    public MqEvent mqEvent() {
        Object result = mqEvent;
        if (result == null) { mqEvent = result = mqEventImpl(); }
        return result == BaseEvent.class ? null : (MqEvent) result;
    }

    public boolean containsMqType(String type) {
        MqEvent event = mqEvent();
        if (event == null) { return false; }
        for (String t : event.type()) {
            if (StringUtils.equalsIgnoreCase(t, type)) { return true; }
        }
        return false;
    }

    private Object mqEventImpl() {
        Class<?> type = this.getClass();
        while (type != null && type != Object.class) {
            MqEvent result = type.getAnnotation(MqEvent.class);
            if (result != null) { return result; }
            type = type.getSuperclass();
        }
        return BaseEvent.class;
    }

    public String toJSONStr() {
        return JSON.toJSONString(this);
    }

}
