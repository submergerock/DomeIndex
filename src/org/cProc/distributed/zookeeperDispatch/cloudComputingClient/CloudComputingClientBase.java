package org.cProc.distributed.zookeeperDispatch.cloudComputingClient;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;



public class CloudComputingClientBase  {

	
	   	protected  ZooKeeper zk = null;
	    protected  Integer mutex;
	    public CloudComputingClientBase() {

	    }
	    synchronized public void process(WatchedEvent event) {
	        synchronized (mutex) {
	        	System.out.println("@gyyEventState:"+event.getState());
	        	System.out.println("@gyyEvent is " + event.getType());
	            mutex.notify();
	        }
	    }
	    
	    synchronized public void notifyMainThread()
	    {
	    	synchronized (mutex) {
					mutex.notify();
	        }
	    }
	    
	     public void waitEvent() {
	        synchronized (mutex) {
	            try {
					mutex.wait(5*60*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        }
	    }
}
