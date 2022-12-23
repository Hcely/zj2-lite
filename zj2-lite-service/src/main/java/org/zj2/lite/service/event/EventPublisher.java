package org.zj2.lite.service.event;


import org.zj2.lite.service.entity.event.BaseEvent;

public interface EventPublisher {
    void publish(BaseEvent<?> event);
}
