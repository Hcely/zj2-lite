package org.zj2.common.uac.org.service;

import org.zj2.common.uac.org.dto.OrgEmployeeDTO;
import org.zj2.common.uac.org.dto.OrgEmployeeExtDTO;
import org.zj2.common.uac.org.dto.req.OrgEmployeeAddReq;
import org.zj2.common.uac.org.dto.req.OrgEmployeeCreateReq;
import org.zj2.common.uac.org.dto.req.OrgEmployeeQuery;
import org.zj2.common.uac.org.dto.req.OrgEmployeeQuitReq;
import org.zj2.lite.common.entity.result.ZListResp;
import org.zj2.lite.service.BaseInnerService;

import java.time.LocalDateTime;

/**
 * OrgEmployeeService
 *
 * @author peijie.ye
 * @date 2022/11/28 11:37
 */
public interface OrgEmployeeService extends BaseInnerService<OrgEmployeeDTO>, OrgEmployeeApi {

    OrgEmployeeDTO getIfAbsentError(String employeeId);

    boolean existEmployeeNo(String orgCode, String employeeNo);

    /**
     * 添加职员
     *
     * @param req
     * @return
     */
    OrgEmployeeDTO addEmployee(OrgEmployeeAddReq req);

    /**
     * 创建职员
     *
     * @param req
     * @return
     */
    OrgEmployeeDTO createEmployee(OrgEmployeeCreateReq req);

    /**
     * 可用
     *
     * @param employeeId
     */
    void enable(String employeeId);

    /**
     * 禁用
     *
     * @param employeeId
     */
    void disable(String employeeId);

    /**
     * 可见
     *
     * @param employeeId
     */
    void visible(String employeeId);

    /**
     * 隐藏
     *
     * @param employeeId
     */
    void divisible(String employeeId);

    /**
     * 离职
     */
    void quit(OrgEmployeeQuitReq req);


    ZListResp<OrgEmployeeExtDTO> pageQuery(OrgEmployeeQuery query);
}
