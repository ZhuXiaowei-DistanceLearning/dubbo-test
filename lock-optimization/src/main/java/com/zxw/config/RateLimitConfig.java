package com.zxw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author zxw
 * @date 2020/7/21 17:04
 */
public class RateLimitConfig {
    public static Queue<Integer> queue = new LinkedBlockingQueue<>(10);
}
