package org.zj2.common.uac.user.constant;

import lombok.Getter;
import org.zj2.lite.CodeEnum;

/**
 *  UserStatusEnum
 *
 * @author peijie.ye
 * @date 2022/11/27 22:11
 */
@Getter
public enum UserEventEnum implements CodeEnum<String> {
    CREATE_USER("创建账户"),

    LOGIN("登录"),

    EDIT_PASSWORD("编辑密码"),

    EDIT_ACCOUNT_NAME("编辑账户名"),

    EDIT_EMAIL("编辑邮箱"),

    EDIT_MOBILE("编辑手机"),

    BIND_WX("绑定微信"),

    SET_VALID("设置有效期"),

    SET_FORBIDDEN("设置停用期"),

    ACTIVATED("激活"),

    ENABLED("启用"),

    DISABLED("禁用"),
    ;

    private final String desc;

    UserEventEnum(String desc) {
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return name();
    }
}
