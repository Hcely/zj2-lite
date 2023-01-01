package org.zj2.common.sys.base.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.zj2.common.sys.base.service.SysNumRuleService;

/**
 *  SysNumRuleController
 *
 * @author peijie.ye
 * @date 2023/1/1 14:54
 */
@Tag(name = "编码规则模块")
@RestController
public class SysNumRuleController {
    @Autowired
    private SysNumRuleService sysNumRuleService;
}
