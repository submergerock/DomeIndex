package org.cProc.distributed.mergeIndex;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.zookeeper.KeeperException;
import org.cProc.GernralRead.ReadData.DataType;
import org.cProc.GernralRead.ReadData.MergeLevel;
import org.cProc.GernralRead.ReadData.ParseIndexFormat;
import org.cProc.GernralRead.TableAndIndexTool.TableAndIndexTool;
import org.cProc.distributed.zookeeperDispatch.cloudComputingServer.CloudComputingServerDoSomeThing;
import org.cProc.index.QueryTargetIndex;
import org.cProc.index.BPlusTree.BTreeType;
import org.cProc.index.BPlusTree.Data.MulitFiledData;
import org.cProc.index.BPlusTree.Data.ReadWriteIndexDataProxy;
import org.cProc.index.BPlusTree.MergeTree.MergerBPlusTree;
import org.cProc.tool.myXMLReader.MyXmlReader;




public class MergeSever  extends  CloudComputingServerDoSomeThing {


    FileSystem fs;

    String fileSepratorString="/";
    String localFileSepratorString="\\";
    String prePath="";
    String tempCharString=".tmp";
    
    String choosePathString= "";
    private String saveInZKSeprater ="-_-!!!!";

    private String hdfsURL ="";
        
    private String fileName="";
    private  String repairFileName="";

    

    private  int  maxFileInOneFile = 30;
    
    private  String tempDir ="D:\\test\\Test\\merge";
    
    
	private static final String pemission = "drwxrwxrwx";
	private static final FsPermission PERMISSION = FsPermission.valueOf(pemission);
	
	//每个队列的�?中率�?�?�?

	//这部分�?过随机数来�?择，
	Random random = new Random();
	private int chooseLoopMax = 12;
	private  int highMax = 8;
	private  int midMax = 10;
	private  int lowMax = 11;
	private  int highChooseCountNumber = 0;// 优先权最�?9
	private  int midChooseCountNumber = 0; // 优先权一�? 3
	private  int lowChooseCountNumber = 0; // 优线权一�?1 
	
	private int chooseCountNumber = 0; //总的统计总数�?
	

    private  int sleepTime = 1000;
    private  String hdfsMasterIP ="";
    private  String hdfsMasterPort ="9000";
    
    private String statsString ="";
	public   PathFilter orgFilter = null;
	public   PathFilter  mergeFilter = null;
	private String appName ="";
	//private  ArrayList<String> indexName =  new   ArrayList<String>();
	private ArrayList<String> highIndexName = new ArrayList<String>();
	private ArrayList<String> midIndexName = new ArrayList<String>();
	private ArrayList<String> lowIndexName = new ArrayList<String>();
	

	String indexPath ="" ;

	
	public MergeSever(String fileName )
	{
		super(fileName);
		
		orgFilter = new PathFilter() {
			@Override
			public boolean accept(Path path) {
				if (path.getName().matches(BTreeType.matchOrgName))
					return true;
				return false;
			}
		};
		
		mergeFilter = new PathFilter() {
			@Override
			public boolean accept(Path path) {
				if (path.getName().matches(BTreeType.matchOrgName) || path.getName().matches(BTreeType.matchMergeName))
					return true;
				return false;
			}
		};
		
		this.fileName = fileName;
		//this.repairFileName = repairFileName;
		initConf(fileName);
	//	hdfsURL ="hdfs://192.168.1.8:9000";
		Configuration conf = new Configuration(); 
    	conf.set("fs.default.name",hdfsURL);
   	try {
			fs = FileSystem.get(conf);
	    	getAllIndex();
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}
	
	private String generateIndexName(String tableName ,String indexName)
	{
		return tableName+saveInZKSeprater+indexName;
	}
	
	private String getIndexTableName(String indexName)
	{
		String[] splits = indexName.split(saveInZKSeprater);
		return splits[0];
	}
	
	private String getIndexIndexName(String indexName)
	{
		String[] splits = indexName.split(saveInZKSeprater);
		return splits[1];
	}
	
	private void getAllIndex()
	{
		//浏览每个表，将每个表的索引；
		try {
			System.out.println("app path is " +prePath+fileSepratorString+appName );
		 FileStatus[] fstatus =	fs.listStatus(new Path(prePath+fileSepratorString+appName));
		 for(int i = 0; i  < fstatus.length ;i++)
		 {
			 //获取每个表名
			 Path path= fstatus[i].getPath();
			 System.out.println("index path is " +  TableAndIndexTool.getIndexPath(path.getName(), appName));
			 //获取每个索引的路�?
			 FileStatus[] indexStatus = fs.listStatus(new Path( TableAndIndexTool.getIndexPath(path.getName(), appName)));
			 if(indexStatus == null)
			 {
				 continue;
			 }
			 for( int k = 0 ; k<indexStatus.length ; k++ )
			 {
				 Path indexPath= indexStatus[k].getPath();
				 System.out.println(path.getName()+"     " + indexPath.getName()  );
				 if(indexPath.getName().equals("drillIndex"))
				 {
					 continue;
				 }
				 String indexFrmPath =  TableAndIndexTool.getIndexFormat(path.getName(), indexPath.getName(), appName);
				 ParseIndexFormat readTableFormat = new ParseIndexFormat(indexFrmPath,fs);
				 switch(readTableFormat.getMergeLevelValue())
				 {
				 	case MergeLevel.high:
				 		highIndexName.add(generateIndexName(path.getName(), indexPath.getName()));
				 		break;
				 	case MergeLevel.mid:
				 		midIndexName.add(generateIndexName(path.getName(), indexPath.getName()));
	 					break;
				 	case MergeLevel.low:
				 		lowIndexName.add(generateIndexName(path.getName(), indexPath.getName()));
	 					break;
				 }
			 }
			 
		 }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
    private void initConf(String confFileName)
    {
    	MyXmlReader reader = new MyXmlReader(confFileName);
    	hdfsURL= reader.getName("hdfsURL").trim();
    	localFileSepratorString = reader.getName("localFileSepratorString").trim();
    	tempDir = reader.getName("tempDir").trim();
           hdfsMasterIP =reader.getName("hdfsMasterIP").trim();
           hdfsMasterPort =reader.getName("hdfsMasterPort").trim();
           appName =reader.getName("appName").trim();
           maxFileInOneFile = Integer.parseInt(reader.getName("maxFileInOneFile").trim());
           TableAndIndexTool.setPrePath(reader.getName("prePath").trim());
           prePath = reader.getName("prePath").trim();
           String sleepTimeStr = reader.getName("sleepTime").trim();
           if(sleepTimeStr != null)
           {
        	   sleepTime = Integer.parseInt(sleepTimeStr);
           }
    }	
    
    private void RecoverMergeFile(	ArrayList<String> list , String mergeTempFileString )
    {
		//在ZK里记录信息； ----T1   这个是原子的
		//修改名称                -----T2  这个不是
		//删除						 -----T3  这个不是
		//清除状�?信息�?     T4       这个是原子的
    	//处理T4未成功：    标志：mergeTempFileString文件已变成了mergeFileString,只需要清理属性；
    	//处理T2，T3未成功：    标志：mergeTempFileString已生成，mergeFileString没有，重命名，查看每个文件是否存在（不存在则是T3），并删除；
    	String fileName = mergeTempFileString.substring(0, mergeTempFileString.length() -BTreeType.tempName.length())+BTreeType.mergeName;
    	try {
			if(fs.exists(new Path(fileName)))
			{
				//现在是T4时刻
				this.clearAttribute();
			}
			else if(fs.exists(new Path(mergeTempFileString))) {
				//现在是T2,T3时刻
				//修改名称                -----T2
				  fs.rename(new Path(mergeTempFileString), new Path(fileName));
				//删除						 -----T3
					for(int i = 0 ; i < list.size() ; i++ )
					{
						System.out.println("delete file " + list.get(i));
						Path tempPath = new Path(list.get(i));
						if(fs.exists(tempPath))
						{
							fs.delete(tempPath);
						}
					}
				//清除状�?信息�?     T4
				this.clearAttribute();	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	statsString="";
    }
    
   //这个涉及到回�?
  //statsString：格式： 选中的目�?分隔�?合并的数�?分隔�?文件1+分隔�?文件2+分隔�?文件3+分隔�?..+分隔�?合并的临时目�?
    public void parseAttribute()
    {	
    	statsString= this.getNextAttibute();
    	String[] infors = statsString.split(saveInZKSeprater);
    	if(infors.length != 0)
    	{
    		int number = Integer.parseInt(infors[0]);
    		if(number !=0)
    		{
    			ArrayList<String> list = new ArrayList<String>();
    			for(int i = 0 ; i < number ;i++)
    			{
    				list.add(infors[2+i]);
    			}
    			String mergeTempFileString =infors[infors.length-1];
    			RecoverMergeFile(list  , mergeTempFileString);
    		}
    	}
    	super.parseAttribute();
    	PrintAttribute();
    }
    
    public void PrintAttribute()
    {
    	super.PrintAttribute();
    }
	//statsString：格式： 选中的目�?分隔�?合并的数�?分隔�?文件1+分隔�?文件2+分隔�?文件3+分隔�?..+分隔�?合并的临时目�?
    public  void saveAttribute()
    {
    	clearAttribute();
    	System.out.println(" save statsString  in ZK  is " + statsString);
    	this.addSaveAttribute(statsString);
    	super.saveAttribute();
    }
    	
    //实现方式�? 目录按天存放
    //1�?�?分钟(60�?�?  �?��是否有多个org文件生成，并合并.每次�?��合并4�?
    //2 :  �?0分种(600�?：检测是否有多个merge(按照大小排序)，每次最多合�?�?
	public void doAction() 
	{		
    	while(!this.lifeOver())
    	{	
    		try {
    			int count = 0 ;
    			//合并org文件
    			int times = 5 ;
    			for(int i = 0 ; i < times;i++ )
    			{	int chooseNumber = random.nextInt(chooseLoopMax);
    				ArrayList<Path>  orglist = getOrgFile(chooseNumber);
    				mergeFile(orglist);
    				Thread.sleep(sleepTime);
    			}
    			//合并merge文件
//    			ArrayList<Path>  merglist = getMergeFile();
//    			mergeFile(merglist);
    			
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	System.out.println("do Action is over");
	}
	
	public String chooseHighIndexDir(int chooseNumber)
	{
		
		chooseCountNumber++; 
		//当chooseCountNumber�?的�?数时，检测上�?��目录   				现在以天为单位，这里是昨�?
		//当chooseCountNumber�?5的�?数时�?�?��上上个小时的目录     现在以天为单位，这里是前�?
		int chooseNextDir = 3600;   //3600*sleepTime的时间间�?
		int chooseNextNextDir = 3600*12;
		String reslut ="";
		if( chooseCountNumber - (chooseCountNumber/chooseNextNextDir)*chooseNextNextDir == 0)  
		{
			//选择上上的目�?
			reslut = QueryTargetIndex.getCurrentTimeNextNDay(-2);
		}
		if(chooseCountNumber - (chooseCountNumber/chooseNextDir)*chooseNextDir == 0) //选择上一小时的目�? 
		{
			//选择上个小时的目�?
			reslut = QueryTargetIndex.getCurrentTimeNextNDay(-1);
		}
		   chooseCountNumber++;
		   reslut =QueryTargetIndex.getCurrentTimeNextNDay(0);
//		   System.out.println("choose index file is " + "/cwtest/bssap/indexfile/callingIndex/data/2188/1");
		//return "/cwtest/bssap/indexfile/callingIndex/data/"+reslut;
		   
		   if(chooseNumber >= highIndexName.size())
		   {
			   return "";
		   }
		   String chooseIndex = highIndexName.get(chooseNumber);
		   if(chooseIndex == null || chooseIndex.length() == 0)
		   {
			   return "";
		   }
		   String tableName = getIndexTableName(chooseIndex);
		   String indexName = getIndexIndexName(chooseIndex);
		   indexPath =  TableAndIndexTool.getIndexFormat(tableName, indexName, appName);
		   String indexDataPathString = TableAndIndexTool.getIndexDataPath(tableName, indexName, appName);
		   System.out.println("indexFormat  is " + indexPath+"   chooseNumber is  " + chooseNumber);
		   System.out.println("choose index is " +indexDataPathString+fileSepratorString+reslut);
		   return indexDataPathString+fileSepratorString+reslut;

	}
	
	public String chooseMidIndexDir(int chooseNumber)
	{
		
		chooseCountNumber++; 
		//当chooseCountNumber�?的�?数时，检测上�?��目录   				现在以天为单位，这里是昨�?
		//当chooseCountNumber�?5的�?数时�?�?��上上个小时的目录     现在以天为单位，这里是前�?
		int chooseNextDir = 3600;   //3600*sleepTime的时间间�?
		int chooseNextNextDir = 3600*12;
		String reslut ="";
		if( chooseCountNumber - (chooseCountNumber/chooseNextNextDir)*chooseNextNextDir == 0)  
		{
			//选择上上的目�?
			reslut = QueryTargetIndex.getCurrentTimeNextNDay(-2);
		}
		if(chooseCountNumber - (chooseCountNumber/chooseNextDir)*chooseNextDir == 0) //选择上一小时的目�? 
		{
			//选择上个小时的目�?
			reslut = QueryTargetIndex.getCurrentTimeNextNDay(-1);
		}
		   chooseCountNumber++;
		   reslut =QueryTargetIndex.getCurrentTimeNextNDay(0);
//		   System.out.println("choose index file is " + "/cwtest/bssap/indexfile/callingIndex/data/2188/1");
		//return "/cwtest/bssap/indexfile/callingIndex/data/"+reslut;
		   
		   if(chooseNumber >= midIndexName.size())
		   {
			   return "";
		   }
		   String chooseIndex = midIndexName.get(chooseNumber);
		   if(chooseIndex == null || chooseIndex.length() == 0)
		   {
			   return "";
		   }
		   String tableName = getIndexTableName(chooseIndex);
		   String indexName = getIndexIndexName(chooseIndex);
		   indexPath =  TableAndIndexTool.getIndexFormat(tableName, indexName, appName);
		   String indexDataPathString = TableAndIndexTool.getIndexDataPath(tableName, indexName, appName);
		   System.out.println("indexFormat  is " + indexPath+"   chooseNumber is  " + chooseNumber);
		   System.out.println("choose index is " +indexDataPathString+fileSepratorString+reslut);
		   return indexDataPathString+fileSepratorString+reslut;

	}
	
	public String chooseLowIndexDir(int chooseNumber)
	{
		
		chooseCountNumber++; 
		//当chooseCountNumber�?的�?数时，检测上�?��目录   				现在以天为单位，这里是昨�?
		//当chooseCountNumber�?5的�?数时�?�?��上上个小时的目录     现在以天为单位，这里是前�?
		int chooseNextDir = 3600;   //3600*sleepTime的时间间�?
		int chooseNextNextDir = 3600*12;
		String reslut ="";
		if( chooseCountNumber - (chooseCountNumber/chooseNextNextDir)*chooseNextNextDir == 0)  
		{
			//选择上上的目�?
			reslut = QueryTargetIndex.getCurrentTimeNextNDay(-2);
		}
		if(chooseCountNumber - (chooseCountNumber/chooseNextDir)*chooseNextDir == 0) //选择上一小时的目�? 
		{
			//选择上个小时的目�?
			reslut = QueryTargetIndex.getCurrentTimeNextNDay(-1);
		}
		   chooseCountNumber++;
		   reslut =QueryTargetIndex.getCurrentTimeNextNDay(0);
//		   System.out.println("choose index file is " + "/cwtest/bssap/indexfile/callingIndex/data/2188/1");
		//return "/cwtest/bssap/indexfile/callingIndex/data/"+reslut;
		   
		   
		   if(chooseNumber >= lowIndexName.size())
		   {
			   return "";
		   }
		   String chooseIndex = lowIndexName.get(chooseNumber);
		   if(chooseIndex == null || chooseIndex.length() == 0)
		   {
			   return "";
		   }
		   String tableName = getIndexTableName(chooseIndex);
		   String indexName = getIndexIndexName(chooseIndex);
		   indexPath =  TableAndIndexTool.getIndexFormat(tableName, indexName, appName);
		   String indexDataPathString = TableAndIndexTool.getIndexDataPath(tableName, indexName, appName);
		   System.out.println("indexFormat  is " + indexPath+"   chooseNumber is  " + chooseNumber);
		   System.out.println("choose index is " +indexDataPathString+fileSepratorString+reslut);
		   return indexDataPathString+fileSepratorString+reslut;

	}
	
	

	public void mergeFile(ArrayList<Path>  list )
	{
		System.out.println("merge file number is " +list.size());
		ArrayList<Path> tempName = new ArrayList<Path>();
		String mergFileNameString = "";
		//将文件拉倒本地；
		for(int i = 0 ; i <list.size() ; i++)
		{
			try {
				String strPath = list.get(i).getName();
				Path tempPath = new Path( tempDir +(localFileSepratorString)+ list.get(i).getName());
				getFileToLocal(""+list.get(i),tempDir +(localFileSepratorString)+ list.get(i).getName());
				tempName.add(tempPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//合并文件
		try {
			Path  path = mergeFiles(tempName);
			if(path == null)
			{
				System.out.println(" failed  null path is " + path);
				return ;
			}
			//将文件传输到HDFS---》tempReslut:  加时间变�?
			try {
				//获取文件大小，计算文件的Block块大小；上传文件
				File file = new File(path+"");
				if(!file.exists())
				{
					System.out.println(" failed merge path is " + path);
					return ;
				}
				FileInputStream fileInputStream = new  FileInputStream(file);
				long size = fileInputStream.available();
				long baseBlock = 64*1024*1024;
				long blockSize =((size/baseBlock)+1)*baseBlock;
				fileInputStream.close();
				Configuration conf = new Configuration();
				conf.set("fs.default.name", fs.getConf().get("fs.default.name") );
				try {
					conf.set("dfs.block.size", blockSize+"");
					conf.set("dfs.replication", fs.getConf().get("dfs.replication"));
					System.out.println("fs.default.name  " + fs.getConf().get("fs.default.name"));
					System.out.println("hdfsMasterIP  " +  hdfsMasterIP);
					System.out.println("hdfsMasterPort  " +  hdfsMasterPort);
					//DistributedFileSystem newfs = new  DistributedFileSystem(new InetSocketAddress(hdfsMasterIP, Integer.parseInt(hdfsMasterPort)),conf);
					DistributedFileSystem newfs = new DistributedFileSystem();
					newfs.initialize(fs.getUri(), conf);
					newfs.copyFromLocalFile(path, new Path(choosePathString+fileSepratorString+path.getName()));
					newfs.setPermission(new Path(choosePathString+fileSepratorString+path.getName()), PERMISSION);
					newfs.close();
					newfs = null;
					conf = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			

			
			//在ZK里记录信息； ----T1
			//保存的信息内容为�?合并的目录命
			//选中的目�?分隔�?合并的数�?分隔�?文件1+分隔�?文件2+分隔�?文件3+分隔�?..+分隔�?合并的临时目�?
			StringBuffer sb = new StringBuffer();
			sb.append(this.choosePathString+saveInZKSeprater);
			sb.append(list.size()+saveInZKSeprater);
			for(int i = 0 ; i <list.size() ; i++)
			{
				sb.append(list.get(i)+saveInZKSeprater);
			}
			sb.append(choosePathString+fileSepratorString+path.getName());
			statsString = sb.toString();
			this.saveAttribute();
			//@gyy Modify
			//修改名称                -----T2
			   mergFileNameString = choosePathString+fileSepratorString+path.getName().substring(0, path.getName().length()-tempCharString.length())+BTreeType.mergeName ;
			  fs.rename(new Path(choosePathString+fileSepratorString+path.getName()), 
							new Path(choosePathString+fileSepratorString+path.getName().substring(0, path.getName().length()-tempCharString.length())+BTreeType.mergeName )
					);
			//删除						 -----T3
				for(int i = 0 ; i < list.size() ; i++ )
				{
					System.out.println("delete file " + list.get(i));
					fs.delete(list.get(i));
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			//清除状�?信息�?     T4
			this.clearAttribute();
			
			//删除本地merge后的文件
			System.out.println("@gyy:localPath is " + path.getName()+"  hdfsPath is " + choosePathString+fileSepratorString+path.getName().substring(0, path.getName().length()-tempCharString.length())+BTreeType.mergeName);
			tempName.add(path);
		} catch (Exception e) {
		  this.clearAttribute();
		}
		//清除本地的临时文�?
		cleanTempFile(tempName);
		
		//打印合并信息�?
		for( int i = 0 ; i < list.size() ; i++)
		{
			System.out.println(System.currentTimeMillis()+":@gyyOrgToMerge: org:" + list.get(i)+"merg:" +mergFileNameString);

		}

		
	}
	
	public void cleanTempFile(ArrayList<Path> tempName)
	{
		System.out.println("delete tempSize is " + tempName.size() );
		for(int i = 0 ; i < tempName.size() ; i++)
		{
			System.out.println("delete temp file "+tempDir+localFileSepratorString+tempName.get(i).getName());
			File file = new File(tempDir+localFileSepratorString+tempName.get(i).getName()+"");
			if(file.exists())
			{	
				boolean result = file.delete();
				System.out.println("file extist and delete reslut is " + result );
				while(!result)
				{
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					result = file.delete();
					System.out.println("file extist and delete reslut is " + result );
				}
			}
			else {
				System.out.println("file  not extist " );
			}
		}
	}
	
	
	public int getMaxTime(String name )
	{
		String [] temp = name.split("_");
		int ts = Integer.parseInt(temp[1]);
		return ts;
	}
	
	
	public int getMinTime(String name)
	{
		String [] temp = name.split("_");
		int te = Integer.parseInt(temp[0]);
		return te;
	}
	
	public int getFileNumber(String name )
	{
		String [] temp = name.split("_");
		int ts = 0;
		if(name.matches(BTreeType.matchMergeName))
		{
			//System.out.println("file name is " + temp[2]);
		 ts = Integer.parseInt(temp[2]);
		}
		else {
			//System.out.println("file name is " + temp[2]);
			ts = Integer.parseInt(temp[2]);
		}
		
		return ts;
	}
	
	
	public String getLocalFileName() {
		// File file = new File();
		return java.util.UUID.randomUUID().toString();
	}
	
	//at local
	private Path  mergeFiles(ArrayList<Path> fileLists) throws  Exception
	{
		//获取表名
		//获取应用�?
		//获取索引�?
		//获取结果�?
		if(fileLists.size() == 0)
		{
			return null;
		}
		int maxTime = Integer.MIN_VALUE;
		int minTime = Integer.MAX_VALUE;
		for(int i = 0 ; i < fileLists.size() ; i++)
		{
			int max = getMaxTime(fileLists.get(i).getName());
			int min = getMinTime(fileLists.get(i).getName());
			if(maxTime < max )
			{
				maxTime = max ;
			}
			if(minTime > min )
			{
				minTime = min ;
			}
		}
		String tempreslut = minTime+"_"+maxTime;
		 //生成索引结构的目�?
		//�?��合并
		ParseIndexFormat readIndexFormat = null;
		try {
			if(fs != null)
			{
				readIndexFormat = new ParseIndexFormat(indexPath,fs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ReadWriteIndexDataProxy proxy = new ReadWriteIndexDataProxy(readIndexFormat);
		ArrayList<byte[]> dataList2 = new ArrayList<byte[]>();
		
		ArrayList<Class> types = readIndexFormat.getIndexColumnType();
		ArrayList<Class>  indexTypes = types;
		for (int i = 0; i < types.size(); i++) {
			dataList2.add(DataType.getMinObject(types.get(i)));
		}
			
		System.out.println("merge file to " + tempDir+localFileSepratorString+tempreslut+tempCharString);
		String tempName =  tempDir+localFileSepratorString+tempreslut+tempCharString;
		MergerBPlusTree<MulitFiledData> table = new MergerBPlusTree<MulitFiledData>(tempName);
		table.setTemplate(new MulitFiledData(dataList2, proxy));
		ArrayList<String> list = new  ArrayList<String>();
		for(int i = 0 ; i < fileLists.size()  ; i++ )
		{
			System.out.println("file is " +fileLists.get(i));
			list.add(fileLists.get(i)+"");
		}
		table.MergeBPlusTree(list);
		//打印合并的源文件
		File tempFile = new File(tempName);
		tempName = tempDir+localFileSepratorString+tempreslut+"_"+table.getFileNumberCount()+"_"+this.getLocalFileName()+tempCharString;
		File newFile = new File(tempName);
		if( !tempFile.renameTo(newFile))
		{
			System.out.println("rename is failed");
		}
		tempFile = null;
		newFile = null;
		return new Path(tempName);
	}


	
	 public  void getFileToLocal (String remotePath ,String localPath)
	 {
	   	try {
	   			fs.setVerifyChecksum(false);
			   FSDataInputStream in = fs.open(new Path(remotePath));
			   File file = new File(localPath);
			   if(!file.exists())
			   {
				   file.delete();
			   }
				DataOutputStream output = new DataOutputStream(new FileOutputStream(file));
				byte[] temp = new byte[1024*10]; 
				while(true)
				{	
					int count = in.read(temp);
					if(count == -1)
					{
						break;
					}
					output.write(temp,0,count);
				}
				output.flush();
				output.close();
				in.close();
			   
			} catch (IOException e) { 
				e.printStackTrace();
			}
	}
	 
	 public void printFileMap()
	 {
		 
	 }
	
	
	
	public ArrayList<Path> getOrgFile(int chooseNumber)
	{
		if(chooseNumber <= highMax)
		{
			if(highChooseCountNumber == highIndexName.size())
			{
				highChooseCountNumber=0;
			}
			choosePathString = chooseHighIndexDir(highChooseCountNumber);
			highChooseCountNumber ++;
		}
		else if(chooseNumber <= midMax){
			if(midChooseCountNumber == midIndexName.size())
			{
				midChooseCountNumber=0;
			}
			choosePathString = chooseMidIndexDir(midChooseCountNumber);
			midChooseCountNumber ++;
		}
		else if(chooseNumber <= lowMax){
			if(lowChooseCountNumber == lowIndexName.size())
			{
				lowChooseCountNumber=0;
			}
			choosePathString = chooseLowIndexDir(lowChooseCountNumber);
			lowChooseCountNumber ++;
		}
		System.out.println("choosePath is " + choosePathString);

		try {
			if( choosePathString.length() == 0 || !fs.exists(new Path(choosePathString)))
			{
				return new ArrayList<Path>();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	ArrayList<Path> fileNameList = new ArrayList<Path>();
    	 try {
    		 if( fs  != null)
    		 {
				FileStatus fileList[] = fs.listStatus(new Path(choosePathString),orgFilter);
				chooseFile(fileNameList,fileList);
//				if(fileList  != null)
//				{
//					if(fileList.length == 0)
//					{
//						return fileNameList;
//					}
//					int count = 0;
//					for(int i = 0 ; i < fileList.length ;i++)
//					{
//						fileNameList.add( fileList[i].getPath());
//						if(fileNameList.size() == orgMaxMerge)
//						{
//							return fileNameList;
//						}
//					}
//					
//				}
			}
    	 } catch (IOException e) {
 			e.printStackTrace();
 		}
    	return fileNameList;
	}
	
	private void chooseFile(ArrayList<Path> fileNameList , FileStatus[] fileList)
	{
		
		if(fileList  != null)
		{
			if(fileList.length == 0)
			{
				return ;
			}
			ArrayList<FileNumberAndName> numberList = new ArrayList<FileNumberAndName>(); 
			for(int i = 0 ; i < fileList.length ;i++)
			{
				
				numberList.add( new FileNumberAndName(getFileNumber(fileList[i].getPath().getName()), fileList[i].getPath()));
			}
	//		System.out.println("****************************************" + numberList +"*****************************");
			java.util.Collections.sort(numberList);
			int fileCount = 0;
			for(int i = 0 ; i < numberList.size() ; i++)
			{				
	//				System.out.println("numberList.get(i).number is " + numberList.get(i).number);
					if(fileCount +  numberList.get(i).number >maxFileInOneFile)
					{
						return ;
					}
					fileNameList.add(numberList.get(i).path);	
					fileCount = fileCount + numberList.get(i).number;
	//				System.out.println("****************************************fileCount"+fileCount+"   "+maxFileInOneFile);
			}
		}
	}

/*	
	public ArrayList<Path> getMergeFile()
	{
//		if(mergeIndexChooseNumber == indexName.size())
//		{
//			mergeIndexChooseNumber=0;
//		}
//		choosePathString = chooseIndexDir(orgIndexChooseNumber);
		mergeIndexChooseNumber  ++;
    	ArrayList<Path> fileNameList = new ArrayList<Path>();
    	 try {
    		 if( fs  != null)
    		 {
				FileStatus fileList[] = fs.listStatus(new Path(choosePathString),mergeFilter);
				if(fileList  != null)
				{
					if(fileList.length == 0 || fileList.length < startMergeLaunch)
					{
						return fileNameList;
					}
					int count = 0;
					int fileCount = 0;
					for(int i = 0 ; i < fileList.length ;i++)
					{
						if(fileCount + getFileNumber(fileList[i].getPath().getName()) >=maxFileInOneFile)
						{
							continue;
						}
						fileNameList.add( fileList[i].getPath());
						if(fileNameList.size() == mergeMaxMerge)
						{
							return fileNameList;
						}
					}
				}
			}
    	 } catch (IOException e) {
 			e.printStackTrace();
 		}
    	return fileNameList;
	}
*/	
	public static void main(String[] args)
	{
		String confile =args[0];
		MergeSever  test = new MergeSever(confile);
		try {
			test.check();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			e.printStackTrace();
		}
	}
}
