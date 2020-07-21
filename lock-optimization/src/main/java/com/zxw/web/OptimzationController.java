package com.zxw.web;

import com.zxw.lock.DistributedLocker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zxw
 * @date 2020/7/21 10:53
 */
@RestController
public class OptimzationController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    DistributedLocker redissonClient;

    private static AtomicInteger nextServerCyclicCounter = new AtomicInteger(0);

    public static Integer STOCK_NUM = 10000;

    public static String MS_REDIS_PREFIX = "stock_";

    @GetMapping("/test2")
    public void test2() {
        Object[] keys = redisTemplate.keys(MS_REDIS_PREFIX + "?").toArray();
        // 如果列表数量为0，则返回空
        int keyCount = keys.length;
        if (keyCount == 0) {
            return;
        }
        int index = this.incrementAndGetModulo(keyCount);
        redissonClient.lock(keys[index] + "_" + index, TimeUnit.SECONDS, 5);
        String value = redisTemplate.opsForValue().get(keys[index]);
        if (value == null) {
            System.out.println("当前仓库:[" + MS_REDIS_PREFIX + index + "]库存为:" + value);
        } else {
            if (Integer.valueOf(value) == 0) {
                redisTemplate.delete((String) keys[index]);
            } else {
                redisTemplate.opsForValue().decrement((String) keys[index]);
                System.out.println("仓库[" + keys[index] + "]:减少了一个商品，剩余" + redisTemplate.opsForValue().get(keys[index]));
            }
            redissonClient.unlock(keys[index] + "_" + index);
        }
    }

    @GetMapping("/test")
    public void test() {
        Object[] keys = redisTemplate.keys(MS_REDIS_PREFIX + "?").toArray();
        // 如果列表数量为0，则返回空
        int keyCount = keys.length;
        if (keyCount == 0) {
            return;
        }
        int index = this.chooseRandomInt(keyCount);
        redissonClient.lock(keys[index] + "_" + index, TimeUnit.SECONDS, 5);
        String value = redisTemplate.opsForValue().get(keys[index]);
        if (value == null) {
            System.out.println("当前仓库:[" + MS_REDIS_PREFIX + index + "]库存为:" + value);
        } else {
            if (Integer.valueOf(value) == 0) {
                redisTemplate.delete((String) keys[index]);
            } else {
                redisTemplate.opsForValue().decrement((String) keys[index]);
                System.out.println("仓库[" + keys[index] + "]:减少了一个商品，剩余" + redisTemplate.opsForValue().get(keys[index]));
            }
            redissonClient.unlock(keys[index] + "_" + index);
        }
    }

    public int chooseRandomInt(int keysCount) {
        return ThreadLocalRandom.current().nextInt(keysCount);
    }

    public int incrementAndGetModulo(int modulo) {
        int current;
        int next;
        do {
            current = this.nextServerCyclicCounter.get();
            next = (current + 1) % modulo;
        } while (!this.nextServerCyclicCounter.compareAndSet(current, next));
        return next;
    }

}
