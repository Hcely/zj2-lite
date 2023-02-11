package org.zj2.common.uac.auth.server;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zj2.common.uac.auth.server.hider.PropertyValueHider;
import org.zj2.common.uac.auth.util.AuthManager;
import org.zj2.lite.common.bean.BeanPropertyContext;
import org.zj2.lite.common.bean.BeanPropertyScanHandler;
import org.zj2.lite.common.bean.PropScanMode;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.common.util.PropertyUtil;
import org.zj2.lite.service.auth.AuthorityResource;
import org.zj2.lite.service.auth.AuthoritySet;
import org.zj2.lite.service.auth.UriResource;
import org.zj2.lite.service.context.AuthContext;

import java.util.List;
import java.util.Set;

/**
 * DataPropertyAuthorityHandler
 *
 * @author peijie.ye
 * @date 2023/1/2 22:53
 */
@Component
public class AuthorizePropertyHandler implements AuthorizeHandler<Object> {
    @Autowired(required = false)
    private List<PropertyValueHider> shredders;

    @Override
    public void authorize(AuthContext context, Object bean) {
        final AuthoritySet authorities = AuthManager.getAuthoritySet(context);
        PropertyUtil.scanProperties(bean, new AuthorizePropertyScanHandler(this, context, authorities));
    }

    private static class AuthorizePropertyScanHandler implements BeanPropertyScanHandler {
        private final AuthorizePropertyHandler handler;
        private final AuthContext context;
        private final AuthoritySet authorities;

        private AuthorizePropertyScanHandler(AuthorizePropertyHandler handler, AuthContext context,
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
                if (authorities.notContainsAuthority(propertyAuthority)) {
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
        String authority = StringUtils.defaultIfEmpty(resource.name(), resource.value());
        return StringUtils.isEmpty(authority) ? cxt.propertyName() : authority;
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
        if (CollUtil.isNotEmpty(shredders)) {
            for (PropertyValueHider shredder : shredders) {
                if (shredder.supports(propertyName)) {
                    return shredder.hide(valueStr);
                }
            }
        }
        return PropertyValueHider.STAR_4_STR;
    }
}
