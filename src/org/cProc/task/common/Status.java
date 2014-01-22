package org.cProc.task.common;

import java.io.Serializable;
import java.util.List;

public class Status implements Serializable,Comparable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//存放通知目录名称的list
	private List<String> tasks;
	//每个任务对应的UUID
	private String taskId;
	//任务处理结果的状态信息，为0时表示正常处理结束
	private Integer status=-1;
	
	private String ip;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<String> getTasks() {
		return tasks;
	}

	public void setTasks(List<String> tasks) {
		this.tasks = tasks;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("taskid:").append(getTaskId());
		sb.append(",path size is:").append(tasks.size()).append(",content is:");
		
		for(String task:tasks)
		{
			sb.append(task).append(",");
		}
		
		return sb.toString();
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
