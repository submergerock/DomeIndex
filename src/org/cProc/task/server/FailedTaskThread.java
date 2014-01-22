package org.cProc.task.server;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class FailedTaskThread implements Runnable, Watcher {

	private ZooKeeper zk = null;
	private CountDownLatch signal = new CountDownLatch(1);

	public FailedTaskThread() throws IOException, InterruptedException,
			KeeperException {
		zk = new ZooKeeper(Const.ZK_CONNSTR, Const.SESSION_TIMEOUT, this);
		signal.await();
	}

	public void run() {

		//while (Const.SERVER_RUNNING.get()) {

			try {

				List<String> list = zk.getChildren(Const.APP_NAME + "/client",
						false);
				if (list == null || list.isEmpty())
					return;

				for (String str : list) {
					Stat stat = zk.exists(Const.APP_NAME + "/client/" + str,
							false);
					System.out.println(stat.getCtime());
					System.out.println(System.currentTimeMillis());
					System.out.println(str);
				}

				list.clear();
				list = null;

			} catch (KeeperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		//}

	}

	public void process(WatchedEvent event) {
		if (event.getState() == KeeperState.SyncConnected) {
			signal.countDown();
		}

	}
	
	public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
		Const.SERVER_RUNNING.set(true);
		Const.APP_NAME = "/TestCreateIndex";
		Const.ZK_CONNSTR="cProc12,cProc14,cProc16,cProc18,cProc20,cProc22,cProc24,cProc26,cProc28";
		
		FailedTaskThread t   = new FailedTaskThread();
		t.run();
	}

}
