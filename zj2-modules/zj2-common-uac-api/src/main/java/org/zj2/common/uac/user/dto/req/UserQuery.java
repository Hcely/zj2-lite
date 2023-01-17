package org.zj2.common.uac.user.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zj2.lite.service.entity.request.PageRequest;

/**
 *  UserQuery
 *
 * @author peijie.ye
 * @date 2023/1/2 18:13
 */
@Getter
@Setter
@NoArgsConstructor
public class UserQuery extends PageRequest {
    private static final long serialVersionUID = 8771726132728176145L;
    private String mobile;
    private String userName;
}
