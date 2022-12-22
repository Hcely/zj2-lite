package org.zj2.lite.service.event;


public interface EventPublisher {
    void publish(BaseEvent<?> event);
}
