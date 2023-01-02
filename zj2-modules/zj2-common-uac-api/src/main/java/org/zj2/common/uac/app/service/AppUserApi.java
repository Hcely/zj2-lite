package org.zj2.common.uac.app.service;

import org.zj2.common.uac.app.dto.AppUserDTO;
import org.zj2.lite.service.ApiReference;
import org.zj2.lite.service.BaseApi;

/**
 *  AppUserApi
 *
 * @author peijie.ye
 * @date 2022/11/28 12:01
 */
@ApiReference
public interface AppUserApi extends BaseApi<AppUserDTO> {
    AppUserDTO getAppUser(String appCode, String userId);
}
