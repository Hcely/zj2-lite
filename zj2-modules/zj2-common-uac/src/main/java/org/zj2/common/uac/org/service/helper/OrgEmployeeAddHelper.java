package org.zj2.common.uac.org.service.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zj2.common.uac.org.dto.OrgDTO;
import org.zj2.common.uac.org.dto.OrgEmployeeDTO;
import org.zj2.common.uac.org.dto.OrgEmployeeLogDTO;
import org.zj2.common.uac.org.dto.req.OrgEmployeeAddReq;
import org.zj2.common.uac.org.service.OrgEmployeeLogService;
import org.zj2.common.uac.org.service.OrgEmployeeService;
import org.zj2.common.uac.org.service.OrgService;
import org.zj2.common.uac.user.dto.UserDTO;
import org.zj2.common.uac.user.service.UserService;
import org.zj2.lite.common.entity.result.ZRBuilder;
import org.zj2.lite.helper.handler.BizVHandler;

/**
 *  OrgEmployeeAddHelper
 *
 * @author peijie.ye
 * @date 2022/12/25 1:08
 */
@Component
public class OrgEmployeeAddHelper implements BizVHandler<OrgEmployeeAddReq> {
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
        OrgDTO org = orgService.getByCode(context.getOrgCode());
        if (org == null) {throw ZRBuilder.failureErr("机构不存在");}
        UserDTO user = userService.get(context.getUserId());
        if (user == null) {throw ZRBuilder.failureErr("用户不存在");}
        OrgEmployeeDTO employee = orgEmployeeService.getEmployee(org.getOrgCode(), user.getUserId());

    }
}
