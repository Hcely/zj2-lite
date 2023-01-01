package org.zj2.common.uac.app.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.zj2.common.uac.app.dto.AppClientDTO;
import org.zj2.common.uac.app.dto.req.AppClientCreateEditReq;
import org.zj2.common.uac.app.entity.AppClient;
import org.zj2.common.uac.app.mapper.AppClientMapper;
import org.zj2.common.uac.app.service.AppClientService;
import org.zj2.lite.common.constant.NoneConstants;
import org.zj2.lite.common.util.BooleanUtil;
import org.zj2.lite.common.util.DateUtil;
import org.zj2.lite.service.BaseServiceImpl;

import java.util.List;

/**
 *  AppServiceImpl
 *
 * @author peijie.ye
 * @date 2022/11/27 20:40
 */
@Service
public class AppClientServiceImpl extends BaseServiceImpl<AppClientMapper, AppClient, AppClientDTO>
        implements AppClientService {

    @Override
    public boolean hasClient(String appCode) {
        return exists(wrapper().eq(AppClientDTO::getAppCode, appCode));
    }

    @Override
    public AppClientDTO getByCode(String appCode, String clientCode) {
        return getOne(wrapper().eq(AppClientDTO::getAppCode, appCode).eq(AppClientDTO::getClientCode, clientCode));
    }

    @Override
    public List<AppClientDTO> queryByApp(String appCode) {
        return query(wrapper().eq(AppClientDTO::getAppCode, appCode));
    }

    @Override
    public AppClientDTO createEditClient(AppClientCreateEditReq req) {
        AppClientDTO appClient = getByCode(req.getAppCode(), req.getClientCode());
        if (appClient == null) {
            appClient = new AppClientDTO();
            appClient.setAppCode(req.getAppCode());
            appClient.setClientCode(req.getClientCode());
            appClient.setEnableFlag(1);
            appClient.setEnabledTime(DateUtil.now());
        }
        if (StringUtils.isNotEmpty(req.getClientName())) {appClient.setClientName(req.getClientName());}
        if (StringUtils.isNotEmpty(req.getNamespace())) {appClient.setNamespace(req.getNamespace());}
        if (req.getTokenTimeout() != null) {appClient.setTokenTimeout(req.getTokenTimeout());}
        save(appClient);
        return appClient;
    }

    @Override
    public void enable(String appClientId) {
        AppClientDTO appClient = get(appClientId);
        if (appClient != null && BooleanUtil.isTrue(appClient.getEnableFlag())) {
            AppClientDTO update = new AppClientDTO();
            update.setAppClientId(appClientId);
            update.setEnableFlag(1);
            update.setEnabledTime(DateUtil.now());
            update.setDisabledTime(NoneConstants.NONE_DATE);
            updateById(update);
        }
    }

    @Override
    public void disable(String appClientId) {
        AppClientDTO appClient = get(appClientId);
        if (appClient != null && BooleanUtil.isTrue(appClient.getEnableFlag())) {
            AppClientDTO update = new AppClientDTO();
            update.setAppClientId(appClientId);
            update.setEnableFlag(0);
            update.setDisabledTime(DateUtil.now());
            updateById(update);
        }
    }
}
