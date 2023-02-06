package org.zj2.common.uac.org.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *  OrgEmployeeCreateReq
 *
 * @author peijie.ye
 * @date 2022/12/9 22:09
 */
@Getter
@Setter
@NoArgsConstructor
public class OrgEmployeeQuitReq implements Serializable {
    private static final long serialVersionUID = 20221209220953L;
    private String employeeId;
    private LocalDateTime quitTime;
}
