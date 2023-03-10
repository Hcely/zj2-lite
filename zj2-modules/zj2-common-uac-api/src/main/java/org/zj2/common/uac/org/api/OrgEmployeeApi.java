package org.zj2.common.uac.org.api;

import org.zj2.common.uac.org.dto.OrgEmployeeDTO;
import org.zj2.lite.service.ApiReference;
import org.zj2.lite.service.BaseApi;

/**
 * OrgEmployeeApi
 *
 * @author peijie.ye
 * @date 2022/11/28 12:01
 */
@ApiReference
public interface OrgEmployeeApi extends BaseApi<OrgEmployeeDTO> {
    OrgEmployeeDTO getEmployee(String orgCode, String userId);
}
