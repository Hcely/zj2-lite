package org.zj2.common.uac.org.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 *  OrgCreateReq
 *
 * @author peijie.ye
 * @date 2022/12/9 12:16
 */
@Getter
@Setter
@NoArgsConstructor
public class OrgCreateReq implements Serializable {
    private static final long serialVersionUID = 20221209121656L;
    /**
     * 机构编码
     */
    private String orgCode;

    /**
     * 机构名称
     */
    private String orgName;
}
