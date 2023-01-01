package org.zj2.common.uac.auth.service.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zj2.common.uac.auth.dto.AuthContext;
import org.zj2.common.uac.org.constant.EmployeeStatusEnum;
import org.zj2.common.uac.org.dto.OrgDTO;
import org.zj2.common.uac.org.dto.OrgEmployeeDTO;
import org.zj2.common.uac.org.service.OrgEmployeeService;
import org.zj2.common.uac.user.dto.UserDTO;
import org.zj2.lite.common.Supportable;
import org.zj2.lite.common.entity.result.ZRBuilder;
import org.zj2.lite.common.util.BooleanUtil;
import org.zj2.lite.helper.handler.BizVHandler;

/**
 *  AuthUserCheckHandler
 *
 * @author peijie.ye
 * @date 2022/12/3 7:59
 */
@Component
public class AuthOrgEmployeeCheckHandler implements Supportable<AuthContext>, BizVHandler<AuthContext> {
    @Autowired
    private OrgEmployeeService orgEmployeeService;

    @Override
    public boolean supports(AuthContext context) {
        return context.getOrg() != null;
    }

    @Override
    public void handle(AuthContext context) {
        UserDTO user = context.getUser();
        OrgDTO org = context.getOrg();
        OrgEmployeeDTO employee = orgEmployeeService.getEmployee(org.getOrgCode(), user.getUserId());
        if (employee == null) {
            throw ZRBuilder.failureErr("非机构职员");
        } else if (!EmployeeStatusEnum.WORKING.eq(employee.getEmployeeStatus())) {
            throw ZRBuilder.failureErr("非机构职员");
        } else if (BooleanUtil.isFalse(employee.getEnableFlag())) {
            throw ZRBuilder.failureErr("机构职员账号停用");
        }
    }
}
