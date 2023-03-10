package org.zj2.lite.service.broadcast;

/**
 * ServerMsgHandler
 *
 * @author peijie.ye
 * @date 2022/12/13 0:17
 */
public interface ServerSignalHandler {
    void onMsg(ServerSignal signal);
}
