package org.zj2.common.uac.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * AppOrgPlusDTO
 *
 * @author peijie.ye
 * @date 2023/1/7 12:28
 */
@Getter
@Setter
@NoArgsConstructor
public class AppOrgExtDTO extends AppOrgDTO {
    private static final long serialVersionUID = -1870168449246953800L;
    private String orgName;

}
