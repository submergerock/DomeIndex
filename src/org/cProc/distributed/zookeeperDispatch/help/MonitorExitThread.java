package org.cProc.distributed.zookeeperDispatch.help;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;


public class MonitorExitThread implements Runnable ,Watcher {
	 private static Log logger = LogFactory.getLog(MonitorExitThread.class); 
	
	private ThreadExit threadExit = null;
	private ZooKeeper zk = null;
	private String exitPath = ""; 
	private Integer mutex = new Integer(1);
	private boolean expire = false;
	private boolean connect = false;
	private CountDownLatch latch = new CountDownLatch(1);
	
	private String conncetString = "";
	private int seesionTimeOut = 0;
	public MonitorExitThread(ThreadExit threadExit ,String connect, int  sessionTimeout ,String exitPath)
	{
		this.conncetString = connect;
		this.seesionTimeOut = sessionTimeout;
		this.threadExit = threadExit;
		   try {
			zk = new ZooKeeper(conncetString, seesionTimeOut, this);
			this.exitPath = exitPath;
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			latch.await(5, TimeUnit.MINUTES);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
	}
	
	public void run()
	{

		while (true) {
			try {
				zk.getChildren(exitPath, true);
	            synchronized (mutex) {
	            	mutex.wait();
	            }
			} catch (KeeperException e) {
				if(expire)
				{
					try {
						zk.close();
						zk = null;
						connect = false;
						zk = new ZooKeeper(conncetString, seesionTimeOut, this); 
					    latch = new CountDownLatch(1);
					    latch.wait(5*60*1000);
					} catch (Exception e1) {
					}
					continue;
				}
			} catch (InterruptedException e) {
			
			}
			threadExit.threadExit();
			break;
		}
		logger.info(" MonitorExitThread  is exit");
	}
	
	
    synchronized public void process(WatchedEvent event) {
    	
    	if(event.getState() == KeeperState.Expired)
    	{
    		logger.info("Expired*************************************");
    		expire = false;
    	}
    	if(event.getState() == KeeperState.SyncConnected)
    	{
    		if( !connect )
    		{
    			 latch.countDown();
    			 connect = true;
    		}
    	
    		
    	}    	
    	if(event.getType() == Event.EventType.NodeChildrenChanged)
    	{
            synchronized (mutex) {
                mutex.notify();
            }
    	}
    	

    }

	
	

}
