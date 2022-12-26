package org.zj2.common.uac.org.service;

import org.zj2.common.uac.org.dto.OrgEmployeeDTO;
import org.zj2.common.uac.org.dto.req.OrgEmployeeAddReq;
import org.zj2.common.uac.org.dto.req.OrgEmployeeCreateReq;
import org.zj2.lite.service.BaseInnerService;

/**
 *  OrgEmployeeService
 *
 * @author peijie.ye
 * @date 2022/11/28 11:37
 */
public interface OrgEmployeeService extends BaseInnerService<OrgEmployeeDTO>, OrgEmployeeApi {
    /**
     * 添加职员
     * @param req
     * @return
     */
    OrgEmployeeDTO addEmployee(OrgEmployeeAddReq req);

    /**
     * 创建职员
     * @param req
     * @return
     */
    OrgEmployeeDTO createEmployee(OrgEmployeeCreateReq req);

    /**
     * 可用
     * @param orgEmployeeId
     */
    void enable(String orgEmployeeId);

    /**
     * 禁用
     * @param orgEmployeeId
     */
    void disable(String orgEmployeeId);

    /**
     * 可见
     * @param orgEmployeeId
     */
    void visible(String orgEmployeeId);

    /**
     * 隐藏
     * @param orgEmployeeId
     */
    void divisible(String orgEmployeeId);

    /**
     * 离职
     * @param orgEmployeeId
     */
    void quit(String orgEmployeeId);
}
