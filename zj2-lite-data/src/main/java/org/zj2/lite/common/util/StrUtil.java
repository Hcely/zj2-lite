package org.zj2.lite.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.TextStringBuilder;
import org.zj2.lite.common.constant.ZJ2Constants;
import org.zj2.lite.common.entity.result.ZError;
import org.zj2.lite.common.text.StrFormatter;
import org.zj2.lite.common.text.StrFormatterManager;

import java.lang.reflect.Field;

/**
 * StrUtil
 * <br>CreateDate 六月 20,2020
 *
 * @author peijie.ye
 * @since 1.0
 */
public class StrUtil {
    private static final Field STACK_TRACE_FIELD = ReflectUtil.getField(Throwable.class, "stackTrace");

    public static String buildErrorStackStr(Throwable error) {
        return buildErrorStackStr(error, Integer.MAX_VALUE);
    }

    public static String buildErrorStackStr(Throwable error, int maxStrCapacity) {
        if(error == null) { return ""; }
        if(error instanceof ZError && !((ZError)error).isStack()) { return StringUtils.abbreviate(error.toString(), maxStrCapacity); }
        StringBuilder sb = new StringBuilder();
        for(int errorCount = 0; sb.length() < maxStrCapacity && error != null; ++errorCount) {
            appendError(sb, error, errorCount == 0, maxStrCapacity);
            error = error.getCause();
        }
        return StringUtils.abbreviate(sb.toString(), maxStrCapacity);
    }

    private static void appendError(StringBuilder sb, Throwable error, boolean isFirstError, int maxStrCapacity) {
        if(isFirstError) {
            // 先加入message 再明细扩容
            sb.append(error.toString());
            sb.ensureCapacity(Math.min(maxStrCapacity, 1024 * 4));
        } else {
            sb.append("\nCause by: ").append(error.toString());
        }
        if(sb.length() >= maxStrCapacity) { return; }
        StackTraceElement[] elements = getErrorStackTrace(error);
        int systemStackCount = 0;
        for(int i = 0, len = CollUtil.size(elements); i < len; ++i) {
            StackTraceElement e = elements[i];
            if(i == 0 || StringUtils.startsWithIgnoreCase(e.getClassName(), ZJ2Constants.ZJ2_PACKAGE_PREFIX)) {
                if(systemStackCount > 0) {
                    sb.append("\n - sys.stack[").append(systemStackCount).append(']' );
                    systemStackCount = 0;
                }
                sb.append("\n - ").append(e.getClassName()).append('#' ).append(e.getMethodName()).append(':' ).append(e.getLineNumber());
                if(sb.length() >= maxStrCapacity) { return; }
            } else {
                ++systemStackCount;
            }
        }
        if(systemStackCount > 0) { sb.append("\n - sys.stack[").append(systemStackCount).append(']' ); }
    }

    private static StackTraceElement[] getErrorStackTrace(Throwable error) {
        try {
            if(STACK_TRACE_FIELD != null) {
                return (StackTraceElement[])STACK_TRACE_FIELD.get(error);
            }
        } catch(Throwable e) {//NOSONAR
        }
        return error.getStackTrace();
    }

    public static String toStrIfNullEmpty(Object value) {
        return value == null ? "" : value.toString();
    }

    public static String toStrIfNull(Object value) {
        return value == null ? null : value.toString();
    }

    public static StrFormatter getFormatter(String format) {
        return StrFormatterManager.DEFAULT.getFormatter(format);
    }

    /**
     * 字符串格式化
     *
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
     *
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
        if(len == 0) { return ""; }
        TextStringBuilder sb = new TextStringBuilder(len);
        if(value1 != null) { sb.append(value1); }
        if(value2 != null) { sb.append(value2); }
        return sb.toString();
    }

    public static String concat(String value1, char value2, String value3) {
        int len = length(value1) + length(value3) + 1;
        TextStringBuilder sb = new TextStringBuilder(len);
        if(value1 != null) { sb.append(value1); }
        sb.append(value2);
        if(value3 != null) { sb.append(value3); }
        return sb.toString();
    }

    public static String concat(String value1, String value2, String value3) {
        int len = length(value1) + length(value2) + length(value3);
        if(len == 0) { return ""; }
        TextStringBuilder sb = new TextStringBuilder(len);
        if(value1 != null) { sb.append(value1); }
        if(value2 != null) { sb.append(value2); }
        if(value3 != null) { sb.append(value3); }
        return sb.toString();
    }

    public static String concat(String value1, String value2, String value3, String value4) {
        return concat(value1, value2, value3, value4, null, null);
    }

    public static String concat(String value1, String value2, String value3, String value4, String value5) {
        return concat(value1, value2, value3, value4, value5, null);
    }

    public static String concat(String value1, String value2, String value3, String value4, String value5, String value6) {
        int len = length(value1) + length(value2) + length(value3) + length(value4) + length(value5) + length(value6);
        if(len == 0) { return ""; }
        TextStringBuilder sb = new TextStringBuilder(len);
        if(value1 != null) { sb.append(value1); }
        if(value2 != null) { sb.append(value2); }
        if(value3 != null) { sb.append(value3); }
        if(value4 != null) { sb.append(value4); }
        if(value5 != null) { sb.append(value5); }
        if(value5 != null) { sb.append(value6); }
        return sb.toString();
    }

    public static String concat(String... values) {
        if(values == null || values.length == 0) { return ""; }
        int len = 0;
        for(String value : values) { len += (value == null ? 0 : value.length()); }
        if(len == 0) { return ""; }
        TextStringBuilder sb = new TextStringBuilder(len);
        for(String value : values) { if(value != null) { sb.append(value); } }
        return sb.toString();
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence str) {
        return str != null && str.length() > 0;
    }

    public static boolean isAnyNotEmpty(CharSequence str0) {
        return isNotEmpty(str0);
    }

    public static boolean isAnyNotEmpty(CharSequence str0, CharSequence str1) {
        return isNotEmpty(str0) || isNotEmpty(str1);
    }

    public static boolean isAnyNotEmpty(CharSequence str0, CharSequence str1, CharSequence str2) {
        return isNotEmpty(str0) || isNotEmpty(str1) || isNotEmpty(str2);
    }

    public static boolean isAnyNotEmpty(CharSequence str0, CharSequence str1, CharSequence str2, CharSequence str3) {
        return isNotEmpty(str0) || isNotEmpty(str1) || isNotEmpty(str2) || isNotEmpty(str3);
    }

    public static boolean isAnyNotEmpty(CharSequence str0, CharSequence str1, CharSequence str2, CharSequence str3, CharSequence str4) {
        return isNotEmpty(str0) || isNotEmpty(str1) || isNotEmpty(str2) || isNotEmpty(str3) || isNotEmpty(str4);
    }

    public static boolean isAnyNotEmpty(CharSequence... strs) {
        if(strs == null || strs.length == 0) { return false; }
        for(CharSequence s : strs) {
            if(isNotEmpty(s)) { return true; }
        }
        return false;
    }

    public static boolean isNoneEmpty(CharSequence str0) {
        return isNotEmpty(str0);
    }

    public static boolean isNoneEmpty(CharSequence str0, CharSequence str1) {
        return isNotEmpty(str0) && isNotEmpty(str1);
    }

    public static boolean isNoneEmpty(CharSequence str0, CharSequence str1, CharSequence str2) {
        return isNotEmpty(str0) && isNotEmpty(str1) && isNotEmpty(str2);
    }

    public static boolean isNoneEmpty(CharSequence str0, CharSequence str1, CharSequence str2, CharSequence str3) {
        return isNotEmpty(str0) && isNotEmpty(str1) && isNotEmpty(str2) && isNotEmpty(str3);
    }

    public static boolean isNoneEmpty(CharSequence str0, CharSequence str1, CharSequence str2, CharSequence str3, CharSequence str4) {
        return isNotEmpty(str0) && isNotEmpty(str1) && isNotEmpty(str2) && isNotEmpty(str3) && isNotEmpty(str4);
    }

    public static boolean isNoneEmpty(CharSequence... strs) {
        if(strs == null || strs.length == 0) { return true; }
        for(CharSequence s : strs) {
            if(isEmpty(s)) { return false; }
        }
        return true;
    }


    public static String substring(CharSequence str, int startIdx) {
        return isEmpty(str) ? "" : substring(str, startIdx, str.length());
    }

    public static String substring(CharSequence str, int startIdx, int endIdx) {
        if(isEmpty(str)) { return ""; }
        int len = str.length();
        if(startIdx >= len) { return ""; }
        startIdx = Math.max(startIdx, 0);
        endIdx = Math.min(endIdx, len);
        if(startIdx >= endIdx) { return ""; }
        return str.subSequence(startIdx, endIdx).toString();
    }

    public static boolean equals(CharSequence value1, CharSequence value2) {
        return equals(value1, value2, 0, length(value2));
    }

    public static boolean equalsIgnoreCase(CharSequence value1, CharSequence value2) {
        return equalsIgnoreCase(value1, value2, 0, length(value2));
    }

    public static boolean equals(CharSequence value1, CharSequence value2, int start2, int end2) {
        final int len = end2 - start2;
        if(len != length(value1)) { return false; }
        if(len == 0) { return true; }
        for(int i = 0; i < len; ++i, ++start2) {
            if(value1.charAt(i) != value2.charAt(start2)) { return false; }
        }
        return true;
    }

    public static boolean equalsIgnoreCase(CharSequence value1, CharSequence value2, int start2, int end2) {
        final int len = end2 - start2;
        if(len != StringUtils.length(value1)) { return false; }
        if(len == 0) { return true; }
        for(int i = 0; i < len; ++i, ++start2) {
            int ch1 = value1.charAt(i);
            int ch2 = value2.charAt(start2);
            if(ch1 == ch2) { continue; }
            if(ch1 < 128 && ch2 < 128) {
                if(Character.toLowerCase(ch1) != Character.toLowerCase(ch2)) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public static int leftCompare(String str1, String str2) {
        final int len1 = StringUtils.length(str1);
        final int len2 = StringUtils.length(str2);
        if(len1 == 0) { return len2 == 0 ? 0 : -1; }
        if(len2 == 0) { return 1; }
        final int len = Math.max(len1, len2);
        for(int idx1 = len1 - len, idx2 = len2 - len; idx1 < len1; ++idx1, ++idx2) {
            char ch1 = idx1 < 0 ? 0 : str1.charAt(idx1);
            char ch2 = idx2 < 0 ? 0 : str2.charAt(idx2);
            if(ch1 != ch2) { return Integer.compare(ch1, ch2); }
        }
        return 0;
    }

    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    public static char firstChar(CharSequence str) {
        return str == null || str.length() == 0 ? 0 : str.charAt(0);
    }

    public static char lastChar(CharSequence str) {
        return str == null || str.length() == 0 ? 0 : str.charAt(str.length() - 1);
    }

    public static int indexOf(CharSequence str, char ch) {
        return str == null ? -1 : indexOf(str, ch, 0, str.length());
    }

    public static int indexOf(CharSequence str, char ch, int startIdx) {
        return str == null ? -1 : indexOf(str, ch, startIdx, str.length());
    }

    public static int indexOf(CharSequence str, char ch, int startIdx, int endIdx) {
        if(str == null || startIdx < 0) { return -1; }
        endIdx = Math.min(endIdx, str.length());
        if(startIdx >= endIdx) { return -1; }
        for(; startIdx < endIdx; ++startIdx) {
            if(str.charAt(startIdx) == ch) { return startIdx; }
        }
        return -1;
    }
}
