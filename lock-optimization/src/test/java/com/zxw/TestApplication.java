package com.zxw;

import com.zxw.lock.DistributedLocker;
import com.zxw.lock.RedissonDistributedLocker;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author zxw
 * @date 2020/7/20 17:08
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestApplication {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    DistributedLocker redissonClient;


    public static Integer STOCK_NUM = 100;

    public static String MS_REDIS_PREFIX = "stock_";

    private static Integer REMAIN_NUM = STOCK_NUM;

    private static Integer PARTITION = 10;

    @Test
    public void generateArray() {
        for (int i = 0; i < PARTITION; i++) {
            if (i == 9) {
                redisTemplate.opsForValue().set("stock_" + (i), String.valueOf(REMAIN_NUM));
            } else {
                redisTemplate.opsForValue().set("stock_" + (i), String.valueOf(STOCK_NUM / 10));
            }
            REMAIN_NUM = REMAIN_NUM - STOCK_NUM / PARTITION;
        }
    }

    @Test
    public void test2() {
        int[] arr = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        Integer cur = 1;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.execute(() -> {
            int cc = cur;
            redissonClient.lock("redis-key");
            String value = redisTemplate.opsForValue().get(MS_REDIS_PREFIX + cc);
            if (Integer.valueOf(value) == 0) {
                arr[cc] = 0;
            } else {
                redisTemplate.opsForValue().decrement(MS_REDIS_PREFIX + cc);
                System.out.println("仓库[" + MS_REDIS_PREFIX + cc + "]:减少了一个商品，剩余" + redisTemplate.opsForValue().get(MS_REDIS_PREFIX + cc));
            }
            redissonClient.unlock("redis-key");
        });
    }

    public static void main(String[] args) {
        HashSet set = new HashSet();
        set.add("1");
        set.add("2");
        set.add("3");
        Object[] array = set.toArray();
        System.out.println(array[0]);
    }
}
