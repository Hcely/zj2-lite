package org.zj2.common.uac.org.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 *  OrgQuery
 *
 * @author peijie.ye
 * @date 2023/1/10 18:42
 */
@Getter
@Setter
@NoArgsConstructor
public class OrgQuery implements Serializable {
    private static final long serialVersionUID = -177290171337353116L;
    private String orgCode;
    private String orgName;
}
