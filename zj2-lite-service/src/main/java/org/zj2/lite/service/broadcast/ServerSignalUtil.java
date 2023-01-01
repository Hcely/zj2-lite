package org.zj2.lite.service.broadcast;

import org.zj2.lite.spring.SpringBeanRef;
import org.zj2.lite.util.TransactionSyncUtil;

/**
 *  ServerSignalUtil
 *
 * @author peijie.ye
 * @date 2022/12/13 0:44
 */
public class ServerSignalUtil {
    private static final SpringBeanRef<ServerSignalBroadcast> BROADCAST_REF = new SpringBeanRef<>(
            ServerSignalBroadcast.class);

    public static boolean broadcast(String tag, String signal) {
        ServerSignalBroadcast broadcast = BROADCAST_REF.get();
        if (broadcast != null) {
            return broadcast.broadcast(tag, signal);
        } else {
            return false;
        }
    }

    public static boolean broadcastAfterCommit(String tag, String signal) {
        ServerSignalBroadcast broadcast = BROADCAST_REF.get();
        if (broadcast != null) {
            TransactionSyncUtil.afterCommit(() -> broadcast.broadcast(tag, signal));
            return true;
        } else {
            return false;
        }
    }

    public static boolean broadcastAfterRollback(String tag, String signal) {
        ServerSignalBroadcast broadcast = BROADCAST_REF.get();
        if (broadcast != null) {
            TransactionSyncUtil.afterRollback(() -> broadcast.broadcast(tag, signal));
            return true;
        } else {
            return false;
        }
    }
}
