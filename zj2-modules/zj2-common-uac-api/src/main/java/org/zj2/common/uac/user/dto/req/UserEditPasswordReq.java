package org.zj2.common.uac.user.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * UserUpdatePasswordReq
 *
 * @author peijie.ye
 * @date 2022/11/28 14:48
 */
@Getter
@Setter
@NoArgsConstructor
public class UserEditPasswordReq implements Serializable {
    private static final long serialVersionUID = 20221128144806L;
    private String userId;
    private String password;
}
