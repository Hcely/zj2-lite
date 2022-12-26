package org.zj2.common.sys.base.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 *  SysConfigEditReq
 *
 * @author peijie.ye
 * @date 2022/12/12 14:56
 */
@Getter
@Setter
@NoArgsConstructor
public class SysConfigEditReq implements Serializable {
    private static final long serialVersionUID = 20221212145654L;
    /**
     *
     */
    private String appCode;
    /**
     *
     */
    private String configCode;
    /**
     * 系统配置名称
     */
    private String configName;
    /**
     * 配置值
     */
    private String configValue;

    /**
     * 配置额外值0
     */
    private String configExtValue0;

    /**
     * 配置额外值1
     */
    private String configExtValue1;
}
