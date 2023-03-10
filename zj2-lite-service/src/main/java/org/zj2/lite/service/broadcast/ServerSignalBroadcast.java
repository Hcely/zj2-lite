package org.zj2.lite.service.broadcast;

/**
 * ServerMsgBroadcast
 *
 * @author peijie.ye
 * @date 2022/12/12 16:02
 */
public interface ServerSignalBroadcast {
    boolean broadcast(String tag, String msg);

    void addSignalListener(ServerSignalListener listener);

    ServerSignalHandler msgHandler();
}
