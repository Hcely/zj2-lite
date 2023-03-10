package org.zj2.lite.session;


import org.zj2.lite.session.element.DomainElement;
import org.zj2.lite.session.element.DomainElementView;

import java.util.List;

/**
 * <br>CreateDate 三月 25,2022
 *
 * @author peijie.ye
 */
@SuppressWarnings("all")
public interface DomainSession<M extends SessionManager, D, V extends DomainElementView> extends Session<M> {
    V element(D elementData);

    List<? extends DomainElement> currentElements();
}
