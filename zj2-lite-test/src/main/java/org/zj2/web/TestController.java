package org.zj2.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zj2.common.sys.base.dto.req.NumNextReq;
import org.zj2.common.sys.base.service.SysSequenceService;

/**
 * TestController
 *
 * @author peijie.ye
 * @date 2022/12/22 18:08
 */
@RestController
public class TestController {
    @Autowired
    private SysSequenceService sysSequenceService;


    @GetMapping("time")
    public long time() {
        return System.currentTimeMillis();
    }


    @GetMapping("generateNum")
    public String generateNum() {
        NumNextReq req = new NumNextReq();
        req.setAppCode("COMMON");
        req.setNumRuleCode("testGenerateNum");
        req.setNumRuleFormat("{header}-{date,yyyyMMdd}{seq,####}");
        req.setSeqIncKeyFormat("{date,yyyyMMdd}");
        req.putParam("header", "AA");
        return sysSequenceService.next(req).getSequenceNo();
    }
}
