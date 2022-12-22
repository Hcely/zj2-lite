package org.zj2.common.uac.auth.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *  LoginUserPasswordReq
 *
 * @author peijie.ye
 * @date 2022/11/28 1:40
 */
@Getter
@Setter
@NoArgsConstructor
public class AuthNamePwReq extends AuthReq {
    private static final long serialVersionUID = 20221128014054L;
    private String name;
    private String nameExtValue;
    private String password;
}
