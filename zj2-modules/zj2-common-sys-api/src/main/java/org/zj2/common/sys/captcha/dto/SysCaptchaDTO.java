package org.zj2.common.sys.captcha.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 验证码表
 * </p>
 *
 * @author peijie.ye
 * @since 2022-12-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysCaptchaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 验证码id
     */
    private String captchaId;

    /**
     * 验证码类型
     */
    private String captchaType;

    /**
     * 业务编码
     */
    private String bizCode;

    /**
     * 业务相关编码
     */
    private String bizReferenceCode;

    /**
     * 验证码值
     */
    private String captchaValue;

    /**
     * 验证码级别
     */
    private Integer captchaLevel;

    /**
     * 验证码额外值0
     */
    private String captchaExtValue0;

    /**
     * 验证码额外值1
     */
    private String captchaExtValue1;

    /**
     * 验证码额外值2
     */
    private String captchaExtValue2;

    /**
     * 验证码有效期
     */
    private LocalDateTime captchaExpireTime;

    /**
     * 可用标识
     */
    private Integer usableFlag;

    /**
     * 使用时间
     */
    private LocalDateTime useTime;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 地址ip
     */
    private String addrIp;

    /**
     * 设备
     */
    private String device;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建用户
     */
    private String createUser;

    /**
     * 创建用户名称
     */
    private String createUserName;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新用户
     */
    private String updateUser;

    /**
     * 更新用户名称
     */
    private String updateUserName;


}
