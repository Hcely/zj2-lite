package org.zj2.common.uac.auth.service;

import org.zj2.lite.service.ApiReference;
import org.zj2.lite.service.auth.AuthoritySet;

/**
 * AuthorityApi
 *
 * @author peijie.ye
 * @date 2023/1/2 19:57
 */
@ApiReference
public interface TokenAuthorityApi {
    AuthoritySet getAuthorities(String tokenId);
}
