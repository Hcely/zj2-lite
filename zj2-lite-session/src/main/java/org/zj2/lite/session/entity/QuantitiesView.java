package org.zj2.lite.session.entity;

import java.util.Collection;
import java.util.Map;

/**
 * <br>CreateDate 三月 23,2022
 *
 * @author peijie.ye
 */
public interface QuantitiesView {
    Quantity get(String key);

    Collection<Map.Entry<String, Quantity>> values();

    boolean modify(String key, Quantity modify);

    default Quantity reset(String key) {
        return set(key, Quantity.ZERO);
    }

    default Quantity set(final String key, Quantity quantity) {
        Quantity modify = (quantity == null ? Quantity.ZERO : quantity).sub(get(key));
        modify(key, modify);
        return modify;
    }

    default void operate(QuantityCalculateSign sign, Quantity quantity) {
        sign.calculate(this, quantity);
    }

    default void operate(QuantityCalculateSign sign, Number main) {
        operate(sign, new Quantity(main));
    }

    default void operate(QuantityCalculateSign sign, Number main, Number vice) {
        operate(sign, new Quantity(main, vice));
    }
}
