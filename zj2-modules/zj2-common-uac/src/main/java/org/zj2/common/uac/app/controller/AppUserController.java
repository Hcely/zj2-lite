package org.zj2.common.uac.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zj2.common.uac.app.dto.AppUserDTO;
import org.zj2.common.uac.app.dto.AppUserExtDTO;
import org.zj2.common.uac.app.dto.req.AppUserQuery;
import org.zj2.common.uac.app.dto.req.AppUserAddReq;
import org.zj2.common.uac.app.service.AppUserService;
import org.zj2.lite.common.entity.result.ZListResp;
import org.zj2.lite.common.entity.result.ZResp;
import org.zj2.lite.common.entity.result.ZResult;
import org.zj2.lite.util.ZRBuilder;

/**
 * AppUserController
 *
 * @author peijie.ye
 * @date 2023/2/6 16:26
 */
@Tag(name = "uac-应用-用户模块")
@RestController
@RequestMapping("/api/uac/app/user")
public class AppUserController {
    @Autowired
    private AppUserService appUserService;

    @Operation(tags = "uac-应用-用户模块", summary = "添加用户")
    @PostMapping("add")
    public ZResp<AppUserDTO> addUser(@RequestBody AppUserAddReq req) {
        AppUserDTO appUser = appUserService.addUser(req);
        return ZRBuilder.successResp(appUser);
    }

    @Operation(tags = "uac-应用-用户模块", summary = "删除用户")
    @PostMapping("remove/{appUserId}")
    public ZResult removeUser(@PathVariable String appUserId) {
        appUserService.removeUser(appUserId);
        return ZRBuilder.successResult();
    }

    @Operation(tags = "uac-应用-用户模块", summary = "启用用户")
    @PostMapping("enable/{appUserId}")
    public ZResult enable(@PathVariable String appUserId) {
        appUserService.enable(appUserId);
        return ZRBuilder.successResult();
    }

    @Operation(tags = "uac-应用-用户模块", summary = "禁用用户")
    @PostMapping("disable/{appUserId}")
    public ZResult disable(@PathVariable String appUserId) {
        appUserService.disable(appUserId);
        return ZRBuilder.successResult();
    }

    @Operation(tags = "uac-应用-用户模块", summary = "查询用户列表")
    @PostMapping("pageQuery")
    public ZListResp<AppUserExtDTO> pageQuery(@RequestBody AppUserQuery query) {
        return appUserService.pageQuery(query);
    }
}
