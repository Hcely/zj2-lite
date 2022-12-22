package org.zj2.common.uac.org.service;

import org.zj2.common.uac.org.dto.OrgEmployeeDTO;
import org.zj2.common.uac.org.dto.req.OrgEmployeeAddReq;
import org.zj2.common.uac.org.dto.req.OrgEmployeeCreateReq;
import org.zj2.lite.service.BaseInnerService;

/**
 *  UserLogService
 *
 * @author peijie.ye
 * @date 2022/11/28 11:37
 */
public interface OrgEmployeeService extends BaseInnerService<OrgEmployeeDTO>, OrgEmployeeApi {
    /**
     *
     * @param req
     * @return
     */
    OrgEmployeeDTO addEmployee(OrgEmployeeAddReq req);


    /**
     *
     */
    OrgEmployeeDTO createEmployee(OrgEmployeeCreateReq req);

    /**
     *
     * @param orgEmployeeId
     */
    void enable(String orgEmployeeId);

    /**
     *
     * @param orgEmployeeId
     */
    void disable(String orgEmployeeId);

    /**
     *
     * @param orgEmployeeId
     */
    void quit(String orgEmployeeId);
}
