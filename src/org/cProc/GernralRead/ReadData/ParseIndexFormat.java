package org.cProc.GernralRead.ReadData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import sun.nio.cs.ext.TIS_620;



public class ParseIndexFormat {
	FileSystem fs  ;
	
	String IndexFormatFile ="";
	
	//the format of the data file
	private final String ColumnContentSeperate =":";
	private final int IndexColumnTypeInContentPos = 0;
	private final int NameInContentPos = 1;
	private final int typeInContentPos = 2;
	public static final String nullInforColumn ="nullInfor";
	private final Class  nullInforType =Integer.class;
	//存放所有索引字段
	ArrayList<String> IndexCloumnSeq = new ArrayList<String>();
	//存放所有索引字段类型
	ArrayList<Class>  IndexCloumnTypeSeq = new ArrayList<Class>();
	
	int offset = 0;
	//存放所有索引字段开始位置
	ArrayList<Integer>  IndexCloumnStartPos = new ArrayList<Integer>();
	//存放所有索引字段长度
	ArrayList<Integer>  IndexCloumnLength = new ArrayList<Integer>();
	//存放time字段名
	private String primryTimeColumn = "";

	private final String primryTimeType ="time";
	
	private String tableName ="";
	private String indexName = "";
	private int indexVersion = 0;
	private int  mergeLevelValue = 1;



	private static ConcurrentHashMap<String, ParseIndexFormat> ParseIndexFormatMap 
	= new ConcurrentHashMap<String, ParseIndexFormat>();

	
	public static ParseIndexFormat getParseIndexFormat(String IndexFormatFile , FileSystem  fs)
	{
		return new   ParseIndexFormat( IndexFormatFile ,   fs);
	}
	
	public  ParseIndexFormat(String IndexFormatFile , FileSystem  fs)
	{
		if(ParseIndexFormatMap.get(IndexFormatFile) != null)
		{
			ParseIndexFormat format = ParseIndexFormatMap.get(IndexFormatFile);
			this.IndexCloumnSeq = format.IndexCloumnSeq;
			this.IndexCloumnTypeSeq = format.IndexCloumnTypeSeq;
			this.IndexCloumnStartPos = format.IndexCloumnStartPos;
			this.IndexCloumnLength = format.IndexCloumnLength;
			this.mergeLevelValue = format.mergeLevelValue;
			this.primryTimeColumn = format.primryTimeColumn;
		}
		else {
			setFileSystem(fs);
			setIndexFormatFile(IndexFormatFile);
			parseIndexFormatFile();
			ParseIndexFormatMap.put(IndexFormatFile, this);
		}

	}
	
	public int getClomunStartPos( int index)
	{
		return IndexCloumnStartPos.get(index);
	}
	
	public int getColumnLengthByte(int index)
	{
		return IndexCloumnLength.get(index);
	}
	
	private void setFileSystem(FileSystem  fs )
	{
		this.fs = fs;
	}
	
	private void setIndexFormatFile(String IndexFormatFile)
	{
		this.IndexFormatFile = IndexFormatFile;
	}
	
	public ArrayList<String> getIndexColumnName()
	{
			return  IndexCloumnSeq;
	}
	public ArrayList<Class> getIndexColumnType()
	{
		return IndexCloumnTypeSeq;
	}
	
	private  void parseIndexFormatFile()
	{
		FSDataInputStream input = null;
		try {
			 input = new FSDataInputStream(fs.open(new Path(IndexFormatFile)));
             readIndexNameAndCheck(input);
			 readIndexCloumn(input);
			 input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private void readIndexNameAndCheck(FSDataInputStream input) throws IOException
	{
		//表名；索引名;索引文件;合并优先级；
		
		String readTableNameLine = input.readLine();
		this.tableName =readTableNameLine.split(ColumnContentSeperate)[1];
		
		String readIndexNameLine = input.readLine();
		 this.indexName =readIndexNameLine.split(ColumnContentSeperate)[1];
		 
//		 String readIndexVerionLine = input.readLine();
//		String indexVersion = readIndexVerionLine.split(ColumnContentSeperate)[1];
////		读取index的name和版本；
//		String mergeLevel = input.readLine();
//
//		String[] mergeStrings =mergeLevel.split(ColumnContentSeperate);
//		String mergeLevelStirng = mergeStrings[1].trim().toUpperCase();
//		if(mergeLevelStirng.equals(MergeLevel.highString))
//		{
//			mergeLevelValue = MergeLevel.high;
//		}
//		else if( mergeLevelStirng.equals(MergeLevel.midString))
//		{
//			mergeLevelValue = MergeLevel.mid;
//		}
//		else if(mergeLevelStirng.equals(MergeLevel.lowString))
//		{
//			mergeLevelValue = MergeLevel.low;
//		}	
	}
	
	public String getTableName()
	{
		return tableName;
	}
	
	public String getIndexName()
	{
		return indexName;
	}
	public int getIndexVersion() {
		return indexVersion;
	}
	public int getMergeLevelValue()
	{
		return mergeLevelValue;
	}
	
	private void readIndexCloumn(FSDataInputStream input)  throws IOException
	{
		while(input.available() > 0)
		{
			String indexInfor = input.readLine();

			parseOneColumn(indexInfor);
		}
		//每行索引数据后追加一个nullInfo的字段
//		 IndexCloumnSeq.add(this.nullInforColumn);
//		 IndexCloumnStartPos.add(offset);
//		 IndexCloumnTypeSeq.add(this.nullInforType);
//		 IndexCloumnLength.add(4);
	}
	
	public String getPrimryTimeColumn()
	{
		return primryTimeColumn;
	}
	
	public int  getPrimryTimeColumnPosInIndex()
	{	
		System.out.println("@gyy:printTimeColumn is " + primryTimeColumn);
		return IndexCloumnSeq.indexOf(primryTimeColumn);
	}
	
	private void parseOneColumn(String indexInfor)
	{
		if(indexInfor.trim().length()==0)
			return;
		//indexInfor格式为：other:called_number:LONG
		 String [] indexInfors = indexInfor.split(ColumnContentSeperate);
		 //是否是时间类型，值为other或time
		 String timeType = indexInfors[IndexColumnTypeInContentPos].trim();
		 //字段名
		 String indexColumn =  indexInfors[ NameInContentPos].trim() ;
		 //找到time字段
		 if(timeType.equals(primryTimeType) && primryTimeColumn.length() == 0)
		 {
			 primryTimeColumn =  indexColumn ;
		 }
		 //字段类型
		 String indexType =  indexInfors[ typeInContentPos].trim() ;
		 //设置索引名称
		 IndexCloumnSeq.add(indexColumn);
		 //设置超始位置
		 IndexCloumnStartPos.add(offset);
		 if(indexType.equals(DataType.ByteType))
		 {
			 IndexCloumnTypeSeq.add(Byte.class);
			 IndexCloumnLength.add(1);
			 offset = offset + 1;
			 
		 }
		 else if(indexType.equals(DataType.ShortType)){
			 IndexCloumnTypeSeq.add(Short.class);
			 IndexCloumnLength.add(2);
			 offset = offset + 2;
		 }
		 else if(indexType.equals(DataType.IntType)){
			 IndexCloumnTypeSeq.add(Integer.class);
			 IndexCloumnLength.add(4);
			 offset = offset + 4;
		 } else if(indexType.equals(DataType.LongType))
		{
			 IndexCloumnTypeSeq.add(Long.class);
			 IndexCloumnLength.add(8);
			 offset = offset + 8;
		}
	}
	
	
	public int getColunmPos(String column)
	{
		return IndexCloumnSeq.indexOf(column);
	}
	
	public Class getColunmType(String column)
	{
		return IndexCloumnTypeSeq.get((getColunmPos(column)));
	}



	public ArrayList<IndexColumnInfor> getColumnPosInTable(ParseTableFormat  format)
	{
		 ArrayList<IndexColumnInfor> reslut= new  ArrayList<IndexColumnInfor>();
		 for(int i = 0 ;  i < IndexCloumnSeq.size() ; i++)
		 {
			 reslut.add(new IndexColumnInfor(IndexCloumnSeq.get(i), IndexCloumnTypeSeq.get(i)));
		 }
		return reslut;
	}
	

	
		
	public static void main(String args[])
	{
		String path ="D:\\test\\index.frm" ;
		Configuration conf = new Configuration();
//		conf.set("fs.default.name","hdfs://192.168.1.8:9000");
		try {
			FileSystem fs = FileSystem.get(conf);
			if(fs != null)
			{
				ParseIndexFormat readIndexFormat = new ParseIndexFormat(path,fs);
				System.out.println(" PrimryTime is " + readIndexFormat.getPrimryTimeColumn()+"  "+readIndexFormat.getPrimryTimeColumnPosInIndex());
				System.out.println(" table name  " + readIndexFormat.getTableName());
				System.out.println(" index name  " + readIndexFormat.getIndexName());
				System.out.println(" index version  " + readIndexFormat.getIndexVersion());
				System.out.println(" merge level " + readIndexFormat.getMergeLevelValue());
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
