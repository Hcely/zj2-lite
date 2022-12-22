package org.zj2.lite.session.entity;

import java.util.Collection;
import java.util.List;

/**
 *
 * <br>CreateDate 三月 27,2022
 * @author peijie.ye
 */
@SuppressWarnings("rawtypes")
public interface SessionResult {
    void addLog(SessionLog log);

    default void addLogs(Collection<SessionLog> logs) {
        if (logs != null) {
            for (SessionLog log : logs) {
                addLog(log);
            }
        }
    }

    <T extends SessionLog> List<T> getLogs();
}
