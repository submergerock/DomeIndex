package org.cProc.tool;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.cProc.task.common.ByteConvert;
import org.cProc.task.common.IRemote;
import org.cProc.tool.myXMLReader.MyXmlReader;

import com.caucho.hessian.client.HessianProxyFactory;

public class ReportStat implements Watcher {

	private ZooKeeper zk = null;
	private CountDownLatch signal = new CountDownLatch(1);

	String zkStr;

	public ReportStat(String confFile) throws IOException,
			InterruptedException, KeeperException {

		init(confFile);
		zk = new ZooKeeper(zkStr, 10000, this);
		signal.await();

		displayInfo();
	}

	private void init(String confFile) {

		MyXmlReader reader = new MyXmlReader(confFile);
		zkStr = reader.getName("connectString");
	}

	private void displayInfo() throws KeeperException, InterruptedException,
			MalformedURLException {
		// 所有主节点的信息
		List<String> masters = zk.getChildren("/newCproc/server", null);
		for (String str : masters) {

			System.out.println("server ip is:" + str);

		}

		// 先获取主节点
		byte[] bytes = zk.getData("/newCproc/server", false, null);
		System.out.println("master server ip is:" + new String(bytes));

		bytes = zk.getData("/newCproc/URL", false, null);
		String url = new String(bytes, 0, bytes.length);
		// client

		HessianProxyFactory factory = new HessianProxyFactory();

		IRemote remote = (IRemote) factory.create(IRemote.class, url);

		Set<String> set = remote.getLiveNodes();
		for (String str : set) {
			System.out.println("client ip is:" + str);
		}

		for (String str : set) {
			StringBuffer sb = new StringBuffer(str);
			sb.append(":");
			
			List<String> list = zk.getChildren(
					"/newCproc/client/" + str, false);

			for (String taskId : list) {
				bytes = zk.getData("/newCproc/client/" + str + "/"+taskId,
						false, null);
				sb.append(ByteConvert.byte2List(bytes));
				sb.append("\n");
			}
			
			System.out.println(sb.toString());

		}
		
		System.out.println("total process:"+remote.getTotalProcess());
	}

	/**
	 * @param args
	 * @throws KeeperException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException,
			InterruptedException, KeeperException {

		String confFile = args[0];
		//String confFile = "E:\\datacube\\createIndex.xml";
		new ReportStat(confFile);

	}

	@Override
	public void process(WatchedEvent event) {
		// TODO Auto-generated method stub
		// EventType type = event.getType();
		if (event.getState() == KeeperState.SyncConnected) {
			signal.countDown();
		}

	}

}
