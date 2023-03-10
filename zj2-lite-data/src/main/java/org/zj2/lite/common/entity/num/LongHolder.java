package org.zj2.lite.common.entity.num;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zj2.lite.common.util.NumUtil;

import java.math.MathContext;

/**
 * IntHolder
 *
 * @author peijie.ye
 * @date 2023/1/18 15:09
 */
@Getter
@Setter
@NoArgsConstructor
public class LongHolder extends Number {
    private static final long serialVersionUID = 2696980399997083841L;
    protected long value;

    public LongHolder(long value) {
        this.value = value;
    }


    public LongHolder add(long v) {
        this.value = value + v;
        return this;
    }

    public LongHolder add(Number v) {
        return add(v, null);
    }

    public LongHolder add(Number v, MathContext context) {
        if(v != null) {
            if(v instanceof NumHolder) { v = ((NumHolder)v).value; }
            this.value = NumUtil.add(this.value, v, context).longValue();
        }
        return this;
    }

    public LongHolder sub(int v) {
        this.value = value - v;
        return this;
    }

    public LongHolder sub(Number v) {
        return sub(v, null);
    }

    public LongHolder sub(Number v, MathContext context) {
        if(v != null) {
            if(v instanceof NumHolder) { v = ((NumHolder)v).value; }
            this.value = NumUtil.sub(this.value, v, context).longValue();
        }
        return this;
    }

    public LongHolder multi(long v) {
        this.value = value * v;
        return this;
    }

    public LongHolder multi(Number v) {
        return multi(v, null);
    }

    public LongHolder multi(Number v, MathContext context) {
        if(v instanceof NumHolder) { v = ((NumHolder)v).value; }
        this.value = NumUtil.multi(this.value, v, context).longValue();
        return this;
    }

    public LongHolder divide(int v) {
        this.value = value / v;
        return this;
    }

    public LongHolder divide(Number v) {
        return divide(v, null);
    }

    public LongHolder divide(Number value, MathContext context) {
        if(value instanceof NumHolder) { value = ((NumHolder)value).value; }
        Number v = NumUtil.divide(this.value, value, context);
        this.value = v == null ? 0 : v.longValue();
        return this;
    }

    @Override
    public int intValue() {
        return (int)value;
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
