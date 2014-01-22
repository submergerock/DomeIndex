package testReadFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.cProc.tool.Bytes;

public class TestMatch implements Watcher {

	private ZooKeeper zk = null;
	private CountDownLatch signal = new CountDownLatch(1);
	private boolean delete = false;

	public TestMatch() throws IOException, InterruptedException,
			KeeperException {
		zk = new ZooKeeper(
				"172.16.4.102:2181",
				10000, this);

		signal.await();

		System.out.println(Bytes.toString(zk.getData("/newCproc/server",
				false, null)));

		// List<String> stat = zk.getChildren("/test", false);
		// System.out.println(stat.get(0));
		// zk.create("/TestCreateIndex/client/dhshhdsjdshdj",
		// "   fuck".getBytes(), Ids.OPEN_ACL_UNSAFE,
		// CreateMode.PERSISTENT_SEQUENTIAL);
		deleteNode("/newCproc");

	}

	public void deleteNode(String path) throws KeeperException,
			InterruptedException {

		List<String> list = zk.getChildren(path, false);
		if (list == null || list.isEmpty()) {
			System.out.println(path + "   "
					+ Bytes.toString(zk.getData(path, false, null)));
			if (delete)
				zk.delete(path, -1);
			return;
		}
		for (String str : list) {
			deleteNode(path + "/" + str);
		}
		if (delete)
			zk.delete(path, -1);

	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException,
			InterruptedException, KeeperException {
		new TestMatch();
	}

	@Override
	public void process(WatchedEvent event) {
		if (event.getState() == KeeperState.SyncConnected) {
			signal.countDown();
		}

	}

}
