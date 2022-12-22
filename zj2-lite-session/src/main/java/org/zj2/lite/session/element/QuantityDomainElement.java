package org.zj2.lite.session.element;


import org.zj2.lite.session.DomainSession;

/**
 *
 * <br>CreateDate 三月 27,2022
 * @author peijie.ye
 */
@SuppressWarnings("rawtypes")
public interface QuantityDomainElement<S extends DomainSession, D>
        extends DomainElement<S, D>, QuantityDomainElementView<D> {
}
