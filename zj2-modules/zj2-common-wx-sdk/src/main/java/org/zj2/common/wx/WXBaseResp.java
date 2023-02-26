package org.zj2.common.wx;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * WXBaseResp
 *
 * @author peijie.ye
 * @date 2023/2/23 14:03
 */
@Getter
@Setter
@NoArgsConstructor
public class WXBaseResp implements Serializable {
    private static final long serialVersionUID = -6809434917679621985L;
    private Integer errcode;
    private String errmsg;

    public boolean isSuccess() {
        return errcode == null || errcode == 0;
    }
}
