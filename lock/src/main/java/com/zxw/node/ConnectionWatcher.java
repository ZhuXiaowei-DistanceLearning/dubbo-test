package com.zxw.node;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author zxw
 * @date 2020/7/2 21:51
 */
public class ConnectionWatcher implements Watcher {
    private static final int SESSION_TIMEOUT = 5000;

    protected ZooKeeper zk;
    private CountDownLatch connectedSignal = new CountDownLatch(1);
    private List<ACL> acl = ZooDefs.Ids.OPEN_ACL_UNSAFE;

    public ZooKeeper connect(String hosts) throws IOException, InterruptedException, KeeperException {
        zk = new ZooKeeper(hosts, SESSION_TIMEOUT, this);
        connectedSignal.await();
        return zk;
    }

    public String create(ZooKeeper zooKeeper, String path) throws KeeperException, InterruptedException {
        return zk.create(path, "".getBytes(), acl, CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    public void process(WatchedEvent event) {
        if (event.getState() == Event.KeeperState.SyncConnected) {
            connectedSignal.countDown();
        }
    }

    public void close() throws InterruptedException {
        zk.close();
    }

    public void delete(String path) throws InterruptedException, KeeperException {
        zk.delete(path, 0);
    }

    public boolean tryLock(String sysPath, String path) throws KeeperException, InterruptedException {
        List<String> children = zk.getChildren("/curator", false);
        if(path.equals(children.get(0))){
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws InterruptedException {
    }
}
