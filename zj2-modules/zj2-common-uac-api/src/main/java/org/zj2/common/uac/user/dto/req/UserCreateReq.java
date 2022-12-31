package org.zj2.common.uac.user.dto.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author peijie.ye
 * @since 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserCreateReq implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户账号名称
     */
    private String userAccountName;

    /**
     * 用户手机区号
     */
    private String userMobileAreaCode;

    /**
     * 用户手机
     */
    private String userMobile;

    /**
     * 用户邮件
     */
    private String userEmail;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户名称
     */
    private String userName;
}
