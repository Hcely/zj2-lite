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
import org.zj2.common.sys.base.dto.SysConfigDTO;
import org.zj2.common.sys.base.dto.req.SysConfigQuery;
import org.zj2.common.sys.base.dto.req.SysConfigSaveReq;
import org.zj2.common.sys.base.service.SysConfigService;
import org.zj2.lite.common.entity.result.ZListResp;
import org.zj2.lite.common.entity.result.ZResp;
import org.zj2.lite.common.entity.result.ZResult;
import org.zj2.lite.util.ZRBuilder;

/**
 *  SysConfigController
 *
 * @author peijie.ye
 * @date 2022/12/28 21:54
 */
@Tag(name = "sys-系统配置模块")
@RestController
@RequestMapping("/api/sys/config")
public class SysConfigController {
    @Autowired
    private SysConfigService sysConfigService;

    @Operation(tags = "sys-系统配置模块", summary = "保存系统配置接口", description = "创建或编辑系统配置")
    @PostMapping("/save")
    public ZResp<SysConfigDTO> save(@RequestBody SysConfigSaveReq req) {
        SysConfigDTO config = sysConfigService.saveConfig(req);
        return ZRBuilder.successResp(config);
    }

    @Operation(tags = "sys-系统配置模块", summary = "查询系统配置接口")
    @PostMapping("/pageQuery")
    public ZListResp<SysConfigDTO> pageQuery(@RequestBody SysConfigQuery query) {
        return sysConfigService.pageQuery(query);
    }

    @Operation(tags = "sys-系统配置模块", summary = "获取系统配置接口")
    @GetMapping("/{sysConfigId}")
    public ZResp<SysConfigDTO> get(@PathVariable String sysConfigId) {
        SysConfigDTO config = sysConfigService.get(sysConfigId);
        return ZRBuilder.successResp(config);
    }

    @Operation(tags = "sys-系统配置模块", summary = "删除系统配置接口")
    @PostMapping("/remove/{sysConfigId}")
    public ZResult remove(@PathVariable String sysConfigId) {
        sysConfigService.removeConfig(sysConfigId);
        return ZRBuilder.successResult();
    }
}
