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
 *
 * <br>CreateDate 七月 19,2022
 * @author peijie.ye
 */
@Getter
@Setter
@NoArgsConstructor
public class NumHolder extends Number {
    private static final long serialVersionUID = 20220719163940L;
    private BigDecimal value = BigDecimal.ZERO;

    public static NumHolder of(String value) {
        return new NumHolder(NumUtil.parse(value));
    }

    public static NumHolder of(Number value) {
        return new NumHolder(NumUtil.of(value));
    }

    public NumHolder(BigDecimal value) {
        this.value = value == null ? BigDecimal.ZERO : value;
    }

    public NumHolder add(Number value) {
        return add(value, null);
    }

    public NumHolder add(Number value, MathContext context) {
        if (value != null) {
            if (value instanceof NumHolder) { value = ((NumHolder) value).value; }
            this.value = NumUtil.add(this.value, value, context);
        }
        return this;
    }

    public NumHolder sub(Number value) {
        return sub(value, null);
    }

    public NumHolder sub(Number value, MathContext context) {
        if (value != null) {
            if (value instanceof NumHolder) { value = ((NumHolder) value).value; }
            this.value = NumUtil.sub(this.value, value, context);
        }
        return this;
    }

    public NumHolder multi(Number value) {
        return multi(value, null);
    }

    public NumHolder multi(Number value, MathContext context) {
        if (value instanceof NumHolder) { value = ((NumHolder) value).value; }
        this.value = NumUtil.multi(this.value, value, context);
        return this;
    }

    public NumHolder divide(Number value) {
        return divide(value, null);
    }

    public NumHolder divide(Number value, MathContext context) {
        if (value instanceof NumHolder) { value = ((NumHolder) value).value; }
        this.value = NumUtil.divide(this.value, value, context);
        return this;
    }

    public NumHolder operate(UnaryOperator<BigDecimal> operator) {
        return operate(operator, null);
    }

    public NumHolder operate(UnaryOperator<BigDecimal> operator, MathContext context) {
        value = operator.apply(value);
        if (context != null) { value = NumUtil.of(value, context); }
        return this;
    }

    public NumHolder operate(Number value, BiFunction<BigDecimal, Number, BigDecimal> operator) {
        return operate(value, operator, null);
    }

    public NumHolder operate(Number value, BiFunction<BigDecimal, Number, BigDecimal> operator, MathContext context) {
        if (value instanceof NumHolder) { value = ((NumHolder) value).value; }
        this.value = operator.apply(this.value, value);
        if (context != null) { this.value = NumUtil.of(this.value, context); }
        return this;
    }

    public NumHolder context(MathContext context) {
        this.value = NumUtil.of(value, context);
        return this;
    }

    public BigDecimal numValue() {
        return value;
    }

    public BigDecimal numValue(MathContext context) {
        return NumUtil.of(value, context);
    }

    @Override
    public int intValue() {
        return value == null ? 0 : value.intValue();
    }

    @Override
    public long longValue() {
        return value == null ? 0 : value.longValue();
    }

    @Override
    public float floatValue() {
        return value == null ? 0 : value.floatValue();
    }

    @Override
    public double doubleValue() {
        return value == null ? 0 : value.doubleValue();
    }

    @Override
    public String toString() {
        return value == null ? "0" : NumUtil.toStr(value);
    }
}
