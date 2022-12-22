package org.zj2.common.uac.app.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.zj2.lite.common.annotation.CryptProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 应用表
 * </p>
 *
 * @author peijie.ye
 * @since 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uac_app_client")
public class AppClient implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用id
     */
    @TableId(value = "app_client_id", type = IdType.ASSIGN_ID)
    private String appClientId;

    /**
     * 应用编码
     */
    private String appCode;

    /**
     * 应用编码
     */
    private String clientCode;

    /**
     * 应用名称
     */
    private String clientName;

    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 应用配置
     */
    private JSONObject clientConfig;

    /**
     * 应用状态
     */
    private Integer enableFlag;

    /**
     * 启用时间
     */
    private LocalDateTime enabledTime;

    /**
     * 禁用时间
     */
    private LocalDateTime disabledTime;

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
