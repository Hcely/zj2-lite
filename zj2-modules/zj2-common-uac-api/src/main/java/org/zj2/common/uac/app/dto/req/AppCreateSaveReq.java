package org.zj2.common.uac.app.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * AppEditReq
 *
 * @author peijie.ye
 * @date 2022/12/26 14:43
 */
@Getter
@Setter
@NoArgsConstructor
public class AppCreateSaveReq implements Serializable {
    private static final long serialVersionUID = 779083549736628009L;
    private String appCode;
    private String appName;
    private Integer allowAllUser;
    private Integer singleSignOn;
    private Long tokenTimeout;
}
