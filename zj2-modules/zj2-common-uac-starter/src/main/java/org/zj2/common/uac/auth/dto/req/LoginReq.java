package org.zj2.common.uac.auth.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String name;
    private String password;
}
