package org.zj2.common.sys.base.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.zj2.common.sys.base.dto.SysConfigDTO;
import org.zj2.common.sys.base.dto.req.SysConfigEditReq;
import org.zj2.common.sys.base.entity.SysConfig;
import org.zj2.common.sys.base.mapper.SysConfigMapper;
import org.zj2.common.sys.base.service.SysConfigService;
import org.zj2.common.sys.base.config.SysConfigUtil;
import org.zj2.lite.service.BaseServiceImpl;
import org.zj2.lite.service.cache.CacheUtil;
import org.zj2.lite.service.context.AuthenticationContext;

/**
 *  SysConfigServiceImpl
 *
 * @author peijie.ye
 * @date 2022/12/10 3:00
 */
@Service
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfigMapper, SysConfig, SysConfigDTO>
        implements SysConfigService {
    @Override
    public SysConfigDTO get(String appCode, String configCode) {
        if (StringUtils.isEmpty(configCode)) {return null;}
        appCode = StringUtils.defaultIfEmpty(appCode, SysConfigDTO.COMMON_APP_CODE);
        return getOne(wrapper().eq(SysConfigDTO::getAppCode, appCode).eq(SysConfigDTO::getConfigCode, configCode));
    }

    @Override
    public SysConfigDTO edit(SysConfigEditReq req) {
        fillEditReqParams(req);
        SysConfigDTO sysConfig = saveConfig(req);
        String key = SysConfigUtil.getConfigKey(sysConfig.getAppCode(), sysConfig.getConfigCode());
        CacheUtil.sendCacheSign(key);
        return sysConfig;
    }

    private void fillEditReqParams(SysConfigEditReq req) {
        String appCode = req.getAppCode();
        if (StringUtils.isEmpty(appCode)) {
            appCode = AuthenticationContext.currentAppCode();
        } else if (StringUtils.equalsIgnoreCase(SysConfigDTO.COMMON_APP_CODE, appCode)) {
            appCode = SysConfigDTO.COMMON_APP_CODE;
        }
        req.setAppCode(appCode);
        req.setConfigName(StringUtils.trimToEmpty(req.getConfigName()));
        req.setConfigValue(StringUtils.trimToEmpty(req.getConfigValue()));
        req.setConfigExtValue0(StringUtils.trimToEmpty(req.getConfigExtValue0()));
        req.setConfigExtValue1(StringUtils.trimToEmpty(req.getConfigExtValue1()));
    }

    private SysConfigDTO saveConfig(SysConfigEditReq req) {
        SysConfigDTO config = get(req.getAppCode(), req.getConfigCode());
        if (config == null) {
            AuthenticationContext.current().setAppCode(req.getAppCode());
            config = new SysConfigDTO();
            config.setConfigCode(req.getConfigCode());
            config.setAppCode(req.getAppCode());
        }
        config.setConfigName(req.getConfigName());
        config.setConfigValue(req.getConfigValue());
        config.setConfigExtValue0(req.getConfigExtValue0());
        config.setConfigExtValue1(req.getConfigExtValue1());
        save(config);
        return config;
    }
}
