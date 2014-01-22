package org.cProc.distributed.createIndex;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.cProc.distributed.zookeeperDispatch.cloudComputingServer.IP;
import org.cProc.log.indexmapinfor.LocalTempIndexFileInfor;
import org.cProc.tool.FileUtils;
import org.cProc.tool.TimeUtils;

public class CreateHDFSIndexImp {
		
	 private static Log Log4jlogger = LogFactory.getLog(CreateHDFSIndexImp.class); 
	public static Integer lock = new Integer(0);
	
	private   final String FILE_SEPERATOR ="/"; 
	private final char FILE_SEPERATOR_CHAR ='/';
	generateIndex pdbssap =null;
	private LocalTempIndexFileInfor  indexFileInfor = null;
	
	//通知目录路径
	 String queryPath="";
	 FileSystem fs=null;
	 ArrayList<String> list= null;
	 
	 //最大最小文件路径
	 String minAndMaxPath = "";
	 
	 //原文件是否是#开始
	 boolean startWith = false;
	 boolean hasMaxMin = false;
	 
	 private String uuid;
	
	 public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public  CreateHDFSIndexImp()
	 {
			 pdbssap = new generateIndex();
	 }
	 
	 public void setMinAndMaxPath(String minAndMaxPath)
	 {
		 this.minAndMaxPath = minAndMaxPath;
	 }
	 
	 public boolean isHasMaxMin() {
		return hasMaxMin;
	}

	public void setHasMaxMin(boolean hasMaxMin) {
		this.hasMaxMin = hasMaxMin;
		
	}

	public void setLocalTempIndexFileInfor(LocalTempIndexFileInfor  indexFileInfor)
	{
		this.indexFileInfor = indexFileInfor;
		pdbssap.setLocalTempIndexFileInfor(indexFileInfor);
	}
		 
	 public void setLunXunPath(String path)
	 {
		 queryPath=path;
	 }
	 
	 public void setFileSystem( FileSystem lfs)
	 {
		 fs = lfs;
	 }
	 
	public void setGenerateConfFile(String confFileName)
	{
		pdbssap.setGenerateConfFile(confFileName);
	}
	public void setTableName( String table)
	{
		pdbssap.setTableName(table);
	}
	public void setAppName( String appName)
	{
		pdbssap.setAppName(appName);
	}
	
	public void setFileList( 	 ArrayList<String> lists )
	{
		
		list = null;
		list = lists;
//		DebugPrint.DebugPrint("@gyy: fileList  length is " + lists.size() ,this);
		for( int i = 0 ; i < lists.size() ; i ++)
		{
				String temp = this.minAndMaxPath+(lists.get(i).replace("/", "#"));
				
				pdbssap.addMaxAndMinPath(temp);
				
				pdbssap.addInputFileName( lists.get(i));
				temp = null;
		}
		this.printFileInfo(this.list);
	}
		
	public void createIndex()
	{
		if(list.size() > 0)
		{
			pdbssap.setHasMaxMin(this.hasMaxMin);
			pdbssap.setUuid(uuid);
			//创建索引
			boolean reslut = pdbssap.startCreateIndex();
			int count = 0 ;
			while (!reslut && count < 7) {//创建索引失败后，挂起10秒钟后再次尝试，最多尝试七次
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				reslut = pdbssap.startCreateIndex();
				count++;
				Log4jlogger.info("@gyy: "+System.currentTimeMillis()+" try to create index " +"times is " + count + Thread.currentThread().getName());
			}
			
			//创建失败时
			if(!reslut)
			{
				
				
				if(Log4jlogger.isErrorEnabled()){
					Log4jlogger.error("@cw create failed-----error");
				}
				
				
				//移除文件夹到临时目录
				ArrayList<String> _source = pdbssap.getSourceFilePaths();
				
				
				StringBuffer sb = new StringBuffer();
				sb.append(pdbssap.getErrorMessage()).append("\n");
				
				for(String str:_source)
				{
					//写入原文件名
					sb.append(str).append("\n");
					
				}
				
				//将创建失败的原文件名及异常信息写入指定文件夹的失败日志文件
				FileUtils.write2HfsFile(sb.toString(), new Path(Config.failedPath+IP.getIP()+"_"+this.uuid), fs);
				sb.delete(0, sb.length());
				sb = null;
			}
			
			//删除通知目录改为server处理
            //删除原文件对应的通知目录文件
//			for( int i=0;i<list.size();i++)
//			{
//				String lunPath = list.get(i).replace(FILE_SEPERATOR_CHAR, '#');
//				if(!this.startWith)
//				{
//					lunPath= lunPath.substring(1);
//				}
//			
//				lunPath = queryPath+FILE_SEPERATOR + lunPath;
//				try {
//					Log4jlogger.info("fileDelete : " + lunPath+" deleted");
//					fs.delete(new Path(lunPath), true);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				Log4jlogger.info("fileDelete : " + lunPath+" deleted");
//			}				
		}
	}

	public void printFileInfo(ArrayList<String> fileInfor )
	{
		synchronized (lock) {
			for( int i=0;i<fileInfor.size();i++)
			{
				Log4jlogger.info(System.currentTimeMillis()+ ":@gyyFileInfor:SourceFileName    :" +  fileInfor.get(i));
			}				
			
		}
	}

	public boolean isStartWith() {
		return startWith;
	}

	public void setStartWith(boolean startWith) {
		this.startWith = startWith;
	}
}
