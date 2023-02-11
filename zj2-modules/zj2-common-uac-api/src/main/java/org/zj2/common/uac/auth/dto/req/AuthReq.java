package org.zj2.common.uac.auth.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * LoginUserPasswordReq
 *
 * @author peijie.ye
 * @date 2022/11/28 1:40
 */
@Getter
@Setter
@NoArgsConstructor
public class AuthReq implements Serializable {
    private static final long serialVersionUID = 20221128014054L;
    private String appCode;
    private String orgCode;
    private String clientCode;
}
