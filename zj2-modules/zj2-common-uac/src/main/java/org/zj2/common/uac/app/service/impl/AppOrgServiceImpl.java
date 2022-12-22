package org.zj2.common.uac.app.service.impl;

import org.springframework.stereotype.Service;
import org.zj2.common.uac.app.dto.AppOrgDTO;
import org.zj2.common.uac.app.entity.AppOrg;
import org.zj2.common.uac.app.mapper.AppOrgMapper;
import org.zj2.common.uac.app.service.AppOrgService;
import org.zj2.lite.service.BaseServiceImpl;

/**
 *  AppOrgServiceImpl
 *
 * @author peijie.ye
 * @date 2022/11/27 20:40
 */
@Service
public class AppOrgServiceImpl extends BaseServiceImpl<AppOrgMapper, AppOrg, AppOrgDTO> implements AppOrgService {
    @Override
    public AppOrgDTO getAppOrg(String appCode, String orgCode) {
        return getOne(wrapper().eq(AppOrgDTO::getAppCode, appCode).eq(AppOrgDTO::getOrgCode, orgCode));
    }
}
