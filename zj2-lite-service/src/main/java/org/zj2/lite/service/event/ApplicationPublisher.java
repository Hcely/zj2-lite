package org.zj2.lite.service.event;

import org.zj2.lite.service.entity.event.BaseEvent;
import org.zj2.lite.spring.SpringUtil;

/**
 * ApplicationPublisher
 * <br>CreateDate 一月 18,2022
 *
 * @author peijie.ye
 * @since 1.0
 */

public class ApplicationPublisher implements EventPublisher {
    @Override
    public void publish(BaseEvent<?> event) {
        SpringUtil.getContext().publishEvent(event);
    }
}
