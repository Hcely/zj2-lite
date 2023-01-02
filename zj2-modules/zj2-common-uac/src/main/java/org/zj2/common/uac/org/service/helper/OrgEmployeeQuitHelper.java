package org.zj2.common.uac.org.service.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zj2.common.uac.org.constant.EmployeeEventEnum;
import org.zj2.common.uac.org.constant.EmployeeStatusEnum;
import org.zj2.common.uac.org.dto.OrgEmployeeDTO;
import org.zj2.common.uac.org.dto.OrgEmployeeLogDTO;
import org.zj2.common.uac.org.service.OrgEmployeeLogService;
import org.zj2.common.uac.org.service.OrgEmployeeService;
import org.zj2.lite.common.util.DateUtil;

import java.time.LocalDateTime;

/**
 *  OrgEmployeeQuitHelper
 *
 * @author peijie.ye
 * @date 2023/1/1 21:10
 */
@Component
public class OrgEmployeeQuitHelper {
    @Autowired
    private OrgEmployeeService orgEmployeeService;
    @Autowired
    private OrgEmployeeLogService orgEmployeeLogService;

    public void handle(String employeeId, LocalDateTime quitTime) {
        OrgEmployeeDTO employee = orgEmployeeService.getIfAbsentError(employeeId);
        if (EmployeeStatusEnum.WORKING.eq(employee.getEmployeeStatus())) {
            updateEmployee(employeeId, quitTime);
            addLog(employee);
        }
    }

    private void updateEmployee(String employeeId, LocalDateTime quitTime) {
        OrgEmployeeDTO update = new OrgEmployeeDTO();
        update.setEmployeeId(employeeId);
        update.setEmployeeStatus(EmployeeStatusEnum.QUIT.getCode());
        update.setQuitTime(quitTime == null ? DateUtil.now() : quitTime);
        orgEmployeeService.updateById(update);
    }

    private void addLog(OrgEmployeeDTO employee) {
        OrgEmployeeLogDTO log = new OrgEmployeeLogDTO();
        log.setLogType(EmployeeEventEnum.QUIT.getCode());
        log.setUserId(employee.getUserId());
        log.setUserName(employee.getUserName());
        log.setEmployeeId(employee.getEmployeeId());
        log.setEmployeeNo(employee.getEmployeeNo());
        log.setOrgCode(employee.getOrgCode());
        orgEmployeeLogService.add(log);
    }

}
