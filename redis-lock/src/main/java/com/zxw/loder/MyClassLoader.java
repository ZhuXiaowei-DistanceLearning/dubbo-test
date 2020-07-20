package com.zxw.loder;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zxw
 * @date 2020/7/7 9:23
 */
public class MyClassLoader extends ClassLoader{
    public static Lock lock = new ReentrantLock();
    public static Condition condition = lock.newCondition();
    public static Condition condition2 = lock.newCondition();

    public static void main(String[] args) {
        String a = new String("123");
        String b = new String("123");
        try {
            a.wait();
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(a);
        System.out.println(b);
        System.out.println(a == b);
    }
}
