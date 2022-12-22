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
    private BigDecimal numValue = BigDecimal.ZERO;

    public static NumHolder of(String value) {
        return new NumHolder(NumUtil.parse(value));
    }

    public static NumHolder of(Number value) {
        return new NumHolder(NumUtil.of(value));
    }

    public NumHolder(BigDecimal numValue) {
        this.numValue = numValue == null ? BigDecimal.ZERO : numValue;
    }

    public NumHolder add(Number value) {
        return add(value, null);
    }

    public NumHolder add(Number value, MathContext context) {
        if (value != null) {
            if (value instanceof NumHolder) {value = ((NumHolder) value).numValue;}
            numValue = NumUtil.add(numValue, value, context);
        }
        return this;
    }

    public NumHolder sub(Number value) {
        return sub(value, null);
    }

    public NumHolder sub(Number value, MathContext context) {
        if (value != null) {
            if (value instanceof NumHolder) {value = ((NumHolder) value).numValue;}
            numValue = NumUtil.sub(numValue, value, context);
        }
        return this;
    }

    public NumHolder multi(Number value) {
        return multi(value, null);
    }

    public NumHolder multi(Number value, MathContext context) {
        if (value instanceof NumHolder) {value = ((NumHolder) value).numValue;}
        numValue = NumUtil.multi(numValue, value, context);
        return this;
    }

    public NumHolder divide(Number value) {
        return divide(value, null);
    }

    public NumHolder divide(Number value, MathContext context) {
        if (value instanceof NumHolder) {value = ((NumHolder) value).numValue;}
        numValue = NumUtil.divide(numValue, value, context);
        return this;
    }

    public NumHolder operate(UnaryOperator<BigDecimal> operator) {
        return operate(operator, null);
    }

    public NumHolder operate(UnaryOperator<BigDecimal> operator, MathContext context) {
        numValue = operator.apply(numValue);
        if (context != null) {numValue = NumUtil.of(numValue, context);}
        return this;
    }

    public NumHolder operate(Number value, BiFunction<BigDecimal, Number, BigDecimal> operator) {
        return operate(value, operator, null);
    }

    public NumHolder operate(Number value, BiFunction<BigDecimal, Number, BigDecimal> operator, MathContext context) {
        if (value instanceof NumHolder) {value = ((NumHolder) value).numValue;}
        numValue = operator.apply(numValue, value);
        if (context != null) {numValue = NumUtil.of(numValue, context);}
        return this;
    }

    public NumHolder context(MathContext context) {
        this.numValue = NumUtil.of(numValue, context);
        return this;
    }

    public BigDecimal numValue() {
        return numValue;
    }

    public BigDecimal numValue(MathContext context) {
        return NumUtil.of(numValue, context);
    }

    @Override
    public int intValue() {
        return numValue == null ? 0 : numValue.intValue();
    }

    @Override
    public long longValue() {
        return numValue == null ? 0 : numValue.longValue();
    }

    @Override
    public float floatValue() {
        return numValue == null ? 0 : numValue.floatValue();
    }

    @Override
    public double doubleValue() {
        return numValue == null ? 0 : numValue.doubleValue();
    }

    @Override
    public String toString() {
        return numValue == null ? "0" : NumUtil.toStr(numValue);
    }
}
