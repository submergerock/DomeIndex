package org.cProc.log.createIndexLog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.cProc.log.loganalyzer.UpLoadThread;
import org.cProc.log.loganalyzer.UpdateIndexMapInfor;
import org.cProc.log.loganalyzer.logAnalyzer;




public class IndexLogger  {	
//	 private static Log logger = LogFactory.getLog(IndexLogger.class); 
//	
//	
//	//1.writer to local 
//	//2.upload to hdfs;
//	//3.if hdfs can't connect ,then stay in the local ;
//
//	
//	private final String fileSepraterString = File.separator;
//	private final String IP ;
//	private final String logPath ;
////	private final String achivePath ;
//	private final String oldLogName ="";
//	
//	private  String logFilePath="";
//	private long logNumber = 0;
//	private long logStartTime = 0;
//	private FileWriter writer = null;
//	private logAnalyzer analyzerThread = null;
//	private IndexThreadInfor threadInfor = null;
//	private ExecutorService changeLogExecutor = null;
//	
//	private ExecutorService upLoadThreadExecutor = null;
//	private UpLoadThread upLoadThread = null;
//
//	private Thread fixedFreshThread = null;
//	private CountDownLatch freshLatch = new  CountDownLatch(1);
//
//	
//	private  volatile boolean over = false;
//	
//	private  ArrayList<String> analyzerMessage = new    ArrayList<String>();
//	
//	private ArrayList<Thread> threadArrayList = new ArrayList<Thread>();
//	
//	FileSystem fs = null;
//	private BlockingQueue<String> messageQueue = new ArrayBlockingQueue<String>(1024*100, true);
//	protected  IndexLogger(FileSystem fs ,String IP,String logPath,String achivePath , IndexThreadInfor threadInfor  ,UpdateIndexMapInfor update)
//	{
//		this.fs = fs;
//		this.logNumber = 0;
//		this.logStartTime =System.currentTimeMillis();	
//		this.IP = IP;
//		this.logPath  = logPath;
//		createLog(getNewFileName());
//		analyzerThread = new logAnalyzer(threadInfor,fs,achivePath , update);
//		Thread thread = new Thread(analyzerThread);
//		thread.start();
//		this.threadInfor = threadInfor;
//		
//		//change file in fix time
//		changeLogExecutor = Executors.newSingleThreadExecutor();
//		changeLogExecutor.execute(new Runnable() {
//			@Override
//			public void run() {
//				fixedFreshThread = Thread.currentThread();
//				while(!over)
//				{
//					try {
//						Thread.sleep(10*60*1000);
//					} catch (InterruptedException e) {
//						endLogFile();
//						break ;
//					}
//					changeLogFile();
//				}
//				freshLatch.countDown();
//			}
//		
//		});
//		upLoadThread = new UpLoadThread(fs,achivePath);
//		upLoadThreadExecutor = Executors.newSingleThreadExecutor();
//		upLoadThreadExecutor.execute(upLoadThread);
//	}	
//	
//
//
//	private void createLog(String name)
//	{
//		try {
//			System.out.println("@gyy: create one log, log name is  " + name );
//			if(writer != null)
//			{
//				writer.close();
//				writer = null;
//			}
//			writer = new FileWriter(new File(name));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		 
//	}
//	
//	
//	private  void writeEndLogFileInfor()
//	{
//		if(writer != null)
//		{
//			try {
//			writer.write(TransLogMessage.startLogFileTail());
//			analyzerMessage.add(TransLogMessage.startLogFileTail());
//			
//			writer.write(System.getProperty("line.separator"));
//			 ArrayList<String> list = threadInfor.getIndexThreadsInfor();
//			 for(int i = 0 ; i < list.size() ; i++)
//			 {
//						writer.write(TransLogMessage.inforMeesageForTask("-1", threadInfor.parseIndexThreadInfor(list.get(i))));
//						analyzerMessage.add(TransLogMessage.inforMeesageForTask("-1", threadInfor.parseIndexThreadInfor(list.get(i))));
//						
//						writer.write(System.getProperty("line.separator"));
//			 }
//			 
//				writer.write(TransLogMessage.endLogFileTail());
//				analyzerMessage.add(TransLogMessage.endLogFileTail());
//								
//				writer.write(System.getProperty("line.separator"));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
//	
//	//this message is creaded by TransLogMeesage
//	public void appendLog(String message)
//	{
//		messageQueue.add(message);
//	}
//	
//	//1.every fix time do fulsh 
//	//2. user call it
//	synchronized public void  flush()
//	{
//		if(writer == null)
//		{
//			return;
//		}
//		ArrayList<String> list = new ArrayList<String>();
//		messageQueue.drainTo(list);		
//		try {
//		for(int i = 0 ; i < list.size() ; i++)
//		{
//				writer.write(list.get(i));
//				analyzerMessage.add(list.get(i));
//				writer.write(System.getProperty("line.separator"));
//		}
//			writer.flush();
//			messageToAnalyzer();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void messageToAnalyzer()
//	{
//		for(int i = 0 ; i < analyzerMessage.size() ; i++)
//		{
//			logger.info("***"+analyzerMessage.get(i));
//			analyzerThread.addFileToAnalyze(analyzerMessage.get(i));
//		}
//		analyzerMessage.clear();
//	}
//	
//	
//	
//	
//	//1.every fix time do changeLogFile();
//	synchronized public void changeLogFile()
//	{
//		//flush the buffer ;
//		flush();
//		messageToAnalyzer();
//		endLogFile();
//		//create the new file
//		createLog(getNewFileName());
//
//	}
//	
//	synchronized public void endLogFile()
//	{
//		if(writer == null)
//		{
//			return;
//		}
//		 flush();
//		writeEndLogFileInfor();
//		messageToAnalyzer();
//		try {
//			writer.close();
//			writer= null;
//			upLoadThread.uploadLogFile(getCurrentFileName());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//		
//	private String getCurrentFileName()
//	{
//
//		return logPath+fileSepraterString+ IP+"_"+logStartTime+"_"+logNumber+".log";
//	}
//	 private String  getNewFileName()
//	{
//		 logNumber++;
//		 logFilePath =  logPath+fileSepraterString+ IP+"_"+logStartTime+"_"+logNumber+".log";
//		return logFilePath;
//	}
//	 
//	 public void threadExit()
//	 {
//		 over = true;
//			//end  the fixed time fresh thread;
//		fixedFreshThread.interrupt();
//		try {
//			freshLatch.await();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		changeLogExecutor.shutdown();
//		logger.info("fresh thread is exit************************");
//		//first,this thread send all the log infor into the  analyzer thread;
//		// and send all the file infor to the update infor;
//		
//		analyzerThread.threadExit();
//		logger.info("analyzer thread is exit************************");
//		
//		upLoadThread.threadExit();
//		
//		upLoadThreadExecutor.shutdown();
//		logger.info("upload thread is exit************************");
//
//	 }
//	 
//	 
//
//	 
//		public static void main(String args[])
//		{
//
//			FileSystem fs = null;
//			try {
//				fs = FileSystem.get(new Configuration());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			IndexLogger logger = new IndexLogger(fs,"127.0.0.1","D:\\test\\logTest\\log","D:\\test\\logTest\\archive",
//					new IndexThreadInfor() {
//						
//						public String parseIndexThreadInfor(String infor) {
//							return 1+"";
//						}
//						@Override
//						public ArrayList<String> getIndexThreadsInfor() {
//						ArrayList<String> list = new ArrayList<String>();
//						list.add(-1+"");
//							return  list;
//						}
//					},
//				new UpdateIndexMapInfor() {
//					public void addNewIndexMap(String message) {
//						System.out.println(" add new IndexMap: " + message);
//					}
//					public void undoIndexMap(ArrayList<String> indexFileArray) {
//						return;
//					}
//				});
//			logger.appendLog(TransLogMessage.startTask("123", "4567788"));
//			logger.appendLog(TransLogMessage.inforMeesageForTask("123", "3:0:3:/smp/cdr/bssap/datafile/1331604402-1331604452-bssap-1-server1106.cdr:/smp/cdr/bssap/datafile/1331604410-1331604456-bssap-1-server1105.cdr:/smp/cdr/bssap/datafile/1331604449-1331604506-bssap-1-server1107.cdr:2::0:/smp/cdr/bssap/indexfile/callingIndex/data/2201/5/1331601460_1331604505_3_2fda3417-b289-4c6f-adf1-1a402915949e.org::0:/smp/cdr/bssap/indexfile/calledIndex/data/2201/5/1331601460_1331604505_3_99fd8f06-8613-4789-a0c2-43b0d99a37d4.org"));
//			logger.appendLog(TransLogMessage.endTask("123"));
//			logger.threadExit();
//
//		}
//	
//	
}
