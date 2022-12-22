package org.zj2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *  TestStarter
 *
 * @author peijie.ye
 * @date 2022/12/22 17:28
 */
@SpringBootApplication
public class TestApplication {
    public static void main(String[] args) {
        try {
            SpringApplication.run(TestApplication.class, args);
        } catch (Throwable e) {// NOSONAR
            e.printStackTrace();
        }
    }
}
