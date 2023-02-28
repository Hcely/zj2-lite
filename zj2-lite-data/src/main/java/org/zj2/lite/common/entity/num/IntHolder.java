package org.zj2.lite.common.entity.num;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zj2.lite.common.util.NumUtil;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

/**
 * IntHolder
 *
 * @author peijie.ye
 * @date 2023/1/18 15:09
 */
@Getter
@Setter
@NoArgsConstructor
public class IntHolder extends Number {
    private static final long serialVersionUID = 2696980399997083841L;
    protected int value;

    public IntHolder(int value) {
        this.value = value;
    }

    public IntHolder add(int v) {
        this.value = value + v;
        return this;
    }

    public IntHolder add(Number v) {
        return add(v, null);
    }

    public IntHolder add(Number v, MathContext context) {
        if (v != null) {
            if (v instanceof NumHolder) { v = ((NumHolder) v).value; }
            this.value = NumUtil.add(this.value, v, context).intValue();
        }
        return this;
    }

    public IntHolder sub(int v) {
        this.value = value - v;
        return this;
    }

    public IntHolder sub(Number v) {
        return sub(v, null);
    }

    public IntHolder sub(Number v, MathContext context) {
        if (v != null) {
            if (v instanceof NumHolder) { v = ((NumHolder) v).value; }
            this.value = NumUtil.sub(this.value, v, context).intValue();
        }
        return this;
    }

    public IntHolder multi(int v) {
        this.value = value * v;
        return this;
    }

    public IntHolder multi(Number v) {
        return multi(v, null);
    }

    public IntHolder multi(Number v, MathContext context) {
        if (v instanceof NumHolder) { v = ((NumHolder) v).value; }
        this.value = NumUtil.multi(this.value, v, context).intValue();
        return this;
    }

    public IntHolder divide(int v) {
        this.value = value / v;
        return this;
    }

    public IntHolder divide(Number v) {
        return divide(v, null);
    }

    public IntHolder divide(Number value, MathContext context) {
        if (value instanceof NumHolder) { value = ((NumHolder) value).value; }
        Number v = NumUtil.divide(this.value, value, context);
        this.value = v == null ? 0 : v.intValue();
        return this;
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
