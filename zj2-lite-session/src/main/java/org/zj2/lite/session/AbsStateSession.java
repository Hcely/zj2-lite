package org.zj2.lite.session;

import org.apache.commons.lang3.StringUtils;
import org.zj2.lite.common.entity.result.ZRBuilder;
import org.zj2.lite.session.entity.DefSessionResult;
import org.zj2.lite.session.entity.SessionAttr;
import org.zj2.lite.session.entity.SessionLog;
import org.zj2.lite.session.entity.SessionResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 *
 * <br>CreateDate 三月 25,2022
 * @author peijie.ye
 */
@SuppressWarnings("all")
public abstract class AbsStateSession<M extends SessionManager> implements Session<M> {
    private static final AtomicIntegerFieldUpdater<AbsStateSession> STATUS_UPDATER = AtomicIntegerFieldUpdater.newUpdater(
            AbsStateSession.class, "state");
    private static final int INIT = 0;
    private static final int STARTING = 1;
    private static final int STARTED = 2;
    private static final int FINISHED = 10;
    private static final int CLOSED = 11;
    private static final int ERROR = 12;
    protected final M sessionManager;
    private volatile int state;
    private SessionLock lock;
    private Map<String, SessionLog> sessionLogs;
    private List<SessionLog> historyLogs;
    private List<SessionAttr> attrs;


    protected AbsStateSession(M sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void start() {
        if (casStatus(INIT, STARTING)) {
            lock = sessionManager.getLock(this);
            if (lock == null) {lock = SessionLock.EMPTY_LOCK;}
            boolean b;
            try {
                b = lock.lock();
            } catch (Throwable e) {
                casStatus(STARTING, ERROR);
                throw ZRBuilder.builder().msg0("session启动失败").buildError(e);
            }
            if (b) {
                casStatus(STARTING, STARTED);
                onStarted();
            } else {
                casStatus(STARTING, ERROR);
                throw ZRBuilder.builder().msg0("session请求频繁，启动失败").buildError();
            }
        }
    }

    @Override
    public SessionResult submit() {
        if (casStatus(STARTED, FINISHED)) {
            try {
                return onSubmitted();
            } finally {
                if (lock != null) {lock.unlock();}
            }
        } else {
            return null;
        }
    }

    @Override
    public void close() {
        if (casStatus(STARTED, CLOSED)) {
            try {
                onClosed();
            } finally {
                if (lock != null) {lock.unlock();}
            }
        }
    }

    @Override
    public M sessionManager() {
        return sessionManager;
    }

    @Override
    public void addSessionLog(SessionLog log) {
        if (sessionLogs == null) {sessionLogs = new LinkedHashMap<>();}
        sessionLogs.compute(log.logKey(), (key, l) -> l == null ? log : l.addModify(log));
    }

    @Override
    public List sessionLogs() {
        return sessionLogs == null ? Collections.emptyList() : new ArrayList<>(sessionLogs.values());
    }

    @Override
    public List historyLogs() {
        if (historyLogs == null) {
            historyLogs = sessionManager.getHistoryLogs(this);
        }
        return historyLogs;
    }

    protected final void checkStatus() {
        if (state != STARTED) {throw ZRBuilder.builder().msg0("session未启动!").buildError();}
    }

    private boolean casStatus(int oldValue, int newValue) {
        return state == oldValue && STATUS_UPDATER.compareAndSet(this, oldValue, newValue);
    }

    protected void onStarted() {
    }

    protected SessionResult onSubmitted() {
        return new DefSessionResult();
    }

    protected void onClosed() {
    }

    @Override
    public <T> T attr(String key) {
        if (attrs != null && StringUtils.isNotEmpty(key)) {
            for (SessionAttr e : attrs) {
                if (key.equals(e.getKey())) {return (T) e.getValue();}
            }
        }
        return null;
    }

    @Override
    public Object attr(String key, Object value) {
        if (StringUtils.isEmpty(key)) {
            return null;
        } else if (attrs == null) {
            if (value != null) {
                attrs = new ArrayList<>(5);
                attrs.add(new SessionAttr(key, value));
            }
            return null;
        } else {
            for (Iterator<SessionAttr> it = attrs.iterator(); it.hasNext(); ) {
                Map.Entry<String, Object> e = it.next();
                if (key.equals(e.getKey())) {
                    Object tmp = e.getValue();
                    if (value == null) {it.remove();} else {e.setValue(value);}
                    return tmp;
                }
            }
            attrs.add(new SessionAttr(key, value));
            return null;
        }
    }

    @Override
    public Collection<SessionAttr> attrs() {
        return attrs == null ? Collections.emptyList() : attrs;
    }
}
