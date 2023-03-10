package org.zj2.lite.common.entity.num;

import org.zj2.lite.common.util.NumUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * NumVector
 *
 * @author peijie.ye
 * @date 2023/2/27 11:47
 */
public class NumVector implements Serializable {
    private static final BigDecimal[] EMPTY_DECIMALS = {};
    private static final long serialVersionUID = -1812229347227816271L;
    private BigDecimal[] values;

    protected NumVector() {
        values = EMPTY_DECIMALS;
    }

    public NumVector(int len) {
        this.values = len < 1 ? EMPTY_DECIMALS : new BigDecimal[len];
        for(int i = 0; i < len; ++i) { values[i] = BigDecimal.ZERO; }
    }

    protected BigDecimal[] getValues() {
        return values;
    }

    protected void setValues(BigDecimal[] values) {
        this.values = values == null ? EMPTY_DECIMALS : values;
    }

    public int length() {
        return values.length;
    }

    public BigDecimal getNum(int idx) {
        return idx < values.length ? values[idx] : BigDecimal.ZERO;
    }

    public NumVector setNum(int idx, Number value) {
        return setNum(idx, value, null);
    }

    public NumVector setNum(int idx, Number value, MathContext context) {
        if(idx < values.length) { values[idx] = NumUtil.of(value, context); }
        return this;
    }

    public NumVector setNums(Number... nums) {
        if(nums == null || nums.length == 0) { return this; }
        int len = Math.min(values.length, nums.length);
        for(int i = 0; i < len; ++i) { values[i] = NumUtil.of(nums[i]); }
        return this;
    }

    public void add() {

    }
}
