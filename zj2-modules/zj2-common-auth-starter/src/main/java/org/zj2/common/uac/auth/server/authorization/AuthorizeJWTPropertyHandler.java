package org.zj2.common.uac.auth.server.authorization;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.zj2.common.uac.auth.server.hider.PropertyValueHider;
import org.zj2.lite.common.bean.BeanPropertyContext;
import org.zj2.lite.common.bean.BeanPropertyScanHandler;
import org.zj2.lite.common.bean.PropScanMode;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.common.util.PropertyUtil;
import org.zj2.lite.service.auth.AuthorityResource;
import org.zj2.lite.service.auth.AuthoritySet;
import org.zj2.lite.service.auth.UriResource;
import org.zj2.lite.service.auth.helper.AuthAfterCompletedHandler;
import org.zj2.lite.service.context.AuthContext;
import org.zj2.lite.service.context.RequestContext;
import org.zj2.lite.service.context.TokenType;
import org.zj2.lite.spring.SpringBeanRef;

import java.util.Set;

/**
 * DataPropertyAuthorityHandler
 *
 * @author peijie.ye
 * @date 2023/1/2 22:53
 */
@Component
public class AuthorizeJWTPropertyHandler extends AuthAbstractHandler implements AuthAfterCompletedHandler {
    private static final SpringBeanRef<PropertyValueHider[]> HIDERS_REF = new SpringBeanRef<>(
            PropertyValueHider[].class);

    @Override
    public boolean supports(RequestContext requestContext, AuthContext authContext, UriResource uriResource) {
        if (authContext.getTokenType() != TokenType.JWT) { return false; }
        return uriResource.isRequiredPropertyAuthority();
    }

    @Override
    public void authorize(RequestContext requestContext, AuthContext authContext, UriResource uriResource,
            Object result) {
        final AuthoritySet authorities = getAuthoritySet(requestContext, authContext);
        PropertyUtil.scanProperties(result, new AuthorizePropertyScanHandler(this, authContext, authorities));
    }

    private static class AuthorizePropertyScanHandler implements BeanPropertyScanHandler {
        private final AuthorizeJWTPropertyHandler handler;
        private final AuthContext context;
        private final AuthoritySet authorities;

        private AuthorizePropertyScanHandler(AuthorizeJWTPropertyHandler handler, AuthContext context,
                AuthoritySet authorities) {
            this.handler = handler;
            this.context = context;
            this.authorities = authorities;
        }

        @Override
        public PropScanMode apply(BeanPropertyContext cxt) {
            String propertyAuthority = handler.getPropertyAuthority(cxt);
            Object value;
            if (handler.isRequiredAuthority(context, propertyAuthority) && (value = cxt.propertyValue()) != null) {
                if (!context.isAuthenticated() || authorities.notContainsAuthority(propertyAuthority)) {
                    Object newValue = handler.hidePropertyValue(cxt.propertyName(), value);
                    cxt.propertyValue(newValue);
                }
            }
            return PropScanMode.DEEP;
        }
    }

    protected String getPropertyAuthority(BeanPropertyContext cxt) {
        if (!cxt.isSimplePropertyType()) { return null; }
        if (!cxt.isPropertyOfBean()) { return null; }
        AuthorityResource resource = cxt.propertyAnnotation(AuthorityResource.class);
        if (resource == null) { return null; }
        String authority = resource.name();
        if (StringUtils.isNotEmpty(authority)) { return authority; }
        authority = resource.value();
        if (StringUtils.isNotEmpty(authority)) { return authority; }
        return cxt.propertyName();
    }

    protected boolean isRequiredAuthority(AuthContext context, String propertyAuthority) {
        if (StringUtils.isEmpty(propertyAuthority)) { return false; }
        if (context == null) { return true; }
        UriResource uriResource = context.getUriResource();
        if (uriResource == null) { return true; }
        Set<String> authorities = uriResource.getPropertyAuthorities();
        if (CollUtil.isEmpty(authorities)) { return true; }
        return authorities.contains(propertyAuthority);
    }

    protected Object hidePropertyValue(String propertyName, Object value) {
        if (!(value instanceof String)) { return null; }
        String valueStr = value.toString();
        if (StringUtils.isEmpty(valueStr)) { return value; }
        PropertyValueHider[] hiders = HIDERS_REF.get();
        if (CollUtil.isNotEmpty(hiders)) {
            for (PropertyValueHider hider : hiders) {
                if (hider.supports(propertyName)) {
                    return hider.hide(valueStr);
                }
            }
        }
        return PropertyValueHider.STAR_4_STR;
    }
}
