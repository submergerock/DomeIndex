package org.cProc.log.loganalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.cProc.log.createIndexLog.IndexLogger;



public class UpLoadThread  implements Runnable{
	
	 private static Log logger = LogFactory.getLog(UpLoadThread.class); 
	
	private BlockingQueue<String> queue = new ArrayBlockingQueue<String>(1024*10);
	private final String fileSepraterString = File.separator;
	private final String hdfsSepraterString="/";
	private FileSystem fs = null;
	private String archivePath ;
	private  volatile boolean exit = false;
	private Thread uploadThread = null;
	private CountDownLatch latch = new  CountDownLatch(1);
	 


	public UpLoadThread(FileSystem fs ,String archivePath )
	{
		try {
			Configuration conf = new Configuration();
			conf.set("fs.default.name", fs.getConf().get("fs.default.name") );
			this.fs =  FileSystem.get(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.archivePath = archivePath;
	}
	
	public void uploadLogFile(String file)
	{
		queue.add(file);
	}
	
	
	public void threadExit()
	{
		exit = true;
		uploadThread.interrupt();
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	

	
	public void run()
	{
		uploadThread = Thread.currentThread();
		while( !exit)
		{
				try {
					String file = queue.take();
					
					upFileToHDFS(file);
				
				} catch (InterruptedException e) {
				
				}
		}
		if(queue.size() > 0)
		{
		  	ArrayList<String> messageList =new   ArrayList<String>();
		  	queue.drainTo(messageList);
				for (int i = 0; i < messageList.size(); i++) {
					upFileToHDFS(messageList.get(i));
					}
		}
		latch.countDown();
		logger.info("logUpLoad thread-----exit");
	}
	
	
	private void upFileToHDFS(String localFileName)
	{
		boolean upload = false;
		String toFile = archivePath+hdfsSepraterString+localFileName.substring(localFileName.lastIndexOf(fileSepraterString), localFileName.length());
		Path toPath = new Path(toFile);
		logger.info("begin  upload  :" + localFileName+"   to  " +  toFile);
		while(!upload)
		{
			try {
				logger.info("1111111111111");
				if(fs.exists(toPath))
				{
					logger.info("2222222222");
						fs.delete(toPath, true);
				}
				fs.copyFromLocalFile(new Path(localFileName),toPath);
				File localFile = new File(localFileName);
				localFile.delete();
				upload = true;
			} catch (IOException e) {
				e.printStackTrace();
				upload = false;
			}
		}
		logger.info("end  upload  :" + localFileName+"   to  " +  toFile);

	}
	

}
