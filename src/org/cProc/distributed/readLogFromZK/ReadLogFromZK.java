package org.cProc.distributed.readLogFromZK;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.Stat;
import org.cProc.distributed.createIndex.ReadFromHDFSSever;
import org.cProc.distributed.zookeeperDispatch.cloudComputingServer.CloudComputingServer;
import org.cProc.distributed.zookeeperDispatch.help.ZKHelper;
import org.cProc.tool.myXMLReader.MyXmlReader;

public class ReadLogFromZK   implements Watcher  {
	
	
	 private static Log Log4jlogger = LogFactory.getLog(ReadFromHDFSSever.class); 
	
	 private  String  connectString = "";
	 private  int   sessionTimeout = 5000 ;
	 private String root ="";
	 private String zklogRequestLogPathString ="";
	 private ZooKeeper zk = null;
	 private boolean zkExpires = false;
	 
	 private  volatile boolean exit  = false;
	 
	 private Integer conditon = new Integer(1);
	 
	public ReadLogFromZK(String confFileName)
	{
		//init the conf file ;
		initConf(confFileName);
		//init the zookeeper;
		initZookeeper();
		
	}
	
	
	private void  initConf(String confFileName)
	{
    	MyXmlReader reader = new MyXmlReader(confFileName);
    	connectString= reader.getName("connectString");
        sessionTimeout =Integer.parseInt(reader.getName("sessionTimeout"));
        root=reader.getName("app");
        zklogRequestLogPathString = root+"/"+CloudComputingServer.createIndexLogString;
	}
	
	private void  initZookeeper()
	{
		
    	if(zk != null &&  !zkExpires)
    	{
    		return ;
    	}
        while(zk == null || !zkExpires ){
            try {
            	Integer mutex = new Integer(-1);
                zk = new ZooKeeper(connectString, sessionTimeout, this);
                synchronized (mutex) {
                	mutex.wait(5000);
				}
                if( zkExpires )
                {
                	zk.close();
                	zk = null;
                }
            } catch (Exception e) {
                zk = null;
                e.printStackTrace();
            }
        }
        //init the zookeeper
        if (zk != null) {
            try {
                Stat s = zk.exists(root, false);
                if (s == null) {
                    zk.create(root, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
                s = zk.exists(zklogRequestLogPathString, false);
                if( s == null){
             	   zk.create(zklogRequestLogPathString, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
            } catch (KeeperException e) {
            	e.printStackTrace();
            	Log4jlogger.error("@gyyException: init zookerException: KeeperException" );
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log4jlogger.error("@gyyException: init zookerException: InterruptedException" );
            }
        }
		
	}
	
	
	private void checkLogPath()
	{
		List<String> list = ZKHelper.getPathChildNode(zk, zklogRequestLogPathString, true);
		if(list == null )
		{
			return ;
		}
		for(int i = 0 ; i < list.size() ; i ++)
		{
			Log4jlogger.info("   " + list.get(i) +"   ");
			saveIndexIntoMySQL(list.get(i));
		}
		
	}
	
	private void saveIndexIntoMySQL(String message)
	{
		System.out.println(message);
	}
	
	
	public void ReadLog()
	{
		while(!exit)
		{
			if(zkExpires)
			{
				initZookeeper();
			}
			checkLogPath();
		}

	}
	
    synchronized public void process(WatchedEvent event) {
    	if(event.getType() == Event.EventType.NodeChildrenChanged)
    	{
    		Log4jlogger.info("NodeChildrenChanged");
            synchronized (conditon) {
            	conditon.notify();
            }
    	}
    	
    	if(event.getState() == KeeperState.Expired)
    	{
    		Log4jlogger.info("Expired*************************************");
    		zkExpires = false;
    		try {
				zk.close();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		zk = null;
    	}
    	

    }
	
	public static void main(String[] args)
	{
		
	}
	

}
