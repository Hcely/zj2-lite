package org.zj2.lite.session;


import org.zj2.lite.session.element.DomainElement;
import org.zj2.lite.session.element.DomainElementView;
import org.zj2.lite.session.entity.DefSessionResult;
import org.zj2.lite.session.entity.SessionLog;
import org.zj2.lite.session.entity.SessionResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * <br>CreateDate 三月 25,2022
 *
 * @author peijie.ye
 */
@SuppressWarnings("all")
public abstract class AbsDomainSession<M extends SessionManager, D, V extends DomainElementView> extends AbsStateSession<M>
        implements DomainSession<M, D, V> {
    private List<SessionLog> rollbackLogs;

    protected AbsDomainSession(M sessionManager) {
        super(sessionManager);
    }

    @Override
    public V element(D elementData) {
        checkStatus();
        return elementImpl(elementData);
    }

    @Override
    public void rollback() {
        List<SessionLog> logs = historyLogs();
        if(logs != null) {
            for(SessionLog log : logs) {
                rollback(log);
                addRollbackLog(log);
            }
        }
    }

    @Override
    public void resetOp() {
        List<SessionLog> logs = historyLogs();
        if(logs != null) {
            for(SessionLog log : logs) { reset(log); }
        }
    }

    protected abstract void rollback(SessionLog<?> sessionLog);

    protected abstract void reset(SessionLog<?> sessionLog);

    protected void addRollbackLog(SessionLog log) {
        if(rollbackLogs == null) { rollbackLogs = new ArrayList<>(10); }
        rollbackLogs.add(log);
    }

    protected <T extends SessionLog> List<T> rollbackLogs() {
        return rollbackLogs == null ? Collections.emptyList() : (List<T>)rollbackLogs;
    }

    protected abstract V elementImpl(D data);

    @Override
    protected final SessionResult onSubmitted() {
        final SessionResult result = new DefSessionResult();
        List<? extends DomainElement> elements = currentElements();
        if(elements != null && !elements.isEmpty()) {
            beforeSaveElements(elements);
            result.addLogs(sessionLogs());
            executeSaveElements(elements, result);
            saveResult(result);
        }
        return result;
    }

    protected void beforeSaveElements(List<? extends DomainElement> elements) {
        ListIterator<? extends DomainElement> it = elements.listIterator(elements.size());
        while(it.hasPrevious()) { it.previous().beforeSave(); }
    }

    protected void executeSaveElements(List<? extends DomainElement> elements, SessionResult result) {
        ListIterator<? extends DomainElement> it = elements.listIterator(elements.size());
        while(it.hasPrevious()) { it.previous().save(result); }
    }

    protected void saveResult(SessionResult result) {
        sessionManager.saveResult(this, result, rollbackLogs());
    }
}
