package org.zj2.common.uac.app.api;

import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.lite.service.ApiReference;
import org.zj2.lite.service.BaseApi;

/**
 * AppApi
 *
 * @author peijie.ye
 * @date 2022/11/28 12:01
 */
@ApiReference
public interface AppApi extends BaseApi<AppDTO> {
    AppDTO getByCode(String appCode);
}
