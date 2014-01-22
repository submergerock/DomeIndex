package org.cProc.task.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.cProc.distributed.zookeeperDispatch.cloudComputingServer.IP;
import org.cProc.task.common.ByteConvert;
import org.cProc.tool.Bytes;

public class ZKThread implements Watcher {

	private static final Logger LOG = Logger.getLogger(ZKThread.class);
	public static final String ZK_SEPARATOR = "/";
	public static ZooKeeper zk = null;
	private CountDownLatch signal = new CountDownLatch(1);

	public ZKThread() throws IOException, InterruptedException, KeeperException {
		zk = new ZooKeeper(Const.ZK_CONNSTR, Const.SESSION_TIMEOUT, this);

		signal.await();
		initZk();

	}

	
	public static List<String> getNodeContent(String path) throws KeeperException, InterruptedException
	{
		
		
		
		byte[] bytes = zk.getData(path, false, null);
		return ByteConvert.byte2List(bytes);

		
	}
	
	
	
	
	private void initZk() throws KeeperException, InterruptedException {

		// app
		Stat stat = zk.exists(Const.APP_NAME, false);
		if (stat == null) {
			zk.create(Const.APP_NAME, "cProc".getBytes(), Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT);
		}

		// server
		stat = zk.exists(Const.APP_NAME + ZK_SEPARATOR + "server", false);
		if (stat == null) {
			zk.create(Const.APP_NAME + ZK_SEPARATOR + "server", "".getBytes(),
					Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}

		// client
		stat = zk.exists(Const.APP_NAME + ZK_SEPARATOR + "client", false);
		if (stat == null) {
			zk.create(Const.APP_NAME + ZK_SEPARATOR + "client",
					"cProc".getBytes(), Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT);
		}

		// url
		stat = zk.exists(Const.APP_NAME + ZK_SEPARATOR + "URL", false);
		if (stat == null) {
			zk.create(Const.APP_NAME + ZK_SEPARATOR + "URL", null,
					Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}

		// 创建自己的临时节点

		stat = zk.exists(Const.APP_NAME + ZK_SEPARATOR + "server"
				+ ZK_SEPARATOR + IP.getIP(), false);
		if (stat == null) {
			zk.create(Const.APP_NAME + ZK_SEPARATOR + "server" + ZK_SEPARATOR
					+ IP.getIP(), "cProc".getBytes(), Ids.OPEN_ACL_UNSAFE,
					CreateMode.EPHEMERAL);
		}

		// 创建master节点

		masterRun();
		// 重新注册

		try {
			zk.getChildren(Const.APP_NAME + ZK_SEPARATOR + "server", this);
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// zk.exists(Const.APP_NAME+ZK_SEPARATOR+"server", this);

	}

	public boolean isMaster() throws KeeperException, InterruptedException {
		/**
		 * 选择的序号是否为最小的,看看server的内容是否为空,如果是，自己为主节点，如果不是，看该节点是否存在
		 */

		byte[] bytes = zk.getData(Const.APP_NAME + ZK_SEPARATOR + "server",
				false, null);

		String content = Bytes.toString(bytes);

		
		
//		List<String> list = zk.getChildren(Const.APP_NAME + ZK_SEPARATOR + "server", false);
//		String first = list.get(0);
//		if(!first.equals(IP.getIP()))//不是第一个不参与选举
//		{
//			return false;
//		}
		
		if ("".equals(content))
		{

			return true;

		}

		Stat stat = zk.exists(Const.APP_NAME + ZK_SEPARATOR + "server"+ZK_SEPARATOR+content, false);
		
		if(stat==null)
			return true;

		//本机
		if(content.equals(IP.getIP()))
		{
			if(Const.SERVER_RUNNING.get()==false)
				return true;
			
		}
		
		return false;
	}

	private void masterRun() {
		try {
			if (isMaster()) {
				// 设置client
				
				try {
					startMaster();
					
					//register
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LOG.info("im master...............");
			} else {
				if(!Const.SERVER_RUNNING.get())
					LOG.info("im slave...............");
			}
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startMaster() throws IOException, KeeperException,
			InterruptedException {

		LOG.info("start master......");

		zk.setData(Const.APP_NAME + ZK_SEPARATOR + "server",
				(IP.getIP().getBytes()), -1);
		
		//zk.create(Const.APP_NAME + ZK_SEPARATOR + "server"+ZK_SEPARATOR+IP.getIP(), null,Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
		

		/**
		 * 1.设置持久化的为running
		 */
		
		
		//遍历所有的client节点
		
		List<String> clientNodes = zk.getChildren(Const.APP_NAME + ZK_SEPARATOR+ "client", false);
		
		
		Map<String, List<String>> runningMaps = new HashMap<String, List<String>>();
		
		
		
		for(String clientNode:clientNodes)
		{
			
			
			
			List<String> list = zk.getChildren(Const.APP_NAME + ZK_SEPARATOR
					+ "client"+ ZK_SEPARATOR+clientNode, false);
			if (list != null && !list.isEmpty()) {

				for (String taskId : list) {

					byte[] bytes = zk.getData(Const.APP_NAME + ZK_SEPARATOR
							+ "client" + ZK_SEPARATOR+clientNode+ZK_SEPARATOR + taskId, false, null);
					
					if(bytes==null)
						continue;
					List<String> valueList = ByteConvert.byte2List(bytes);
					if(valueList==null)
						continue;
					runningMaps.put(taskId, valueList);
					zk.delete(Const.APP_NAME + ZK_SEPARATOR
							+ "client" + ZK_SEPARATOR+clientNode+ZK_SEPARATOR + taskId, -1);
				}
				
				//加上心跳
				//IndexServer.hbthread.updateLastTime(clientNode);

			}
			
			
			
			
			
		}
		
		
		
		
		
		
	

		/**
		 * 2.设置url
		 */

		zk.setData(Const.APP_NAME + ZK_SEPARATOR + "URL",
				("http://" + IP.getIP() + ":" + Const.JETTY_PORT + "/index")
						.getBytes(), -1);

		/**
		 * 启动
		 */

		IndexServer server = new IndexServer();
		server.start(runningMaps);
		new Thread(new ZkClientThread()).start();
	}

	public void process(WatchedEvent event) {

		EventType type = event.getType();

		LOG.info("event happend........type is:" + type);

		if (event.getState() == KeeperState.SyncConnected) {
			signal.countDown();
		}

		if (type == EventType.NodeChildrenChanged) {

			masterRun();

			// 重新注册

			try {
				zk.getChildren(Const.APP_NAME + ZK_SEPARATOR + "server", this);
			} catch (KeeperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		// TODO Auto-generated method stub
		// ZKThread thread = new ZKThread();

	}

}
