package com.zxw.node;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import javax.imageio.spi.ServiceRegistry;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;


/**
 * @author zxw
 * @date 2020/7/2 22:00
 */
public class ServiceRegistryImpl implements Watcher {
    private static CountDownLatch latch = new CountDownLatch(1);
    private static final int SESSION_TIMEOUT = 5000;
    private static final String REGISTRY_PATH = "/registry";
    protected ZooKeeper zk;
    private CountDownLatch connectedSignal = new CountDownLatch(1);

    public void process(WatchedEvent watchedEvent) {
        // 客户端处于连接状态
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            connectedSignal.countDown();
        }
    }

    public void connect(String hosts) throws IOException, InterruptedException {
        zk = new ZooKeeper(hosts, SESSION_TIMEOUT, this);
        connectedSignal.await();
    }

    public void close() throws InterruptedException {
        zk.close();
    }
}
