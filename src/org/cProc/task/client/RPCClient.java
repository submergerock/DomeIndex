package org.cProc.task.client;

import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.cProc.distributed.zookeeperDispatch.cloudComputingClient.CloudComputingClient;
import org.cProc.distributed.zookeeperDispatch.cloudComputingServer.IP;
import org.cProc.distributed.zookeeperDispatch.help.ZKHelper;
import org.cProc.task.common.IRemote;
import org.cProc.task.common.Status;
import org.cProc.tool.Bytes;
import org.cProc.tool.Pair;
import org.cProc.tool.myXMLReader.MyXmlReader;

import com.caucho.hessian.client.HessianProxyFactory;

public class RPCClient implements Watcher{
	private static Log log = LogFactory.getLog(CloudComputingClient.class); 

	//server端url
	private String serverIP = null;
	//ZK上server端url的存放节点
	private String serverUrl;
	private ZooKeeper zk = null;
	//配置文件
	private String confFile;
	//zk连接 路径  
	private String connectFileName;
	
	private Integer sessionTimeout;
	
	HessianProxyFactory factory = new HessianProxyFactory();
	
	private int stepSecond = 1;
	
	private String app;
	
	private CountDownLatch connectedSignal = new CountDownLatch(1);
	
	public RPCClient(String confFileStr) {
		this.confFile = confFileStr;
		MyXmlReader reader = new MyXmlReader(confFile);
		this.connectFileName = reader.getName("connectString");
		this.sessionTimeout = Integer.parseInt(reader.getName("sessionTimeout"));
		this.serverUrl = reader.getName("serverUrl");
		if (reader.getName("stepSecond") != null)
			stepSecond = Integer.parseInt(reader.getName("stepSecond"));
		
		app = reader.getName("app");
	}
	
	//初始化ZK
	private synchronized void  initZookeeper() {
	    if(zk != null) {
	    		return ;
	    }
	    
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
			
		if (zk != null) {
			try {
				zk.close();
				zk = null;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		log.info("init zookeeper");
	    while(zk == null ){
            try {
            	zk = new ZooKeeper(connectFileName, sessionTimeout, this);
            	connectedSignal.await();
            } catch (Exception e) {
                zk = null;
                e.printStackTrace();
            }
	    }
	    String path =  app + "/client/" + IP.getIP();
    	try {
    		Stat stat = zk.exists(path, null);
    		if(stat == null)
			zk.create(path, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    log.info("init  zk  is OK ");
	}
	
	//ping server，确定server端是否可用
	private boolean checkServer() {
		boolean isOK = false;
		try {
			//第一次需从ZK上取server url
			if (serverIP == null || serverIP.equals("")) {
				getServerIP();
			}
//			System.out.println("ping............" + serverIP);
			IRemote remote = (IRemote) factory.create(IRemote.class, serverIP);
			String ip = IP.getIP();
			remote.ping(ip);
			isOK = true;
		} catch (Exception e) {
//			System.out.println("ping............" + serverIP + " error!!!!!!!!!!!!!!!");
			log.error(e.getMessage());
		} 
		return isOK;
	}
	
	private void getServerIP() throws Exception {
		initZookeeper();
		Stat s = zk.exists(serverUrl, false);
		if (s != null) {
		    serverIP = Bytes.toString(ZKHelper.getZKNodeData(zk, serverUrl));
//		    System.out.println("serverUrl===================" + serverIP);
		}
	}
	
	//从server端取任务
	public Pair getTaskFromServer() throws MalformedURLException {
		Pair task = null;
		int i = 0;
		while (!checkServer()) {
			try {
				getServerIP();
				i++;
				//ping不通时，每次ping的间隔增加1秒
				Thread.sleep(i * 1000 * stepSecond);
				if(i>=10)
					i=0;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(e.getMessage());
			}
		}
		IRemote remote = (IRemote) factory.create(IRemote.class, serverIP); 
		log.info("begin send request for:"+serverIP);
		String ip = IP.getIP();
		Status s = remote.getTask(ip);
		s.setIp(ip);
		List<String> tasks = s.getTasks();
		if (tasks != null && !tasks.isEmpty()) {
			String taskStr = "";
			Iterator<String> ite = tasks.iterator();
			while(ite.hasNext()) {
				taskStr += (ite.next() + "@");
			}
			taskStr = taskStr.substring(0, taskStr.length()-1);
			task = new Pair(taskStr,s.getTaskId());
//			System.out.println("task================="+taskStr);
		}
		
		return task;
	}
	
	//向server端返回处理结果
	public boolean reportStatus(Status s) {
		boolean rtn = false;
		try {
			int i = 0;
			while (!checkServer()) {
				try {
					getServerIP();
					i++;
					Thread.sleep(i * 1000 * stepSecond);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			try {
				IRemote remote = (IRemote) factory.create(IRemote.class, serverIP);
				String ip = IP.getIP();
				s.setIp(ip);
				remote.reportStatus(s);
				rtn = true;
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("report error! taskid:" + s.getTaskId());
			}
			
		} catch(Exception e) {
			log.error(e.getMessage(),e);
		}
		return rtn;
	}

	public static void main(String[] srts) {
		try {
		HessianProxyFactory factory = new HessianProxyFactory();
		IRemote remote = (IRemote) factory.create(IRemote.class, "http://192.168.0.106:55555/index"); 
		String ip = IP.getIP();
		remote.ping(ip);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(WatchedEvent event) {
		// TODO Auto-generated method stub
		if (event.getState() == KeeperState.SyncConnected) {
			connectedSignal.countDown();
		}
	}
}
