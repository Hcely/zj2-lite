package org.zj2.common.sys.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zj2.common.sys.base.dto.SysConfigDTO;
import org.zj2.common.sys.base.dto.req.SysConfigEditReq;
import org.zj2.common.sys.base.service.SysConfigService;

/**
 *  SysConfigController
 *
 * @author peijie.ye
 * @date 2022/12/28 21:54
 */
@RestController
@RequestMapping("/api/sys/config")
public class SysConfigController {
    @Autowired
    private SysConfigService sysConfigService;


    @PostMapping("/edit")
    public SysConfigDTO edit(@RequestBody SysConfigEditReq req) {
        return sysConfigService.edit(req);
    }


}
