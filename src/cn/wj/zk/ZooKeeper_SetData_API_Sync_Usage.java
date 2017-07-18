package cn.wj.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by WangJing on 2017/2/22.
 */
public class ZooKeeper_SetData_API_Sync_Usage implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk = null;
    private static Stat stat = new Stat();

    public static void main(String[] args) throws Exception {
        String path = "/zk-book";
        zk = new ZooKeeper("127.0.0.1:2181", 5000, new ZooKeeper_SetData_API_Sync_Usage());
        connectedSemaphore.await();
        zk.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        //System.out.println("[1]" + new String(zk.getData(path, true, null)));
        Stat stat1 = zk.setData(path, "456".getBytes(), -1);
        System.out.println("[2]" + stat1.getCzxid() + ", " + stat1.getMzxid() + ", " + stat1.getVersion());
        Stat stat2 = zk.setData(path, "456".getBytes(), stat1.getVersion());
        System.out.println("[3]" + stat2.getCzxid() + ", " + stat2.getMzxid() + ", " + stat2.getVersion());
        try {
            zk.setData(path, "456".getBytes(), stat1.getVersion());
        } catch (KeeperException e) {
            System.out.println("Error:" + e.code() + ", " + e.getMessage());
        }
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Override
    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            if (Event.EventType.None == event.getType() && null == event.getPath()) {
                connectedSemaphore.countDown();
            } else if (Event.EventType.NodeDataChanged == event.getType()) {
                /*try {
                    System.out.println("[4]" + new String(zk.getData(event.getPath(), true, stat)));
                    System.out.println("[5]" + stat.getCzxid() + ", " + stat.getMzxid() + "," + stat.getVersion());
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
        }
    }
}
