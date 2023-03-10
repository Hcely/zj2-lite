package org.zj2.common.uac.user.constant;

import lombok.Getter;
import org.zj2.lite.CodeEnum;

/**
 * UserStatusEnum
 *
 * @author peijie.ye
 * @date 2022/11/27 22:11
 */
@Getter
public enum UserValueTypeEnum implements CodeEnum<String> {
    ACCOUNT_NAME("账户", false, true),

    MOBILE("手机", true, true),

    EMAIL("邮箱", true, true),

    WX_USER("微信", false, false),

    FACE_ID("人脸", false, false),
    ;

    private final String code;
    private final String desc;
    private final boolean crypt;
    private final boolean sysValue;// 系统固定值

    UserValueTypeEnum(String desc, boolean crypt, boolean sysValue) {
        this.code = name();
        this.desc = desc;
        this.crypt = crypt;
        this.sysValue = sysValue;
    }
}
