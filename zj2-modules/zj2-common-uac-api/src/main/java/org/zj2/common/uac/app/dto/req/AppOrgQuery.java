package org.zj2.common.uac.app.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zj2.lite.service.entity.request.PageRequest;

/**
 * AppOrgQuery
 *
 * @author peijie.ye
 * @date 2023/1/2 18:17
 */
@Getter
@Setter
@NoArgsConstructor
public class AppOrgQuery extends PageRequest {
    private static final long serialVersionUID = -3736815108228813601L;
    private String appCode;
    private String orgCode;
    private String orgName;
    private Integer enableFlag;
}
