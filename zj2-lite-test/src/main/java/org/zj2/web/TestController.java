package org.zj2.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  TestController
 *
 * @author peijie.ye
 * @date 2022/12/22 18:08
 */
@RestController
public class TestController {
    @GetMapping("time")
    public long time() {
        return System.currentTimeMillis();
    }
}
