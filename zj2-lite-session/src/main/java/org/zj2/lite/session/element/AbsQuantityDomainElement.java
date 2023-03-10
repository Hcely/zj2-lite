package org.zj2.lite.session.element;


import org.zj2.lite.session.DomainSession;
import org.zj2.lite.session.entity.Quantities;
import org.zj2.lite.session.entity.QuantitiesView;
import org.zj2.lite.session.entity.Quantity;

import java.util.Collection;
import java.util.Map;

/**
 * <br>CreateDate 三月 27,2022
 *
 * @author peijie.ye
 */
@SuppressWarnings("rawtypes")
public abstract class AbsQuantityDomainElement<S extends DomainSession, D> extends AbsDomainElement<S, D>
        implements QuantityDomainElement<S, D>, QuantitiesView {
    protected final Quantities quantities;
    protected final Quantities unmodifiableQuantities;
    protected final Quantities unmodifiableSourceQuantities;

    protected AbsQuantityDomainElement(S session, QuantityDomainElement parent, D elementData, Quantities quantities) {
        super(session, parent, elementData);
        this.quantities = quantities;
        this.unmodifiableQuantities = Quantities.unmodifiable(quantities);
        this.unmodifiableSourceQuantities = Quantities.unmodifiable(quantities.copy());
    }

    @Override
    public boolean modify(String key, Quantity modify) {
        if(modify == null || modify.eqZero()) { return false; }
        return modify0(true, key, modify);
    }

    protected boolean modify0(boolean leaf, String key, Quantity modify) {
        final Quantity before = quantities.get(key);
        final boolean b = quantities.modify(key, modify);
        if(b && !modify.eqZero()) { onModify(leaf, key, before, modify, quantities.get(key)); }
        if(parent != null) {
            if(parent instanceof AbsQuantityDomainElement) {
                ((AbsQuantityDomainElement)parent).modify0(!b && leaf, key, modify);
            } else {
                ((QuantityDomainElement)parent).start().modify(key, modify);
            }
        }
        return b;
    }

    @Override
    public Quantities quantities() {
        return unmodifiableQuantities;
    }

    @Override
    public Quantity get(String key) {
        return quantities.get(key);
    }

    @Override
    public Collection<Map.Entry<String, Quantity>> values() {
        return quantities.values();
    }

    @Override
    public Quantities sourceQuantities() {
        return unmodifiableSourceQuantities;
    }

    protected void onModify(boolean leaf, String quantityType, Quantity before, Quantity modify, Quantity after) {
    }
}
