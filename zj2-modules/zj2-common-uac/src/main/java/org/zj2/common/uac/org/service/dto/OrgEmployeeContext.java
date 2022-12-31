package org.zj2.common.uac.org.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zj2.common.uac.org.dto.OrgDTO;
import org.zj2.common.uac.org.dto.OrgEmployeeDTO;
import org.zj2.common.uac.org.dto.req.OrgEmployeeAddReq;
import org.zj2.common.uac.org.dto.req.OrgEmployeeCreateReq;
import org.zj2.common.uac.user.dto.UserDTO;

import java.time.LocalDateTime;

/**
 *  OrgEmployeeContext
 *
 * @author peijie.ye
 * @date 2022/12/28 22:38
 */
@Getter
@Setter
public class OrgEmployeeContext {
    private boolean addMode;
    private String orgCode;
    private String userId;
    private String userName;
    private String userMobileAreaCode;
    private String userMobile;
    private String employeeNo;
    private LocalDateTime entryTime;
    //
    private OrgDTO org;
    private UserDTO user;
    private OrgEmployeeDTO employee;

    public OrgEmployeeContext(OrgEmployeeAddReq req) {
        this.addMode = true;
        this.orgCode = req.getOrgCode();
        this.userId = req.getUserId();
        this.employeeNo = req.getEmployeeNo();
        this.entryTime = req.getEntryTime();
    }

    public OrgEmployeeContext(OrgEmployeeCreateReq req) {
        this.addMode = false;
        this.orgCode = req.getOrgCode();
        this.userName = req.getUserName();
        this.userMobileAreaCode = req.getUserMobileAreaCode();
        this.userMobile = req.getUserMobile();
        this.employeeNo = req.getEmployeeNo();
        this.entryTime = req.getEntryTime();
    }
}
