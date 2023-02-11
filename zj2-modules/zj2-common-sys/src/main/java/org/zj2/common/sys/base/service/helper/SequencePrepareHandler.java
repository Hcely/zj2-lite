package org.zj2.common.sys.base.service.helper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.zj2.common.sys.base.dto.NumNextContext;
import org.zj2.lite.helper.handler.BizVHandler;
import org.zj2.lite.service.context.AuthContext;

/**
 *  SequenceBakHandler
 *
 * @author peijie.ye
 * @date 2022/12/11 23:57
 */
@Component
public class SequencePrepareHandler implements BizVHandler<NumNextContext> {
    @Override
    public void handle(NumNextContext context) {
        context.putParam("date", context.getDate());
        context.putParam("now", context.getDate());
        context.putParam("currentDate", context.getDate());
        putAppCode(context);
        putOrgCode(context);
    }

    private void putAppCode(NumNextContext context) {
        String appCode = context.getAppCode();
        if (StringUtils.isEmpty(appCode)) {
            appCode = AuthContext.currentAppCode();
            context.setAppCode(appCode);
        }
        if (StringUtils.isEmpty(appCode)) { return; }
        context.putParam("app", appCode);
        context.putParam("appCode", appCode);
    }

    private void putOrgCode(NumNextContext context) {
        String orgCode = context.getOrgCode();
        if (StringUtils.isEmpty(orgCode)) {
            orgCode = AuthContext.currentOrgCode();
            context.setOrgCode(orgCode);
        }
        if (StringUtils.isEmpty(orgCode)) { return; }
        if (!context.hasParam("org")) { context.putParam("org", orgCode); }
        if (!context.hasParam("orgCode")) { context.putParam("orgCode", orgCode); }
    }
}
