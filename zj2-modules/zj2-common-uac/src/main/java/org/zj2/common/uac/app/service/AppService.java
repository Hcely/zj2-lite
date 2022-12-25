package org.zj2.common.uac.app.service;

import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.lite.service.BaseInnerService;

/**
 *  AppService
 *
 * @author peijie.ye
 * @date 2022/12/3 7:37
 */
public interface AppService extends BaseInnerService<AppDTO>, AppApi {
    AppDTO createOrUpdate();

    void editSecret();



}
