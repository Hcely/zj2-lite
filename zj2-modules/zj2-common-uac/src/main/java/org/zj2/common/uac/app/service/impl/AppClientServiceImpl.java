package org.zj2.common.uac.app.service.impl;

import org.springframework.stereotype.Service;
import org.zj2.common.uac.app.dto.AppClientDTO;
import org.zj2.common.uac.app.entity.AppClient;
import org.zj2.common.uac.app.mapper.AppClientMapper;
import org.zj2.common.uac.app.service.AppClientService;
import org.zj2.lite.service.BaseServiceImpl;

import java.util.List;

/**
 *  AppServiceImpl
 *
 * @author peijie.ye
 * @date 2022/11/27 20:40
 */
@Service
public class AppClientServiceImpl extends BaseServiceImpl<AppClientMapper, AppClient, AppClientDTO>
        implements AppClientService {

    @Override
    public boolean hasClient(String appCode) {
        return exists(wrapper().eq(AppClientDTO::getAppCode, appCode));
    }

    @Override
    public AppClientDTO getByCode(String appCode, String clientCode) {
        return getOne(wrapper().eq(AppClientDTO::getAppCode, appCode).eq(AppClientDTO::getClientCode, clientCode));
    }

    @Override
    public List<AppClientDTO> queryByApp(String appCode) {
        return query(wrapper().eq(AppClientDTO::getAppCode, appCode));
    }
}
