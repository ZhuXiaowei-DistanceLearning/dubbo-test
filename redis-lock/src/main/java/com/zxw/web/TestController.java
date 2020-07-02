package com.zxw.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zxw
 * @date 2020/7/2 13:34
 */
@RestController("/test")
public class TestController {
    @GetMapping("/test01")
    public void test01() {
        System.out.println("test01");
    }
}
