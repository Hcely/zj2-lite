package org.zj2.lite.session;


import org.zj2.lite.session.entity.SessionAttr;
import org.zj2.lite.session.entity.SessionLog;
import org.zj2.lite.session.entity.SessionResult;

import java.util.Collection;
import java.util.List;

/**
 * <br>CreateDate 三月 25,2022
 *
 * @author peijie.ye
 */
@SuppressWarnings("rawtypes")
public interface Session<M extends SessionManager> extends AutoCloseable {
    String sessionId();

    /**
     * 开始
     */
    void start();

    /**
     * 回滚
     */
    void rollback();

    /**
     * 重置
     */
    void resetOp();

    /**
     * 提交
     */
    SessionResult submit();

    @Override
    void close();

    M sessionManager();

    void addSessionLog(SessionLog log);

    List<? extends SessionLog> sessionLogs();//NOSONAR

    List<? extends SessionLog> historyLogs();//NOSONAR

    <T> T attr(String key);

    Object attr(String key, Object value);

    Collection<SessionAttr> attrs();
}
