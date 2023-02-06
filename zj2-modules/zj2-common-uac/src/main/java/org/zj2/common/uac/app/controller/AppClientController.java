package org.zj2.common.uac.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zj2.common.uac.app.dto.AppClientDTO;
import org.zj2.common.uac.app.dto.req.AppClientQuery;
import org.zj2.common.uac.app.dto.req.AppClientSaveReq;
import org.zj2.common.uac.app.service.AppClientService;
import org.zj2.lite.common.entity.result.ZListResp;
import org.zj2.lite.common.entity.result.ZResp;
import org.zj2.lite.common.entity.result.ZResult;
import org.zj2.lite.util.ZRBuilder;

/**
 * AppClientController
 *
 * @author peijie.ye
 * @date 2023/2/6 16:08
 */
@Tag(name = "uac-应用-客户端模块")
@RestController
@RequestMapping("/api/uac/app/client")
public class AppClientController {
    @Autowired
    private AppClientService appClientService;

    @Operation(tags = "uac-应用-客户端模块", summary = "创建客户端")
    @PostMapping("create")
    public ZResp<AppClientDTO> createClient(@RequestBody AppClientSaveReq req) {
        AppClientDTO client = appClientService.saveClient(req);
        return ZRBuilder.successResp(client);
    }

    @Operation(tags = "uac-应用-客户端模块", summary = "编辑客户端")
    @PostMapping("edit")
    public ZResult editClient(@RequestBody AppClientSaveReq req) {
        appClientService.saveClient(req);
        return ZRBuilder.successResult();
    }

    @Operation(tags = "uac-应用-客户端模块", summary = "删除客户端")
    @PostMapping("remove/{appClientId}")
    public ZResult removeClient(@PathVariable String appClientId) {
        appClientService.removeClient(appClientId);
        return ZRBuilder.successResult();
    }

    @Operation(tags = "uac-应用-客户端模块", summary = "启用客户端")
    @PostMapping("enable/{appClientId}")
    public ZResult enable(@PathVariable String appClientId) {
        appClientService.enable(appClientId);
        return ZRBuilder.successResult();
    }

    @Operation(tags = "uac-应用-客户端模块", summary = "禁用客户端")
    @PostMapping("disable/{appClientId}")
    public ZResult disable(@PathVariable String appClientId) {
        appClientService.disable(appClientId);
        return ZRBuilder.successResult();
    }

    @Operation(tags = "uac-应用-客户端模块", summary = "查询客户端列表")
    @PostMapping("pageQuery")
    public ZListResp<AppClientDTO> pageQuery(@RequestBody AppClientQuery query) {
        return appClientService.pageQuery(query);
    }
}
