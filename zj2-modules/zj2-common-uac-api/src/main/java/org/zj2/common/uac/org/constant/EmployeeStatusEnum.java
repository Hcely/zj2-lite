package org.zj2.common.uac.org.constant;

import lombok.Getter;
import org.zj2.lite.common.CodeEnum;

/**
 *  UserStatusEnum
 *
 * @author peijie.ye
 * @date 2022/11/27 22:11
 */
@Getter
public enum EmployeeStatusEnum implements CodeEnum<Integer> {
    WORKING(100, "在职"),

    LEFT(9999, "离职"),
    ;

    private final Integer code;
    private final String desc;

    EmployeeStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
