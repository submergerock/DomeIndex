package org.cProc.distributed.zookeeperDispatch.help;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;


public class ZKHelper {
	
	public static final int tryTime = 10;
	public static final int tryInternel = 10000;
	
	public static List<String> getPathChildNode(ZooKeeper zk,String node , boolean watch )
	{
		List<String> orglist = null;
		int count = 0;
		  while(orglist == null && count < tryTime)
		   {	
	 		   count++;
				try {
					orglist = zk.getChildren(node, watch);
					if(orglist== null)
					{
						System.out.println("@gyy:getZKNodeData:failed"+"try time is " + count);
						try {
							Thread.sleep(tryInternel);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					else {
						return orglist;
					}
				} catch (KeeperException e) {
					System.out.println("@gyy:getZKNodeData:getData KeeperException");
					e.printStackTrace();
				} catch (InterruptedException e) {
					System.out.println("@gyy:getZKNodeData:getData InterruptedException");
					e.printStackTrace();
				}
		   }
			return orglist;
	}
	
	
	public static byte[] getZKNodeData(ZooKeeper zk ,String node)
	{
		byte[] data = null;
		int count = 0;
 	   while(data == null && count < tryTime)
	   {	
 		   count++;
			try {
				data = zk.getData(node, null, null);
			} catch (KeeperException e) {
				System.out.println("@gyy:getZKNodeData:getData KeeperException");
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.out.println("@gyy:getZKNodeData:getData InterruptedException");
				e.printStackTrace();
			}
			if(data== null)
			{
				System.out.println("@gyy:getZKNodeData:failed"+"try time is " + count);
				try {
					Thread.sleep(tryInternel);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
	   }
 	   return data;
	}
	
	public static boolean deleteNodeData(ZooKeeper zk ,String node)
	{
		byte[] data = null;
		int count = 0;
 	   while(data == null && count < tryTime)
	   {	
 		   count++;
			try {
				zk.delete(node, -1);
				return true;
			} catch (KeeperException e) {
				System.out.println("@gyy:ZKHelper:deleteNodeData:  KeeperException");
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.out.println("ZKHelper:deleteNodeData: InterruptedException");
				e.printStackTrace();
			}
			if(data== null)
			{
				System.out.println("ZKHelper:deleteNodeData::failed"+"try time is " + count);
				try {
					Thread.sleep(tryInternel);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
	   }
 	   return false;
	}
	
	
	public static boolean createPesiNode(ZooKeeper zk ,String node , byte[] data)
	{
		int count = 0;
 	   while( count < tryTime)
	   {	
 		   count++;
			try {
				  if(zk.exists(node, false) == null)
				  {
			    	  zk.create(node, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				  }
				return true;
			} catch (KeeperException e) {
				System.out.println("@gyy:ZKHelper:createPesiNode:  KeeperException");
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.out.println("ZKHelper:createPesiNode: InterruptedException");
				e.printStackTrace();
			}
				System.out.println("ZKHelper:createPesiNode:failed"+"try time is " + count);
				try {
					Thread.sleep(tryInternel);
				} catch (InterruptedException e) {
					e.printStackTrace();
			}
	   }
 	   return false;
	}
	
	public static boolean createPesiSeqChildNode(ZooKeeper zk ,String node , byte[] data)
	{
		if(zk == null)
		{
			return false;
		}
		int count = 0;
 	   while( count < tryTime)
	   {	
 		   count++;
			try {
			      zk.create(node+"/", data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
				return true;
			} catch (KeeperException e) {
				System.out.println("@gyy:ZKHelper:createPesiNode:  KeeperException");
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.out.println("ZKHelper:createPesiNode: InterruptedException");
				e.printStackTrace();
			}
				System.out.println("ZKHelper:createPesiNode:failed"+"try time is " + count);
				try {
					Thread.sleep(tryInternel);
				} catch (InterruptedException e) {
					e.printStackTrace();
			}
	   }
 	   return false;
	}
	
	

}
