package org.zj2.common.sys.base.service.helper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.zj2.common.sys.base.dto.NumNextContext;
import org.zj2.lite.service.context.AuthenticationContext;
import org.zj2.lite.helper.handler.BizVHandler;

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
        String appCode = context.getAppCode();
        if (StringUtils.isEmpty(appCode)) {appCode = AuthenticationContext.currentAppCode();}
        context.setAppCode(appCode);
        context.putParam("app", appCode);
        context.putParam("appCode", appCode);
        String orgCode = AuthenticationContext.currentOrgCode();
        context.putParam("org", orgCode);
        context.putParam("orgCode", orgCode);
    }
}
