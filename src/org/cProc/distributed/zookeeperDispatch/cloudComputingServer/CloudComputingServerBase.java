package org.cProc.distributed.zookeeperDispatch.cloudComputingServer;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class CloudComputingServerBase  implements Watcher{

	   protected  ZooKeeper zk = null;
	    protected  Integer mutex;
	    
	    public CloudComputingServerBase() {

	    }
	    
	    synchronized public void process(WatchedEvent event) {
	        synchronized (mutex) {
	            mutex.notify();
	        }
	    }
	    
	    synchronized public void notifyMainThread()
	    {
	    	synchronized (mutex) {
					mutex.notify();
	        }
	    }
	    
	     public void waitEvent( ) {
	        synchronized (mutex) {
	            try {
					mutex.wait(5*60*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        }
	    }
	
}
