package org.zj2.lite.common.entity;

public enum Ternary {
    TRUE, DEFAULT, FALSE;

    public static boolean isDefault(Ternary value) {
        return value == null || value == DEFAULT;
    }

    public static boolean of(boolean defaultValue, Ternary value0) {
        return isDefault(value0) ? defaultValue : (value0 == TRUE);
    }

    public static boolean of(boolean defaultValue, Ternary value0, Ternary value1) {
        if (!isDefault(value0)) { return value0 == TRUE; }
        if (!isDefault(value1)) { return value1 == TRUE; }
        return defaultValue;
    }

    public static boolean of(boolean defaultValue, Ternary value0, Ternary value1, Ternary value2) {
        if (!isDefault(value0)) { return value0 == TRUE; }
        if (!isDefault(value1)) { return value1 == TRUE; }
        if (!isDefault(value2)) { return value2 == TRUE; }
        return defaultValue;
    }


    public static boolean of(boolean defaultValue, Ternary... values) {
        if (values == null || values.length == 0) { return defaultValue; }
        for (Ternary v : values) { if (!isDefault(v)) { return v == TRUE; } }
        return defaultValue;
    }

}
