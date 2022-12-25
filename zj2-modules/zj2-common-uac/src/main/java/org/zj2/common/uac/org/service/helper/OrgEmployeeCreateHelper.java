package org.zj2.common.uac.org.service.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zj2.common.uac.org.dto.req.OrgEmployeeAddReq;
import org.zj2.common.uac.org.service.OrgEmployeeLogService;
import org.zj2.common.uac.org.service.OrgEmployeeService;
import org.zj2.common.uac.org.service.OrgService;
import org.zj2.common.uac.user.service.UserService;
import org.zj2.lite.helper.handler.BizVHandler;

/**
 *  OrgEmployeeAddHelper
 *
 * @author peijie.ye
 * @date 2022/12/25 1:08
 */
@Component
public class OrgEmployeeCreateHelper implements BizVHandler<OrgEmployeeAddReq> {
    @Autowired
    private OrgService orgService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrgEmployeeService orgEmployeeService;
    @Autowired
    private OrgEmployeeLogService orgEmployeeLogService;

    @Override
    public void handle(OrgEmployeeAddReq context) {
        
    }
}
