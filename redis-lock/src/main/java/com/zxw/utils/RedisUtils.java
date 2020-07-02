package com.zxw.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zxw
 * @date 2020/7/1 15:47
 */
public class RedisUtils {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String LOCKED_SUCCESS = "OK";
    private static final String NX = "NX";
    private static final String EXPIRE_TIME = "PX";

    public static boolean tryDistributeLock(StringRedisTemplate stringRedisTemplate, String lockKey, String uniqueId, long expreTime) {
        return false;
    }

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(50);
    }
}
