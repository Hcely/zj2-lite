package org.zj2.common.uac.org.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zj2.common.uac.EnableUacController;
import org.zj2.common.uac.org.dto.OrgDTO;
import org.zj2.common.uac.org.dto.req.OrgEditReq;
import org.zj2.common.uac.org.dto.req.OrgQuery;
import org.zj2.common.uac.org.service.OrgService;
import org.zj2.lite.common.entity.result.ZListResp;
import org.zj2.lite.common.entity.result.ZResp;
import org.zj2.lite.common.entity.result.ZResult;
import org.zj2.lite.util.ZRBuilder;

/**
 * OrgController
 *
 * @author peijie.ye
 * @date 2023/2/6 17:12
 */
@ConditionalOnBean(annotation = EnableUacController.class)
@Api(tags = "uac-机构模块")
@RestController
@RequestMapping("/api/uac/org")
public class OrgController {
    @Autowired
    private OrgService orgService;

    @ApiOperation("获取机构信息")
    @GetMapping("{orgCode}")
    public ZResp<OrgDTO> getByCode(@PathVariable String orgCode) {
        OrgDTO org = orgService.getByCode(orgCode);
        return ZRBuilder.successResp(org);
    }

    @ApiOperation("创建机构")
    @PostMapping("create")
    public ZResp<OrgDTO> create(@RequestBody OrgEditReq req) {
        OrgDTO org = orgService.create(req);
        return ZRBuilder.successResp(org);
    }

    @ApiOperation("编辑机构")
    @PostMapping("edit")
    public ZResult edit(@RequestBody OrgEditReq req) {
        orgService.edit(req);
        return ZRBuilder.successResult();
    }

    @ApiOperation("启用机构")
    @PostMapping("enable/{orgCode}")
    public ZResult enable(@PathVariable String orgCode) {
        orgService.enable(orgCode);
        return ZRBuilder.successResult();
    }

    @ApiOperation("禁用机构")
    @PostMapping("disable/{orgCode}")
    public ZResult disable(@PathVariable String orgCode) {
        orgService.disable(orgCode);
        return ZRBuilder.successResult();
    }

    @ApiOperation("查询机构列表")
    @PostMapping("pageQuery")
    public ZListResp<OrgDTO> pageQuery(@RequestBody OrgQuery query) {
        return orgService.pageQuery(query);
    }
}
