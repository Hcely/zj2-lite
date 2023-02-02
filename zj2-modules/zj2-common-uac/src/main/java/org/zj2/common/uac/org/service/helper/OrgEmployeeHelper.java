package org.zj2.common.uac.org.service.helper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.zj2.common.uac.constant.UacNumRules;
import org.zj2.common.uac.org.constant.EmployeeEventEnum;
import org.zj2.common.uac.org.constant.EmployeeStatusEnum;
import org.zj2.common.uac.org.dto.OrgDTO;
import org.zj2.common.uac.org.dto.OrgEmployeeDTO;
import org.zj2.common.uac.org.dto.OrgEmployeeLogDTO;
import org.zj2.common.uac.org.service.OrgEmployeeLogService;
import org.zj2.common.uac.org.service.OrgEmployeeService;
import org.zj2.common.uac.org.service.OrgService;
import org.zj2.common.uac.org.service.dto.OrgEmployeeContext;
import org.zj2.common.uac.user.dto.UserDTO;
import org.zj2.common.uac.user.dto.req.UserCreateReq;
import org.zj2.common.uac.user.service.UserService;
import org.zj2.lite.common.util.DateUtil;
import org.zj2.lite.helper.handler.BizVHandler;
import org.zj2.lite.service.context.AuthenticationContext;
import org.zj2.lite.util.ZRBuilder;

import java.time.LocalDateTime;

/**
 *  OrgEmployeeAddHelper
 *
 * @author peijie.ye
 * @date 2022/12/25 1:08
 */
@Component
public class OrgEmployeeHelper implements BizVHandler<OrgEmployeeContext> {
    @Autowired
    private OrgService orgService;
    @Autowired
    private OrgEmployeeService orgEmployeeService;
    @Autowired
    private OrgEmployeeLogService orgEmployeeLogService;
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public void handle(OrgEmployeeContext context) {
        // 加载机构信息
        loadOrg(context);
        // 检查职员号是否唯一
        checkEmployeeNo(context);
        if (context.isAddMode()) {
            // 加载用户信息
            loadUserForAdd(context);
        } else {
            // 加载用户信息，如果不存在则创建用户
            loadUserForCreate(context);
        }
        checkUserEmployee(context);
        addEmployee(context);
        addEmployeeLog(context);
    }

    private void loadOrg(OrgEmployeeContext context) {
        String orgCode = context.getOrgCode();
        if (StringUtils.isEmpty(orgCode)) {
            orgCode = AuthenticationContext.currentOrgCode();
            context.setOrgCode(orgCode);
        }
        OrgDTO org = orgService.getByCode(orgCode);
        if (org == null) { throw ZRBuilder.failureErr("机构不存在"); }
        context.setOrg(org);
    }

    private void checkEmployeeNo(OrgEmployeeContext context) {
        String employeeNo = context.getEmployeeNo();
        if (StringUtils.isEmpty(employeeNo)) { return; }
        boolean exist = orgEmployeeService.existEmployeeNo(context.getOrgCode(), employeeNo);
        if (exist) { throw ZRBuilder.failureErr("职员号已存在"); }
    }

    private void loadUserForAdd(OrgEmployeeContext context) {
        UserDTO user = userService.getIfAbsentError(context.getUserId());
        context.setUser(user);
    }

    private void loadUserForCreate(OrgEmployeeContext context) {
        String userMobileAreaCode = context.getUserMobileAreaCode();
        String userMobile = context.getUserMobile();
        UserDTO user = userService.getByMobile(userMobileAreaCode, userMobile);
        if (user == null) {
            UserCreateReq createReq = new UserCreateReq();
            createReq.setUserMobileAreaCode(userMobileAreaCode);
            createReq.setUserMobile(userMobile);
            createReq.setUserPassword(StringUtils.substring(userMobile, StringUtils.length(userMobile) - 6));
            user = userService.create(createReq);
        }
        context.setUser(user);
    }

    private void checkUserEmployee(OrgEmployeeContext context) {
        UserDTO user = context.getUser();
        OrgEmployeeDTO employee = orgEmployeeService.getEmployee(context.getOrgCode(), user.getUserId());
        if (employee != null && EmployeeStatusEnum.WORKING.eq(employee.getEmployeeStatus())) {
            throw ZRBuilder.failureErr("用户已经在职");
        }
    }

    private void addEmployee(OrgEmployeeContext context) {
        UserDTO user = context.getUser();
        OrgEmployeeDTO employee = new OrgEmployeeDTO();
        String employeeNo = context.getEmployeeNo();
        if (StringUtils.isEmpty(employeeNo)) {
            employeeNo = nextEmployeeNo(context);
        }
        employee.setEmployeeNo(employeeNo);
        //
        employee.setUserId(user.getUserId());
        employee.setUserName(user.getUserName());
        employee.setVisibleFlag(1);
        employee.setEmployeeStatus(EmployeeStatusEnum.WORKING.getCode());
        employee.setEnableFlag(1);
        employee.setEnabledTime(DateUtil.now());
        LocalDateTime entryTime = context.getEntryTime();
        employee.setEntryTime(entryTime == null ? DateUtil.now() : entryTime);
        employee.setOrgCode(context.getOrgCode());
        orgEmployeeService.add(employee);
        context.setEmployee(employee);
    }

    private String nextEmployeeNo(OrgEmployeeContext context) {
        String orgCode = context.getOrgCode();
        for (int i = 0; i < 10000; ++i) {
            String employeeNo = UacNumRules.EMPLOYEE_NO_RULE.orgCode(orgCode).next();
            if (!orgEmployeeService.existEmployeeNo(orgCode, employeeNo)) {
                return employeeNo;
            }
        }
        throw ZRBuilder.failureErr("生成职员号失败");
    }

    private void addEmployeeLog(OrgEmployeeContext context) {
        OrgEmployeeDTO employee = context.getEmployee();
        OrgEmployeeLogDTO log = new OrgEmployeeLogDTO();
        log.setLogType(EmployeeEventEnum.ENTRY.getCode());
        log.setUserId(employee.getUserId());
        log.setUserName(employee.getUserName());
        log.setEmployeeId(employee.getEmployeeId());
        log.setEmployeeNo(employee.getEmployeeNo());
        log.setOrgCode(employee.getOrgCode());
        orgEmployeeLogService.add(log);
    }
}
