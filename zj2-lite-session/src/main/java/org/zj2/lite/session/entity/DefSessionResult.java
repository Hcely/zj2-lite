package org.zj2.lite.session.entity;

import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <br>CreateDate 三月 27,2022
 *
 * @author peijie.ye
 */
@SuppressWarnings("all")
@NoArgsConstructor
public class DefSessionResult implements SessionResult, Serializable {
    private static final long serialVersionUID = 20220327044022L;
    private List<SessionLog> logs;

    @Override
    public void addLog(SessionLog log) {
        if(logs == null) { logs = new ArrayList<>(10); }
        logs.add(log);
    }

    @Override
    public <T extends SessionLog> List<T> getLogs() {
        return logs == null ? Collections.emptyList() : (List<T>)logs;
    }
}
