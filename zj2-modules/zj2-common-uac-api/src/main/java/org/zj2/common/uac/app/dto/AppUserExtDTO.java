package org.zj2.common.uac.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zj2.lite.service.auth.AuthorityResource;

/**
 * AppUserPlusDTO
 *
 * @author peijie.ye
 * @date 2023/1/7 12:48
 */
@Getter
@Setter
@NoArgsConstructor
public class AppUserExtDTO extends AppUserDTO {
    private static final long serialVersionUID = -8367967494174773801L;
    @AuthorityResource
    private String userName;
}
