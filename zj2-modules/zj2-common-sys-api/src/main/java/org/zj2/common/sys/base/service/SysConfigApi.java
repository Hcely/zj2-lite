package org.zj2.common.sys.base.service;

import org.zj2.common.sys.base.dto.SysConfigDTO;
import org.zj2.lite.service.ApiReference;

/**
 *  SysConfigApi
 *
 * @author peijie.ye
 * @date 2022/12/10 2:23
 */
@ApiReference
public interface SysConfigApi {
    SysConfigDTO get(String appCode, String configCode);
}
