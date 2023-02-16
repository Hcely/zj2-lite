package org.zj2.common.uac.auth.util;

import org.zj2.lite.common.entity.result.ZError;
import org.zj2.lite.util.ZRBuilder;

/**
 * AppUtil
 *
 * @author peijie.ye
 * @date 2022/12/9 2:12
 */
public class AuthUtil {
    public static ZError unAuthenticationErr(String msg) {
        return ZRBuilder.failureErr(msg).setStatus(401);
    }

    public static ZError unAuthorityErr(String msg) {
        return ZRBuilder.failureErr(msg).setStatus(403);
    }
}
