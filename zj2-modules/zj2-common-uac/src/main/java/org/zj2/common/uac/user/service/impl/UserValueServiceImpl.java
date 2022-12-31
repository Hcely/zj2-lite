package org.zj2.common.uac.user.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.zj2.common.uac.user.constant.UserConstants;
import org.zj2.common.uac.user.constant.UserValueTypeEnum;
import org.zj2.common.uac.user.dto.UserValueDTO;
import org.zj2.common.uac.user.mapper.UserValueMapper;
import org.zj2.common.uac.user.entity.UserValue;
import org.zj2.common.uac.user.service.UserValueService;
import org.zj2.lite.common.util.DateUtil;
import org.zj2.lite.service.BaseServiceImpl;
import org.zj2.lite.util.CryptUtil;

import java.time.LocalDateTime;

/**
 *  UserServiceImpl
 *
 * @author peijie.ye
 * @date 2022/11/27 20:40
 */
@Service
public class UserValueServiceImpl extends BaseServiceImpl<UserValueMapper, UserValue, UserValueDTO>
        implements UserValueService {

    @Override
    public boolean existUserValue(UserValueTypeEnum valueType, String userValue) {
        return existUserValue(valueType, userValue, null);
    }

    @Override
    public boolean existUserValue(UserValueTypeEnum valueType, String userValue, String userExtValue) {
        if (StringUtils.isEmpty(userValue)) {return false;}
        if (valueType.isCrypt()) {userValue = CryptUtil.encrypt(userValue);}
        return exists(wrapper().eq(UserValueDTO::getUserValueType, valueType.getCode())
                .eq(UserValueDTO::getUserValue, userValue)
                .eq(UserValueDTO::getUserExtValue, StringUtils.defaultString(userExtValue))
                .eq(UserValueDTO::getEnableFlag, 1));
    }

    @Override
    public String findUserId(UserValueTypeEnum valueType, String userValue, String userExtValue) {
        if (valueType.isCrypt()) {userValue = CryptUtil.encrypt(userValue);}
        if (UserValueTypeEnum.MOBILE.eq(valueType)) {
            if (StringUtils.isEmpty(userExtValue)) {userExtValue = UserConstants.DEF_MOBILE_AREA_CODE;}
        }
        UserValueDTO value = getOne(wrapper().eq(UserValueDTO::getUserValueType, valueType.getCode())
                .eq(UserValueDTO::getUserValue, userValue)
                .eq(UserValueDTO::getUserExtValue, StringUtils.defaultString(userExtValue))
                .eq(UserValueDTO::getEnableFlag, 1));
        return value == null ? null : value.getUserId();
    }

    @Override
    public UserValueDTO addUserValue(boolean editValue, String userId, UserValueTypeEnum valueType, String userValue,
            String userExtValue) {
        if (valueType.isCrypt()) {userValue = CryptUtil.encrypt(userValue);}
        LocalDateTime now = DateUtil.now();
        // 作废之前 value
        if (editValue) {
            UserValueDTO update = new UserValueDTO();
            update.setEnableFlag(0);
            update.setDisabledTime(now);
            update(update, wrapper().eq(UserValueDTO::getUserValueType, valueType.getCode())
                    .eq(UserValueDTO::getUserId, userId).eq(UserValueDTO::getEnableFlag, 1));
        }
        // 仅作废
        if (StringUtils.isEmpty(userValue)) {return null;}
        // 新增
        UserValueDTO value = new UserValueDTO();
        value.setUserId(userId);
        value.setUserValueType(valueType.getCode());
        value.setUserValue(userValue);
        value.setUserExtValue(StringUtils.defaultString(userExtValue));
        value.setEnableFlag(1);
        value.setEnabledTime(now);
        return add(value);
    }
}
