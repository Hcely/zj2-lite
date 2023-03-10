package org.zj2.common.uac.user.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * UserOperationReq
 *
 * @author peijie.ye
 * @date 2022/12/3 5:33
 */
@Getter
@Setter
@NoArgsConstructor
public class UserOperationReq implements Serializable {
    private static final long serialVersionUID = 20221203053316L;
    private String userId;
    private String remark;
    private LocalDateTime expireTime;
}
