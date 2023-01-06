package org.zj2.common.uac.auth.authorize;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zj2.common.uac.auth.dto.UserAuthorityResources;
import org.zj2.lite.common.bean.BeanPropertyContext;
import org.zj2.lite.common.bean.BeanPropertyScanHandler;
import org.zj2.lite.common.bean.PropScanMode;
import org.zj2.lite.common.util.CollUtil;
import org.zj2.lite.common.util.PropertyUtil;
import org.zj2.lite.service.auth.AuthorityResource;

import java.util.List;

/**
 *  DataPropertyAuthorityHandler
 *
 * @author peijie.ye
 * @date 2023/1/2 22:53
 */
@Component
public class AuthorizeBeanPropertyHandler extends AbstractAuthorizeHandler<Object> {
    @Autowired(required = false)
    private List<PropertyHideShredder> shredders;

    @Override
    public void authorize(Object bean) {
        final UserAuthorityResources authorities = getAuthorityResources();
        PropertyUtil.scanProperties(bean, new AuthorizePropertyScanHandler(this, authorities));
    }

    private static class AuthorizePropertyScanHandler implements BeanPropertyScanHandler {
        private final AuthorizeBeanPropertyHandler propertyHandler;
        private final UserAuthorityResources authorities;

        private AuthorizePropertyScanHandler(AuthorizeBeanPropertyHandler propertyHandler,
                UserAuthorityResources authorities) {
            this.propertyHandler = propertyHandler;
            this.authorities = authorities;
        }

        @Override
        public PropScanMode apply(BeanPropertyContext cxt) {
            AuthorityResource resource;
            Object value;
            if (cxt.isPropertyOfBean() && cxt.isSimplePropertyType() && (value = cxt.propertyValue()) != null
                    && (resource = cxt.propertyAnnotation(AuthorityResource.class)) != null) {
                if (!authorities.containsAuthority(resource.value())) {
                    Object newValue = propertyHandler.hidePropertyValue(cxt.propertyName(), value);
                    cxt.propertyValue(newValue);
                }
            }
            return PropScanMode.DEEP;
        }
    }

    protected Object hidePropertyValue(String propertyName, Object value) {
        if (!(value instanceof String)) { return null; }
        String valueStr = value.toString();
        if (StringUtils.isEmpty(valueStr)) { return value; }
        if (CollUtil.isNotEmpty(shredders)) {
            for (PropertyHideShredder shredder : shredders) {
                if (shredder.supports(propertyName)) {
                    return shredder.hide(valueStr);
                }
            }
        }
        return PropertyHideShredder.STAR_4_STR;
    }
}
