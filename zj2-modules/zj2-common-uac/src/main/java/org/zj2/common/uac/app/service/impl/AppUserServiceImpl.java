package org.zj2.common.uac.app.service.impl;

import org.springframework.stereotype.Service;
import org.zj2.common.uac.app.dto.AppUserDTO;
import org.zj2.common.uac.app.dto.AppUserExtDTO;
import org.zj2.common.uac.app.dto.req.AppUserAddReq;
import org.zj2.common.uac.app.dto.req.AppUserQuery;
import org.zj2.common.uac.app.entity.AppUser;
import org.zj2.common.uac.app.mapper.AppUserMapper;
import org.zj2.common.uac.app.service.AppUserService;
import org.zj2.lite.common.constant.NoneConstants;
import org.zj2.lite.common.entity.result.ZListResp;
import org.zj2.lite.common.util.BooleanUtil;
import org.zj2.lite.common.util.DateUtil;
import org.zj2.lite.service.BaseServiceImpl;

/**
 * AppUserServiceImpl
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

    @Override
    public AppUserDTO addUser(AppUserAddReq req) {
        AppUserDTO exist = getAppUser(req.getAppCode(), req.getUserId());
        if(exist == null) {
            AppUserDTO appUser = new AppUserDTO();
            appUser.setAppCode(req.getAppCode());
            appUser.setUserId(req.getUserId());
            appUser.setEnableFlag(1);
            appUser.setEnabledTime(DateUtil.now());
            add(appUser);
            return appUser;
        } else {
            return exist;
        }
    }

    @Override
    public void removeUser(String appUserId) {
        AppUserDTO appUser = get(appUserId);
        if(appUser != null) { delete(appUserId); }
    }

    @Override
    public void enable(String appUserId) {
        AppUserDTO appUser = get(appUserId);
        if(appUser != null && BooleanUtil.isFalse(appUser.getEnableFlag())) {
            AppUserDTO update = new AppUserDTO();
            update.setAppUserId(appUserId);
            update.setEnableFlag(1);
            update.setEnabledTime(DateUtil.now());
            update.setDisabledTime(NoneConstants.NONE_DATE);
            updateById(update);
        }
    }

    @Override
    public void disable(String appUserId) {
        AppUserDTO appUser = get(appUserId);
        if(appUser != null && BooleanUtil.isTrue(appUser.getEnableFlag())) {
            AppUserDTO update = new AppUserDTO();
            update.setAppUserId(appUserId);
            update.setEnableFlag(0);
            update.setDisabledTime(DateUtil.now());
            updateById(update);
        }
    }

    @Override
    public ZListResp<AppUserExtDTO> pageQuery(AppUserQuery query) {
        return pageQuery(query, mapper::query);
    }
}
