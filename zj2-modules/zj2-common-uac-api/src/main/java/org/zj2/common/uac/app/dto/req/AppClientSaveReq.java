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
public class AppClientSaveReq implements Serializable {
    private static final long serialVersionUID = 779083549736628009L;
    private String appCode;
    private String clientCode;
    private String clientName;
    private String namespace;
    private Long tokenTimeout;
}
