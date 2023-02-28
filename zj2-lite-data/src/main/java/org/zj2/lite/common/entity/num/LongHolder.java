package org.zj2.lite.common.entity.num;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Override
    public int intValue() {
        return (int) value;
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
