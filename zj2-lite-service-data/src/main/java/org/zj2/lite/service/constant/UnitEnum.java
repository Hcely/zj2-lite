package org.zj2.lite.service.constant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.zj2.lite.common.CodeEnum;
import org.zj2.lite.common.util.NumUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * <br>CreateDate 四月 09,2022
 * @author peijie.ye
 */
public enum UnitEnum implements CodeEnum<String> {
    //
    G("G", Type.WEIGHT, "0.001", "克", "克数", "GRAM", "GRAMS"),

    KG("KG", Type.WEIGHT, BigDecimal.ONE, "㎏", "14", "15", "公斤", "公斤数", "千克", "千克数", "KGS", "KILOGRAM",
            "KILOGRAMS"),

    LB("磅", Type.WEIGHT, "0.4536", "磅数", "磅重", "英镑", "LBS", "POUND", "POUNDS"),

    T("吨", Type.WEIGHT, "1000", "吨数", "TON", "TONS"),

    //
    MM("毫米", Type.LENGTH, "0.001", "㎜", "MILLIMETER", "MILLIMETERS"),

    CM("厘米", Type.LENGTH, "0.01", "㎝", "CM", "CENTIMETER", "CENTIMETERS"),

    M("米", Type.LENGTH, BigDecimal.ONE, "米数", "METRE", "METRES"),

    IN("英寸", Type.LENGTH, "0.0254", "寸数", "INCH", "IN", "\"", "“", "”"),

    FT("英尺", Type.LENGTH, "0.3048", "英尺数", "FOOT", "FOOTS"),

    YD("码", Type.LENGTH, "0.9144", "码数", "YDS", "YARD", "YARDS"),

    //
    SQUARE_MM("㎟", Type.SQUARE, "0.000001", "平方毫米", "㎜^2", "㎜2", "㎜²", "MM^2", "MM2", "MM²", "MILLIMETER^2",
            "MILLIMETER2", "MILLIMETER²"),

    SQUARE_CM("㎠", Type.SQUARE, "0.0001", "平方厘米", "㎝^2", "㎝2", "㎝²", "CM^2", "CM2", "CM²", "CENTIMETER^2",
            "CENTIMETER2", "CENTIMETER²"),

    SQUARE_M("㎡", Type.SQUARE, BigDecimal.ONE, "平方米", "M^2", "M2", "M²", "METRE^2", "METRE2", "METRE²"),

    SQUARE_IN("IN²", Type.SQUARE, "0.00064516", "平方英寸", "IN^2", "IN2", "INCH^2", "INCH2", "INCH²", "\"^2", "\"2",
            "\"²", "“^2", "“2", "“²", "”^2", "”2", "”²"),

    SQUARE_FT("FT²", Type.SQUARE, "0.09290304", "平方英尺", "FT^2", "FT2", "FT²", "FOOT^2", "FOOT2", "FOOT²"),

    SQUARE_YD("YD²", Type.SQUARE, "0.83612736", "平方码", "YD^2", "YD2", "YARD^2", "YARD2", "YARD²"),

    //
    QTY_PIECE("件", Type.QTY, BigDecimal.ONE, "件数", "PCS", "PIECE", "PIECES"),

    QTY_BOLT("匹", Type.QTY, BigDecimal.ONE, "匹数", "BOLT", "BOLTS"),

    QTY_PACKET("包", Type.QTY, BigDecimal.ONE, "包数", "PACKET", "PACKETS"),

    QTY_BOX("箱", Type.QTY, BigDecimal.ONE, "箱数", "BOX", "BOXES"),

    QTY_BEAM("条", Type.QTY, BigDecimal.ONE, "条数", "BEAM", "BEAMS"),

    //
    CNY("CNY", Type.CURRENCY, BigDecimal.ONE, "人民币", "RMB"),

    HKD("HKD", Type.CURRENCY, BigDecimal.ONE, "港币"),

    USD("USD", Type.CURRENCY, BigDecimal.ONE, "美元"),
    ;
    private static volatile Map<String, UnitEnum> UNIT_MAP;//NOSONAR
    @Getter
    private final String code;
    @Getter
    private final String desc;
    @Getter
    private final String nameLowerCase;
    @Getter
    private final Type type;
    @Getter
    private final BigDecimal convertValue;
    //
    private final String[] aliasNames;

    UnitEnum(String desc, Type type, String convertValue, String... aliasNames) {
        this(desc, type, new BigDecimal(convertValue), aliasNames);
    }

    UnitEnum(String desc, Type type, BigDecimal convertValue, String... aliasNames) {
        this.code = name();
        this.desc = desc;
        this.nameLowerCase = StringUtils.lowerCase(desc);
        this.type = type;
        this.convertValue = convertValue;
        this.aliasNames = aliasNames;
    }


    public BigDecimal of(Number source, String sourceUnit) {
        return convert(source, get(sourceUnit), this, NumUtil._10);
    }

    public BigDecimal of(Number source, String sourceUnit, MathContext context) {
        return convert(source, get(sourceUnit), this, context);
    }

    public BigDecimal of(Number source, UnitEnum sourceUnit) {
        return convert(source, sourceUnit, this, NumUtil._10);
    }

    public BigDecimal of(Number source, UnitEnum sourceUnit, MathContext context) {
        return convert(source, sourceUnit, this, context);
    }

    public BigDecimal toBasic(Number value) {
        return toBasic(value, NumUtil._10);
    }

    public BigDecimal toBasic(Number value, MathContext context) {
        if (type == Type.QTY || type == Type.CURRENCY) {return NumUtil.of(value);}
        return NumUtil.multi(value, convertValue, context);
    }


    public static boolean isWeightUnit(String code) {
        return checkUnit(code, Type.WEIGHT);
    }

    public static boolean isLengthUnit(String code) {
        return checkUnit(code, Type.LENGTH);
    }

    public static boolean isSquareUnit(String code) {
        return checkUnit(code, Type.SQUARE);
    }

    public static boolean isQtyUnit(String code) {
        return checkUnit(code, Type.QTY);
    }

    public static boolean isCurrencyUnit(String code) {
        return checkUnit(code, Type.CURRENCY);
    }

    private static boolean checkUnit(String code, Type type) {
        UnitEnum unit = get(code);
        return unit != null && unit.type == type;
    }

    public static BigDecimal convertBasic(Number sourceValue, String sourceUnit) {
        return convertBasic(sourceValue, sourceUnit, null);
    }

    public static BigDecimal convertBasic(Number sourceValue, String sourceUnit, MathContext context) {
        UnitEnum unit = get(sourceUnit);
        return unit == null ? NumUtil.of(sourceValue) : unit.toBasic(sourceValue, context);
    }

    public static BigDecimal convert(Number sourceValue, String sourceUnit, String targetUnit) {
        return convert(sourceValue, sourceUnit, targetUnit, null);
    }

    public static BigDecimal convert(Number sourceValue, String sourceUnit, String targetUnit, MathContext context) {
        if (sourceValue == null) {return null;}
        if (NumUtil.eqZero(sourceValue)) {return BigDecimal.ZERO;}
        BigDecimal convert = getConvert(sourceUnit, targetUnit);
        if (convert == null) {return NumUtil.of(sourceValue, context);}
        return NumUtil.multi(sourceValue, convert, context);
    }

    public static BigDecimal convert(Number sourceValue, UnitEnum sourceUnit, UnitEnum targetUnit) {
        return convert(sourceValue, sourceUnit, targetUnit, NumUtil._10);
    }

    public static BigDecimal convert(Number sourceValue, UnitEnum sourceUnit, UnitEnum targetUnit,
            MathContext context) {
        if (sourceUnit == null || targetUnit == null || (sourceUnit.type != targetUnit.type)) {
            return NumUtil.of(sourceValue, context);
        }
        return NumUtil.multi(sourceValue, getConvert(sourceUnit, targetUnit), context);
    }

    public static BigDecimal getConvert(String source, String target) {
        if (StringUtils.equalsAnyIgnoreCase(source, target)) {return BigDecimal.ONE;}
        UnitEnum sourceUnit = get(source);
        if (sourceUnit == null) {return null;}
        UnitEnum targetUnit = get(target);
        if (targetUnit == null) {return null;}
        return getConvert(sourceUnit, targetUnit);
    }

    public static BigDecimal getConvert(UnitEnum sourceUnit, UnitEnum targetUnit) {
        if (sourceUnit == targetUnit) {return BigDecimal.ONE;}
        if (sourceUnit.type != targetUnit.type) {return null;}
        return NumUtil.divide(sourceUnit.convertValue, targetUnit.convertValue, NumUtil._10);
    }

    public static String normalizeCode(String value) {
        UnitEnum unit = get(value);
        return unit == null ? value : unit.getCode();
    }

    public static String normalizeName(String value) {
        UnitEnum unit = get(value);
        return unit == null ? value : unit.getDesc();
    }

    public static UnitEnum get(String code) {
        return get(code, null);
    }

    public static UnitEnum get(String code, UnitEnum defaultUnit) {
        if (StringUtils.isEmpty(code)) {return defaultUnit;}
        code = StringUtils.trimToNull(code);
        if (code == null) {return defaultUnit;}
        Map<String, UnitEnum> unitMap = unitMap();
        UnitEnum result = unitMap.get(code);
        if (result == null) {result = unitMap.getOrDefault(code.toUpperCase(), defaultUnit);}
        return result;
    }

    public static String getDesc(String code) {
        UnitEnum unit = get(code);
        return unit == null ? code : unit.desc;
    }

    private static Map<String, UnitEnum> unitMap() {
        Map<String, UnitEnum> map = UNIT_MAP;
        if (map == null) {
            synchronized (UnitEnum.class) {
                if ((map = UNIT_MAP) == null) {UNIT_MAP = map = createUnitMap();}
            }
        }
        return map;
    }

    private static Map<String, UnitEnum> createUnitMap() {
        Map<String, UnitEnum> map = new HashMap<>(128);
        for (UnitEnum e : values()) {
            map.put(e.code, e);
            map.put(e.desc, e);
            map.put(e.name(), e);
            if (e.aliasNames != null) {for (String name : e.aliasNames) {map.put(name, e);}}
        }
        return map;
    }

    @Override
    public boolean eq(Object value) {
        if (value == null) {return false;}
        if (value == this) {return true;}
        String valueStr = value instanceof CodeEnum ?
                String.valueOf(((CodeEnum<?>) value).getCode()) :
                value.toString();
        if (StringUtils.equalsIgnoreCase(valueStr, this.code) || StringUtils.equalsIgnoreCase(valueStr, this.desc)) {
            return true;
        }
        return StringUtils.equalsAnyIgnoreCase(valueStr, aliasNames);
    }


    public enum Type {
        WEIGHT, LENGTH, SQUARE, QTY, CURRENCY
    }

    /**
     * 组合单位，如: g/m
     * <br>CreateDate 六月 18,2022
     * @author peijie.ye
     */
    @Getter
    @Setter
    @NoArgsConstructor
    public static class UnionUnit implements Serializable {
        private static final long serialVersionUID = 20220618174305L;
        //分子单位
        private UnitEnum numeratorUnit;
        //分母单位
        private UnitEnum denominatorUnit;

        public UnionUnit(UnitEnum numeratorUnit, UnitEnum denominatorUnit) {
            this.numeratorUnit = numeratorUnit;
            this.denominatorUnit = denominatorUnit;
        }

        public static UnionUnit valueOf(String value) {
            if (StringUtils.isEmpty(value)) {return null;}
            int idx = value.indexOf('/');
            UnitEnum numeratorUnit = idx == -1 ? UnitEnum.get(value) : UnitEnum.get(value.substring(0, idx));
            UnitEnum denominatorUnit = idx == -1 ? null : UnitEnum.get(value.substring(idx + 1));
            if (numeratorUnit == null && denominatorUnit == null) {return null;}
            return new UnionUnit(numeratorUnit, denominatorUnit);
        }

        public String getName() {
            return getName(false);
        }

        public String getNameLowerCase() {
            return getName(true);
        }

        private String getName(boolean lowerCase) {
            if (numeratorUnit == null && denominatorUnit == null) {return "";}
            StringBuilder sb = new StringBuilder(32);
            if (numeratorUnit != null) {sb.append(lowerCase ? numeratorUnit.nameLowerCase : numeratorUnit.desc);}
            if (denominatorUnit != null) {
                if (sb.length() > 0) {sb.append('/');}
                sb.append(lowerCase ? denominatorUnit.nameLowerCase : denominatorUnit.desc);
            }
            return sb.toString();
        }
    }

    public static UnitEnum getByName(String name) {
        for (UnitEnum unitEnum : UnitEnum.values()) {
            if (StringUtils.equals(name, unitEnum.getDesc())) {
                return unitEnum;
            }
        }
        return null;
    }

}
