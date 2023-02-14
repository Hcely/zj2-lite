package org.zj2.lite.service.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.common.util.StrUtil;

import java.lang.reflect.Method;

/**
 * UriResourceUtil
 *
 * @author peijie.ye
 * @date 2023/2/7 11:07
 */
public class UriNameUtil {
    public static String getMethodName(Class<?> clazz, String methodName, Class<?>[] paramsTypes) {
        String className = clazz.getName();
        int classNameLen = className.length();
        int capacity = classNameLen + methodName.length() + 3 + 20 * CollUtil.size(paramsTypes);
        StringBuilder sb = new StringBuilder(capacity);
        for (int i = 0; i < classNameLen; ++i) {
            char ch = className.charAt(i);
            sb.append(ch == '.' ? '/' : ch);
        }
        sb.append(className).append('/').append(methodName);
        if (paramsTypes == null || paramsTypes.length == 0) {
            sb.append("()");
        } else {
            sb.append('(');
            int i = 0;
            for (Class<?> p : paramsTypes) {
                if (++i > 1) { sb.append(','); }
                sb.append(p.getSimpleName());
            }
            sb.append(')');
        }
        return sb.toString();
    }

    public static String getUriPath(Class<?> clazz, Method method) {
        if (method == null) { return ""; }
        String uriPrefix = getUriPrefix(clazz);
        String uriPart = getUriPart(method);
        return getUriPath(uriPrefix, uriPart);
    }

    public static String getUriPath(String uriPrefix, String uriPart) {
        if (StringUtils.isEmpty(uriPrefix) && StringUtils.isEmpty(uriPart)) { return ""; }
        StringBuilder sb = new StringBuilder(StringUtils.length(uriPrefix) + StringUtils.length(uriPart) + 1);
        if (StringUtils.isNotEmpty(uriPrefix)) {
            if (StrUtil.firstChar(uriPrefix) != '/') { sb.append('/'); }
            sb.append(uriPrefix);
        }
        if (StringUtils.isNotEmpty(uriPart)) {
            if (StrUtil.lastChar(sb) != '/') { sb.append('/'); }
            sb.append(uriPart);
        }
        if (StrUtil.lastChar(sb) == '/') { sb.setLength(sb.length() - 1); }
        return sb.toString();
    }

    private static String getUriPrefix(Class<?> clazz) {
        RequestMapping mapping = clazz.getAnnotation(RequestMapping.class);
        if (mapping != null) { return getUriPath(mapping.value(), mapping.path()); }
        return null;
    }

    private static String getUriPart(Method method) {
        GetMapping getMapping = method.getAnnotation(GetMapping.class);
        if (getMapping != null) { return getUriPath(getMapping.value(), getMapping.path()); }
        PostMapping postMapping = method.getAnnotation(PostMapping.class);
        if (postMapping != null) { return getUriPath(postMapping.value(), postMapping.path()); }
        PutMapping putMapping = method.getAnnotation(PutMapping.class);
        if (putMapping != null) { return getUriPath(putMapping.value(), putMapping.path()); }
        PatchMapping patchMapping = method.getAnnotation(PatchMapping.class);
        if (patchMapping != null) { return getUriPath(patchMapping.value(), patchMapping.path()); }
        DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
        if (deleteMapping != null) { return getUriPath(deleteMapping.value(), deleteMapping.path()); }
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        if (mapping != null) { return getUriPath(mapping.value(), mapping.path()); }
        return null;
    }

    private static String getUriPath(String[] value, String[] path) {
        if (value != null && value.length > 0) { return value[0]; }
        if (path != null && path.length > 0) { return path[0]; }
        return "";
    }

}
