package org.zj2.common.uac.auth.service.helper;

import org.zj2.common.uac.auth.dto.AuthContext;
import org.zj2.lite.helper.BizReference;
import org.zj2.lite.helper.CommonBizHelper;

/**
 *  AuthNamePwHelper
 *
 * @author peijie.ye
 * @date 2022/12/3 9:25
 */
@BizReference({//
        AuthNamePwHandler.class,//
        AuthUserCheckHandler.class,//
        AuthLocalAppOrgHandler.class,//
        AuthOrgEmployeeCheckHandler.class,//
        AuthAppOrgCheckHandler.class,//
        AuthAppUserCheckHandler.class,//
        AuthCreateTokenHandler.class//
})
public class AuthNamePwHelper extends CommonBizHelper<AuthContext> {
}
