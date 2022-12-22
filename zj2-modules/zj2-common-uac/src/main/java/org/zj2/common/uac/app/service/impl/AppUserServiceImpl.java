package org.zj2.common.uac.app.service.impl;

import org.springframework.stereotype.Service;
import org.zj2.common.uac.app.dto.AppOrgDTO;
import org.zj2.common.uac.app.dto.AppUserDTO;
import org.zj2.common.uac.app.entity.AppOrg;
import org.zj2.common.uac.app.entity.AppUser;
import org.zj2.common.uac.app.mapper.AppOrgMapper;
import org.zj2.common.uac.app.mapper.AppUserMapper;
import org.zj2.common.uac.app.service.AppOrgService;
import org.zj2.common.uac.app.service.AppUserService;
import org.zj2.lite.service.BaseServiceImpl;

/**
 *  AppUserServiceImpl
 *
 * @author peijie.ye
 * @date 2022/11/27 20:40
 */
@Service
public class AppUserServiceImpl extends BaseServiceImpl<AppUserMapper, AppUser, AppUserDTO> implements AppUserService {
    @Override
    public AppUserDTO getAppUser(String appCode, String userId) {
        return getOne(wrapper().eq(AppUserDTO::getAppCode, appCode).eq(AppUserDTO::getUserId, userId));
    }
}
