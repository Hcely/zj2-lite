package org.zj2.common.sys.base.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zj2.lite.service.entity.request.PageRequest;

/**
 *  SysNumRuleQuery
 *
 * @author peijie.ye
 * @date 2023/1/2 14:50
 */
@Getter
@Setter
@NoArgsConstructor
public class SysNumRuleQuery extends PageRequest {
    private static final long serialVersionUID = 455896141378245375L;
    @Schema(description = "单号规则编码")
    private String numRuleCode;
    @Schema(description = "单号规则名称")
    private String numRuleName;
}
