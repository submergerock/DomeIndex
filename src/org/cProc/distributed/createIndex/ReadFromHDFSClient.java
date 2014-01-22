package org.cProc.distributed.createIndex;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.log4j.PropertyConfigurator;
import org.cProc.distributed.zookeeperDispatch.cloudComputingClient.CloudComputingClient;
import org.cProc.distributed.zookeeperDispatch.cloudComputingClient.RequestStateChangeHelper;
import org.cProc.log.createIndexLog.IndexThreadInfor;
import org.cProc.log.indexmapinfor.IndexMapInfor;
import org.cProc.log.loganalyzer.UpdateIndexMapInfor;
import org.cProc.task.client.RPCClient;
import org.cProc.tool.Pair;
import org.cProc.tool.myXMLReader.MyXmlReader;




public class ReadFromHDFSClient  extends CloudComputingClient implements IndexThreadInfor,UpdateIndexMapInfor{
	
	static
	{
		PropertyConfigurator.configure(ReadFromHDFSClient.class.getClassLoader().getResource("mylog4j.properties"));
		
	}
	 private static Log Log4jlogger = LogFactory.getLog(ReadFromHDFSClient.class); 
	
    FileSystem fs;
    long endTime = 0;
    long  hdfsFileNumber = 0;
    private String subCut="#";
    private String cut = "@";
    private String hdfsURL ="";
    
    //配置文件createIndex.xml的绝对路径+文件名
    private String  indexConfile  ="";
    
    private String localLogPathString="";
    private String hdfsLogPathString="";
    
    private int stepSecond = 1;
    
    //存放创建索引的线程
    private ArrayList<CreateHDFSIndex>  createIndexList = new ArrayList<CreateHDFSIndex>();
    
    //默认的索引创建的线程数
    int threadNumber = 7;
    
    //具有优先级的阻塞队列，用来存放ZK任务节点信息
    private BlockingQueue<Pair<String,String>> queue = new  ArrayBlockingQueue <Pair<String,String>>(10);
    
	private static final String pemission = "drwxrwxrwx";
	private static final FsPermission PERMISSION = FsPermission.valueOf(pemission);
	
	private CountDownLatch allThreadLatch = null;
	
	
	public ReadFromHDFSClient(String conFileName)
	{
		super(conFileName);
		indexConfile = conFileName;
		//从配置文件读取配置信息
		initConf(conFileName);
		Configuration conf = new Configuration();
		conf.set("fs.default.name", hdfsURL );
		conf.set("dfs.replication", "2" );

		
		try {
			fs = FileSystem.get(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    //IndexLoggerManger.createNewLogger(fs, IP.getIP(), localLogPathString, hdfsLogPathString, this,this);
		//创建多个线程创建索引，线程数通过配置文件createIndexThreadNumber配置，默认为7
	    for(int i  = 0 ; i  < threadNumber ; i++ )
		{
			  CreateHDFSIndex index  = new CreateHDFSIndex(queue,this,indexConfile,fs );
			  createIndexList.add(index);
			  Thread thread = new Thread( index);
			  thread.start();
		}
	    
	}
	
    private void initConf(String confFileName)
    {
    	MyXmlReader reader = new MyXmlReader(confFileName);
		hdfsURL= reader.getName("hdfsURL");
		threadNumber = Integer.parseInt(reader.getName("createIndexThreadNumber"));
		localLogPathString = reader.getName("localLogPath");
		hdfsLogPathString = reader.getName("hdfsAchiveLogPath");
		if (reader.getName("stepSecond") != null)
			stepSecond = Integer.parseInt(reader.getName("stepSecond"));
		
		String isText = reader.getName("isText");
		
		if(isText!=null)
		{
			Config.isText  = Boolean.parseBoolean(isText.trim());
		}
		
		String split = reader.getName("split");
		if(split!=null)
		{
			Config.split  = split.trim();
		}
		String _failedPath = reader.getName("failed");
		if(_failedPath!=null)
		{
			Config.failedPath  = _failedPath.trim();
		}
		String ip = reader.getName("ip");
		if(ip!=null)
		{
			Config.IP = ip.trim();
		}
		
		
		Log4jlogger.info(" isText-->"+isText+"    ,split---->"+split+"    ,ip---->"+Config.IP);
		
    }
    
	private  void  parseRequest()
	{
		 //format:path@ID@path@ID@path@ID     write for the client , save for sever change recover the file and ID;
		
	}
	
	//Client��Ҫ������
	  public void doAction(String RequestContent,String nodeName)
	{
		  Log4jlogger.info("@gyyQueue: start add queue RequestContent  is " + RequestContent +"   nodeName  is " + nodeName );
		  queue.add(new Pair(RequestContent,nodeName));	  
		  Log4jlogger.info("@gyyQueue:end add queue RequestContent  is " + RequestContent +"   nodeName  is " + nodeName );
	}
	  
	   public  void clearAllTask()
	   {
		   queue.clear();
		   Log4jlogger.info("@gyyClear: clear all task in the queue");
	   }
	  		
		
		//change the getIndexThreadsInfor reslut to the format :  task_number
		//getIndexThreadInfor create infor, and parseIndexThreadInfor parse the infor to taks_number(String)
		public ArrayList<String> getIndexThreadsInfor()
		{
			ArrayList<String> list = new ArrayList<String>();
			for(int i = 0; i < createIndexList.size() ; i++)
			{
				list.add(createIndexList.get(i).getCurrentTaskId()+"");
			}
			return list;
			
		}
		public String parseIndexThreadInfor(String infor)
		{
			return infor;
		}
			
				
		
	   synchronized	public boolean copyToHDFS(String locaFileString , String hdfsFileString)
		{
			try {
				
				File localFile = new File(locaFileString);
				
				
				Path hdfsPath = new Path(hdfsFileString);
				if(fs.exists(hdfsPath))
				{
					fs.delete(hdfsPath, true);
				}
				
				
				
				fs.copyFromLocalFile(new Path(locaFileString),hdfsPath);
				fs.setPermission(hdfsPath, PERMISSION);
				FileStatus status = fs.getFileStatus(new Path(hdfsFileString));
				if(status.getLen() !=localFile.length())
				{
					
					Log4jlogger.error("local file is:"+locaFileString+"  hdfs is:"+hdfsFileString+" size does not match !");
				}
				
				localFile.delete();
				status = null;
			} catch (IOException e) {	
				e.printStackTrace();
				Log4jlogger.error("copyfromLocal :"+e.getMessage());
				return false;
			}
				return true;
		}
	   
	    public void requestRollBack(String message)
	    {
		    	String[] splits = message.split(RequestStateChangeHelper.zkNodeInforSeperater);
		    	String indexInfor = splits[RequestStateChangeHelper.indexInforPos];
		    	IndexMapInfor map = new IndexMapInfor();
		    	map.parseMessageInfor(indexInfor);
		    	HashMap<String, String> indexMap = map.getIndexVerionMap();
		        Iterator<Entry<String, String>> iterator =	indexMap.entrySet().iterator();
		        while(iterator.hasNext())
		        {
		        	String hdfsIndexPath = iterator.next().getKey();
		        	try {
						fs.delete(new Path(hdfsIndexPath), true);
					} catch (IOException e) {
						e.printStackTrace();
					}
		        }
	    	
	    }
	    
		public void addNewIndexMap(String message)
		{
			//add this message to ZK;
			Log4jlogger.info("log save in zk:"+message);
			addLogIndex(message);
			
		}
		public  void undoIndexMap(ArrayList<String> indexFileArray)
		{
			//if hdfs exist , then delete ;
			for(int i = 0 ; i < indexFileArray.size() ; i++)
			{
				Path path = new Path(indexFileArray.get(i));
				try {
					if(fs.exists(path))
					{
						fs.delete(path, true);
					}
				} catch (IOException e) {
					Log4jlogger.info(e.getMessage());
				}
			}
		}
		
	     public void exit()
		{
	    	 System.out.println("size is : "  +  createIndexList.size());
			for(int i = 0 ; i < createIndexList.size() ; i++)
			{
				createIndexList.get(i).threadExit();
				allThreadLatch.countDown();
			}
		
		}
		
	     public void waitAllOver()
	     {
	    	 try {
				allThreadLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Log4jlogger.info("all working thread are over");
			//IndexLoggerManger.loggerExit();
	     }
	     
		public void close()
		{
			super.close();
		}
		
		//当任务队列为空时从服务端获取任务
		public void getTaskFromServer() {
			RPCClient rpcclient = new RPCClient(indexConfile);
			int i = 0;
	    	while(true) {
	    		try {
		    		if (queue.isEmpty()) {
		    			Pair task = rpcclient.getTaskFromServer();
		    			if (task != null ) {
		    				queue.put(task);
		    				i = 0;
		    			} else {
		    				i++;
		    				//stepSecond秒
		    				if(i>=10)
		    					i=0;
		    				Thread.sleep(i*1000*stepSecond);
		    				Log4jlogger.info("end sleep:"+i);
		    			}
		    		}else
		    		{
		    			Thread.sleep(1000*30);
		    		}
	    		} catch(Exception e) {
	    			Log4jlogger.error(e.getMessage(), e);
	    		}
	    	}
			
		}
		
		public static void main(String[] args)
		{
		  String confile =args[0];
		//String confile = "E:\\datacube\\createIndex.xml";
			ReadFromHDFSClient  test = new ReadFromHDFSClient(confile);
//			test.check();
			test.getTaskFromServer();
			test.waitAllOver();
			Log4jlogger.info("main thread-----exit");
			test.close();
		}
		
		

}
