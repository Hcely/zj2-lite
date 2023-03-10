package org.zj2.lite.session;


import org.zj2.lite.session.entity.SessionLog;
import org.zj2.lite.session.entity.SessionResult;

import java.util.List;
import java.util.function.Consumer;

/**
 * <br>CreateDate 三月 25,2022
 *
 * @author peijie.ye
 */
@SuppressWarnings("rawtypes")
public interface SessionManager<T, S extends Session> {
    S session(T op);

    void session(T op, Consumer<S> consumer);

    void rollbackOp(T op);

    SessionLock getLock(S session);

    void saveResult(S session, SessionResult result, List<SessionLog> rollbackLogs);

    List<? extends SessionLog> getHistoryLogs(S session);//NOSONAR
}
