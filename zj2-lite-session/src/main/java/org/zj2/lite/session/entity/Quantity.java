package org.zj2.lite.session.entity;

import lombok.Getter;
import org.zj2.lite.common.util.NumUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 *
 * <br>CreateDate 三月 23,2022
 * @author peijie.ye
 */
@Getter
public class Quantity implements Serializable {
    public static final Quantity ZERO = new Quantity(null, null);

    public static Quantity min(Quantity q, Number main, Number vice) {
        if (main == null && vice == null) {return q == null ? ZERO : q;}
        if (q == null) {return new Quantity(NumUtil.min(main, BigDecimal.ZERO), NumUtil.min(vice, BigDecimal.ZERO));}
        return new Quantity(NumUtil.min(q.main, main), NumUtil.min(q.vice, vice));
    }

    public static Quantity min(Quantity a, Quantity b) {
        return b == null ? min(a, null, null) : min(a, b.main, b.vice);
    }

    public static Quantity max(Quantity q, Number main, Number vice) {
        if (main == null && vice == null) {return q == null ? ZERO : q;}
        if (q == null) {return new Quantity(NumUtil.max(main, BigDecimal.ZERO), NumUtil.max(vice, BigDecimal.ZERO));}
        return new Quantity(NumUtil.max(q.main, main), NumUtil.max(q.vice, vice));
    }

    public static Quantity max(Quantity a, Quantity b) {
        return b == null ? max(a, null, null) : max(a, b.main, b.vice);
    }

    private static final long serialVersionUID = 20220323152709L;
    private final BigDecimal main;
    private final BigDecimal vice;

    public Quantity(Number main) {
        this(main, null);
    }

    public Quantity(Quantity quantity) {
        this.main = quantity == null ? BigDecimal.ZERO : quantity.main;
        this.vice = quantity == null ? BigDecimal.ZERO : quantity.vice;
    }

    public Quantity(Number main, Number vice) {
        this.main = main == null ? BigDecimal.ZERO : NumUtil.of(main);
        this.vice = vice == null ? BigDecimal.ZERO : NumUtil.of(vice);
    }

    public Quantity sub(Quantity quantity) {
        return quantity == null ? this : sub(quantity.main, quantity.vice);
    }

    public Quantity sub(Number main, Number vice) {
        if (NumUtil.eqZero(main) && NumUtil.eqZero(vice)) {
            return this;
        } else {
            return new Quantity(NumUtil.sub(this.main, main), NumUtil.sub(this.vice, vice));
        }
    }

    public Quantity add(Quantity quantity) {
        return quantity == null ? this : add(quantity.main, quantity.vice);
    }

    public Quantity add(Number main, Number vice) {
        if (NumUtil.eqZero(main) && NumUtil.eqZero(vice)) {
            return this;
        } else {
            return new Quantity(NumUtil.add(this.main, main), NumUtil.add(this.vice, vice));
        }
    }

    public Quantity multi(Number value) {
        return multi(value, value, null);
    }

    public Quantity multi(Number value, MathContext context) {
        return multi(value, value, context);
    }

    public Quantity multi(Number main, Number vice) {
        return multi(main, vice, null);
    }

    public Quantity multi(Number main, Number vice, MathContext context) {
        if (main == null && vice == null) {return ZERO;}
        return new Quantity(NumUtil.multi(this.main, main, context), NumUtil.multi(this.vice, vice, context));
    }

    public Quantity multi(Quantity quantity) {
        return multi(quantity, null);
    }

    public Quantity multi(Quantity quantity, MathContext context) {
        return quantity == null ? ZERO : multi(quantity.main, quantity.vice, context);
    }

    public Quantity copy() {
        return new Quantity(this);
    }

    public Quantity negate() {
        if (NumUtil.eqZero(main) && NumUtil.eqZero(vice)) {
            return this;
        } else {
            return new Quantity(NumUtil.negate(this.main), NumUtil.negate(this.vice));
        }
    }

    public boolean eq(Quantity quantity) {
        if (quantity == null) {return eqZero();}
        return NumUtil.eq(main, quantity.main) && NumUtil.eq(vice, quantity.vice);
    }

    public boolean eqZero() {
        return NumUtil.eqZero(main) && NumUtil.eqZero(vice);
    }
}
