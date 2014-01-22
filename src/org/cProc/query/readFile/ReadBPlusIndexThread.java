package org.cProc.query.readFile;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.cProc.GernralRead.ReadData.DataType;
import org.cProc.GernralRead.ReadData.ParseIndexFormat;
import org.cProc.index.BPlusTree.BPlusTreeMaxAndMinCompare;
import org.cProc.index.BPlusTree.Data.MulitFiledData;
import org.cProc.index.BPlusTree.Data.ReadWriteIndexDataProxy;
import org.cProc.index.BPlusTree.query.QueryInstanceFactory;
import org.cProc.index.BPlusTree.query.QueryInterface;
import org.cProc.sql.IndexFilter;
import org.cProc.sql.MaxAndMinValueOfSQLGen;
import org.cProc.sql.SupplementNewFiled;
import org.cProc.tool.Bytes;


public class ReadBPlusIndexThread extends ReadFromLocalDiskThreadBase<SQLIndex> {
	public static final Log LOG = LogFactory.getLog(ReadBPlusIndexThread.class.getName());
	
	private int dataSize = 0;
	private String startPath = "";
	private String fileName = "";
	private String sql ="";
	private String hdfsPath = "";
	private final String SQLAndIndexSepraterString = "-_-!";
	private MaxAndMinValueOfSQLGen gen = null;
	private String lastSQL="";
	public static  String fileSeperatorString ="/";


	public static ArrayList<ReadBPlusIndexThread> InitReadThread(
			String conPath, Integer condition) {
		
		ArrayList<ReadBPlusIndexThread> list = new ArrayList<ReadBPlusIndexThread>();
		if(conPath == null)
		{
			ReadBPlusIndexThread thread = new ReadBPlusIndexThread(condition);
			thread.setDiskPath("D:\\");
			list.add(thread);
			return list;
		}
		Configuration conf = new Configuration();
		conf.addResource(new Path(conPath));
		//从配置文件中读取HDFS数据文件的目录
		String disks = conf.get("dfs.data.dir");
		if (disks == null || disks.length() == 0) {
			ReadBPlusIndexThread thread = new ReadBPlusIndexThread(condition);
			thread.setDiskPath("/");
			list.add(thread);
			return list;
	    }

		
		
		//数据文件存放在多个目录时，为每个目录创建一个查询线程
		String[] offsets = disks.split(",");
		for (int i = 0; i < offsets.length; i++) {
//		for (int i = 0; i < 1; i++) {	
			
			
			
			//LOG.info("offsets[i].trim()----->"+offsets[i].trim());
			
			
			ReadBPlusIndexThread thread = new ReadBPlusIndexThread(condition);
			thread.setDiskPath(offsets[i].trim()+fileSeperatorString);
//			thread.setDiskPath("/");
			//LOG.info("thread " + i +"  is " + offsets[i]);
			list.add(thread);
		}
		return list;
	}

	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
	}

	public void setDiskPath(String startPath) {
		this.startPath = startPath;
	}

	public ReadBPlusIndexThread(Integer condition) {
		super(condition);
	}

	//sqlFormat:  SQL-_-!indexPath
	public void addReadIndex(String fileName, String sql,String hdfsPath) {
//		//LOG.info("fileName is " + fileName );
//		//LOG.info("startPath is " + startPath);
//		//LOG.info("sql  is " + sql);
		//fileName为索引文件名，startPath为当前线程所负责查询的数据文件目录
		//为true时则说明索引文件在当前线程负责查询的目录之中，
		if (fileName.startsWith(startPath)) {
			this.fileName = fileName;
			this.sql = sql;
			this.hdfsPath = hdfsPath;
			addReadIndex();
		}
	}

	public SQLIndex currentReadPos() {
		String[] temps =  sql.split(SQLAndIndexSepraterString);
		return new SQLIndex(fileName, temps[0],hdfsPath,temps[1]);
	}
	
	//format: fileName#offset,offset,offset
	//例子： file1#123,456,789
	public void readData(SQLIndex data)
	{
		//LOG.info("-----searchCDR: BplusTree-----");
		//System.out.println("1111");
		String SQLString = data.getSQL();
		String[] splits = SQLString.split(SupplementNewFiled.SEPRATEOLDSQLANDNEWSQL);
		String SQL = splits[0];//补充SQL
		String oldSQL = splits[1];//原SQL
		//LOG.info("-----searchCDR  SQL: SQL-----");
		//LOG.info("-----searchCDR  OLDSQL: "+oldSQL);
		MaxAndMinValueOfSQLGen gen = null;

		//索引格式	
    	ParseIndexFormat readIndexFormat = null;
		String path =data.getIndexPath();//索引定义文件
		Configuration conf = new Configuration();
//		conf.set("fs.default.name",data.getHdfsPath());
		FileSystem fs = null;
		try {
			 fs = FileSystem.get(conf);
			if(fs != null)
			{
				readIndexFormat = new ParseIndexFormat(path,fs);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(  !lastSQL.equals( SQL ))
		{
			gen = new MaxAndMinValueOfSQLGen(SQL , fs );	
		}
		
		ReadWriteIndexDataProxy proxy = new ReadWriteIndexDataProxy(readIndexFormat);
		ArrayList<byte[]> dataList = new ArrayList<byte[]>();
		//存放索引各列的类型
		ArrayList<Class> typesArrayList = readIndexFormat.getIndexColumnType();
		//根据索引各列的类型，设定各列的初始值，比如：Long为[0,0,0,0,0,0,0,0],Integer为[0,0,0,0]
		for(int i = 0 ; i < typesArrayList.size() ;i++)
		{
			dataList.add(
					DataType.getBytes(typesArrayList.get(i), 
					         Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(typesArrayList.get(i)))+"")
						);
		
		}
		////System.out.println("@createIndex fileName is " +data.getFileName() );
		QueryInterface<MulitFiledData> query =  new QueryInstanceFactory<MulitFiledData>().getQueryInstance(data.getFileName());
		   query.setTemplate(new MulitFiledData(dataList,proxy));
		   long start_time = System.currentTimeMillis();
		   //获得查询的最大值和最下值
			ArrayList<Object> dataListMin = new ArrayList<Object>();
			ArrayList<Object> dataListMax = new ArrayList<Object>();
		
			query.setIndexFilter(new IndexFilter<MulitFiledData>(oldSQL, fs, readIndexFormat));
			if(BPlusTreeMaxAndMinCompare.BPlusTreeMaxAndMinCompare(query.getMaxData(), 
					query.getMinData(),gen.getConditonMaxBytes(),gen.getConditonMinBytes(),gen.getParseTableFormat()))
			{
				query.findRangeData(gen.getMinMulitFiledData(proxy), gen.getMaxMulitFiledData(proxy));
			}
			//查询的时候是否存在 " == 无效值的情况"
			else if(gen.getHaveUnsignedMax()){
				query.findRangeData(gen.getMinMulitFiledData(proxy), gen.getMaxMulitFiledData(proxy));
			}
			//over
		   long end_time = System.currentTimeMillis();
		   ArrayList<String> offsetStrings =query.getSortFileOffsetResult();
		   ArrayList<Integer> fileNumbers = query.getFileNumberReuslt();
			query.queryClose();
			//System.out.println("66666");
		   query = null;
		   for(int m = 0; m < offsetStrings.size() ;m++ )
		   {	
			   byte[]  fileAndOffsets = offsetStrings.get(m).getBytes();
			   this.writeOneData(fileAndOffsets, fileAndOffsets.length);
			   fileAndOffsets = null;
		   }
		   //System.out.println("77777");
	}
	
		
}
 class SQLIndex {
	private String fileName =  "";
	private String query = "";
	private String hdfsPath =  "";
	private String indexFormatPath ="";
	public String getHdfsPath() {
		return hdfsPath;
	}

	public void setHdfsPath(String hdfsPath) {
		this.hdfsPath = hdfsPath;
	}

	public String getFileName() {
		return fileName;
	}

	public SQLIndex(String fileName, String sql,String hdfsPath , String indexPath) {
		this.fileName = fileName;
		this.query = sql;
		this.hdfsPath = hdfsPath;
		indexFormatPath = indexPath;
	}

	public void setIndexPath(String indexPath) {
		this.indexFormatPath = indexPath;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public  String getSQL() {
		return query;
	}
	
	public String getIndexPath()
	{
		return indexFormatPath;
	}

	public void setSQL(String sql) {
		this.query = sql;
	}
	
	
}

 