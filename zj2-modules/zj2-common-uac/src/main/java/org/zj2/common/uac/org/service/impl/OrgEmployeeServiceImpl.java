package org.zj2.common.uac.org.service.impl;

import org.springframework.stereotype.Service;
import org.zj2.common.uac.org.dto.OrgEmployeeDTO;
import org.zj2.common.uac.org.dto.req.OrgEmployeeAddReq;
import org.zj2.common.uac.org.dto.req.OrgEmployeeCreateReq;
import org.zj2.common.uac.org.entity.OrgEmployee;
import org.zj2.common.uac.org.mapper.OrgEmployeeMapper;
import org.zj2.common.uac.org.service.OrgEmployeeService;
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
    @Override
    public OrgEmployeeDTO getEmployee(String orgCode, String userId) {
        return getOne(wrapper().eq(OrgEmployeeDTO::getOrgCode, orgCode).eq(OrgEmployeeDTO::getUserId, userId)
                .orderByAsc(OrgEmployeeDTO::getEmployeeStatus));
    }

    @Override
    public OrgEmployeeDTO addEmployee(OrgEmployeeAddReq req) {
        return null;
    }

    @Override
    public OrgEmployeeDTO createEmployee(OrgEmployeeCreateReq req) {
        return null;
    }

    @Override
    public void enable(String orgEmployeeId) {

    }

    @Override
    public void disable(String orgEmployeeId) {

    }

    @Override
    public void quit(String orgEmployeeId) {

    }
}
