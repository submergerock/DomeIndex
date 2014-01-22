package org.cProc.distributed.createIndex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.log4j.PropertyConfigurator;
import org.apache.zookeeper.KeeperException;
import org.cProc.distributed.zookeeperDispatch.cloudComputingClient.RequestStateChangeHelper;
import org.cProc.distributed.zookeeperDispatch.cloudComputingServer.CloudComputingServer;
import org.cProc.log.indexmapinfor.IndexMapInfor;

import org.cProc.tool.myXMLReader.MyXmlReader;




public class ReadFromHDFSSever  extends  CloudComputingServer {

	static
	{
		PropertyConfigurator.configure(ReadFromHDFSSever.class.getClassLoader().getResource("mylog4j.properties"));
		
	}
	
	 private static Log Log4jlogger = LogFactory.getLog(ReadFromHDFSSever.class); 
	
    FileSystem fs;
    long endTime = 0;
    String parentName ="";

    long  hdfsFileNumber = 0;
    private String subCut="#";
    private String cut = "@";
    private String hdfsURL ="";
        
    private String orgRequestContent="";
    private String fileName="";
    private  int  onceDoWithFileNumber = 6;
    private  int  watchTime = 30000;
    private ArrayList<String> maxtimePath = new ArrayList<String>();

    
    private PathFilter  cdrFilter = null;
   
	
    
    //轮询回看2分钟
    private  final int backSeeTime = 2*60*1000;
    //近两分钟之内的文�?
    private  Map<String , Long> backSeeFileTimeMap = new ConcurrentHashMap<String, Long>();
  	
	public ReadFromHDFSSever(String fileName )
	{
		super(fileName);
		this.fileName = fileName;
		initConf(fileName);
		Configuration conf = new Configuration(); 
    	conf.set("fs.default.name",hdfsURL);
   	try {
			fs = FileSystem.get(conf);
		} catch (IOException e) { 
			e.printStackTrace();
		}
		
		
		//文件选择器，选择cdr后缀的文件
		cdrFilter = new PathFilter() {
				@Override
				public boolean accept(Path path) {
					if (path.getName().endsWith(Config.suffix))
						return true;
					return false;
				}
			};
	}
	
		
    private void initConf(String confFileName)
    {
    		MyXmlReader reader = new MyXmlReader(confFileName);
    		hdfsURL= reader.getName("hdfsURL");
    		//通知目录
    		parentName = reader.getName("sourceRoot");
    		if(reader.getName("oneMachDoWithFileNumber")!= null)
    		{
    			//最大合并文件个数
    			onceDoWithFileNumber =Integer.parseInt(reader.getName("oneMachDoWithFileNumber"));
    			Log4jlogger.info("onceDoWithFileNumber ---->"+onceDoWithFileNumber);
    		}
    		if(reader.getName("watchTime")!= null)
    		{
    			//轮询时间
    			watchTime =Integer.parseInt(reader.getName("watchTime"));
    			Log4jlogger.info("watchTime ---->"+watchTime);
    		}
    		
    		String temp = reader.getName("suffix");
    		if(temp!=null)
    		{
    			//原文件后缀名
    			Config.suffix = temp.trim();
    		}
    		
    		temp = reader.getName("ip");
    		if(temp!=null)
    		{
    			Config.IP = temp.trim();
    		}
    		
    }	
	
    
    public void parseAttribute()
    {
    	endTime 				= Long.parseLong(getNextAttibute());
    	hdfsFileNumber = Long.parseLong(getNextAttibute());
    	super.parseAttribute();
    	PrintAttribute();
    }
    
    public void PrintAttribute()
    {
    	Log4jlogger.info("endTime  is " + endTime);
    	Log4jlogger.info("hdfsFileNumber  is " + hdfsFileNumber);
    	super.PrintAttribute();
    }
    public  void saveAttribute()
    {
    	clearAttribute();
		this.addSaveAttribute(endTime+"");
		this.addSaveAttribute(hdfsFileNumber+"");
    	super.saveAttribute();
    }
    
    public  String  EncapsulationRequest()
	{
		 //format:path@ID@path@ID@path@ID     write for the client , save for sever change recover the file and ID;
		return  orgRequestContent;
	}
	
    
    public void beforeSendData()
    {
    	 //format:path@ID@path@ID@path@ID     write for the client , save for sever change recover the file and ID;
    	
    }
    
	
	//��master��Ҫ������
	public void doAction()  
	{
//		RepairThread repair = new RepairThread(fileName);
//		repair.setCreateIndexConf(repairFileName);
//		repair.setFileSystem(fs);
//		Thread thread = new Thread(repair);
//		//thread.start();
		try{
			while(!this.lifeOver() && !this.isExit())
	    	{	
	    		CheckNewFile();
	    		try {
					Thread.sleep(watchTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    	}
		}
		catch(KeeperException e1){
			Log4jlogger.info("@gyy:doAction: KeeperException");
		}
		catch(InterruptedException e2)
		{
			Log4jlogger.info("@gyy:doAction: InterruptedException");
		}
    	Log4jlogger.info("do Action is over");
	}
	
	
	 public void CheckNewFile( ) throws KeeperException, InterruptedException
	    {
    	 List<String>  list = GetFileNames();
    	 int start  = 0 ;
    	 int end = (start+ onceDoWithFileNumber) > list.size() ? list.size() :  (start+ onceDoWithFileNumber) ;
//    	 Tools.DebugPrint.DebugPrint("@gyy  file Number is :" + list.size() ,this);
    	 while(end <= list.size())
    	 {				
					// format:path@ID@path@ID@path@ID write for the client , save
					// for sever change recover the file and ID;
					orgRequestContent = "";
					for (int m = start; m <end; m++) {
						Log4jlogger.info(System.currentTimeMillis()+":@gyySelectOneFileIs " + list.get(m));
						String nodePath = list.get(m);						
						hdfsFileNumber++;
						orgRequestContent = orgRequestContent + nodePath + cut
								+ hdfsFileNumber + cut;
					}
					if (orgRequestContent.length() > 0) {
						orgRequestContent = orgRequestContent.substring(0,
								orgRequestContent.length() - 1);
						Log4jlogger.info("@gyySendMessageIs " + orgRequestContent);
						sendData(EncapsulationRequest());
					}				
				if(end >= list.size())  // over
				{
					break ;
				}
				start = end;
				end = (end + onceDoWithFileNumber )  <= list.size() ?   (end + onceDoWithFileNumber ):list.size();
    	 }
	    }
	 
	 
	    public String GetParentName()
	    {	
	    	return parentName;
	    }
	    private  List<String> GetFileNames( )
	    {
	    	String parentName = GetParentName();
	    	ArrayList<String> fileNameList = new ArrayList<String>();
	    	 try {
	    		 Log4jlogger.info(parentName+"******************************");
	    		 if( fs  != null)
	    		 {
					FileStatus fileList[] = fs.listStatus(new Path(parentName),cdrFilter);
					if(fileList  != null)
					{
									int size = fileList.length;
									long onceMaxTime = 0;
									long cdrEndTime =endTime;
								
									for (int i = 0; i < size; i++) {
										if (onceMaxTime < fileList[i].getModificationTime()) {
											onceMaxTime = fileList[i].getModificationTime();
										}
									//本次轮询文件，先查看前二分钟内的数据里，是否有上次没有的�?
										if(fileList[i].getModificationTime() <= cdrEndTime  
										   &&  fileList[i].getModificationTime() > (endTime -backSeeTime) )
										{
											if(!maxtimePath.contains(fileList[i].getPath().getName()))
											{
												fileNameList.add(fileList[i].getPath().getName());
												Log4jlogger.info("@gyyOutOfThinkFiles " + fileList[i].getPath().getName()
												+" file time is " + backSeeFileTimeMap.get(fileList[i].getPath().getName())
												+" last endTime is  " + endTime
												);
											}
										}
										if (fileList[i].getModificationTime() > cdrEndTime) {
											fileNameList.add(fileList[i].getPath().getName());											
										}
									}
									endTime = onceMaxTime;
					}
					//如果fileList不为空，则保存这次查询的前两分钟内的文件名；
					if(fileList  != null)
					{				
									maxtimePath.clear();
									backSeeFileTimeMap.clear();			
									int size = fileList.length;								
									for (int i = 0; i < size; i++) {
										if(fileList[i].getModificationTime() <= endTime  
										   && fileList[i].getModificationTime() > (endTime -backSeeTime))
										{
											maxtimePath.add(fileList[i].getPath().getName());
											backSeeFileTimeMap.put(fileList[i].getPath().getName(), fileList[i].getModificationTime());
											
										}
									}
									
					}
					
				}
	    	 } catch (IOException e) {
	 			e.printStackTrace();
	 		}
	    	return fileNameList;
	    }
	    
	    
	    //message format: request , indexInfor
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
	    
	     public  void exit()
	    {
	    	
	    }
	    
	public static void main(String[] args)
	{
		//String confile =args[0];
		String confile = "E:\\datacube\\createIndex.xml";
		ReadFromHDFSSever  test = new ReadFromHDFSSever(confile);
		test.check();
		
	}
}
