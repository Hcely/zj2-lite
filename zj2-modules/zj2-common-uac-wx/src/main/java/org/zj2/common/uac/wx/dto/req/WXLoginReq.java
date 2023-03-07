package org.zj2.common.uac.wx.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * WXLoginReq
 *
 * @author peijie.ye
 * @date 2023/3/5 20:28
 */
@Getter
@Setter
@NoArgsConstructor
public class WXLoginReq implements Serializable {
    private static final long serialVersionUID = -603173073356653463L;
    private String wxAppId;
    private String code;

}
