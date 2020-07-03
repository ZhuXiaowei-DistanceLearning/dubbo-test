package com.zxw.web;

import com.zxw.constants.RedisKeyManager;
import com.zxw.lock.RedissonDistributedLocker;
import com.zxw.node.DistributedLock;
import com.zxw.utils.RedisUtils;
import com.zxw.utils.ZookeeperUtils;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.java2d.pipe.SpanIterator;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author zxw
 * @date 2020/7/2 11:55
 */
@RestController
public class RedissonController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedissonDistributedLocker lock;
    /**
     * 锁测试共享变量
     */
    private static int lockCount = 10;

    /**
     * 无锁测试共享变量
     */
    private Integer count = 10;

    /**
     * 模拟线程数
     */
    private static int threadNum = 10;

    @GetMapping("/lock4")
    public void lock4() throws KeeperException, InterruptedException {
        System.out.println(redisTemplate.opsForValue().get("redis"));
        DistributedLock distributedLock = new DistributedLock();
        String path = distributedLock.getPath();
        distributedLock.lockAcquired(path.split("/")[2]);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        redisTemplate.opsForValue().increment("redis");
        distributedLock.delete(path);
        System.out.println(redisTemplate.opsForValue().get("redis"));
    }

    @GetMapping("/lock3")
    public void lock3() {
        System.out.println(redisTemplate.opsForValue().get("redis"));
        String uuid = UUID.randomUUID().toString();
        ZookeeperUtils.acquire(10000, TimeUnit.SECONDS);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        redisTemplate.opsForValue().increment("redis");
        ZookeeperUtils.release();
        System.out.println(redisTemplate.opsForValue().get("redis"));
    }

    /**
     * 模拟并发测试加锁和不加锁
     *
     * @return
     */
    @GetMapping("/lock2")
    public void lock2() {
        System.out.println(redisTemplate.opsForValue().get("redis"));
        String uuid = UUID.randomUUID().toString();
        RedisUtils.lock(redisTemplate, RedisKeyManager.LOCK, uuid, 10000);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        redisTemplate.opsForValue().increment("redis");
        RedisUtils.unlock(redisTemplate, RedisKeyManager.LOCK);
        System.out.println(redisTemplate.opsForValue().get("redis"));
    }

    /**
     * 模拟并发测试加锁和不加锁
     *
     * @return
     */
    @GetMapping("/lock")
    public void lock() {
        System.out.println(redisTemplate.opsForValue().get("redis"));
        lock.lock("redis-lock", TimeUnit.SECONDS, 30);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        redisTemplate.opsForValue().increment("redis");
        lock.unlock("redis-lock");
        System.out.println(redisTemplate.opsForValue().get("redis"));
//        System.out.println("---结束---");
//        System.out.println("开始执行");
//        // 计数器
//        final CountDownLatch countDownLatch = new CountDownLatch(1);
//        for (int i = 0; i < threadNum; i++) {
//            MyRunnable myRunnable = new MyRunnable(countDownLatch);
//            Thread myThread = new Thread(myRunnable);
//            myThread.start();
//        }
//        System.out.println("执行结束");
//        // 释放所有线程
//        countDownLatch.countDown();
//        System.out.println("lockCount[" + lockCount + "]");
//        System.out.println("count[" + count + "]");
    }

    /**
     * 加锁测试
     */
    private void testLockCount() {
        String lockKey = "lock-test";
        try {
            // 加锁，设置超时时间2s
            lock.lock(lockKey, TimeUnit.SECONDS, 2);
            System.out.println("加锁了");
            lockCount--;
        } catch (Exception e) {
        } finally {
            System.out.println("释放锁");
            lock.unlock(lockKey);
        }
    }

    /**
     * 无锁测试
     */
    private void testCount() {
        count--;
    }


    public class MyRunnable implements Runnable {
        /**
         * 计数器
         */
        final CountDownLatch countDownLatch;

        public MyRunnable(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        public void run() {
            try {
                // 调用await()方法的线程会被挂起，它会等待直到count值为0才继续执行
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 无锁操作
            testCount();
            // 加锁操作
            testLockCount();
        }

    }
}
