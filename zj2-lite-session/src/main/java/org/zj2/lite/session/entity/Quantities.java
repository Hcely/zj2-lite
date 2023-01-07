package org.zj2.lite.session.entity;


import org.zj2.lite.common.util.NumUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * <br>CreateDate 三月 23,2022
 * @author peijie.ye
 */
public class Quantities implements QuantitiesView, Serializable {
    private static final long serialVersionUID = 20220323165746L;

    public static Quantities unmodifiable(Quantities q) {
        return new UnmodifiableQuantities(q);
    }

    private final Map<String, Quantity> quantityMap;

    public Quantities(Map<String, Quantity> quantityMap) {
        this.quantityMap = quantityMap;
    }

    public Quantities copy() {
        Map<String, Quantity> map = new LinkedHashMap<>(16);
        for (Map.Entry<String, Quantity> e : quantityMap.entrySet()) {
            map.put(e.getKey(), new Quantity(e.getValue()));
        }
        return new Quantities(map);
    }

    @Override
    public Quantity get(String key) {
        return quantityMap.getOrDefault(key, Quantity.ZERO);
    }

    public Quantity get(Object key) {
        if (key == null) { return Quantity.ZERO; }
        String keyStr = key instanceof Enum ? ((Enum<?>) key).name() : key.toString();
        return get(keyStr);
    }

    public boolean modify(String key, BigDecimal mainQuantity, BigDecimal viceQuantity) {
        if (NumUtil.allEqZero(mainQuantity, viceQuantity)) { return false; }
        Quantity value = quantityMap.get(key);//NOSONAR
        if (value != null) {
            quantityMap.put(key, value.add(mainQuantity, viceQuantity));
            return true;
        }
        return false;
    }

    @Override
    public boolean modify(String key, Quantity modify) {
        return modify != null && modify(key, modify.getMain(), modify.getVice());
    }

    @Override
    public Collection<Map.Entry<String, Quantity>> values() {
        return Collections.unmodifiableCollection(quantityMap.entrySet());
    }

    public boolean eq(Quantities q) {
        if (q == null) { return eqZero(); }
        for (Map.Entry<String, Quantity> e : quantityMap.entrySet()) {
            if (!e.getValue().eq(q.quantityMap.get(e.getKey()))) { return false; }
        }
        for (Map.Entry<String, Quantity> e : q.quantityMap.entrySet()) {
            if (!e.getValue().eq(quantityMap.get(e.getKey()))) { return false; }
        }
        return true;
    }

    public Quantities add(Quantities q) {
        if (q == null) { return this.copy(); }
        Map<String, Quantity> map = new LinkedHashMap<>();
        for (Map.Entry<String, Quantity> e : quantityMap.entrySet()) {
            map.put(e.getKey(), e.getValue().add(q.quantityMap.get(e.getKey())));
        }
        for (Map.Entry<String, Quantity> e : q.quantityMap.entrySet()) {
            if (!map.containsKey(e.getKey())) { map.put(e.getKey(), e.getValue().copy()); }
        }
        return new Quantities(map);
    }

    public Quantities sub(Quantities q) {
        if (q == null) { return this.copy(); }
        Map<String, Quantity> map = new LinkedHashMap<>();
        for (Map.Entry<String, Quantity> e : quantityMap.entrySet()) {
            map.put(e.getKey(), e.getValue().sub(q.quantityMap.get(e.getKey())));
        }
        for (Map.Entry<String, Quantity> e : q.quantityMap.entrySet()) {
            if (!map.containsKey(e.getKey())) { map.put(e.getKey(), e.getValue().negate()); }
        }
        return new Quantities(map);
    }

    public boolean eqZero() {
        for (Quantity quantity : quantityMap.values()) {
            if (!quantity.eqZero()) { return false; }
        }
        return true;
    }

    private static class UnmodifiableQuantities extends Quantities {
        private static final long serialVersionUID = 3759257114756756306L;

        public UnmodifiableQuantities(Quantities quantities) {
            super(quantities.quantityMap);
        }

        @Override
        public boolean modify(String key, Quantity modify) {
            return false;
        }
    }
}
