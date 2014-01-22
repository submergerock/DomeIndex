package org.cProc.task.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.cProc.task.common.ByteConvert;

public class HeartBeatThread extends Thread {
	
	public static final ConcurrentHashMap<String, Long> clients = new ConcurrentHashMap<String, Long>(100);
	
	private static final long DEAD_TIME  =60*1000*30;
	
	private static final Logger LOG = Logger.getLogger(HeartBeatThread.class);
	
	
	 
	
	
	
	
	public void updateLastTime(String ip)
	{
		LOG.info("update last :"+ip);
		clients.put(ip, System.currentTimeMillis());
		
	}
	
	
	
	public void run()
	{
		
		while(true)
		{
			
			
			
			if(clients.isEmpty())
			{
				
				try {
					Thread.sleep(60*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			
			
			
			List<String> list = new ArrayList<String>();
			for(Map.Entry<String, Long> client:clients.entrySet())
			{
				
				long current = System.currentTimeMillis();
				long last = client.getValue();
				System.out.println(current+"  "+last);
				if(current-last>DEAD_TIME)
				{
					
					//挂了
					list.add(client.getKey());
					
					//获取该client下的所有任务
					//去掉 running标志,删除节点
					
					
					
					
					try {
						resetDownMachine(client.getKey());
					} catch (Exception e) {
						// TODO: handle exception
						LOG.error(e.getMessage());
					}
					
					
					
					
					
					//					if
					LOG.error("client:"+client.getKey()+" dead................");
				}
				
				
				
			}
			
			for(String ip:list)
				clients.remove(ip);
			list.clear();
			
			
			try {
				Thread.sleep(60*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
		
		
	}
	

	public void resetDownMachine(String clientIp) throws InterruptedException, KeeperException
	{
		
		//clientIp
		
		ZooKeeper zk = ZKThread.zk;
		
		
		List<String> list = zk.getChildren(Const.APP_NAME + ZKThread.ZK_SEPARATOR
				+ "client"+ ZKThread.ZK_SEPARATOR+clientIp, false);
		if (list != null && !list.isEmpty()) {

			for (String taskId : list) {

				byte[] bytes = zk.getData(Const.APP_NAME +  ZKThread.ZK_SEPARATOR
						+ "client" +  ZKThread.ZK_SEPARATOR+clientIp+ ZKThread.ZK_SEPARATOR + taskId, false, null);
				
				if(bytes==null)
					continue;
				List<String> valueList = ByteConvert.byte2List(bytes);
				if(valueList==null)
					continue;
				//
				
				IRemoteImpl.FAILED.put(taskId, valueList);
				IRemoteImpl.MAPS.put(taskId, valueList);
				
				
				zk.delete(Const.APP_NAME + ZKThread.ZK_SEPARATOR
						+ "client" + ZKThread.ZK_SEPARATOR+clientIp+ZKThread.ZK_SEPARATOR + taskId, -1);
			}
			
			//加上心跳
			//IndexServer.hbthread.updateLastTime(clientNode);

		}
	}
	

}
