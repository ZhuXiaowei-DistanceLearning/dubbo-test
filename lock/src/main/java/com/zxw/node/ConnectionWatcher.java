package com.zxw.node;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

/**
 * @author zxw
 * @date 2020/7/2 21:51
 */
public class ConnectionWatcher implements Watcher {
    public void process(WatchedEvent watchedEvent) {

    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("---开始---");
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                System.out.println("正在执行[" + countDownLatch.getCount() + "]");
                countDownLatch.countDown();
            }, i + "").start();
        }
        countDownLatch.await();
        System.out.println("---结束---");
    }
}
