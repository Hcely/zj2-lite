package org.zj2.common.uac.user.service.impl;

import org.springframework.stereotype.Service;
import org.zj2.common.uac.user.dto.UserPasswordDTO;
import org.zj2.common.uac.user.entity.UserPassword;
import org.zj2.common.uac.user.mapper.UserPasswordMapper;
import org.zj2.common.uac.user.service.UserPasswordService;
import org.zj2.lite.common.util.DateUtil;
import org.zj2.lite.service.BaseServiceImpl;

import java.time.LocalDateTime;

/**
 * UserPasswordServiceImpl
 *
 * @author peijie.ye
 * @date 2022/11/27 20:40
 */
@Service
public class UserPasswordServiceImpl extends BaseServiceImpl<UserPasswordMapper, UserPassword, UserPasswordDTO> implements UserPasswordService {
    @Override
    public UserPasswordDTO addPassword(boolean editPassword, String userId, String password) {
        LocalDateTime now = DateUtil.now();
        if(editPassword) {
            // 作废之前的
            UserPasswordDTO update = new UserPasswordDTO();
            update.setEnableFlag(0).setDisabledTime(now);
            update(update, wrapper().eq(UserPasswordDTO::getUserId, userId).eq(UserPasswordDTO::getEnableFlag, 1));
        }
        //
        UserPasswordDTO dto = new UserPasswordDTO();
        dto.setUserId(userId);
        dto.setPassword(password);
        dto.setEnableFlag(1);
        dto.setEnabledTime(now);
        return add(dto);
    }

}
