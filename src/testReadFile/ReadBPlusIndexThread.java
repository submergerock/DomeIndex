package testReadFile;

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
			thread.setDiskPath("Q:\\");
			list.add(thread);
			return list;
		}
		Configuration conf = new Configuration();
		conf.addResource(new Path(conPath));
		String disks = conf.get("dfs.data.dir");
		if (disks == null || disks.length() == 0) {
			ReadBPlusIndexThread thread = new ReadBPlusIndexThread(condition);
			thread.setDiskPath("/");
			list.add(thread);
			return list;
	}

		String[] offsets = disks.split(",");
		for (int i = 0; i < offsets.length; i++) {
//		for (int i = 0; i < 1; i++) {	
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
		String SQL = splits[0];
		String oldSQL = splits[1];
		//LOG.info("-----searchCDR  SQL: SQL-----");
		//LOG.info("-----searchCDR  OLDSQL: "+oldSQL);
		MaxAndMinValueOfSQLGen gen = null;

			
    	ParseIndexFormat readIndexFormat = null;
		String path =data.getIndexPath();
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
		ArrayList<Class> typesArrayList = readIndexFormat.getIndexColumnType();
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

 