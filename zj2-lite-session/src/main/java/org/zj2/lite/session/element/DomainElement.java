package org.zj2.lite.session.element;


import org.zj2.lite.session.DomainSession;
import org.zj2.lite.session.entity.SessionResult;

/**
 * <br>CreateDate 三月 27,2022
 *
 * @author peijie.ye
 */
@SuppressWarnings("rawtypes")
public interface DomainElement<S extends DomainSession, D> extends DomainElementView<D> {
    String elementId();

    S session();

    default void beforeSave() {
    }

    void save(SessionResult result);
}
