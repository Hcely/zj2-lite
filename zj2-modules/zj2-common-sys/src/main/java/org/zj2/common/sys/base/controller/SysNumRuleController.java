package org.zj2.common.sys.base.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zj2.common.sys.base.dto.SysNumRuleDTO;
import org.zj2.common.sys.base.dto.req.SysNumRuleQuery;
import org.zj2.common.sys.base.service.SysNumRuleService;
import org.zj2.lite.common.entity.result.ZListResp;
import org.zj2.lite.common.entity.result.ZRBuilder;
import org.zj2.lite.common.entity.result.ZResp;
import org.zj2.lite.common.entity.result.ZResult;

/**
 *  SysNumRuleController
 *
 * @author peijie.ye
 * @date 2023/1/1 14:54
 */
@Tag(name = "单号规则模块")
@RestController
@RequestMapping("/api/sys/numRule")
public class SysNumRuleController {
    @Autowired
    private SysNumRuleService sysNumRuleService;

    @Operation(tags = "单号规则模块", summary = "保存单号规则", description = "有则更新无则创建")
    @PostMapping("/save")
    public ZResp<SysNumRuleDTO> save(@RequestBody SysNumRuleDTO numRule) {
        SysNumRuleDTO result = sysNumRuleService.saveRule(numRule);
        return ZRBuilder.successResp(result);
    }

    @Operation(tags = "单号规则模块", summary = "单号规则查询接口")
    @PostMapping("/pageQuery")
    public ZListResp<SysNumRuleDTO> pageQuery(@RequestBody SysNumRuleQuery query) {
        return sysNumRuleService.pageQuery(query);
    }

    @Operation(tags = "单号规则模块", summary = "获取单号规则")
    @GetMapping("/{numRuleCode}")
    public ZResp<SysNumRuleDTO> get(@PathVariable String numRuleCode) {
        SysNumRuleDTO result = sysNumRuleService.getRule(numRuleCode);
        return ZRBuilder.successResp(result);
    }

    @Operation(tags = "单号规则模块", summary = "删除单号规则")
    @PostMapping("/remove/{numRuleCode}")
    public ZResult remove(@PathVariable String numRuleCode) {
        sysNumRuleService.getRule(numRuleCode);
        return ZRBuilder.successResult();
    }
}
