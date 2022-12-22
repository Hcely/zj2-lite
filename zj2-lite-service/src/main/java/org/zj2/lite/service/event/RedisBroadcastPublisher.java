package org.zj2.lite.service.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * redis订阅消息发布者
 * <br>CreateDate 一月 16,2022
 * @author peijie.ye
 * @since 1.0
 */
@Slf4j
@Component
@ConditionalOnBean(StringRedisTemplate.class)
class RedisBroadcastPublisher implements EventPublisher {
    public static final String TYPE = "redisBroadcast";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void publish(BaseEvent<?> event) {
        MqEvent mqEvent = event.mqEvent();
        if (mqEvent != null && event.containsMqType(TYPE)) {
            String topic = mqEvent.topic();
            if (StringUtils.isNotEmpty(topic)) {send(topic, event.toJSONStr());}
        }
    }

    public void send(String topic, Object value) {
        String messageStr = value instanceof String ?
                value.toString() :
                JSON.toJSONString(value, SerializerFeature.DisableCircularReferenceDetect);
        log.info("[Send-RedisBroadcast]-[topic=" + topic + "]-" + messageStr);
        stringRedisTemplate.convertAndSend(topic, messageStr);
    }
}
