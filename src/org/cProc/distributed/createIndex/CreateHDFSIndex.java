package org.cProc.distributed.createIndex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.cProc.GernralRead.TableAndIndexTool.TableAndIndexTool;
import org.cProc.log.createIndexLog.IndexLogger;
import org.cProc.log.createIndexLog.IndexLoggerManger;
import org.cProc.log.createIndexLog.TransLogMessage;
import org.cProc.log.indexmapinfor.IndexMapInfor;
import org.cProc.log.indexmapinfor.LocalTempIndexFileInfor;
import org.cProc.task.client.RPCClient;
import org.cProc.task.common.Status;
import org.cProc.tool.Pair;
import org.cProc.tool.myXMLReader.MyXmlReader;







public class CreateHDFSIndex implements Runnable  {

	 private static Log Log4jlogger = LogFactory.getLog(CreateHDFSIndex.class); 
	public static Integer lock = new Integer(0);
	
	private static final String SPLIT_CHAR = ",";
	private volatile boolean exit = false;
	private Thread doIndexThread = null;
	
	private BlockingQueue<Pair<String, String>> queue = null;
	String fileName = null;
	private   final String FILE_SEPERATOR ="/"; 
	private final char FILE_SEPERATOR_CHAR ='/';

	//原文件名是否以#开始
	private boolean startWith = false;
	private boolean hasMaxMin = false;
	String cut = "@";

	ReadFromHDFSClient parent = null;
	String content = "";
	String uuid = "";
	//String nodeName = "";
	String confFileName = "";
	String hdfsURL = "";
	String queryPath = "";
	FileSystem fs = null;
	String minAndMaxPathStringRoot ="";
	
	//忽略的表
	List<String> invalidTables = new ArrayList<String>();
	
	//private IndexLogger logger = null;
	//private  volatile int  nodeID = 0;
	private CountDownLatch latch = new  CountDownLatch(1);
	private RPCClient rpcclient;
	
	
	public CreateHDFSIndex(BlockingQueue<Pair<String, String>> queue,
			ReadFromHDFSClient lparent, String lConfFileName, FileSystem lfs ) {
		this.queue = queue;
		parent = lparent;
		confFileName = lConfFileName;
		fs = lfs;
		//logger = IndexLoggerManger.getLogger();
	}

	public void InitConf() {
		MyXmlReader reader = new MyXmlReader(confFileName);
		//通知目录路径，原数据文件名的/替换成#即为通知目录名，通知目录名含数据文件、库名、表名等信息
		//ex. #smp#cdr#bssap#datafile#00a099c2-c715-4a77-b894-4d2a6874c977.cdr
		//通知目录存放在sourceRoot所配的路径下
		queryPath = reader.getName("sourceRoot");
		Log4jlogger.info("queryPath  is " + queryPath);
		
		//最大最小值文件目录，每个数据文件可有一个最大最小值文件，内含每个字段的最大值和最小值信息
		minAndMaxPathStringRoot = reader.getName("minAndMaxPath");
		
		//目录路径中cdr前面的前缀部分，如/smp
		TableAndIndexTool.setPrePath(reader.getName("databasePrePath").trim());
		
		//忽略的文件,用逗号分割
		String temp = reader.getName("excludeTable");
		if(temp!=null)
		{
			String [] arr = temp.split(SPLIT_CHAR);
			if(arr!=null && arr.length!=0)
			{
				for(String str:arr){
					if(str==null)
						continue;
					str = str.trim();
					if(str.equals(""))
						continue;
					this.invalidTables.add(str);
				}
					
			}
			arr = null;
		}
		
		//是否有最大最小文件
		hasMaxMin = Boolean.parseBoolean(reader.getName("maxmin"));
		temp = null;
        rpcclient = new RPCClient(confFileName);
	}

	// for hdfs index
	public void run() {
		doIndexThread= Thread.currentThread();
		InitConf();
		while ( !exit) {
			Pair<String, String> reslut = null;
			try {
				//任务队列中竞争到一个任务
				reslut = queue.take();
			} catch (InterruptedException e) {
				continue;
			}
			content = reslut.getFirst();
			uuid = reslut.getSecond();
			//logger.appendLog(TransLogMessage.startTask(uuid, content));
			RunHDFS();
			//logger.flush();
			//nodeID = 0;
		}
		
		//收到停止指令exit为true，退出循环准备关闭线程
		Random random = new  Random();
		int sleep = random.nextInt(20);
		try {
			Thread.sleep(sleep*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//任务队列中还有未执行的任务时，先执行完任务再退出线程，这段代码貌似有些问题
		while (queue.size() > 0 ) {
			ArrayList<Pair<String, String>> list = new  ArrayList<Pair<String, String>>();
			for(int i = 0 ; i < list.size() ; i++)
			{
				Pair<String, String> reslut = list.get(i);
				content = reslut.getFirst();
				uuid = reslut.getSecond();
				//logger.appendLog(TransLogMessage.startTask(uuid, content));
				RunHDFS();
				//logger.flush();
				uuid = "";
			}
		}
		Log4jlogger.info("thread " + Thread.currentThread().getName() +"   is   exit");
		
		//将记数器清零，threadExit()方法继续执行，当前线程结束
		latch.countDown();

	}

	public void RunHDFS() {
		
		
		ArrayList<LocalTempIndexFileInfor> indexFileInforArray = new ArrayList<LocalTempIndexFileInfor>();
		//用来按表存放数据文件名的，同一个表的数据文件存放在同一个list中
		HashMap<String,ArrayList<String>> tableMap = new HashMap<String,ArrayList<String>>();
		
		//content为@分割的数据文件名
		//ex.  #smp#cdr#bssap#datafile#13e85b6e-b331-46a1-9c31-69b78bbf3691.cdr@34@#smp#cdr#bssap#datafile#c9e0237f-f6b7-43a5-b486-77859492a1a3.cdr@35
		StringTokenizer as = new StringTokenizer(content, cut);
		while (as.hasMoreTokens()) {
			String fileName = as.nextToken();
//			fileName = fileName.replace("#", FILE_SEPERATOR);
			
			if(fileName.startsWith("#")  )  //第一个字符为#号时，将#替换成/，取得实际文件名
			{
				fileName = fileName.replace("#", FILE_SEPERATOR);
				startWith =  true;
			} else {//第一个字符不为#时，加上/
				fileName = FILE_SEPERATOR + fileName.replace("#", FILE_SEPERATOR);
			}
			
			//跳过索引文件的序号
			//long fileNumber = Long.parseLong(as.nextToken());
			String tableName =TableAndIndexTool.getTableName(fileName);
			String appName = TableAndIndexTool.getAppName(fileName);
			String appNameAddTableName = appName+"#"+tableName;
			ArrayList<String> list = tableMap.get(appNameAddTableName);
			if(list == null)
			{
				list = new ArrayList<String>();
				tableMap.put(appNameAddTableName, list);
			}
			list.add(fileName);
		}
		Iterator<Entry<String, ArrayList<String>> >  iter = tableMap.entrySet().iterator();
		
		while( iter.hasNext())
		{
			//存放索引文件信息
			LocalTempIndexFileInfor indexFileInfor = new LocalTempIndexFileInfor();
			indexFileInforArray.add(indexFileInfor);
			//list中为表的所有数据原文件名
			Entry<String, ArrayList<String>> entry = iter.next();
			//表名信息
			String appNameAddTableName =entry.getKey();
			indexFileInfor.addSourceFile(entry.getValue());
			
			String[] strs =  appNameAddTableName.split("#");
			String  appName = strs[0];
			String  tableName = strs[1];

			
			Log4jlogger.info("@cw tableName:"+tableName+" result--->"+checkIsTable(tableName));
			
			//不属于配置文件所配的忽略文件的范围时创建索引
			if(checkIsTable(tableName))	{
				CreateHDFSIndexImp imp = new CreateHDFSIndexImp();
				imp.setMinAndMaxPath(minAndMaxPathStringRoot);
				imp.setStartWith(this.startWith);
				imp.setGenerateConfFile(confFileName);
				imp.setTableName(tableName);
				imp.setAppName(appName);
				imp.setLunXunPath(queryPath);
				imp.setFileList(entry.getValue());
				imp.setHasMaxMin(this.hasMaxMin);
				imp.setFileSystem(fs);
				imp.setLocalTempIndexFileInfor(indexFileInfor);
				imp.setUuid(uuid);
				imp.createIndex();
				imp = null;
			} else {//将配置文件中配的忽略文件对应的原文件删除
//				for (int i = 0; i < entry.getValue().size(); i++) {
//					String lunPath = entry.getValue().get(i).replace(FILE_SEPERATOR_CHAR, '#');
//					if(!startWith)
//					{
//						lunPath = queryPath + FILE_SEPERATOR + lunPath.substring(1);
//					}else{					
//						lunPath = queryPath + FILE_SEPERATOR + lunPath;
//					}
//					Log4jlogger.debug("path  is " + lunPath);
//					try {
//					//	fs.delete(new Path(lunPath));
//						fs.delete(new Path(lunPath), true);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					Log4jlogger.info(lunPath + " deleted , PS: not create index");
//				}
				
				Status s = new Status();
				s.setStatus(0);
				s.setTaskId(uuid);
				if(rpcclient.reportStatus(s)) {
					//logger.appendLog(TransLogMessage.endTask(uuid));
				}
				return;
				
				
			}
		}

		//上传数据文件至HDFS
		boolean result = upLoadDataToHDFS(indexFileInforArray)	;
		System.out.println("reslut===========" + result);
		//when all is upLoad ,then report the status;
		if(result) {   
			Status s = new Status();
			s.setStatus(0);
			s.setTaskId(uuid);
			if(rpcclient.reportStatus(s)) {
				//logger.appendLog(TransLogMessage.endTask(uuid));
			}
		
		}else
		{//failed
			
			Status s = new Status();
			s.setStatus(0);
			s.setTaskId(uuid);
			if(rpcclient.reportStatus(s)) {
				//logger.appendLog(TransLogMessage.endTask(uuid));
			}
			
			//记录失败日志
			Log4jlogger.error("upload faild...."+uuid);
			
			
		}
	

	}
	
	

	private boolean upLoadDataToHDFS( ArrayList<LocalTempIndexFileInfor> indexFileInforArray ) 
	{
		//upload all the data and log the infor
		//1.create indexMap ;
		String message = IndexMapInfor.changeIndexFileInforToMessage(indexFileInforArray);
		//2.write  indexMap to zk:start Map
		//RequestStateChangeHelper.changeZKNodeRequestToStart(parent, nodeName, message);
		//3.write indexMap to log;
		//logger.appendLog(TransLogMessage.inforMeesageForTask(uuid, message));
		//logger.flush();
		//4.updataAllData to hdfs;
		int tryUpLoadCount = 0 ;
		for(int i = 0 ; i < indexFileInforArray.size(); i++)
		{
			LocalTempIndexFileInfor localFileInfor = indexFileInforArray.get(i);
			HashMap<String, String> map = localFileInfor.getFromLocalToHDFSMap(); 
			Iterator<Entry<String, String>> iterator =map.entrySet().iterator();
			while(iterator.hasNext())
			{
				Entry<String, String> entry = iterator.next();
				String locaFileString= entry.getKey();
				String hdfsFileString = entry.getValue();
				boolean reslut = parent.copyToHDFS(locaFileString, hdfsFileString);
				while(!reslut && tryUpLoadCount < 7)
				{
						try {
							Thread.sleep(20000);
						} catch (InterruptedException e) {
							Log4jlogger.warn("when upLoad indexfile to hdfs, interupted the thread");
						}
						reslut =  parent.copyToHDFS(locaFileString, hdfsFileString);
				}
				if(!reslut)
				{
					return false;
				}
			}
		}
		//5.write indexMap to zk : end Map
		//RequestStateChangeHelper.changeZKNodeRequestToEnd(parent, nodeName);
		return true;
	}
	
	private boolean checkIsTable(String table)
	{
		return !this.invalidTables.contains(table);
	}
	

	public String  getCurrentTaskId()
	{
		return  uuid;
	}
	
	public void threadExit()
	{
		exit = true;
		doIndexThread.interrupt();
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args)
	{
		CreateHDFSIndexImp imp = new CreateHDFSIndexImp();
		imp.setGenerateConfFile(args[1]);
		imp.createIndex();
		imp = null;
	}
	

	
}
