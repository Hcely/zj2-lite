package org.zj2.common.uac.auth.service;

import org.zj2.common.uac.auth.dto.UserAuthorityResources;
import org.zj2.lite.service.ApiReference;

/**
 *  AuthorityApi
 *
 * @author peijie.ye
 * @date 2023/1/2 19:57
 */
@ApiReference
public interface AuthorityApi {
    UserAuthorityResources getUserAuthorities(String appCode, String orgCode, String userId);

    UserAuthorityResources getTempAuthorities(String appCode, String orgCode, String userId);
}
