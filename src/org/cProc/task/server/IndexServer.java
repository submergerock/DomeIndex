package org.cProc.task.server;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class IndexServer {

	public static  FindNewTaskThread taskThread;
	private WebThread webThread;
	private Thread task;
	private Thread web;
	public static HeartBeatThread hbthread;
	

	public void start(Map<String,List<String>> maps) throws IOException
	{
		if(Const.SERVER_RUNNING.get())
			return;
		Const.SERVER_RUNNING.set(true);
		
		taskThread = new FindNewTaskThread(maps);
		
		task = new Thread(taskThread);
		
		
		task.start();
		
		webThread = new WebThread(taskThread);
		
		web = new Thread(webThread);
		
		
		web.start();
		
		hbthread = new HeartBeatThread();
		hbthread.start();
		
	}

	public void stop() {
		Const.SERVER_RUNNING.set(false);
		task.stop();
		web.stop();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
