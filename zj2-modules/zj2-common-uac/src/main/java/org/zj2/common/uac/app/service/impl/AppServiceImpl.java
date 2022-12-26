package org.zj2.common.uac.app.service.impl;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.common.uac.app.dto.req.AppCreateEditReq;
import org.zj2.common.uac.app.entity.App;
import org.zj2.common.uac.app.mapper.AppMapper;
import org.zj2.common.uac.app.service.AppService;
import org.zj2.common.uac.auth.util.AppUtil;
import org.zj2.lite.common.constant.NoneConstants;
import org.zj2.lite.common.entity.result.ZRBuilder;
import org.zj2.lite.common.util.BooleanUtil;
import org.zj2.lite.common.util.DateUtil;
import org.zj2.lite.service.BaseServiceImpl;
import org.zj2.lite.service.context.AuthenticationContext;
import org.zj2.lite.util.PatternUtil;

/**
 *  AppServiceImpl
 *
 * @author peijie.ye
 * @date 2022/11/27 20:40
 */
@Service
public class AppServiceImpl extends BaseServiceImpl<AppMapper, App, AppDTO> implements AppService {
    @Value("${zj.app.commonSecret:ndierknN@0f;Fuowe!sd%s}")
    private String commonAppSecret;

    @Override
    public AppDTO getByCode(String appCode) {
        if (AppDTO.COMMON_APP_CODE.equalsIgnoreCase(appCode)) {
            AppDTO commonApp = new AppDTO();
            commonApp.setAppCode(appCode);
            commonApp.setAppName(appCode);
            commonApp.setAppSecret(commonAppSecret);
            commonApp.setEnableFlag(1);
            commonApp.setAllowAllUser(1);
            return commonApp;
        }
        return getByCode0(appCode);
    }

    private AppDTO getByCode0(String appCode) {
        return StringUtils.isEmpty(appCode) ? null : getOne(wrapper().eq(AppDTO::getAppCode, appCode));
    }

    @Override
    public AppDTO create(AppCreateEditReq req) {
        // 处理参数
        req.setAppCode(StringUtils.trimToEmpty(req.getAppCode()));
        req.setAppName(StringUtils.trimToEmpty(req.getAppName()));
        // 检验参数
        if (StringUtils.length(req.getAppCode()) > 60) {throw ZRBuilder.failureErr("应用编码超过60个字");}
        if (AppDTO.COMMON_APP_CODE.equalsIgnoreCase(req.getAppCode()) || !PatternUtil.isWord(req.getAppCode())) {
            throw ZRBuilder.failureErr("应用编码格式不合法");
        }
        //
        boolean exist = exists(wrapper().eq(AppDTO::getAppCode, req.getAppCode()));
        if (exist) {throw ZRBuilder.failureErr("应用编码已存在");}
        //
        AuthenticationContext.current().setAppCode(req.getAppCode());
        AppDTO app = new AppDTO();
        app.setAppCode(req.getAppCode());
        app.setAppName(req.getAppName());
        app.setAppSecret(AppUtil.randomAppSecret());
        app.setAllowAllUser(ObjectUtils.defaultIfNull(req.getAllowAllUser(), 0));
        app.setSingleSignOn(ObjectUtils.defaultIfNull(req.getSingleSignOn(), 1));
        app.setTokenTimeout(ObjectUtils.defaultIfNull(req.getTokenTimeout(), 3600000L * 4));
        app.setEnableFlag(1);
        app.setEnabledTime(DateUtil.now());
        return add(app);
    }


    @Override
    public void editApp(AppCreateEditReq req) {
        AppDTO app = getByCode0(req.getAppCode());
        if (app == null) {throw ZRBuilder.failureErr("应用不存在");}
        AppDTO update = new AppDTO();
        update.setAppId(app.getAppId());
        if (StringUtils.isNotEmpty(req.getAppName())) {update.setAppName(req.getAppName());}
        if (req.getAllowAllUser() != null) {update.setAllowAllUser(req.getAllowAllUser());}
        if (req.getSingleSignOn() != null) {update.setSingleSignOn(req.getSingleSignOn());}
        if (req.getTokenTimeout() != null) {update.setTokenTimeout(req.getTokenTimeout());}
        updateById(update);
    }

    @Override
    public void editSecret(String appCode, String appSecret) {
        AppDTO app = getByCode0(appCode);
        if (app == null) {throw ZRBuilder.failureErr("应用不存在");}
        if (!AppUtil.validSecret(appSecret)) {throw ZRBuilder.failureErr("应用密钥不合法");}
        //
        AppDTO update = new AppDTO();
        update.setAppId(app.getAppId());
        update.setAppSecret(appSecret);
        updateById(update);
    }

    @Override
    public void enable(String appCode) {
        AppDTO app = getByCode0(appCode);
        if (app != null && BooleanUtil.isFalse(app.getEnableFlag())) {
            AppDTO update = new AppDTO();
            update.setAppId(app.getAppId());
            update.setEnableFlag(1);
            update.setEnabledTime(DateUtil.now());
            update.setDisabledTime(NoneConstants.NONE_DATE);
            updateById(update);
        }
    }

    @Override
    public void disable(String appCode) {
        AppDTO app = getByCode0(appCode);
        if (app != null && BooleanUtil.isTrue(app.getEnableFlag())) {
            AppDTO update = new AppDTO();
            update.setAppId(app.getAppId());
            update.setEnableFlag(0);
            update.setDisabledTime(DateUtil.now());
            updateById(update);
        }
    }
}
