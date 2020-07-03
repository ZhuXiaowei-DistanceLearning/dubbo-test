package com.zxw.node;


import com.zxw.lock.LockListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁机制
 *
 * @author zxw
 * @date 2020/7/2 23:01
 */
public class DistributedLock extends ConnectionWatcher {
    private final String path = "/curator/lock";

    public boolean lockAcquired(String path2) {
        try {
            while (!tryLock(path, path2)) {

            }
            return true;
        } catch (Exception e) {
            System.out.println("---连接失败，失败原因[" + e.getMessage() + "]");
        }
        return false;
    }

    public String getPath() {
        ZooKeeper zk = null;
        try {
            zk = connect("localhost:2181");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        try {
            return create(zk, path);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    public boolean lockReleased(String path) {
        try {
            delete(path);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        return true;
    }
}
