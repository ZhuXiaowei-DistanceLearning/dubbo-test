package com.zxw.web;

import com.zxw.config.RateLimitConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zxw
 * @date 2020/7/21 17:00
 */
@Controller
public class RateLimitController {

    @GetMapping("/rate/test")
    public void test() {
        Integer integer = RateLimitConfig.queue.poll();
        try {
            Thread.sleep(2000);
            System.out.println("当前是第" + integer + "请求");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
