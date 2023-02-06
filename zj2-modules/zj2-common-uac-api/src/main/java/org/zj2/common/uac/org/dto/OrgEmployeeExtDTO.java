package org.zj2.common.uac.org.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zj2.lite.common.annotation.CryptProperty;
import org.zj2.lite.service.auth.AuthorityResource;

/**
 * OrgEmployeeExtDTO
 *
 * @author peijie.ye
 * @date 2023/2/6 15:15
 */
@Getter
@Setter
@NoArgsConstructor
public class OrgEmployeeExtDTO extends OrgEmployeeDTO {
    private static final long serialVersionUID = -8997859957119205630L;
    @AuthorityResource
    @CryptProperty
    private String userMobile;
    @AuthorityResource
    @CryptProperty
    private String userEmail;
}
