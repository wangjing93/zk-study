package cn.wj.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by WangJing on 2017/2/21.
 */
public class ZooKeeper_Constructor_Usage_Simple implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException {
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZooKeeper_Constructor_Usage_Simple());
        System.out.println("zk state:" + zooKeeper.getState());
        try {
            connectedSemaphore.await();
        } catch (InterruptedException e) {

        }
        System.out.println("ZooKeeper session established");
    }
    @Override
    public void process(WatchedEvent event) {
        System.out.println("Receive watched event:" + event);
        if (Event.KeeperState.SyncConnected == event.getState()) {
            connectedSemaphore.countDown();
        }
    }
}
