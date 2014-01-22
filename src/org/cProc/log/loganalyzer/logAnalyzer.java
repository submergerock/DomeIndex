package org.cProc.log.loganalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.FileSystem;
import org.cProc.log.createIndexLog.IndexThreadInfor;
import org.cProc.log.createIndexLog.MessageInfor;



public class logAnalyzer {
//	
//	 private static Log logger = LogFactory.getLog(logAnalyzer.class); 
//	
//	private MessageInfor messageInfor = new MessageInfor();
//	private logContent content = null;
//	private IndexThreadInfor indexInfor = null;
//	private String currentMessage = "";
//	private  volatile boolean exit = false;
//	
//	private Thread thread = null;
//	private CountDownLatch latch = new  CountDownLatch(1);
//	
//	private BlockingQueue<String> messageQueue = new ArrayBlockingQueue<String>(1024*24, true);
//	
//	public logAnalyzer(IndexThreadInfor indexInfor , FileSystem fs , String achivePath ,UpdateIndexMapInfor  update )
//	{
//		this.indexInfor = indexInfor;
//		content = new logContent(indexInfor);		
//		content.setUpdateIndexMapInfor(update);
//	}
//	
//	
//	public void addFileToAnalyze(String message)
//	{
//		messageQueue.add(message);
//	}
//	
//	
//	public void threadExit()
//	{
//		exit = true;
//		thread.interrupt();
//		try {
//			latch.await();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public   void run()
//	{
//		thread = Thread.currentThread();
//		  while( !exit)
//		  {
//					try {
//						currentMessage = messageQueue.take();
//						logger.info(currentMessage);
//						if(messageInfor.parseLogLine(currentMessage))
//						{
//							content.addContent(messageInfor);
//						}
//					} catch (InterruptedException e) {
//						
//					}
//		  }
//		  
//		  	ArrayList<String> messageList =new   ArrayList<String>();
//		  	messageQueue.drainTo(messageList);
//			if (messageList.size() > 0) {
//				for (int i = 0; i < messageList.size(); i++) {
//					logger.info(messageList.get(i));
//					if (messageInfor.parseLogLine(messageList.get(i))) {
//						content.addContent(messageInfor);
//					}
//				}
//			}
//			logger.info("logAnalyzer thread-----exit");
//			latch.countDown();
//	}
//	
//
//	
//
//	
}
