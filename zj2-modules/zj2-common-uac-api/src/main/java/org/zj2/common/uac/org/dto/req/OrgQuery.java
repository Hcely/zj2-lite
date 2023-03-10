package org.zj2.common.uac.org.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zj2.lite.service.entity.request.PageRequest;

/**
 * OrgQuery
 *
 * @author peijie.ye
 * @date 2023/1/10 18:42
 */
@Getter
@Setter
@NoArgsConstructor
public class OrgQuery extends PageRequest {
    private static final long serialVersionUID = -177290171337353116L;
    private String orgCode;
    private String orgName;
}
