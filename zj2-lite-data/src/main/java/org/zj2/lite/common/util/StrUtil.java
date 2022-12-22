package org.zj2.lite.common.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.TextStringBuilder;
import org.zj2.lite.common.text.StrFormatter;
import org.zj2.lite.common.text.StrFormatterManager;

import java.util.List;

/**
 * StrUtil
 * <br>CreateDate 六月 20,2020
 * @author peijie.ye
 * @since 1.0
 */
public class StrUtil {

    public static String toStrIfNullEmpty(Object value) {
        return value == null ? "" : value.toString();
    }

    public static StrFormatter getFormatter(String format) {
        return StrFormatterManager.DEFAULT.getFormatter(format);
    }

    /**
     * 字符串格式化
     * @param format 格式：xxx{}xxx{}xxx{}xxx 或者 xxx{2}xxx{0}xxx{1}xxx 或者 xxx%sxxx%sxxx%sxxx
     * @return
     */
    public static String format(String format, Object arg0) {
        return getFormatter(format).format(arg0);
    }

    public static String format(String format, Object arg0, Object arg1) {
        return getFormatter(format).format(arg0, arg1);
    }

    public static String format(String format, Object arg0, Object arg1, Object arg2) {
        return getFormatter(format).format(arg0, arg1, arg2);
    }

    public static String format(String format, Object arg0, Object arg1, Object arg2, Object arg3) {
        return getFormatter(format).format(arg0, arg1, arg2, arg3);
    }

    public static String format(String format, Object arg0, Object arg1, Object arg2, Object arg3, Object arg4) {
        return getFormatter(format).format(arg0, arg1, arg2, arg3, arg4);
    }

    public static String format(String format, Object... args) {
        return getFormatter(format).format(args);
    }

    /**
     * 字符串格式化
     * @param format xxxx{field1}xxx{field2}xxx{field3}xxx
     * @param obj
     * @return
     */
    public static String formatObj(String format, Object obj) {
        return getFormatter(format).formatObj(obj);
    }

    public static String concat(String value) {
        return value == null ? "" : value;
    }

    public static String concat(String value1, String value2) {
        int len = length(value1) + length(value2);
        if (len == 0) {return "";}
        TextStringBuilder sb = new TextStringBuilder(len);
        if (value1 != null) {sb.append(value1);}
        if (value2 != null) {sb.append(value2);}
        return sb.toString();
    }


    public static String concat(String value1, String value2, String value3) {
        int len = length(value1) + length(value2) + length(value3);
        if (len == 0) {return "";}
        TextStringBuilder sb = new TextStringBuilder(len);
        if (value1 != null) {sb.append(value1);}
        if (value2 != null) {sb.append(value2);}
        if (value3 != null) {sb.append(value3);}
        return sb.toString();
    }

    public static String concat(String value1, String value2, String value3, String value4) {
        int len = length(value1) + length(value2) + length(value3) + length(value4);
        if (len == 0) {return "";}
        TextStringBuilder sb = new TextStringBuilder(len);
        if (value1 != null) {sb.append(value1);}
        if (value2 != null) {sb.append(value2);}
        if (value3 != null) {sb.append(value3);}
        if (value4 != null) {sb.append(value4);}
        return sb.toString();
    }

    public static int length(String value) {
        return value == null ? 0 : value.length();
    }

    public static String concat(String... values) {
        if (values == null || values.length == 0) {return "";}
        int len = 0;
        for (String value : values) {len += (value == null ? 0 : value.length());}
        if (len == 0) {return "";}
        TextStringBuilder sb = new TextStringBuilder(len);
        for (String value : values) {if (value != null) {sb.append(value);}}
        return sb.toString();
    }

    public static boolean equals(String value1, String value2) {
        if (StringUtils.isEmpty(value1) && StringUtils.isEmpty(value2)) {return true;}
        return StringUtils.equals(value1, value2);
    }

    public static boolean equalsIgnoreCase(String value1, String value2) {
        if (StringUtils.isEmpty(value1) && StringUtils.isEmpty(value2)) {return true;}
        return StringUtils.equalsIgnoreCase(value1, value2);
    }

    public static boolean equals(String value1, String value2, int start2, int end2) {
        final int len = end2 - start2;
        if (len != StringUtils.length(value1)) {return false;}
        if (len == 0) {return true;}
        for (int i = 0; i < len; ++i, ++start2) {
            if (value1.charAt(i) != value2.charAt(start2)) {return false;}
        }
        return true;
    }

    public static boolean equalsIgnoreCase(String value1, String value2, int start2, int end2) {
        final int len = end2 - start2;
        if (len != StringUtils.length(value1)) {return false;}
        if (len == 0) {return true;}
        for (int i = 0; i < len; ++i, ++start2) {
            int ch1 = value1.charAt(i);
            int ch2 = value2.charAt(start2);
            if (ch1 == ch2) {continue;}
            if (ch1 < 128 && ch2 < 128) {
                if (Character.toLowerCase(ch1) != Character.toLowerCase(ch2)) {
                    return false;
                }
            } else {return false;}
        }
        return true;
    }

    public static int leftCompare(String str1, String str2) {
        final int len1 = StringUtils.length(str1);
        final int len2 = StringUtils.length(str2);
        if (len1 == 0) {return len2 == 0 ? 0 : -1;}
        if (len2 == 0) {return 1;}
        final int len = Math.max(len1, len2);
        for (int idx1 = len1 - len, idx2 = len2 - len; idx1 < len1; ++idx1, ++idx2) {
            char ch1 = idx1 < 0 ? '0' : str1.charAt(idx1);
            char ch2 = idx2 < 0 ? '0' : str2.charAt(idx2);
            if (ch1 != ch2) {return Integer.compare(ch1, ch2);}
        }
        return 0;
    }

    public static char lastChar(CharSequence str) {
        return str == null || str.length() == 0 ? 0 : str.charAt(str.length() - 1);
    }
}
