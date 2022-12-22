package org.zj2.common.sys.base.service.helper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.zj2.common.sys.base.dto.SequenceNextContext;
import org.zj2.lite.service.context.AuthenticationContext;
import org.zj2.lite.helper.handler.BizVHandler;

/**
 *  SequenceBakHandler
 *
 * @author peijie.ye
 * @date 2022/12/11 23:57
 */
@Component
public class SequencePrepareHandler implements BizVHandler<SequenceNextContext> {
    @Override
    public void handle(SequenceNextContext context) {
        context.addParam("date", context.getDate());
        context.addParam("now", context.getDate());
        context.addParam("currentDate", context.getDate());
        String appCode = context.getAppCode();
        if (StringUtils.isEmpty(appCode)) {appCode = AuthenticationContext.currentAppCode();}
        context.setAppCode(appCode);
        context.addParam("app", appCode);
        context.addParam("appCode", appCode);
        String orgCode = AuthenticationContext.currentOrgCode();
        context.addParam("org", orgCode);
        context.addParam("orgCode", orgCode);
    }
}
