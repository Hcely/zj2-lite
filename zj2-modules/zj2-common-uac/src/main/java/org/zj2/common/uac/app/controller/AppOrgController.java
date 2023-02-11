package org.zj2.common.uac.app.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zj2.common.uac.app.dto.AppOrgDTO;
import org.zj2.common.uac.app.dto.AppOrgExtDTO;
import org.zj2.common.uac.app.dto.req.AppOrgAddReq;
import org.zj2.common.uac.app.dto.req.AppOrgQuery;
import org.zj2.common.uac.app.service.AppOrgService;
import org.zj2.lite.common.entity.result.ZListResp;
import org.zj2.lite.common.entity.result.ZResp;
import org.zj2.lite.common.entity.result.ZResult;
import org.zj2.lite.util.ZRBuilder;

/**
 * AppOrgController
 *
 * @author peijie.ye
 * @date 2023/2/6 16:02
 */
@Api(tags = "uac-应用-机构模块")
@RestController
@RequestMapping("/api/uac/app/org")
public class AppOrgController {
    @Autowired
    private AppOrgService appOrgService;

    @ApiOperation("添加机构")
    @PostMapping("add")
    public ZResp<AppOrgDTO> addOrg(@RequestBody AppOrgAddReq req) {
        AppOrgDTO appOrg = appOrgService.addOrg(req);
        return ZRBuilder.successResp(appOrg);
    }

    @ApiOperation("删除机构")
    @PostMapping("remove/{appOrgId}")
    public ZResult removeOrg(@PathVariable String appOrgId) {
        appOrgService.removeOrg(appOrgId);
        return ZRBuilder.successResult();
    }

    @ApiOperation("启用机构")
    @PostMapping("enable/{appOrgId}")
    public ZResult enable(String appOrgId) {
        appOrgService.enable(appOrgId);
        return ZRBuilder.successResult();
    }

    @ApiOperation("禁用机构")
    @PostMapping("disable/{appOrgId}")
    public ZResult disable(String appOrgId) {
        appOrgService.disable(appOrgId);
        return ZRBuilder.successResult();
    }

    @ApiOperation("查询机构列表")
    @PostMapping("pageQuery")
    public ZListResp<AppOrgExtDTO> pageQuery(@RequestBody AppOrgQuery query) {
        return appOrgService.pageQuery(query);
    }
}
