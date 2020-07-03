package com.zxw.lock;

import org.apache.zookeeper.KeeperException;

/**
 * @author zxw
 * @date 2020/7/3 10:14
 */
public interface ZookeeperOperation {
    public boolean execute() throws KeeperException, InterruptedException;
}
