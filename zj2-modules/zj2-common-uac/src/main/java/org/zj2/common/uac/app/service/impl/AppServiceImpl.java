package org.zj2.common.uac.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zj2.common.uac.app.constant.AppConfigs;
import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.common.uac.app.entity.App;
import org.zj2.common.uac.app.mapper.AppMapper;
import org.zj2.common.uac.app.service.AppService;
import org.zj2.lite.service.BaseServiceImpl;

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
            commonApp.setAppConfig(new JSONObject());
            commonApp.setEnableFlag(1);
            AppConfigs.allowAllUser.setValue(commonApp.getAppConfig(), true);
            return commonApp;
        }
        return StringUtils.isEmpty(appCode) ? null : getOne(wrapper().eq(AppDTO::getAppCode, appCode));
    }
}
