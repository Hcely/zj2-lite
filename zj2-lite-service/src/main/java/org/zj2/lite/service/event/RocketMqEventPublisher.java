//package org.zj2.lite.service.event;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.rocketmq.client.producer.SendResult;
//import org.apache.rocketmq.spring.core.RocketMQTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.support.GenericMessage;
//import org.springframework.stereotype.Component;
//import org.zj2.lite.service.context.AuthenticationContext;
//import org.zj2.lite.util.TransactionSyncUtil;
//
//import java.util.Map;
//
///**
// * RocketMq消息发布者
// * <br>CreateDate 一月 16,2022
// * @author peijie.ye
// * @since 1.0
// */
//@Slf4j
//@Component
//@ConditionalOnClass(RocketMQTemplate.class)
//@ConditionalOnBean(RocketMQTemplate.class)
//public class RocketMqEventPublisher implements EventPublisher {
//    public static final String TYPE = "rocketMq";
//    @Autowired
//    private RocketMQTemplate rocketMQTemplate;
//
//    @Override
//    public void publish(BaseEvent<?> event) {
//        send(event);
//    }
//
//    public void publishAfterCommit(BaseEvent<?> event) {
//        if (event != null) {TransactionSyncUtil.afterCommit(event, this::send);}
//    }
//
//    public String send(BaseEvent<?> event) {
//        MqEvent mqEvent = event.mqEvent();
//        if (mqEvent != null && event.containsMqType(TYPE)) {
//            String topic = mqEvent.topic();
//            if (StringUtils.isNotEmpty(topic)) {
//                String tag = StringUtils.defaultIfEmpty(mqEvent.tag(), event.getTag());
//                return send(topic + ':' + tag, event.toJSONStr());
//            }
//        }
//        return null;
//    }
//
//
//    public void sendAfterCommit(String topicTag, Object value) {
//        if (value != null) {TransactionSyncUtil.afterCommit(() -> send(topicTag, value));}
//    }
//
//    /**
//     * @param topicTag
//     * @param value
//     * @return msgId
//     */
//    public String send(String topicTag, Object value) {
//        if (value == null) {return null;}
//        String messageStr = value instanceof String ?
//                value.toString() :
//                JSON.toJSONString(value, SerializerFeature.DisableCircularReferenceDetect);
//        //noinspection rawtypes,unchecked
//        Map<String, Object> uacHeaders = (Map) AuthenticationContext.current().buildHeaders();
//        Message<String> message = new GenericMessage<>(messageStr, uacHeaders);
//        try {
//            SendResult result = rocketMQTemplate.syncSend(topicTag, message);
//            log.info("[Send-RocketMq]-[topic=" + topicTag + ",msgId=" + result.getMsgId() + "]-" + messageStr);
//            return result.getMsgId();
//        } catch (Throwable e) {
//            log.error("[Send-RocketMq]-[topic=" + topicTag + "]-" + messageStr);
//            throw e;
//        }
//    }
//}
