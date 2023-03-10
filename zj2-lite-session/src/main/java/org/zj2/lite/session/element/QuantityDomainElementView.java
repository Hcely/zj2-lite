package org.zj2.lite.session.element;


import org.zj2.lite.session.entity.Quantities;
import org.zj2.lite.session.entity.QuantitiesView;

/**
 * <br>CreateDate 三月 27,2022
 *
 * @author peijie.ye
 */

public interface QuantityDomainElementView<D> extends DomainElementView<D> {
    Quantities sourceQuantities();

    Quantities quantities();

    QuantitiesView start();
}
