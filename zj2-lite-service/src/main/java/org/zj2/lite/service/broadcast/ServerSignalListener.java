package org.zj2.lite.service.broadcast;

import org.zj2.lite.Supportable;

/**
 *  ServerMsgListener
 *
 * @author peijie.ye
 * @date 2022/12/12 16:11
 */
public interface ServerSignalListener extends Supportable<String>, ServerSignalHandler {
    @Override
    boolean supports(String tag);
}
