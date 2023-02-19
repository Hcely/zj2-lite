package org.zj2.common.uac.org.constant;

import lombok.Getter;
import org.zj2.lite.CodeEnum;

/**
 *  UserStatusEnum
 *
 * @author peijie.ye
 * @date 2022/11/27 22:11
 */
@Getter
public enum EmployeeEventEnum implements CodeEnum<String> {
    ENTRY("入职"),

    QUIT("离职"),

    VISIBLE("可见"),

    DIVISIBLE("隐藏"),

    ENABLED("启用"),

    DISABLED("禁用"),
    ;

    private final String desc;

    EmployeeEventEnum(String desc) {
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return name();
    }
}
