package org.zj2.common.uac.org.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zj2.common.uac.org.constant.EmployeeStatusEnum;
import org.zj2.common.uac.org.dto.OrgEmployeeDTO;
import org.zj2.common.uac.org.dto.req.OrgEmployeeAddReq;
import org.zj2.common.uac.org.dto.req.OrgEmployeeCreateReq;
import org.zj2.common.uac.org.entity.OrgEmployee;
import org.zj2.common.uac.org.mapper.OrgEmployeeMapper;
import org.zj2.common.uac.org.service.OrgEmployeeService;
import org.zj2.common.uac.org.service.dto.OrgEmployeeContext;
import org.zj2.common.uac.org.service.helper.OrgEmployeeHelper;
import org.zj2.lite.service.BaseServiceImpl;

/**
 *  OrgEmployeeServiceImpl
 *
 * @author peijie.ye
 * @date 2022/11/27 20:40
 */
@Service
public class OrgEmployeeServiceImpl extends BaseServiceImpl<OrgEmployeeMapper, OrgEmployee, OrgEmployeeDTO>
        implements OrgEmployeeService {
    @Autowired
    private OrgEmployeeHelper orgEmployeeHelper;

    @Override
    public OrgEmployeeDTO getEmployee(String orgCode, String userId) {
        return getOne(wrapper().eq(OrgEmployeeDTO::getOrgCode, orgCode).eq(OrgEmployeeDTO::getUserId, userId)
                .orderByAsc(OrgEmployeeDTO::getEmployeeStatus));
    }

    @Override
    public boolean existEmployeeNo(String orgCode, String employeeNo) {
        return exists(wrapper().eq(OrgEmployeeDTO::getOrgCode, orgCode).eq(OrgEmployeeDTO::getEmployeeNo, employeeNo));
    }

    @Override
    public OrgEmployeeDTO addEmployee(OrgEmployeeAddReq req) {
        OrgEmployeeContext context = new OrgEmployeeContext(req);
        orgEmployeeHelper.handle(context);
        return context.getEmployee();
    }

    @Override
    public OrgEmployeeDTO createEmployee(OrgEmployeeCreateReq req) {
        OrgEmployeeContext context = new OrgEmployeeContext(req);
        orgEmployeeHelper.handle(context);
        return context.getEmployee();
    }

    @Override
    public void enable(String orgEmployeeId) {

    }

    @Override
    public void disable(String orgEmployeeId) {

    }

    @Override
    public void visible(String orgEmployeeId) {

    }

    @Override
    public void divisible(String orgEmployeeId) {

    }

    @Override
    public void quit(String orgEmployeeId) {

    }
}
