package com.zxw.utils;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * @author zxw
 * @date 2020/7/3 10:32
 */
public class ZookeeperUtils {
    private static String address = "localhost:2181";
    public static CuratorFramework client;

    static {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(address, retryPolicy);
        client.start();
    }

    private static class SingletonHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         * 参考：http://ifeve.com/zookeeper-lock/
         * 这里建议 new 一个
         */
        private static InterProcessMutex mutex = new InterProcessMutex(client, "/curator/lock");
    }

    private static InterProcessMutex getMutex() {
        return SingletonHolder.mutex;
    }

    public static boolean acquire(long time, TimeUnit timeUnit) {
        try {
            return getMutex().acquire(time, timeUnit);
        } catch (Exception e) {
            return false;
        }
    }

    public static void release() {
        try {
            getMutex().release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
