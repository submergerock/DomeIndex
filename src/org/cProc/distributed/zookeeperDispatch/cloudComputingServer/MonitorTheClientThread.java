package org.cProc.distributed.zookeeperDispatch.cloudComputingServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.cProc.distributed.createIndex.ReadFromHDFSClient;
import org.cProc.tool.Bytes;


//���ã������ʱ�ļ���?
public class MonitorTheClientThread implements Watcher,Runnable{
	
	 private static Log Log4jlogger = LogFactory.getLog(MonitorTheClientThread.class); 
	
	Integer mutex = -1;
	CloudComputingServer server = null;
	ZooKeeper zk = null;
	String  clientPath ="";
	private CountDownLatch connectSignal = new CountDownLatch(1);
	List<String> lastDirNode = new ArrayList<String>(); //tmpPath
	HashMap<String,String> map = new HashMap<String,String>();
	 String cut="@";
	 private volatile boolean exit = false; 
	 private Thread currentThread = null;
	 private  CountDownLatch exitLatch = new  CountDownLatch(1);
	
	
	public MonitorTheClientThread(CloudComputingServer lserver,String connectString , int sessionTimeout ,String lclientPath)
	{
		server = lserver;
		//ex. /TestCreateIndex/Client/Tmp
		clientPath = lclientPath;
		  if(zk == null){
	            try {
	                System.out.println("����һ���µ�����:���client��״̬");
	                //创建ZK，注册Watch，当ZK变化时，触发process方法 
	                zk = new ZooKeeper(connectString, sessionTimeout, this);
	                connectSignal.await();//线程挂起直到connectSignal为0,process中发现与ZK建立稳定连接后connectSignal为0
		               if( zk.exists(clientPath, false) == null)
		               {
		            	   //创建一个/TestCreateIndex/Client/Tmp持久化节点
		            	   zk.create(clientPath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		               }
	                mutex = new Integer(-1);
	            } catch (Exception e) {
	                zk = null;
	                e.printStackTrace();
	            }
	        }
	}
	
	public void run()
	{     

		while(!exit)
		{
			currentThread = Thread.currentThread();
			 try {
				 ///TestCreateIndex/Client/Tmp下的内容
				  List<String> orglist = zk.getChildren(clientPath, true);
				  List<String> orglistOp = new ArrayList<String>();
				  List<String> oldPathOp = new ArrayList<String>();
				  int i = 0;
				  for( i = 0 ; i < orglist.size() ; i++)
				  {
					  orglistOp.add(orglist.get(i));
				  }
				  for( i = 0 ; i < lastDirNode.size(); i++)
				  {
					  oldPathOp.add(lastDirNode.get(i));
				  }
                  //orglistOp里为orglist-lastDirNode差集
				  orglistOp.removeAll(lastDirNode);   
				  //oldPathOp里为lastDirNode-orglist的差集
				  oldPathOp.removeAll(orglist);	
				  //lastDirNode用来存放上-次/TestCreateIndex/Client/Tmp节点下的内容
				  //下面两行代码等同于lastDirNode=orglist，即把本次的orglist放入lastDirNode用作下次比较用
				  lastDirNode.addAll(orglistOp);
				  lastDirNode.removeAll(oldPathOp);
				 
				  //将orglist里不包含在lastDirNode里的节点存入map
				  for(int m = 0 ; m < orglistOp.size() ; m++)
				  {
					  map.put(orglistOp.get(m),
							  Bytes.toString(zk.getData(clientPath+"/"+orglistOp.get(m),null,null))
							  );
					  String pesiPath = Bytes.toString(zk.getData(clientPath+"/"+orglistOp.get(m), null, null));
					  System.out.println("content is " + pesiPath);

				  }
			  
				  //将lastDirNode中不包含在orglist的内容取出，即取出失效的client节点的持久化节点
				  for(int m = 0 ; m < oldPathOp.size() ; m++)
				  {
					  //ex. /TelecomProject/Client/Pei/0000000263
					  String filePath = map.get(oldPathOp.get(m));
					  if(server.isActive())
					  {
						  Thread thread = new Thread(new BalanceWhenFailThread(server,zk,filePath,clientPath));
						  thread.start();
					  }
				  }
				 
				  synchronized (mutex) {
				            mutex.wait();//扶起，当process()发现ZK有变化时再重复执行
				   }
			} catch (KeeperException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		exitLatch.countDown();
		Log4jlogger.info("MonitorTheClientThread  is exit");
		
	}
	
		public void process(WatchedEvent event) {
			//ZK节点变化时通知线程继续执行
			if(event.getType() == Event.EventType.NodeChildrenChanged)
	    	{	
	    		  synchronized (mutex) {
	    	            mutex.notify();
	    	        }
	    	}
			//和ZK建立稳定连接后执行构造函数await后面的代码
	    	if(event.getState() == KeeperState.SyncConnected)
	    	{	
	    		connectSignal.countDown();
	    		return ;
	    	}  
    }
		
		public void exit()
		{
			exit = true;
			currentThread.interrupt();
			try {
				exitLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		

}
