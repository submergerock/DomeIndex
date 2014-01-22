package org.cProc.task.server;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.cProc.task.common.ByteConvert;
import org.cProc.task.common.Status;
import org.eclipse.jetty.util.log.Log;

public class ZkClientThread implements Watcher, Runnable {

	public static final ArrayBlockingQueue<Status> STATUS_QUEUE = new ArrayBlockingQueue<Status>(
			100000);

	private ZooKeeper zk = null;
	private CountDownLatch signal = new CountDownLatch(1);

	
	private static final Logger LOG = Logger.getLogger(ZkClientThread.class);
	
	
	public ZkClientThread() throws IOException, InterruptedException,
			KeeperException {
		
		zk = new ZooKeeper(Const.ZK_CONNSTR, Const.SESSION_TIMEOUT, this);
		signal.await();
	}

	
	
	
	
	
	
	public void run() {
		// TODO Auto-generated method stub

		while (true) {
			try {
				Status status = STATUS_QUEUE.take();
				if (status == null)
					continue;
				LOG.info("zk process queue.....");
				
				if (-1 == status.getStatus()) {// 发送的请求
					LOG.info("add to client node, id is -->"+status.getTaskId());
					zk.create(Const.APP_NAME + "/client/" + status.getIp()+"/"+status.getTaskId(),
							ByteConvert.list2byte(status.getTasks()),
							Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				} else if (0 == status.getStatus()) {
					LOG.info("remove  client node, id is -->"+status.getTaskId());
					
					zk.delete(Const.APP_NAME + "/client/" + status.getIp()+"/"+status.getTaskId(),
							-1);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOG.error(e.getMessage());
			} catch (KeeperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOG.error(e.getMessage());
			}

		}

	}

	@Override
	public void process(WatchedEvent event) {
		if (event.getState() == KeeperState.SyncConnected) {
			signal.countDown();
		}

	}
}
