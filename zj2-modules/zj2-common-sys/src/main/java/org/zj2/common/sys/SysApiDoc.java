package org.zj2.common.sys;

import org.springframework.stereotype.Component;
import org.zj2.lite.service.ApiDoc;

/**
 * ApiDocInfo
 *
 * @author peijie.ye
 * @date 2023/2/16 12:56
 */
@Component
public class SysApiDoc implements ApiDoc {
    @Override
    public String getGroupName() {
        return "系统模块";
    }

    @Override
    public String getBasePackage() {
        return this.getClass().getPackageName();
    }
}
