package org.zj2.lite.service.broadcast;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.zj2.lite.common.util.CollUtil;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 *  RedisServerMsgBroadcast
 *
 * @author peijie.ye
 * @date 2022/12/13 0:28
 */
@ConditionalOnMissingBean(ServerSignalBroadcast.class)
@Component
public class RedisServerSignalBroadcast extends AbstractServerSignalBroadcast {
    public static final String DEF_BROADCAST_CHANNEL = "SERVER_BROADCAST_CHANNEL";
    @Autowired
    private StringRedisTemplate redisTemplate;
    private String broadcastChannel = DEF_BROADCAST_CHANNEL;
    @Autowired(required = false)
    private List<ServerSignalListener> serverSignalListeners;

    public String getBroadcastChannel() {
        return broadcastChannel;
    }

    public void setBroadcastChannel(String broadcastChannel) {
        this.broadcastChannel = broadcastChannel;
    }

    @PostConstruct
    protected void init() {
        //
        RedisConnection redisConnection = redisTemplate.getRequiredConnectionFactory().getConnection();
        redisConnection.subscribe(
                (message, bytes) -> msgHandler().onMsg(JSON.parseObject(message.getBody(), ServerSignal.class)),
                broadcastChannel.getBytes(StandardCharsets.UTF_8));
        //
        for (ServerSignalListener l : CollUtil.descIterable(serverSignalListeners)) {
            addSignalListener(l);
        }
    }

    @Override
    protected void broadcast(ServerSignal msg) {
        redisTemplate.convertAndSend(broadcastChannel, JSON.toJSONString(msg));
    }
}
