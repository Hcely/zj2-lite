package org.zj2.common.uac.app.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * AppOrgAddReq
 *
 * @author peijie.ye
 * @date 2023/2/6 16:04
 */
@Getter
@Setter
@NoArgsConstructor
public class AppUserAddReq implements Serializable {
    private static final long serialVersionUID = 6540981089181255892L;
    private String appCode;
    private String userId;
}
