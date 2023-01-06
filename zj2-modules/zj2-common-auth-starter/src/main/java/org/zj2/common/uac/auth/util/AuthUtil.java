package org.zj2.common.uac.auth.util;

import org.apache.commons.lang3.StringUtils;
import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.common.uac.app.service.AppApi;
import org.zj2.lite.common.entity.result.ZError;
import org.zj2.lite.common.entity.result.ZRBuilder;
import org.zj2.lite.service.cache.CacheUtil;
import org.zj2.lite.service.context.AuthenticationContext;
import org.zj2.lite.spring.SpringBeanRef;

/**
 *  AppUtil
 *
 * @author peijie.ye
 * @date 2022/12/9 2:12
 */
public class AuthUtil {
    private static final SpringBeanRef<AppApi> appApiRef = new SpringBeanRef<>(AppApi.class);

    public static AppDTO getApp() {
        String appCode = StringUtils.defaultIfEmpty(AuthenticationContext.currentAppCode(), AppDTO.COMMON_APP_CODE);
        AppApi api = appApiRef.get();
        if (api == null) {return null;}
        return CacheUtil.DEF_CACHE.get(AppDTO.class, appCode, api::getByCode);
    }

    public static ZError unAuthenticationErr(String msg) {
        return ZRBuilder.failureErr(msg).setStatus(401);
    }

    public static ZError unAuthorityErr(String msg) {
        return ZRBuilder.failureErr(msg).setStatus(403);
    }
}
