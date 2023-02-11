package org.zj2.common.uac.auth.util;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.zj2.lite.common.constant.NoneConstants;
import org.zj2.lite.common.entity.ByteKey;
import org.zj2.lite.common.entity.Ternary;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.service.auth.AuthenticationIgnored;
import org.zj2.lite.service.auth.AuthenticationRequired;
import org.zj2.lite.service.auth.AuthorityResource;
import org.zj2.lite.service.auth.UriResource;
import org.zj2.lite.service.context.TokenType;
import org.zj2.lite.service.util.UriNameUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * UriResourceUtil
 *
 * @author peijie.ye
 * @date 2023/2/7 11:07
 */
public class UriResourceManager {
    private static final String ZJ2_PACKAGE = "org.zj2";
    private static final TokenType[] DEFAULT_TOKEN_TYPE = {TokenType.JWT};
    private static final Map<ByteKey, UriResource> resourceMap = new HashMap<>(512);
    //    private static final Map<String, UriResource> resourceKeyNameMap = new HashMap<>(512);

    private static class UriResource0 extends UriResource {
        private static final long serialVersionUID = 4469250917205636137L;

        @Override
        public void setName(String name) {
            super.setName(name);
        }

        @Override
        public void setUriPath(String uriPath) {
            super.setUriPath(uriPath);
        }

        @Override
        public void setRequiredAuthentication(boolean requiredAuthentication) {
            super.setRequiredAuthentication(requiredAuthentication);
        }

        @Override
        public void setRequiredTokenTypes(TokenType[] requiredTokenTypes) {
            super.setRequiredTokenTypes(requiredTokenTypes);
        }

        @Override
        public void setRequiredUriAuthority(boolean requiredUriAuthority) {
            super.setRequiredUriAuthority(requiredUriAuthority);
        }

        @Override
        public void setUriAuthority(String uriAuthority) {
            super.setUriAuthority(uriAuthority);
        }

        @Override
        public void setRequiredPropertyAuthority(boolean requiredPropertyAuthority) {
            super.setRequiredPropertyAuthority(requiredPropertyAuthority);
        }

        @Override
        public void setPropertyAuthorities(Set<String> propertyAuthorities) {
            super.setPropertyAuthorities(propertyAuthorities);
        }

        @Override
        public void setDataAuthority(String dataAuthority) {
            super.setDataAuthority(dataAuthority);
        }

        @Override
        public void setRequiredDataAuthority(boolean requiredDataAuthority) {
            super.setRequiredDataAuthority(requiredDataAuthority);
        }
    }

    public static UriResource get(ProceedingJoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        if (target == null) { return null; }
        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) { return null; }
        Class<?> clazz = target.getClass();
        Method method = ((MethodSignature) signature).getMethod();
        return get(clazz, method);
    }

    public static UriResource get(Class<?> clazz, Method method) {
        return get(clazz, method, method.getName(), method.getParameterTypes());
    }

    public static UriResource get(Class<?> clazz, Method method, Class<?>[] paramTypes) {
        return get(clazz, method, method.getName(), paramTypes);
    }

    public static UriResource get(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
        return get(clazz, null, methodName, paramTypes);
    }

    private static UriResource get(Class<?> clazz, Method method, String methodName, Class<?>[] paramTypes) {
        ByteKey key = getMethodKey(clazz, methodName, paramTypes);
        UriResource resource = resourceMap.get(key);
        if (resource == null) {
            synchronized (resourceMap) {
                if ((resource = resourceMap.get(key)) == null) {
                    resource = buildResource(clazz, method, methodName, paramTypes);
                    resourceMap.put(key, resource);
                }
            }
        }
        return resource;
    }

    private static UriResource buildResource(Class<?> clazz, Method method, String methodName, Class<?>[] paramTypes) {
        UriResource0 resource = new UriResource0();
        resource.setName(UriNameUtil.getMethodName(clazz, methodName, paramTypes));
        resource.setUriPath(UriNameUtil.getUriPath(clazz, method));
        fillResourceAuthentication(resource, clazz, method);
        fillResourceAuthority(resource, clazz, method);
        return resource;
    }


    private static void fillResourceAuthentication(UriResource0 resource, Class<?> clazz, Method method) {
        // 无需认证
        if (method != null) {
            if (method.getAnnotation(AuthenticationIgnored.class) != null) {
                resource.setRequiredAuthentication(false);
                return;
            }
        }
        AuthenticationRequired required = method == null ? null : method.getAnnotation(AuthenticationRequired.class);
        if (required == null) {
            // 没指定认证，父级指定无需认证
            if (clazz.getAnnotation(AuthenticationIgnored.class) != null) {
                resource.setRequiredAuthentication(false);
                return;
            }
            // 读取父级的认证要求
            required = clazz.getAnnotation(AuthenticationRequired.class);
            // 非本身服务，无需处理
            if (required == null && !StringUtils.startsWithIgnoreCase(clazz.getName(), ZJ2_PACKAGE)) {
                resource.setRequiredAuthentication(false);
                return;
            }
        }
        resource.setRequiredAuthentication(true);
        TokenType[] requiredTypes = required == null ? DEFAULT_TOKEN_TYPE : required.requiredType();
        resource.setRequiredTokenTypes(CollUtil.defaultIfEmpty(requiredTypes, DEFAULT_TOKEN_TYPE));
    }

    private static void fillResourceAuthority(UriResource0 resource, Class<?> clazz, Method method) {
        AuthorityResource authorityResource = method == null ? null : method.getAnnotation(AuthorityResource.class);
        AuthorityResource parentResource = clazz.getAnnotation(AuthorityResource.class);
        if (authorityResource == null && parentResource == null) {
            resource.setRequiredUriAuthority(false);
            resource.setRequiredPropertyAuthority(false);
            resource.setRequiredDataAuthority(false);
        } else {
            String authorityUri = authorityResource == null ?
                    resource.getUriPath() :
                    StringUtils.defaultIfEmpty(authorityResource.name(), authorityResource.value());
            if (StringUtils.isEmpty(authorityUri)) { authorityUri = resource.getName(); }
            resource.setUriAuthority(authorityUri);
            resource.setRequiredUriAuthority(
                    getRequired(authorityResource, parentResource, AuthorityResource::requiredUriAuthority));
            resource.setRequiredPropertyAuthority(
                    getRequired(authorityResource, parentResource, AuthorityResource::requiredPropertyAuthority));
            resource.setRequiredDataAuthority(
                    getRequired(authorityResource, parentResource, AuthorityResource::requiredDataAuthority));
            String[] propertyAuthorities = getPropertyAuthorities(authorityResource, parentResource);
            resource.setPropertyAuthorities(
                    CollUtil.isEmpty(propertyAuthorities) ? CollUtil.emptySet() : CollUtil.newSet(propertyAuthorities));
            resource.setDataAuthority(getDataAuthority(authorityResource, parentResource));
            if (StringUtils.isEmpty(resource.getDataAuthority())) { resource.setRequiredDataAuthority(false); }
            //
            if (!resource.isRequiredAuthentication()) {
                resource.setRequiredAuthentication(
                        resource.isRequiredUriAuthority() || resource.isRequiredPropertyAuthority()
                                || resource.isRequiredDataAuthority());
            }
        }
    }

    private static boolean getRequired(AuthorityResource authorityResource, AuthorityResource parentResource,
            Function<AuthorityResource, Ternary> getter) {
        if (authorityResource != null && !Ternary.isDefault(getter.apply(authorityResource))) {
            return Ternary.of(true, getter.apply(authorityResource));
        }
        if (parentResource != null && !Ternary.isDefault(getter.apply(parentResource))) {
            return Ternary.of(true, getter.apply(parentResource));
        }
        return true;
    }

    private static String[] getPropertyAuthorities(AuthorityResource authorityResource,
            AuthorityResource parentResource) {
        if (authorityResource != null && CollUtil.isNotEmpty(authorityResource.propertyAuthority())) {
            return authorityResource.propertyAuthority();
        }
        if (parentResource != null && CollUtil.isNotEmpty(parentResource.propertyAuthority())) {
            return parentResource.propertyAuthority();
        }
        return NoneConstants.EMPTY_STRINGS;
    }

    private static String getDataAuthority(AuthorityResource authorityResource, AuthorityResource parentResource) {
        if (authorityResource != null && StringUtils.isNotEmpty(authorityResource.dataAuthority())) {
            return authorityResource.dataAuthority();
        }
        if (parentResource != null && StringUtils.isNotEmpty(parentResource.dataAuthority())) {
            return parentResource.dataAuthority();
        }
        return "";
    }

    private static ByteKey getMethodKey(Class<?> clazz, String methodName, Class<?>[] paramsTypes) {
        ByteKey key = new ByteKey(16);
        key.append(clazz.hashCode()).append(methodName.hashCode());
        if (paramsTypes != null && paramsTypes.length > 0) {
            key.append(getParamHash(paramsTypes));
        }
        return key.flush();
    }

    private static long getParamHash(Class<?>[] paramsTypes) {
        long result = 1;
        for (Class<?> e : paramsTypes) { result = 31 * result + e.getName().hashCode(); }
        return result;
    }
}
