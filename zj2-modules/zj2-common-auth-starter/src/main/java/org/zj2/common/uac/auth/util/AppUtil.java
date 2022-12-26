package org.zj2.common.uac.auth.util;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.zj2.common.uac.app.service.AppApi;
import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.lite.common.util.StrUtil;
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
    private static final String SECRET_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_-";

    public static String randomAppSecret() {
        StringBuilder sb = new StringBuilder(48);
        int bound = SECRET_CHARS.length();
        for (int i = 0; i < 32; ++i) {
            sb.append(SECRET_CHARS.charAt(RandomUtils.nextInt(0, bound)));
        }
        return sb.toString();
    }

    public static boolean validSecret(String value) {
        if (StringUtils.length(value) < 20) {return false;}
        for (int i = 0, len = value.length(); i < len; ++i) {
            char c = value.charAt(i);
            if (SECRET_CHARS.indexOf(c) == -1) {return false;}
        }
        return true;
    }

    public static AppDTO getApp() {
        String appCode = StringUtils.defaultIfEmpty(AuthenticationContext.currentAppCode(), AppDTO.COMMON_APP_CODE);
        return getApp(appCode);
    }

    public static AppDTO getApp(String appCode) {
        AppApi api = appApiRef.get();
        if (api == null) {return null;}
        return CacheUtil.DEF_CACHE.getCache(getAppCacheKey(appCode), appCode, api::getByCode);
    }

    public static String getAppCacheKey(String appCode) {
        return StrUtil.concat("AuthApp:", appCode);
    }
}
