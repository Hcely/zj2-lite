package org.zj2.common.uac.app.service;

import org.zj2.common.uac.app.dto.AppClientDTO;
import org.zj2.lite.service.BaseApi;
import org.zj2.lite.service.annotation.ApiReference;

import java.util.List;

/**
 *  AppApi
 *
 * @author peijie.ye
 * @date 2022/11/28 12:01
 */
@ApiReference
public interface AppClientApi extends BaseApi<AppClientDTO> {
    boolean hasClient(String appCode);

    AppClientDTO getByCode(String appCode, String clientCode);

    List<AppClientDTO> queryByApp(String appCode);
}
