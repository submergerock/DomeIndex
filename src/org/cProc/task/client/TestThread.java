package org.cProc.task.client;

import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;

import org.cProc.distributed.zookeeperDispatch.cloudComputingServer.IP;
import org.cProc.task.common.IRemote;
import org.cProc.task.common.Status;

import com.caucho.hessian.client.HessianProxyFactory;

public class TestThread implements Runnable{
	public static int i = 0;
	//@Override
	public void run() {
		HessianProxyFactory factory = new HessianProxyFactory();
		IRemote remote;
		while (1>0) {
			try {
				remote = (IRemote) factory.create(IRemote.class, "http://192.168.0.180:55555/index");
				String ip = IP.getIP();
				Status s = remote.getTask(ip);
				String taskStr = "";
				List<String> tasks = s.getTasks();
				if (tasks != null && !tasks.isEmpty()) {
					
					Iterator<String> ite = tasks.iterator();
					while(ite.hasNext()) {
						taskStr += (ite.next() + "@");
						ClientTest.i++;
					}
					taskStr = taskStr.substring(0, taskStr.length()-1);
					System.out.println("tasks=" + taskStr);
					System.out.println("count=" + ClientTest.i);
					Thread.sleep(30000);
					Status sta = new Status();
					sta.setStatus(0);
					sta.setTasks(tasks);
					sta.setTaskId(s.getTaskId());
					System.out.println("start return status...");
					
					remote.reportStatus(sta);
					System.out.println("OK");
				} else {
					Thread.sleep(1000);
				}
				
				
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
	}

	public static void main(String[] args) {
		TestThread thread = new TestThread();
		while (1> 0) {
			thread.run();
		}
	}
}
