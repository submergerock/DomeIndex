package org.cProc.task.server;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import org.cProc.task.common.IRemote;
import org.cProc.task.common.Status;
import org.cProc.tool.CommonUtils;

import com.caucho.hessian.server.HessianServlet;

public class IRemoteImpl extends HessianServlet implements IRemote {

	public static final ConcurrentHashMap<String, List<String>> MAPS = new ConcurrentHashMap<String, List<String>>();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(IRemoteImpl.class);

	private FindNewTaskThread thread;

	
	public  static final ConcurrentHashMap<String, List<String>> FAILED = new ConcurrentHashMap<String, List<String>>();
	
	private FileSystem fs = null;
	
	private static final AtomicLong COUNT = new AtomicLong(0);

	public IRemoteImpl(FindNewTaskThread thread) {
		
		
		
		
		this.thread = thread;
		this.fs = thread.getFS();
	}
	


	public synchronized Status getTask(String ip) {
		
		if(!FAILED.isEmpty())
		{
			
			
			
			Set<String> set = FAILED.keySet();
			String taskId = null; 
			for(String str:set)
				taskId = str;
			
			
			LOG.info("failed task id is:"+taskId); 
			LOG.info("get call task request from:"+ip);
			// TODO Auto-generated method stub
			Status status = new Status();
			status.setTasks(FAILED.get(taskId));
			status.setTaskId(taskId);
			status.setIp(ip);
			if(!status.getTasks().isEmpty()){
			   ZkClientThread.STATUS_QUEUE.add(status);
			   MAPS.put(status.getTaskId(), status.getTasks());
			   LOG.info("add content is:"+status.toString());
			}
			
			FAILED.remove(taskId);
			IndexServer.hbthread.updateLastTime(ip);
			
			return status;
			
			
			
		}
		
		LOG.info("get call task request from:"+ip);
		// TODO Auto-generated method stub
		Status status = new Status();
		status.setTasks(this.thread.getTasks());
		status.setTaskId(CommonUtils.getUUID());
		status.setIp(ip);
		if(!status.getTasks().isEmpty()){
		   ZkClientThread.STATUS_QUEUE.add(status);
		   
		   MAPS.put(status.getTaskId(), status.getTasks());
		   LOG.info("add content is:"+status.toString());
		}
		
		IndexServer.hbthread.updateLastTime(ip);
		
		return status;
	}

	public synchronized void reportStatus(Status status) {
		// TODO Auto-generated method stub
		//
		LOG.info("reportStatus task request from :"+status.getIp());
		String taskId = status.getTaskId();

		// 删除通知目录

		LOG.info("remove task id:"+taskId);
		
		List<String> tasks = MAPS.get(taskId);
		COUNT.addAndGet(tasks.size());
		for (String str : tasks) {
			try {
				LOG.info("delete notify path:"+Const.NOTIFY_PATH +"/"+ str);
				fs.delete(new Path(Const.NOTIFY_PATH +"/"+ str), true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOG.error("delete notify error:"+str);
			}
		}
		IndexServer.hbthread.updateLastTime(status.getIp());
		MAPS.remove(taskId);
		
		thread.removeRunning(tasks);

		
		// 删除zk
		ZkClientThread.STATUS_QUEUE.add(status);
	}

	public void ping(String ip) {
		
		LOG.info("get ping request  from:"+ip);
		IndexServer.hbthread.updateLastTime(ip);
		//TODO
		
		

	}



	
	public void addFailedTask(String taskId, List<String> list) {
		// TODO Auto-generated method stub
		
		if(list==null || list.isEmpty())
			return;
		
		if(taskId==null)
			return;
		
		
		//查看running是否有
		
		FAILED.put(taskId, list);
		
		
	}



	
	public Set<String> getLiveNodes() {
		// TODO Auto-generated method stub
		
		return HeartBeatThread.clients.keySet();
	}



	@Override
	public Long getTotalProcess() {
		// TODO Auto-generated method stub
		return COUNT.get();
	}

}
