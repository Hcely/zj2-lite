package org.zj2.common.uac.auth.util;

import org.apache.commons.lang3.StringUtils;
import org.zj2.common.uac.app.service.AppApi;
import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.lite.service.context.AuthenticationContext;
import org.zj2.lite.service.cache.CacheUtil;
import org.zj2.lite.spring.SpringBeanRef;

/**
 *  AppUtil
 *
 * @author peijie.ye
 * @date 2022/12/9 2:12
 */
public class AppUtil {
    private static final SpringBeanRef<AppApi> appApiRef = new SpringBeanRef<>(AppApi.class);
    public static AppDTO getApp() {
        String appCode = StringUtils.defaultIfEmpty(AuthenticationContext.currentAppCode(), AppDTO.COMMON_APP_CODE);
        return getApp(appCode);
    }

    public static AppDTO getApp(String appCode) {
        AppApi api = appApiRef.get();
        if (api == null) {return null;}
        return CacheUtil.DEF_CACHE.get("AuthApp", appCode, api::getByCode);
    }
}
