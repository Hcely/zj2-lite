package org.zj2.common.uac.org.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zj2.lite.service.entity.request.PageRequest;

/**
 * OrgEmployeeQuery
 *
 * @author peijie.ye
 * @date 2023/2/6 15:12
 */
@Getter
@Setter
@NoArgsConstructor
public class OrgEmployeeQuery extends PageRequest {
    private static final long serialVersionUID = -3989100865488824766L;
    private String orgCode;
    private String userMobile;
    private String userEmail;
    private String userName;
    private Integer enableFlag;
}
