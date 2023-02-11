package org.zj2.common.uac.auth.server;

import org.zj2.lite.service.context.AuthContext;

/**
 * DataPropertyAuthorityHandler
 *
 * @author peijie.ye
 * @date 2023/1/2 22:53
 */
public interface AuthorizeHandler<T> {
    void authorize(AuthContext context, T value);
}
