package org.zj2.lite.session.entity;

import java.io.Serializable;

/**
 * <br>CreateDate 三月 27,2022
 *
 * @author peijie.ye
 */
public interface SessionLog<L extends SessionLog<?>> extends Serializable {
    String logKey();

    SessionLog<L> addModify(L log);
}
