package com.zxw.node;

/**
 * @author zxw
 * @date 2020/7/2 21:50
 */
public interface LockListener {
     void lockAcquired();

    void lockRelease();
}
