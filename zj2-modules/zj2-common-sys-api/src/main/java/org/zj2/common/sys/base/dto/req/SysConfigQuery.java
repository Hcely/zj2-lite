package org.zj2.common.sys.base.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zj2.lite.service.entity.request.PageRequest;

/**
 * SysConfigQuery
 *
 * @author peijie.ye
 * @date 2023/1/2 14:54
 */
@Getter
@Setter
@NoArgsConstructor
public class SysConfigQuery extends PageRequest {
    private static final long serialVersionUID = 6166844062806793167L;
    private String appCode;
    private String configCode;
    private String configName;
}
