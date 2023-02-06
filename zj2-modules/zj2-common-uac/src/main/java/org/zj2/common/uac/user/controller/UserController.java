package org.zj2.common.uac.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zj2.common.uac.user.constant.UserValueTypeEnum;
import org.zj2.common.uac.user.dto.UserDTO;
import org.zj2.common.uac.user.dto.req.UserCreateReq;
import org.zj2.common.uac.user.dto.req.UserEditEmailReq;
import org.zj2.common.uac.user.dto.req.UserEditMobileReq;
import org.zj2.common.uac.user.dto.req.UserEditPasswordReq;
import org.zj2.common.uac.user.dto.req.UserEditValueReq;
import org.zj2.common.uac.user.dto.req.UserOperationReq;
import org.zj2.common.uac.user.dto.req.UserQuery;
import org.zj2.common.uac.user.service.UserService;
import org.zj2.lite.common.entity.result.ZListResp;
import org.zj2.lite.common.entity.result.ZResp;
import org.zj2.lite.common.entity.result.ZResult;
import org.zj2.lite.util.ZRBuilder;

/**
 * UserController
 *
 * @author peijie.ye
 * @date 2023/2/6 17:40
 */
@Tag(name = "uac-用户模块")
@RestController
@RequestMapping("/api/uac/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(tags = "uac-用户模块", summary = "查询用户列表")
    @PostMapping("query")
    public ZListResp<UserDTO> pageQuery(@RequestBody UserQuery query) {
        return userService.pageQuery(query);
    }

    @Operation(tags = "uac-用户模块", summary = "获取用户")
    @GetMapping("{userId}")
    public ZResp<UserDTO> get(@PathVariable String userId) {
        UserDTO user = userService.get(userId);
        return ZRBuilder.successResp(user);
    }

    @Operation(tags = "uac-用户模块", summary = "创建用户")
    @PostMapping("create")
    public ZResp<UserDTO> create(@RequestBody UserCreateReq user) {
        UserDTO u = userService.create(user);
        return ZRBuilder.successResp(u);
    }

    @Operation(tags = "uac-用户模块", summary = "编辑密码")
    @PostMapping("editPassword")
    public ZResult editPassword(@RequestBody UserEditPasswordReq req) {
        userService.editPassword(req);
        return ZRBuilder.successResult();
    }

    @Operation(tags = "uac-用户模块", summary = "编辑手机号")
    @PostMapping("editMobile")
    public ZResult editMobile(@RequestBody UserEditMobileReq req) {
        userService.editUserValue(new UserEditValueReq(req.getUserId(), UserValueTypeEnum.MOBILE, req.getUserMobile(),
                req.getUserMobileAreaCode()));
        return ZRBuilder.successResult();
    }

    @Operation(tags = "uac-用户模块", summary = "编辑邮件")
    @PostMapping("editEmail")
    public ZResult editEmail(@RequestBody UserEditEmailReq req) {
        userService.editUserValue(
                new UserEditValueReq(req.getUserId(), UserValueTypeEnum.EMAIL, req.getUserEmail(), null));
        return ZRBuilder.successResult();
    }

    @Operation(tags = "uac-用户模块", summary = "启用用户")
    @PostMapping("enable")
    public ZResult enable(@RequestBody UserOperationReq req) {
        userService.enable(req);
        return ZRBuilder.successResult();
    }

    @Operation(tags = "uac-用户模块", summary = "禁用用户")
    @PostMapping("disable")
    public ZResult disable(@RequestBody UserOperationReq req) {
        userService.disable(req);
        return ZRBuilder.successResult();
    }

    @Operation(tags = "uac-用户模块", summary = "设置停用期限")
    @PostMapping("forbidden")
    public ZResult forbidden(@RequestBody UserOperationReq req) {
        userService.forbidden(req);
        return ZRBuilder.successResult();
    }

    @Operation(tags = "uac-用户模块", summary = "设置有效期")
    @PostMapping("valid")
    public ZResult valid(@RequestBody UserOperationReq req) {
        userService.valid(req);
        return ZRBuilder.successResult();
    }

    @Operation(tags = "uac-用户模块", summary = "激活用户")
    @PostMapping("activate")
    public ZResult activate(@RequestBody UserOperationReq req) {
        userService.activate(req);
        return ZRBuilder.successResult();
    }
}
