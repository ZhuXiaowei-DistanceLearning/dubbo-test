package com.zxw.lock;

import org.apache.zookeeper.ZooKeeper;

/**
 * @author zxw
 * @date 2020/7/2 21:50
 */
public interface LockListener {
    boolean lockAcquired();

    boolean lockReleased();

}
