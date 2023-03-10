package org.zj2.lite.session.element;


import org.zj2.lite.session.DomainSession;

/**
 * <br>CreateDate 三月 24,2022
 *
 * @author peijie.ye
 */
@SuppressWarnings("rawtypes")
public abstract class AbsDomainElement<S extends DomainSession, D> implements DomainElement<S, D> {
    protected final S session;
    protected final DomainElement parent;
    protected final D elementData;

    protected AbsDomainElement(S session, DomainElement parent, D elementData) {
        this.session = session;
        this.parent = parent;
        this.elementData = elementData;
    }

    @Override
    public S session() {
        return session;
    }

    public DomainElement parent() {
        return parent;
    }

    @Override
    public D elementData() {
        return elementData;
    }
}
