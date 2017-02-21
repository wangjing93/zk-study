package cn.wj.zk;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by WangJing on 2017/2/21.
 */
public class ZooKeeper_Create_API_Sync_Usage implements Watcher {

    private static CountDownLatch connectedSemaphoer = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZooKeeper_Create_API_Sync_Usage());
        connectedSemaphoer.await();
        String path1 = zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("Success create znode: " + path1);
        String path2 = zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("Success create znode: " + path2);
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("Receive watched event:" + event);
        if (Event.KeeperState.SyncConnected == event.getState()) {
            connectedSemaphoer.countDown();
        }
    }
}
