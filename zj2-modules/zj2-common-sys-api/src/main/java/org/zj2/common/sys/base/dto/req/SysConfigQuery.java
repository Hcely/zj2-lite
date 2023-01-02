package org.zj2.common.sys.base.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zj2.lite.service.entity.request.PageRequest;

/**
 *  SysConfigQuery
 *
 * @author peijie.ye
 * @date 2023/1/2 14:54
 */
@Getter
@Setter
@NoArgsConstructor
public class SysConfigQuery extends PageRequest {
    private static final long serialVersionUID = 6166844062806793167L;
    @Schema(description = "应用编码")
    private String appCode;
    @Schema(description = "系统配置编码")
    private String configCode;
    @Schema(description = "系统配置名称")
    private String configName;
}
