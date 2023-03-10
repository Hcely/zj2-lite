package org.zj2.common.uac.org.service.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zj2.common.uac.org.constant.EmployeeEventEnum;
import org.zj2.common.uac.org.dto.OrgEmployeeDTO;
import org.zj2.common.uac.org.dto.OrgEmployeeLogDTO;
import org.zj2.common.uac.org.service.OrgEmployeeLogService;
import org.zj2.common.uac.org.service.OrgEmployeeService;
import org.zj2.lite.common.util.BooleanUtil;

/**
 * OrgEmployeeVisibleHelper
 *
 * @author peijie.ye
 * @date 2023/1/1 21:10
 */
@Component
public class OrgEmployeeEnableHelper {
    @Autowired
    private OrgEmployeeService orgEmployeeService;
    @Autowired
    private OrgEmployeeLogService orgEmployeeLogService;

    public void handle(String employeeId, boolean enable) {
        OrgEmployeeDTO employee = orgEmployeeService.getIfAbsentError(employeeId);
        if(BooleanUtil.isTrue(employee.getEnableFlag()) != enable) {
            updateEmployee(employeeId, enable);
            addLog(employee, enable);
        }
    }

    private void updateEmployee(String employeeId, boolean visible) {
        OrgEmployeeDTO update = new OrgEmployeeDTO();
        update.setEmployeeId(employeeId);
        update.setEnableFlag(visible ? 1 : 0);
        orgEmployeeService.updateById(update);
    }

    private void addLog(OrgEmployeeDTO employee, boolean enable) {
        OrgEmployeeLogDTO log = new OrgEmployeeLogDTO();
        log.setLogType(enable ? EmployeeEventEnum.ENABLED.getCode() : EmployeeEventEnum.DISABLED.getCode());
        log.setUserId(employee.getUserId());
        log.setUserName(employee.getUserName());
        log.setEmployeeId(employee.getEmployeeId());
        log.setEmployeeNo(employee.getEmployeeNo());
        log.setOrgCode(employee.getOrgCode());
        orgEmployeeLogService.add(log);
    }

}
