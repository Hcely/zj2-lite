package org.zj2.common.sys.base.config;

import org.apache.commons.lang3.StringUtils;
import org.zj2.common.sys.base.dto.SysConfigDTO;
import org.zj2.common.sys.base.service.SysConfigApi;
import org.zj2.lite.service.cache.CacheUtil;
import org.zj2.lite.service.context.AuthenticationContext;
import org.zj2.lite.spring.SpringBeanRef;

import java.time.LocalDateTime;

/**
 *  SysConfigUtil
 *
 * @author peijie.ye
 * @date 2022/12/12 2:45
 */
public class SysConfigUtil {
    private static final String COMMON_APP = "COMMON";
    private static final EmptyConfig EMPTY_CONFIG = new EmptyConfig();
    private static final SpringBeanRef<SysConfigApi> SEQUENCE_API_REF = new SpringBeanRef<>(SysConfigApi.class);

    public static String getConfigKey(String appCode, String configCode) {
        if (StringUtils.isEmpty(appCode)) { appCode = COMMON_APP; }
        StringBuilder sb = new StringBuilder(StringUtils.length(appCode) + StringUtils.length(configCode) + 16);
        sb.append("SYS_CONFIG:").append(appCode);
        if (StringUtils.isNotEmpty(configCode)) {
            sb.append(':').append(configCode);
        }
        return sb.toString();
    }

    public static SysConfigDTO config(String configCode) {
        if (StringUtils.isEmpty(configCode)) { return EMPTY_CONFIG; }
        String appCode = AuthenticationContext.currentAppCode();
        SysConfigDTO config = null;
        if (StringUtils.isNotEmpty(appCode)) {
            String cacheKey = getConfigKey(appCode, configCode);
            config = CacheUtil.DEF_CACHE.getCache(cacheKey, configCode, s -> getConfig0(appCode, configCode), true);
        }
        if (config == null) {
            String cacheKey = getConfigKey(COMMON_APP, configCode);
            config = CacheUtil.DEF_CACHE.getCache(cacheKey, configCode, s -> getConfig0(COMMON_APP, configCode), true);
        }
        return config == null ? EMPTY_CONFIG : config;
    }

    private static SysConfigDTO getConfig0(String appCode, String configCode) {
        SysConfigApi sysConfigApi = SEQUENCE_API_REF.get();
        return sysConfigApi == null ? null : sysConfigApi.get(appCode, configCode);
    }

    private static final class EmptyConfig extends SysConfigDTO {

        private static final long serialVersionUID = -93247274151172300L;

        @Override
        public SysConfigDTO setSysConfigId(String sysConfigId) {
            return this;
        }

        @Override
        public SysConfigDTO setConfigCode(String configCode) {
            return this;
        }

        @Override
        public SysConfigDTO setConfigName(String configName) {
            return this;
        }

        @Override
        public SysConfigDTO setConfigValue(String configValue) {
            return this;
        }

        @Override
        public SysConfigDTO setConfigExtValue0(String configExtValue0) {
            return this;
        }

        @Override
        public SysConfigDTO setConfigExtValue1(String configExtValue1) {
            return this;
        }

        @Override
        public SysConfigDTO setAppCode(String appCode) {
            return this;
        }

        @Override
        public SysConfigDTO setCreateTime(LocalDateTime createTime) {
            return this;
        }

        @Override
        public SysConfigDTO setCreateUser(String createUser) {
            return this;
        }

        @Override
        public SysConfigDTO setCreateUserName(String createUserName) {
            return this;
        }

        @Override
        public SysConfigDTO setUpdateTime(LocalDateTime updateTime) {
            return this;
        }

        @Override
        public SysConfigDTO setUpdateUser(String updateUser) {
            return this;
        }

        @Override
        public SysConfigDTO setUpdateUserName(String updateUserName) {
            return this;
        }
    }
}
