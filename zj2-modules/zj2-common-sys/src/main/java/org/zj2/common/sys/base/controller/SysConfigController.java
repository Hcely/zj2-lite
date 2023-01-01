package org.zj2.common.sys.base.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zj2.common.sys.base.dto.SysConfigDTO;
import org.zj2.common.sys.base.dto.req.SysConfigEditReq;
import org.zj2.common.sys.base.service.SysConfigService;
import org.zj2.lite.common.entity.result.ZRBuilder;
import org.zj2.lite.common.entity.result.ZResp;

/**
 *  SysConfigController
 *
 * @author peijie.ye
 * @date 2022/12/28 21:54
 */
@Tag(name = "系统配置模块")
@RestController
@RequestMapping("/api/sys/config")
public class SysConfigController {
    @Autowired
    private SysConfigService sysConfigService;

    @Operation(tags = "系统配置模块", summary = "编辑配置接口", description = "创建或编辑系统配置")
    @PostMapping("/edit")
    public ZResp<SysConfigDTO> edit(@RequestBody SysConfigEditReq req) {
        SysConfigDTO config = sysConfigService.edit(req);
        return ZRBuilder.successResp(config);
    }

}
