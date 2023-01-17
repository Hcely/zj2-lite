package org.zj2.common.uac.app.service.impl;

import org.springframework.stereotype.Service;
import org.zj2.common.uac.app.dto.AppOrgDTO;
import org.zj2.common.uac.app.dto.AppOrgPlusDTO;
import org.zj2.common.uac.app.dto.req.AppOrgQuery;
import org.zj2.common.uac.app.entity.AppOrg;
import org.zj2.common.uac.app.mapper.AppOrgMapper;
import org.zj2.common.uac.app.service.AppOrgService;
import org.zj2.lite.common.constant.NoneConstants;
import org.zj2.lite.common.entity.result.ZListResp;
import org.zj2.lite.common.util.BooleanUtil;
import org.zj2.lite.common.util.DateUtil;
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

    @Override
    public AppOrgDTO addOrg(String appCode, String orgCode) {
        AppOrgDTO exist = getAppOrg(appCode, orgCode);
        if (exist == null) {
            AppOrgDTO appOrg = new AppOrgDTO();
            appOrg.setAppCode(appCode);
            appOrg.setOrgCode(orgCode);
            appOrg.setEnableFlag(1);
            appOrg.setEnabledTime(DateUtil.now());
            return add(appOrg);
        } else {
            return exist;
        }
    }

    @Override
    public void removeOrg(String appOrgId) {
        AppOrgDTO appOrg = get(appOrgId);
        if (appOrg != null) { delete(appOrgId); }
    }

    @Override
    public void enable(String appOrgId) {
        AppOrgDTO appOrg = get(appOrgId);
        if (appOrg != null && BooleanUtil.isFalse(appOrg.getEnableFlag())) {
            AppOrgDTO update = new AppOrgDTO();
            update.setAppOrgId(appOrgId);
            update.setEnableFlag(1);
            update.setEnabledTime(DateUtil.now());
            update.setDisabledTime(NoneConstants.NONE_DATE);
            updateById(update);
        }
    }

    @Override
    public void disable(String appOrgId) {
        AppOrgDTO appOrg = get(appOrgId);
        if (appOrg != null && BooleanUtil.isTrue(appOrg.getEnableFlag())) {
            AppOrgDTO update = new AppOrgDTO();
            update.setAppOrgId(appOrgId);
            update.setEnableFlag(0);
            update.setDisabledTime(DateUtil.now());
            updateById(update);
        }
    }

    @Override
    public ZListResp<AppOrgPlusDTO> pageQuery(AppOrgQuery query) {
        return null;
    }
}
