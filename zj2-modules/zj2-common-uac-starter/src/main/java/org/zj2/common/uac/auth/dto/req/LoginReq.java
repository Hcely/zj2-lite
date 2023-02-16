package org.zj2.common.uac.auth.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * LoginReq
 *
 * @author peijie.ye
 * @date 2023/2/7 10:44
 */
@Getter
@Setter
@NoArgsConstructor
public class LoginReq implements Serializable {
    private static final long serialVersionUID = 8643904348041145346L;
    private String clientCode;
    private String orgCode;
    private String nameHeader;
    @NotEmpty(message = "账号不能为空")
    private String name;
    @NotEmpty(message = "密码不能为空")
    private String password;
}
