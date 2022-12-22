package org.zj2.lite.common.util;

import org.apache.commons.lang3.StringUtils;

/**
 *  BooleanUtil
 *
 * @author peijie.ye
 * @date 2022/12/3 8:45
 */
public class BooleanUtil {

    public static boolean isTrue(Integer value) {
        return value != null && value != 0;
    }

    public static boolean isFalse(Integer value) {
        return !isTrue(value);
    }

    public static boolean isTrue(Boolean value) {
        return value != null && value;
    }

    public static boolean isFalse(Boolean value) {
        return !isTrue(value);
    }

    public static boolean isTrue(Object value) {
        if (value == null) {return false;}
        if (value == Boolean.TRUE) {return true;}
        if (value instanceof Integer) {return ((Integer) value) != 0;}
        String val = value.toString();
        int len = StringUtils.length(val);
        if (len == 0) {return false;}
        if (len == 4) {
            return val.equalsIgnoreCase("TRUE");
        } else if (len == 1) {
            if (val.equalsIgnoreCase("1")) {return true;}
            if (val.equalsIgnoreCase("Y")) {return true;}
            return val.equalsIgnoreCase("T");
        } else if (len == 3) {
            return val.equalsIgnoreCase("YES");
        } else if (len == 2) {
            return val.equalsIgnoreCase("ON");
        } else {
            return false;
        }
    }

    public static boolean isFalse(Object value) {
        return !isTrue(value);
    }

    public static Boolean parse(Object value) {
        return parse(value, null);
    }

    public static Boolean parse(Object value, Boolean defaultValue) {//NOSONAR
        if (value == null) {return defaultValue;}
        if (value.getClass() == Boolean.class) {return (Boolean) value;}
        if (value instanceof Integer) {return ((Integer) value) != 0;}
        String val = value.toString();
        int len = StringUtils.length(val);
        if (len == 0) {return defaultValue;}
        if (len == 5) {
            if (val.equalsIgnoreCase("FALSE")) {return Boolean.FALSE;}
        } else if (len == 4) {
            if (val.equalsIgnoreCase("TRUE")) {return Boolean.TRUE;}
        } else if (len == 1) {
            char ch = Character.toUpperCase(val.charAt(0));
            if (ch == '1') {return Boolean.TRUE;}
            if (ch == '0') {return Boolean.FALSE;}
            if (ch == 'Y') {return Boolean.TRUE;}
            if (ch == 'N') {return Boolean.FALSE;}
            if (ch == 'T') {return Boolean.TRUE;}
            if (ch == 'F') {return Boolean.FALSE;}
        } else if (len == 3) {
            if (val.equalsIgnoreCase("YES")) {return Boolean.TRUE;}
            if (val.equalsIgnoreCase("OFF")) {return Boolean.FALSE;}
        } else if (len == 2) {
            if (val.equalsIgnoreCase("ON")) {return Boolean.TRUE;}
            if (val.equalsIgnoreCase("NO")) {return Boolean.FALSE;}
        }
        return defaultValue;
    }
}
