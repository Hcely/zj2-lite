package org.zj2.common.uac.auth.dto;

import lombok.Getter;
import lombok.Setter;
import org.zj2.common.uac.app.dto.AppClientDTO;
import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.common.uac.auth.dto.req.AuthReq;
import org.zj2.common.uac.org.dto.OrgDTO;
import org.zj2.common.uac.user.dto.UserDTO;
import org.zj2.lite.service.auth.AuthorizationJWT;

/**
 *  AuthContext
 *
 * @author peijie.ye
 * @date 2022/12/3 7:50
 */
@Getter
@Setter
public class AuthContext {
    private final Object req;
    private UserDTO user;
    private AppDTO app;
    private AppClientDTO client;
    private OrgDTO org;
    private AuthorizationJWT token;


    public <T extends AuthReq> AuthContext(T req) {
        this.req = req;
    }

    public <T extends AuthReq> T getReq() {
        //noinspection unchecked
        return (T) req;
    }
}
