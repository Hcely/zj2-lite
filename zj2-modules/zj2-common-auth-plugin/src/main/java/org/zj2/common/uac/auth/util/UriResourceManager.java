package org.zj2.common.uac.auth.util;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.zj2.lite.common.constant.ZJ2Constants;
import org.zj2.lite.common.entity.ByteKey;
import org.zj2.lite.common.entity.Ternary;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.common.util.ReflectUtil;
import org.zj2.lite.service.auth.AuthenticationIgnored;
import org.zj2.lite.service.auth.AuthenticationRequired;
import org.zj2.lite.service.auth.AuthorityResource;
import org.zj2.lite.service.auth.UriResource;
import org.zj2.lite.service.context.TokenType;
import org.zj2.lite.service.util.ServiceUriUtil;

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

        @Override
        public void setTags(Set<String> tags) {
            super.setTags(tags);
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
        if (method == null) {
            method = ReflectUtil.findMethod(clazz, methodName, paramTypes);
        }
        resource.setName(ServiceUriUtil.getMethodName(clazz, methodName, paramTypes));
        resource.setUriPath(ServiceUriUtil.getUriPath(clazz, method));
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
            // 非本身服务，默认无需处理
            if (required == null && !StringUtils.startsWith(clazz.getName(), ZJ2Constants.ZJ2_PACKAGE_PREFIX)) {
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
            Set<String> propertyAuthorities = getValues(authorityResource, parentResource,
                    AuthorityResource::propertyAuthority);
            resource.setPropertyAuthorities(propertyAuthorities);
            resource.setDataAuthority(getDataAuthority(authorityResource, parentResource));
            Set<String> tags = getValues(authorityResource, parentResource, AuthorityResource::tags);
            resource.setTags(tags);
            if (StringUtils.isEmpty(resource.getDataAuthority())) { resource.setRequiredDataAuthority(false); }
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

    private static Set<String> getValues(AuthorityResource authorityResource, AuthorityResource parentResource,
            Function<AuthorityResource, String[]> getter) {
        String[] values;
        if (authorityResource != null && CollUtil.isNotEmpty(values = getter.apply(authorityResource))) {//NOSONAR
            return CollUtil.newSet(values);
        }
        if (parentResource != null && CollUtil.isNotEmpty(values = getter.apply(parentResource))) {//NOSONAR
            return CollUtil.newSet(values);
        }
        return CollUtil.emptySet();
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
