package org.zj2.lite.common.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * NumUtil
 * <br>CreateDate February 06,2020
 *
 * @author peijie.ye
 * @since 1.0
 */
public class NumUtil {
    public static final MathContext _0 = new MathContext(0, RoundingMode.HALF_UP);
    public static final MathContext _1 = new MathContext(1, RoundingMode.HALF_UP);
    public static final MathContext _2 = new MathContext(2, RoundingMode.HALF_UP);
    public static final MathContext _3 = new MathContext(3, RoundingMode.HALF_UP);
    public static final MathContext _4 = new MathContext(4, RoundingMode.HALF_UP);
    public static final MathContext _5 = new MathContext(5, RoundingMode.HALF_UP);
    public static final MathContext _6 = new MathContext(6, RoundingMode.HALF_UP);
    public static final MathContext _8 = new MathContext(8, RoundingMode.HALF_UP);
    public static final MathContext _10 = new MathContext(10, RoundingMode.HALF_UP);
    public static final MathContext _12 = new MathContext(12, RoundingMode.HALF_UP);
    //
    public static final MathContext CEIL = new MathContext(0, RoundingMode.CEILING);
    public static final MathContext FLOOR = new MathContext(0, RoundingMode.FLOOR);
    public static final NumUtilPlus plus = new NumUtilPlus();

    public static BigDecimal add(Number num1, Number num2) {
        return add(num1, num2, null);
    }

    public static BigDecimal add(Number num1, Number num2, MathContext context) {
        if(num1 == null) { return of(num2, context); }
        if(num2 == null) { return of(num1, context); }
        BigDecimal result = of(num1).add(of(num2));
        return context == null ? result : result.setScale(context.getPrecision(), context.getRoundingMode());
    }

    public static BigDecimal add(Number... nums) {
        if(nums == null || nums.length == 0) { return BigDecimal.ZERO; }
        BigDecimal result = BigDecimal.ZERO;
        for(Number num : nums) {
            if(num != null) { result = result.add(of(num)); }
        }
        return result;
    }

    public static BigDecimal diff(Number num1, Number num2) {
        return diff(num1, num2, null);
    }

    public static BigDecimal diff(Number num1, Number num2, MathContext context) {
        return abs(sub(num1, num2, context));
    }

    public static BigDecimal sub(Number num1, Number num2) {
        return sub(num1, num2, null);
    }

    public static BigDecimal sub(Number num1, Number num2, MathContext context) {
        BigDecimal result = of(num1).subtract(of(num2));
        return context == null ? result : result.setScale(context.getPrecision(), context.getRoundingMode());
    }

    public static BigDecimal multi(Number num1, Number num2) {
        return multi(num1, num2, null);
    }

    public static BigDecimal multi(Number... nums) {
        if(nums == null || nums.length == 0) { return BigDecimal.ZERO; }
        BigDecimal result = of(nums[0], false);
        if(result == null) { return BigDecimal.ZERO; }
        for(int i = 1, len = nums.length; i < len; ++i) {
            BigDecimal num = of(nums[i], false);
            if(num == null) { return BigDecimal.ZERO; }
            result = result.multiply(num);
        }
        return result;
    }

    public static BigDecimal multi(Number num1, Number num2, MathContext context) {
        BigDecimal result = of(num1).multiply(of(num2));
        return context == null ? result : result.setScale(context.getPrecision(), context.getRoundingMode());
    }

    public static BigDecimal divide(Number num1, Number num2) {
        return divide(num1, num2, null);
    }

    public static BigDecimal divide(Number num1, Number num2, MathContext context) {
        if(eqZero(num2)) { return null; }
        context = context == null ? _8 : context;
        return of(num1).divide(of(num2), context.getPrecision(), context.getRoundingMode());
    }

    public static int compare(Number num1, Number num2) {
        //noinspection NumberEquality
        if(num1 == num2) { return 0; }
        if(num1 instanceof Integer && num2 instanceof Integer) {
            return Integer.compare(num1.intValue(), num2.intValue());
        }
        return of(num1).compareTo(of(num2));
    }

    public static boolean eq(Number num1, Number num2) {
        return compare(num1, num2) == 0;
    }

    public static boolean anyEqZero(Number... nums) {
        if(nums != null && nums.length > 0) {
            for(Number n : nums) { if(eqZero(n)) { return true; } }
        }
        return false;
    }

    public static boolean allEqZero(Number... nums) {
        if(nums != null && nums.length > 0) {
            for(Number n : nums) { if(!eqZero(n)) { return false; } }
        }
        return true;
    }

    public static boolean eqZero(Number num) {
        if(num == null) {
            return true;
        } else if(num instanceof Integer) {
            return num.intValue() == 0;
        } else if(num instanceof Long) {
            return num.longValue() == 0;
        } else {
            return of(num).compareTo(BigDecimal.ZERO) == 0;
        }
    }

    public static boolean gt(Number num1, Number num2) {
        return compare(num1, num2) > 0;
    }

    public static boolean gte(Number num1, Number num2) {
        return compare(num1, num2) >= 0;
    }

    public static boolean lt(Number num1, Number num2) {
        return compare(num1, num2) < 0;
    }

    public static boolean lte(Number num1, Number num2) {
        return compare(num1, num2) <= 0;
    }

    public static boolean gtZero(Number num1) {
        return of(num1).compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean gteZero(Number num1) {
        return of(num1).compareTo(BigDecimal.ZERO) >= 0;
    }

    public static boolean ltZero(Number num1) {
        return of(num1).compareTo(BigDecimal.ZERO) < 0;
    }

    public static boolean lteZero(Number num1) {
        return of(num1).compareTo(BigDecimal.ZERO) <= 0;
    }

    public static BigDecimal between(Number num, Number min, Number max) {
        if(num == null) { return of(min); }
        BigDecimal bNum = of(num);
        BigDecimal bMin = of(min, false);
        if(bMin != null && bNum.compareTo(bMin) < 0) { return bMin; }
        BigDecimal bMax = of(max, false);
        if(bMax != null && bNum.compareTo(bMax) > 0) { return bMax; }
        return bNum;
    }

    public static BigDecimal min(Number num1, Number num2) {
        BigDecimal bNum1 = of(num1);
        BigDecimal bNum2 = of(num2);
        return bNum1.compareTo(bNum2) < 0 ? bNum1 : bNum2;
    }

    public static BigDecimal max(Number num1, Number num2) {
        BigDecimal bNum1 = of(num1);
        BigDecimal bNum2 = of(num2);
        return bNum1.compareTo(bNum2) > 0 ? bNum1 : bNum2;
    }

    public static BigDecimal max(Number num1, Number... nums) {
        BigDecimal result = of(num1, false);
        if(nums != null && nums.length > 0) {
            for(Number e : nums) {
                if(e != null) { result = result == null ? of(e, false) : max(result, e); }
            }
        }
        return result;
    }

    public static BigDecimal min(Number num1, Number... nums) {
        BigDecimal result = of(num1, false);
        if(nums != null && nums.length > 0) {
            for(Number e : nums) {
                if(e != null) { result = result == null ? of(e, false) : min(result, e); }
            }
        }
        return result;
    }

    public static BigDecimal abs(Number num) {
        return of(num).abs();
    }

    public static BigDecimal absNegate(Number num) {
        return of(num).abs().negate();
    }

    public static BigDecimal negate(Number num) {
        return of(num).negate();
    }

    public static BigDecimal of(String num) {
        return of(num, true, null);
    }

    public static BigDecimal of(String num, boolean nullAsZero) {
        return of(num, nullAsZero, null);
    }

    public static BigDecimal of(String num, boolean nullAsZero, MathContext context) {
        BigDecimal result = parse(num);
        if(result == null) { return nullAsZero ? BigDecimal.ZERO : null; }
        return context == null ? result : result.setScale(context.getPrecision(), context.getRoundingMode());
    }

    public static BigDecimal of(Number num) {
        return of(num, true);
    }

    public static BigDecimal of(Number num, MathContext context) {
        return of(num, true, context);
    }

    public static BigDecimal of(Number num, boolean nullAsZero) {
        return of(num, nullAsZero, null);
    }

    public static BigDecimal of(Number num, boolean nullAsZero, MathContext context) {
        if(num == null) {
            return nullAsZero ? BigDecimal.ZERO : null;
        } else {
            BigDecimal result = num.getClass() == BigDecimal.class ? (BigDecimal)num : new BigDecimal(num.toString());
            return context == null ? result : result.setScale(context.getPrecision(), context.getRoundingMode());
        }
    }

    public static String toAmountStr(Number num, String unit) {
        return toAmountStr(num, unit, null);
    }

    public static String toAmountStr(Number num) {
        return toAmountStr(num, "", null);
    }

    public static String toAmountStr(Number num, MathContext context) {
        return toAmountStr(num, "", context);
    }

    public static String toAmountStr(Number num, String unit, MathContext context) {
        if(num == null) { return ""; }
        String numStr = toStr(num, context);
        int len = numStr.length();
        int idx = numStr.indexOf('.' );
        if(idx == -1) { idx = len; }
        int i = 0;
        StringBuilder sb = new StringBuilder(len + StringUtils.length(unit) + 8);
        for(int l = idx % 3; i < idx; l = 3) {
            if(l == 3 && sb.length() > 0) { sb.append(',' ); }
            for(int n = 0; n < l && i < idx; ++i, ++n) { sb.append(numStr.charAt(i)); }
        }
        for(; i < len; ++i) { sb.append(numStr.charAt(i)); }
        if(StringUtils.isNotEmpty(unit)) { sb.append(unit); }
        return sb.toString();
    }

    public static String toStr(Number value) {
        if(value == null) {
            return "null";
        } else if(value instanceof BigDecimal) {
            return ((BigDecimal)value).stripTrailingZeros().toPlainString();
        } else {
            return value.toString();
        }
    }

    public static String toStr(BigDecimal value) {
        return value == null ? "null" : value.stripTrailingZeros().toPlainString();
    }

    public static String toStr(Number value, MathContext context) {
        return value == null ? "null" : of(value, context).stripTrailingZeros().toPlainString();
    }

    public static String toStr(Number value, String unit) {
        return toStr(value, unit, null);
    }

    public static String toStr(Number num, String unit, MathContext context) {
        if(num == null) { return ""; }
        String numStr = toStr(num, context);
        StringBuilder sb = new StringBuilder(numStr.length() + StringUtils.length(unit));
        sb.append(numStr);
        if(StringUtils.isNotEmpty(unit)) { sb.append(unit); }
        return sb.toString();
    }

    public static BigDecimal parse(String value) {
        return parse(BigDecimal.class, value, null);
    }

    public static BigDecimal parse(String value, Number fallback) {
        BigDecimal result = parse(BigDecimal.class, value, null);
        return result == null ? of(fallback, false) : result;
    }

    public static <T extends Number> T parse(Class<T> type, String value) {
        return parse(type, value, null);
    }

    public static Integer parseInt(String str) {
        return parse(Integer.class, str, null);
    }

    public static Integer parseInt(String str, Integer fallback) {
        return parse(Integer.class, str, fallback);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Number> T parse(Class<T> type, String value, T fallback) {
        if(StringUtils.isEmpty(value)) { return fallback; }
        try {
            if(type == Integer.class) {
                Number result = Integer.parseInt(value);
                return (T)result;
            } else if(type == Long.class) {
                Number result = Long.parseLong(value);
                return (T)result;
            } else if(type == BigDecimal.class) {
                return (T)new BigDecimal(value);
            } else if(type == Double.class) {
                Number result = Double.parseDouble(value);
                return (T)result;
            } else if(type == Byte.class) {
                Number result = Byte.parseByte(value);
                return (T)result;
            } else if(type == Short.class) {
                Number result = Short.parseShort(value);
                return (T)result;
            } else if(type == Float.class) {
                Number result = Float.parseFloat(value);
                return (T)result;
            }
        } catch(Throwable ignored) { } //NOSONAR
        return fallback;
    }

    public static boolean isInt(Number value) {
        return value == null || value instanceof Integer || value instanceof Long || value instanceof Short || value instanceof Byte
                || value.longValue() == value.doubleValue();
    }

    public static BigDecimal defaultIfZero(Number number, Number defaultValue) {
        return eqZero(number) ? of(defaultValue, false) : of(number);
    }

    public static class NumUtilPlus {
        private NumUtilPlus() {
        }

        public BigDecimal subNoNegate(Number num1, Number num2) {
            return subNoNegate(num1, num2, null);
        }

        public BigDecimal subNoNegate(Number num1, Number num2, MathContext context) {
            BigDecimal value = sub(num1, num2, context);
            return NumUtil.max(value, BigDecimal.ZERO);
        }

        public BigDecimal percent(Number num, Number total) {
            return percent(num, total, _2);
        }

        public BigDecimal percent(Number num, Number total, MathContext context) {
            BigDecimal dTotal = of(total);
            return lteZero(dTotal) ? null : multi(divide(num, dTotal), 100, context);
        }

        public BigDecimal multiPercent(Number num, Number percent) {
            return multiPercent(num, percent, null);
        }

        public BigDecimal multiPercent(Number num, Number percent, MathContext context) {
            BigDecimal dPercent = of(percent);
            return eqZero(dPercent) ? BigDecimal.ZERO : multi(num, NumUtil.divide(dPercent, 100), context);
        }

        public int max(int num1, int num2) {
            //noinspection ManualMinMaxCalculation
            return num1 > num2 ? num1 : num2;
        }

        public int max(int num1, int... nums) {
            if(nums != null && nums.length > 0) {
                for(int n : nums) {
                    if(n > num1) { num1 = n; }
                }
            }
            return num1;
        }

        public int min(int num1, int num2) {
            //noinspection ManualMinMaxCalculation
            return num1 < num2 ? num1 : num2;
        }

        public int min(int num1, int... nums) {
            if(nums != null && nums.length > 0) {
                for(int n : nums) {
                    if(n < num1) { num1 = n; }
                }
            }
            return num1;
        }

        public int between(int num, int min, int max) {
            //noinspection ManualMinMaxCalculation
            return num < min ? min : (num > max ? max : num);//NOSONAR
        }

        public int floorLog2(int i) {
            return i < 1 ? 0 : (31 - Integer.numberOfLeadingZeros(i));
        }

        public int ceilLog2(int i) {
            return i < 1 ? 0 : (32 - Integer.numberOfLeadingZeros(i - 1));
        }

        public int ceilPower2(int i) {
            return 1 << ceilLog2(i);
        }

        public int ceilPower4(int i) {
            int shift = ceilLog2(i);
            if(shift == 0) { return 1; }
            return 1 << ceilMultipleOf(shift, 2);
        }

        public int ceilPower8(int i) {
            int shift = ceilLog2(i);
            if(shift == 0) { return 1; }
            return 1 << ceilMultipleOf(shift, 3);
        }

        public int ceilPower16(int i) {
            int shift = ceilLog2(i);
            if(shift == 0) { return 1; }
            return 1 << ceilMultipleOf(shift, 4);
        }

        public int ceilMultipleOf(int i, int multiple) {
            if(i < multiple) { return multiple; }
            int value = (i / multiple) * multiple;
            return value < i ? (value + multiple) : value;
        }
    }
}
