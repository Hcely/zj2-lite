package org.zj2.common.uac.app.service;

import org.zj2.common.uac.app.dto.AppOrgDTO;
import org.zj2.lite.service.BaseApi;
import org.zj2.lite.service.annotation.ApiReference;

/**
 *  AppOrgApi
 *
 * @author peijie.ye
 * @date 2022/11/28 12:01
 */
@ApiReference
public interface AppOrgApi extends BaseApi<AppOrgDTO> {
    AppOrgDTO getAppOrg(String appCode, String orgCode);
}
