package org.cProc.task.common;

import java.util.List;
import java.util.Set;

/**
 * hessian远程调用的方法
 * @author zxc
 *
 */
public interface IRemote {
	
	
	public  Status getTask(String ip);
	
	
	
	public void  reportStatus(Status status);
	
	
	public void ping(String ip);
	
	
	public void addFailedTask(String taskId,List<String> list);
	
	public Set<String> getLiveNodes();
	
	
	public Long getTotalProcess();
	
	

}
