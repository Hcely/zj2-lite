package org.zj2.common.uac.app.constant;

import org.zj2.lite.common.entity.JSONKey;

/**
 *  AppConfigs
 *
 * @author peijie.ye
 * @date 2022/12/4 0:51
 */
public interface AppConfigs {//NOSONAR
    JSONKey<Boolean> allowAllUser = JSONKey.booleanParam("allowAllUser", false);
    JSONKey<Boolean> onlyToken = JSONKey.booleanParam("onlyToken", false);
    JSONKey<Long> tokenTimeout = JSONKey.longParam("tokenTimeout", 3600000L * 4);
}
