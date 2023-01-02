package org.zj2.lite.service.broadcast;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zj2.lite.service.util.ServerInfoUtil;
import org.zj2.lite.util.AsyncUtil;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 *  AbstractServerMsgBroadcast
 *
 * @author peijie.ye
 * @date 2022/12/12 21:43
 */
public abstract class AbstractServerSignalBroadcast implements ServerSignalBroadcast, ServerSignalHandler {
    private final CopyOnWriteArrayList<ServerSignalListener> listeners = new CopyOnWriteArrayList<>();
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean broadcast(String tag, String signal) {
        if (StringUtils.isEmpty(tag) || StringUtils.isEmpty(signal)) {return false;}
        if (tag.length() > 250) {
            logger.warn("系统信号广播:tag[{}]不能超过250字符", tag);
            return false;
        }
        if (signal.length() > 250) {
            logger.warn("系统信号广播:signal[{}]不能超过250字符", signal);
            return false;
        }
        final ServerSignal serverSignal = new ServerSignal(ServerInfoUtil.getServerId(), System.currentTimeMillis(), tag,
                signal);
        AsyncUtil.execute(() -> {
            onMsg0(serverSignal);
            broadcast(serverSignal);
        });
        return true;
    }

    @Override
    public void addSignalListener(ServerSignalListener listener) {
        if (listener != null) {
            synchronized (this) {if (!listeners.contains(listener)) {listeners.add(listener);}}
        }
    }

    @Override
    public ServerSignalHandler msgHandler() {
        return this;
    }

    @Override
    public void onMsg(ServerSignal signal) {
        if (signal != null && !StringUtils.equalsIgnoreCase(ServerInfoUtil.getServerId(), signal.getServerId())) {
            onMsg0(signal);
        }
    }

    private void onMsg0(ServerSignal msg) {
        CopyOnWriteArrayList<ServerSignalListener> list = listeners;
        for (int i = 0, len = list.size(); i < len; ++i) {
            ServerSignalListener listener = list.get(0);
            if (listener != null) {onMsg0(listener, msg);}
        }
    }

    private void onMsg0(ServerSignalListener listener, ServerSignal msg) {
        try {
            if (listener.supports(msg.getTag())) {
                listener.onMsg(msg);
            }
        } catch (Throwable e) {
            logger.error(listener.getClass().getSimpleName() + "-系统信号广播处理异常:" + JSON.toJSONString(msg), e);
        }
    }

    protected abstract void broadcast(ServerSignal msg);

}
