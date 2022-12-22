package org.zj2.lite.service.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.spring.InstanceHolder;
import org.zj2.lite.spring.SpringUtil;
import org.zj2.lite.util.TransactionSyncUtil;

import java.util.Collection;

/**
 * 事件发送Manager
 */
public class EventPublishManager {
    static final Logger logger = LoggerFactory.getLogger(EventPublishManager.class);
    private static final ThreadLocal<Boolean> PUBLISH_IGNORE_ERR_FLAG = new ThreadLocal<>();
    private static final InstanceHolder<EventPublisher[]> eventPublishers = new InstanceHolder<>(() -> {
        Collection<EventPublisher> coll = CollUtil.values(SpringUtil.getBeansOfType(EventPublisher.class));
        EventPublisher[] publishers = new EventPublisher[coll.size() + 1];
        publishers[0] = new ApplicationPublisher();
        int i = 1;
        for (EventPublisher publisher : coll) {
            publishers[i] = publisher;
            ++i;
        }
        return publishers;
    });

    private EventPublishManager() {
    }

    public static boolean isPublishing() {
        return PUBLISH_IGNORE_ERR_FLAG.get() != null;
    }

    public static boolean isIgnoreError() {
        Boolean b = PUBLISH_IGNORE_ERR_FLAG.get();
        return b != null && b;
    }

    /**
     * 发布事件
     * @param event
     */
    public static void publish(BaseEvent<?> event) {
        if (event != null) {publish(event, false);}
    }

    /**
     * 事务提交后发布事件
     * @param event
     */
    public static void publishAfterCommit(BaseEvent<?> event) {
        if (event != null) {TransactionSyncUtil.afterCommit(event, e -> publish(e, true));}
    }

    public static void publishIgnoreError(BaseEvent<?> event) {
        if (event != null) {publish(event, true);}
    }

    private static void publish(BaseEvent<?> event, boolean ignoreError) {
        EventPublisher[] publishers = eventPublishers.get();
        if (CollUtil.isEmpty(publishers)) {
            logger.debug("没有可供使用 EventPublisher");
        } else {
            try {
                //noinspection StringBufferReplaceableByString
                String msg = new StringBuilder(96).append("事件[").append(event.getClass().getSimpleName()).append('-')
                        .append(event.getTag()).append("]发送").toString();
                logger.info(msg);
                PUBLISH_IGNORE_ERR_FLAG.set(ignoreError);
                for (EventPublisher publisher : publishers) {
                    try {
                        publisher.publish(event);
                    } catch (Throwable e) {// NOSONAR
                        logger.error("事件推送异常", e);
                        if (ignoreError) {
                            //  SysError.error("事件推送异常").errorData(event).throwable(e).record();
                        } else {
                            throw e;
                        }
                    }
                }
            } finally {
                PUBLISH_IGNORE_ERR_FLAG.remove();
            }
        }
    }
}