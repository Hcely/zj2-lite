package org.zj2.common.uac.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zj2.common.uac.app.dto.AppDTO;
import org.zj2.common.uac.app.dto.req.AppCreateSaveReq;
import org.zj2.common.uac.app.dto.req.AppEditSecretReq;
import org.zj2.common.uac.app.dto.req.AppQuery;
import org.zj2.common.uac.app.service.AppService;
import org.zj2.lite.common.entity.result.ZListResp;
import org.zj2.lite.common.entity.result.ZResp;
import org.zj2.lite.common.entity.result.ZResult;
import org.zj2.lite.util.ZRBuilder;

/**
 * AppController
 *
 * @author peijie.ye
 * @date 2023/2/6 15:51
 */
@Tag(name = "uac-应用模块")
@RestController
@RequestMapping("/api/uac/app")
public class AppController {
    @Autowired
    private AppService appService;

    @Operation(tags = "uac-应用模块", summary = "获取应用")
    @GetMapping("{appCode}")
    public ZResp<AppDTO> getByCode(@PathVariable String appCode) {
        AppDTO app = appService.getByCode(appCode);
        return ZRBuilder.successResp(app);
    }

    @Operation(tags = "uac-应用模块", summary = "创建应用")
    @PostMapping("create")
    public ZResp<AppDTO> createApp(@RequestBody AppCreateSaveReq req) {
        AppDTO app = appService.addApp(req);
        return ZRBuilder.successResp(app);
    }

    @Operation(tags = "uac-应用模块", summary = "编辑应用信息")
    @PostMapping("editApp")
    public ZResult editApp(@RequestBody AppCreateSaveReq req) {
        appService.editApp(req);
        return ZRBuilder.successResult();
    }

    @Operation(tags = "uac-应用模块", summary = "编辑应用密钥")
    @PostMapping("editSecret")
    public ZResult editSecret(@RequestBody AppEditSecretReq req) {
        appService.editSecret(req);
        return ZRBuilder.successResult();
    }

    @Operation(tags = "uac-应用模块", summary = "启用应用")
    @PostMapping("enable/{appCode}")
    public ZResult enable(@PathVariable String appCode) {
        appService.enable(appCode);
        return ZRBuilder.successResult();
    }

    @Operation(tags = "uac-应用模块", summary = "禁用应用")
    @PostMapping("disable/{appCode}")
    public ZResult disable(@PathVariable String appCode) {
        appService.disable(appCode);
        return ZRBuilder.successResult();
    }

    @Operation(tags = "uac-应用模块", summary = "查询应用列表")
    @PostMapping("pageQuery")
    public ZListResp<AppDTO> pageQuery(@RequestBody AppQuery query) {
        return appService.pageQuery(query);
    }
}
