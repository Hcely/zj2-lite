package org.zj2.common.uac.org.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zj2.common.uac.org.dto.OrgEmployeeDTO;
import org.zj2.common.uac.org.dto.OrgEmployeeExtDTO;
import org.zj2.common.uac.org.dto.req.OrgEmployeeAddReq;
import org.zj2.common.uac.org.dto.req.OrgEmployeeCreateReq;
import org.zj2.common.uac.org.dto.req.OrgEmployeeQuery;
import org.zj2.common.uac.org.dto.req.OrgEmployeeQuitReq;
import org.zj2.common.uac.org.service.OrgEmployeeService;
import org.zj2.lite.common.entity.result.ZListResp;
import org.zj2.lite.common.entity.result.ZResp;
import org.zj2.lite.common.entity.result.ZResult;
import org.zj2.lite.util.ZRBuilder;

/**
 * OrgEmployeeController
 *
 * @author peijie.ye
 * @date 2023/2/6 17:22
 */
@Api(tags = "uac-机构-职员模块")
@RestController
@RequestMapping("/api/uac/org/employee")
public class OrgEmployeeController {
    @Autowired
    private OrgEmployeeService orgEmployeeService;

    @ApiOperation(value = "添加职员", notes = "添加现有的用户")
    @PostMapping("add")
    public ZResp<OrgEmployeeDTO> addEmployee(@RequestBody OrgEmployeeAddReq req) {
        OrgEmployeeDTO employee = orgEmployeeService.addEmployee(req);
        return ZRBuilder.successResp(employee);
    }

    @ApiOperation(value = "创建职员", notes = "如果用户不存在则创建对应用户")
    @PostMapping("create")
    public ZResp<OrgEmployeeDTO> createEmployee(@RequestBody OrgEmployeeCreateReq req) {
        OrgEmployeeDTO employee = orgEmployeeService.createEmployee(req);
        return ZRBuilder.successResp(employee);
    }

    @ApiOperation("启用职员")
    @PostMapping("enable/{employeeId}")
    public ZResult enable(@PathVariable String employeeId) {
        orgEmployeeService.enable(employeeId);
        return ZRBuilder.successResult();
    }

    @ApiOperation("禁用职员")
    @PostMapping("disable/{employeeId}")
    public ZResult disable(@PathVariable String employeeId) {
        orgEmployeeService.disable(employeeId);
        return ZRBuilder.successResult();
    }

    @ApiOperation("显示职员")
    @PostMapping("visible/{employeeId}")
    public ZResult visible(@PathVariable String employeeId) {
        orgEmployeeService.visible(employeeId);
        return ZRBuilder.successResult();
    }

    @ApiOperation("隐藏职员")
    @PostMapping("divisible/{employeeId}")
    public ZResult divisible(@PathVariable String employeeId) {
        orgEmployeeService.divisible(employeeId);
        return ZRBuilder.successResult();
    }

    @ApiOperation("离职")
    @PostMapping("quit")
    public ZResult quit(@RequestBody OrgEmployeeQuitReq req) {
        orgEmployeeService.quit(req);
        return ZRBuilder.successResult();
    }

    @ApiOperation("查询职员列表")
    @PostMapping("query")
    public ZListResp<OrgEmployeeExtDTO> pageQuery(@RequestBody OrgEmployeeQuery query) {
        return orgEmployeeService.pageQuery(query);
    }
}
