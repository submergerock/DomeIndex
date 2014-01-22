package org.cProc.GernralRead.ReadData.ReadDataProxy;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.cProc.GernralRead.ReadData.DataType;
import org.cProc.GernralRead.ReadData.ParseTableFormat;
import org.cProc.distributed.createIndex.Config;


public class TextReadDataProxy  implements ReadDataProxy{
	

	private String fileName = "";
	private  long offset = 0; 
	private String currentData="";
	private String[]  columnDatas =null;
	private FileSystem fs = null;
	
//	private final String ColumnSeperate ="\t";
	private BufferedReader input = null;
	private int SeperateLength = 2;
	private ParseTableFormat tableFormat  =null;
	public  TextReadDataProxy(FileSystem fs  )
	{
		
		this.fs = fs;
	}
	public void setParseTableFormat(ParseTableFormat tableFormat)
	{
		this.tableFormat = tableFormat;
	}
	
	//获取文件系统
	public void  setFileName(String fileName )
	{
		this.fileName = fileName;
	}

	public void startReadFile() throws IOException
	{
	
			offset = 0;
			input = new BufferedReader(new InputStreamReader(fs.open(new Path(fileName))));
		
	}
	
	public void closeReadFile()
	{	
		try {
			if (input != null) {
				input.close();
			}
			input = null;
		} catch (IOException e) {
			e.printStackTrace();
			input = null;
		}
		offset = 0;
	}
	
	public boolean readNextData() throws IOException {
		if (input == null) {
			startReadFile();
		}
		try {

			currentData = input.readLine();
			if(currentData == null)
			{
				offset = 0;
				return false;
			}
			
			/**
			 * 不加2
			 */
			offset = offset+currentData.getBytes().length+SeperateLength;
			columnDatas  =  currentData.split(Config.split);
			return true;
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	public byte[] getColumnData(int pos)
	{
		if("".equals(columnDatas[pos]))//为空
		{
//			return new byte[tableFormat.getLengthOfFileds().get(pos)];
			return null;
			
		}else if(null==columnDatas[pos])
		{
			return null;
		}
		return DataType.getBytes(tableFormat.getColumnType(tableFormat.getColumnName(pos)), columnDatas[pos]);
	}
	public long  getOffset()
	{
		return offset;
	}

}
