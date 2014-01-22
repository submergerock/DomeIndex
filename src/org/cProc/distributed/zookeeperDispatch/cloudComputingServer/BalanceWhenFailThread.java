package org.cProc.distributed.zookeeperDispatch.cloudComputingServer;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.cProc.distributed.zookeeperDispatch.cloudComputingClient.RequestStateChangeHelper;
import org.cProc.distributed.zookeeperDispatch.help.ZKHelper;
import org.cProc.tool.Bytes;

public class BalanceWhenFailThread implements Runnable {

	private ZooKeeper zk;
	private String path;
	private int chooseNumber;
	private String  clientTmp ="";
	private RequestStateOperater operater = null;
	public  BalanceWhenFailThread(RequestStateOperater operater,ZooKeeper lzk,String lpath,String lclientPath)
	{
		zk = lzk;
		//ex. /TelecomProject/Client/Pei/0000000263
		path = lpath;
		chooseNumber = 0;
		clientTmp = lclientPath;
		this.operater = operater;
	}
	
	//将失效client的持久化节点拷贝到有效的client下
	public void run()
	{
		List<String> subFiles = new ArrayList<String>();
		do
		{
			try {
				//取出失效client的持久化节点下的数据
				subFiles = zk.getChildren(path, false);
			} catch (KeeperException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < subFiles.size(); i++) {
				String reslut = ChoosePath();
				String from = path + "/" + subFiles.get(i);
				String pathName = reslut + "/"+ (0 - Math.abs(Long.parseLong(subFiles.get(i))));
				System.out.println("move the file " + from +" TO " +  pathName);
				try {
					if(checkMessage(zk,from))
					{
						String reslutPath = zk.create(pathName, zk.getData(from, null, null), ZooDefs.Ids.OPEN_ACL_UNSAFE,
								CreateMode.PERSISTENT);
						if(reslutPath == null )
						{
							System.out.println("move the file " + from +" TO " +  pathName + " IS Failed");
						}
					}
					zk.delete(from, -1);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			try {
				subFiles = zk.getChildren(path, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}while(subFiles.size() != 0);
		try {
			zk.delete(path, -1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//@:return true; create new node that have the same node; then delete that node
	//@:return false :delete the node;
	public boolean  checkMessage(ZooKeeper zk ,String node)
	{
		 byte[]  data = ZKHelper.getZKNodeData(zk, node);
		 //if no request, then 
		 if(data == null)
		 {
			 return false  ;
		 }
		 String message = new String(data);
		 int state =  RequestStateChangeHelper.getMessageType(message);
		 if(state == RequestStateChangeHelper.requetStartUpLoadState)
		 {
			 //chech the indexfile ,if exits ,then delete;
			 operater.requestRollBack(message);
			 return true;
		 }
		 return false;
	}
	
	//挑选一个有效client的持久化节点作为目标节点用来存入失效节点下的数据
	public String ChoosePath()
	{
 	   List<String> orglist = null;
	   try {
		orglist = zk.getChildren(clientTmp,false);
		while(orglist.size() == 0)
		{
        	  try {
  				Thread.sleep(1000);
  			} catch (InterruptedException e) {
  				e.printStackTrace();
  			}
			orglist = zk.getChildren(clientTmp,false);
		}
		
	} catch (KeeperException e) {
		e.printStackTrace();
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
    String[] nodes = orglist.toArray(new String[orglist.size()]);
    Arrays.sort(nodes);
    
    byte[] reslut  = null;
    while(reslut  == null)
    {
    	  try {
			reslut = zk.getData( clientTmp+"/"+nodes[chooseNumber%nodes.length], false,null);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	  chooseNumber++;
    	  if( reslut != null)
    	  {
    		  return  Bytes.toString(reslut);
    	  }
    	  try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    return  Bytes.toString(reslut);
	}
	
	
}
