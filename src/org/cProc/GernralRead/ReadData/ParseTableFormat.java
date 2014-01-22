package org.cProc.GernralRead.ReadData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.sun.org.apache.bcel.internal.generic.NEW;


public   class  ParseTableFormat {
	
	
	private static ConcurrentHashMap<String, ParseTableFormat> ParseTableFormatMap 
	= new ConcurrentHashMap<String, ParseTableFormat>();

	FileSystem fs  ;
	
	String tableFormatFile ="";
	
	//the format of the data file
	private final String ColumnContentSeperate =":";
	private final String inforInContentSeperate =",";
	private final int CloumnContentPos = 1;
	private final int NameInContentPos = 0;
	private final int TypeInContentPos = 1;
	private final int ByteLengthInContentPos = 2;
	ArrayList<String> CloumnSeq = new ArrayList<String>();
	HashMap<String,Class> CloumnAndTypeMap = new  	HashMap<String,Class> ();
	HashMap<String,Integer> StringAndLengthMap = new  	HashMap<String,Integer> (); //forString
	int offset = 0;
	private ArrayList<Integer > startPosOfFileds = new ArrayList<Integer > ();
	private ArrayList<Integer > lengthOfFileds = new ArrayList<Integer > ();
	
	private String tableName ="";
	private int  tableVersion =0;
	

	public  ParseTableFormat(String tableFormatFile , FileSystem  fs)
	{
		if(ParseTableFormatMap.get(tableFormatFile) != null)
		{
			ParseTableFormat format = ParseTableFormatMap.get(tableFormatFile);
			this.CloumnSeq = format.CloumnSeq;
			this.CloumnAndTypeMap = format.CloumnAndTypeMap;
			this.StringAndLengthMap = format.StringAndLengthMap;
			this.startPosOfFileds = format.startPosOfFileds;
			this.lengthOfFileds   = format.lengthOfFileds;
		}
		else {
			setFileSystem(fs);
			setTableFormatFile(tableFormatFile);
			parseTableFormatFile();
			ParseTableFormatMap.put(tableFormatFile,this);
		}
		
	}
	
	
	
	private void setFileSystem(FileSystem  fs )
	{
		this.fs = fs;
	}
	
	private void setTableFormatFile(String tableFormatFile)
	{
		this.tableFormatFile = tableFormatFile;
	}
	
	
	
	private  void parseTableFormatFile()
	{	
//		DebugPrint.DebugPrint("start:parseTableFormatFile" ,this);
		FSDataInputStream input = null;
//		tableFormatFile ="D:\\UDM\\beijing\\table.frm";
		try {
			 input = new FSDataInputStream(fs.open(new Path(tableFormatFile)));
             readTableNameAndCheck(input);
			 readTableCloumn(input);
			 input.close();

		} catch (Exception e) {
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
	
	private void readTableNameAndCheck(FSDataInputStream input) throws IOException
	{
		String tableNameLine = input.readLine();
		//String versionStringLine = input.readLine();
	    String[]	 tableNameArray = tableNameLine.split(ColumnContentSeperate);
	    tableName = tableNameArray[1];
	  //  String[]	 versionStringArray = versionStringLine.split(ColumnContentSeperate);
	   // tableVersion = Integer.parseInt(versionStringArray[1]);
		
	}
	
	private void readTableCloumn(FSDataInputStream input)  throws IOException
	{
		while(input.available() > 0)
		{
			String tableName = input.readLine();
			String[] contents = tableName.split(ColumnContentSeperate);
			String[] ColumInfor =  contents[CloumnContentPos].split(inforInContentSeperate);
			parseOneColumn(ColumInfor);
		}
		
	}
	
	private void parseOneColumn(String[] ColumInfor)
	{
		startPosOfFileds.add(offset);
		int size = 0;
		CloumnSeq.add(ColumInfor[NameInContentPos].trim());
		if(ColumInfor[TypeInContentPos].trim().equalsIgnoreCase(DataType.IntType))
		{
			CloumnAndTypeMap.put(ColumInfor[NameInContentPos].trim(), Integer.class);
			size = 4;
			
		}
		else if(ColumInfor[TypeInContentPos].trim().equalsIgnoreCase(DataType.LongType))
		{
			CloumnAndTypeMap.put(ColumInfor[NameInContentPos].trim(), Long.class);
			size = 8;
			
		}else  if(ColumInfor[TypeInContentPos].trim().equalsIgnoreCase(DataType.StringType))
		{
			CloumnAndTypeMap.put(ColumInfor[NameInContentPos].trim(), String.class);
			StringAndLengthMap.put(ColumInfor[NameInContentPos].trim(), Integer.parseInt(ColumInfor[ByteLengthInContentPos].trim()));
			size = Integer.parseInt(ColumInfor[ByteLengthInContentPos].trim());
			
		}else if(ColumInfor[TypeInContentPos].trim().equalsIgnoreCase(DataType.ByteType))
		{
			CloumnAndTypeMap.put(ColumInfor[NameInContentPos].trim(), Byte.class);
			size = 1;
		}else if(ColumInfor[TypeInContentPos].trim().equalsIgnoreCase(DataType.ShortType))
		{
			CloumnAndTypeMap.put(ColumInfor[NameInContentPos].trim(), Short.class);
			size = 2;
		}
		lengthOfFileds.add(size);
		offset  +=size;
		
	}
	
	
	public ArrayList<Integer > getStartPosOfFileds()
	{
		return  startPosOfFileds;
	}
	
	public ArrayList<Integer >  getLengthOfFileds()
	{
		return lengthOfFileds;
	}
	
	public int getColunmPos(String column)
	{
		return CloumnSeq.indexOf(column);
	}
	
	public Class getColumnType(String column)
	{
		return CloumnAndTypeMap.get(column);
	}
	
	public String getColumnName(int pos)
	{
		return  CloumnSeq.get(pos);
	}
	
	public int  getDataSize()
	{
		return startPosOfFileds.get(startPosOfFileds.size()-1) +lengthOfFileds.get(lengthOfFileds.size()-1);
	}
	
	public 	ArrayList<String>   getColumnName()
	{
		return CloumnSeq ;
	}
	
	public HashMap<String,Class> getColumnNameAndTypeHashMap()
	{
		return CloumnAndTypeMap;
	}
	
	public String getTableName()
	{
		return tableName;
	}
	
	
	public int getTableVersion()
	{
		return tableVersion;
	}
	
	public static void main(String args[])
	{
		String path ="D:\\test\\testFormat.txt" ;
		Configuration conf = new Configuration();
//		conf.set("fs.default.name","hdfs://192.168.1.8:9000");
		try {
			FileSystem fs = FileSystem.get(conf);
			if(fs != null)
			{
				ParseTableFormat readTableFormat = new ParseTableFormat(path,fs);
				System.out.println(readTableFormat.getColunmPos("start_time_s"));
				System.out.println(readTableFormat.getColunmPos("start_time_s"));
				System.out.println("dataSize is :" + readTableFormat.getDataSize());
				System.out.println("tablename is :" + readTableFormat.getTableName());
				System.out.println("tableVersion is :" + readTableFormat.getTableVersion());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
