package org.zj2.common.sys.base.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zj2.lite.service.entity.request.PageRequest;

/**
 * SysNumRuleQuery
 *
 * @author peijie.ye
 * @date 2023/1/2 14:50
 */
@Getter
@Setter
@NoArgsConstructor
public class SysNumRuleQuery extends PageRequest {
    private static final long serialVersionUID = 455896141378245375L;
    private String numRuleCode;
    private String numRuleName;
}
