package org.cProc.distributed.zookeeperDispatch.cloudComputingClient;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.cProc.tool.Bytes;
import org.cProc.tool.myXMLReader.MyXmlReader;

public class FindAllClientIP  implements Watcher{

   	private    String  connectFileName ="";
   	private     int sessionTimeout =0;
   	private     String  app ="";
   	private     String clientTmpPath="";
   	private   ZooKeeper zk = null;
   	
    public void process(WatchedEvent event) {
    	
    }
   	
   	public   void getConfig( String fileName)
   	{
    	MyXmlReader reader = new MyXmlReader(fileName);
    	connectFileName = reader.getName("connectString");
    	sessionTimeout = Integer.parseInt( reader.getName("sessionTimeout") );
    	app = reader.getName("app");
    	clientTmpPath= 	app+"/"+reader.getName("clientTmp");
    	
    	
    	
   	}
   	
   	public FindAllClientIP( String fileName)
   	{
   		getConfig(fileName);
   		try {
			zk = new ZooKeeper(connectFileName, sessionTimeout, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
   	}
   	
   	public void printAllClientIP()
   	{
   		
   		try {
		List<String> list	= zk.getChildren(clientTmpPath, false);

		for(int i = 0 ; i < list.size() ; i ++)
		{

				String tmpClient = list.get(i);
				String peiPath = Bytes.toString(zk.getData(clientTmpPath+"/"+tmpClient, null, null));
				String ip			=  Bytes.toString(zk.getData(peiPath,null,null));
				System.out.println(" app is " + app + " client is " + ip);
		}
		
		for(int i = 0 ; i < list.size() ; i ++)
		{
				String tmpClient = list.get(i);
				String peiPath = Bytes.toString(zk.getData(clientTmpPath+"/"+tmpClient, null, null));
				String ip			=  Bytes.toString(zk.getData(peiPath,null,null));
				List<String>  tasks = zk.getChildren(peiPath, null);
				for(int m = 0 ; m < tasks.size() ; m++ )
				{
					String  task = Bytes.toString(zk.getData(peiPath+"/"+tasks.get(m), null, null));
					if(task != null && task.length() != 0)
					{
						System.out.println(" app is " + app + " client is " + ip + " task  is " + task);
					}
				}
		}
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   	}
   	
	public  static void main(String[] args)
	{
		for(int i = 0 ; i < args.length ; i++)
		{
			FindAllClientIP ip = new FindAllClientIP( args[i]);
			ip.printAllClientIP();
		}
	}
	
}
