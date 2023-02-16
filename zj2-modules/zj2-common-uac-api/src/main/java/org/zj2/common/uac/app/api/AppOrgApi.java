package org.zj2.common.uac.app.api;

import org.zj2.common.uac.app.dto.AppOrgDTO;
import org.zj2.lite.service.ApiReference;
import org.zj2.lite.service.BaseApi;

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
