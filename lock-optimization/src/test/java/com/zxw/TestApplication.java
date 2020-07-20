package com.zxw;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author zxw
 * @date 2020/7/20 17:08
 */
public class TestApplication {
    public static void main(String[] args) throws InterruptedException {
        LongAdder longAdder = new LongAdder();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                longAdder.add(1);
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                longAdder.add(1);
            }
        }, "t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(longAdder);
    }
}
