package com.zxw.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * @author zxw
 * @date 2020/7/2 19:27
 */
public interface Lock {
    /**
     * 加锁
     */
    void Lock();

    /**
     * 解锁
     */
    void unLock();

    /**
     * 一直请求获取锁
     *
     * @return
     */
    boolean tryLock();

    /**
     * 设置等待超时时间
     *
     * @param time     时间
     * @param timeUnit 时间单位
     * @return
     */
    boolean tryLock(long time, TimeUnit timeUnit);
}
