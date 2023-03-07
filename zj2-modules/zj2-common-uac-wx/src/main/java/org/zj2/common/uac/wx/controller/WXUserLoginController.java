package org.zj2.common.uac.wx.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zj2.common.uac.wx.dto.req.WXLoginReq;

/**
 * WXUserLoginController
 *
 * @author peijie.ye
 * @date 2023/3/5 18:33
 */
@RestController
@RequestMapping("")
public class WXUserLoginController {

    public void userLogin(@RequestBody WXLoginReq req) {

    }
}
