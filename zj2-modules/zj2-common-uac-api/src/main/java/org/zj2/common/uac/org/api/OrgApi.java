package org.zj2.common.uac.org.api;

import org.zj2.common.uac.org.dto.OrgDTO;
import org.zj2.lite.service.ApiReference;
import org.zj2.lite.service.BaseApi;

/**
 * OrgApi
 *
 * @author peijie.ye
 * @date 2022/11/28 12:01
 */
@ApiReference
public interface OrgApi extends BaseApi<OrgDTO> {
    OrgDTO getByCode(String orgCode);
}
