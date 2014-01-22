package org.cProc.index.BPlusTree.query.page;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cProc.gc.DirectByteBufferCleaner;
import org.cProc.index.BPlusTree.BTreeType;
import org.cProc.index.BPlusTree.Data.Data;
import org.cProc.tool.Bytes;



public class HeadPage<T extends Data>  extends Page{

	
	private static Log Log4jlogger = LogFactory.getLog(HeadPage.class); 
	
	public static int HEADSTART = 0;
	public int bPlusPageSize = 0;
	private int rootPage = -1;
	private long bplusTreeStartPos = 0;
	
	 long dataPageStartPos = 0; 
	 int dataPageSize = 0;
	 public int type = 0;
	
	
	
	private  ArrayList<byte []> minData =  new ArrayList<byte []>();
	private  ArrayList<byte []> maxData= new ArrayList<byte []>();
    public HashMap<Integer,String>  fileMap = new HashMap<Integer,String>()   ;
    private byte[] otherInfor = new byte[BTreeType.headOtherInfor];
    
    //从1开始
    public long getBPlusTreePagePos(int number)
    {
    	return bplusTreeStartPos +(number -1 ) *bPlusPageSize;
    }
    
    public  long getBPlusTreePageStartPos()
    {
    	return bplusTreeStartPos;
    }
    
    public  long getHeadSize()
    {
    	return bplusTreeStartPos;
    }
    
	public HeadPage()
	{
		
	}
	
	public HashMap<Integer,String> getFileMap()
	{
		return fileMap;
	}
	
	public   ArrayList<byte []> getMaxData()
	{
		return maxData;
	}
	
	public   ArrayList<byte []> getMinData()
	{
		return minData;
	}
	public int getRootPagePos()
	{
		return rootPage;
	}
	public String getFileName(int fileNumber)
	{
		return fileMap.get(fileNumber);
	}
	
	public void printAllFileName()
	{
		
		Iterator<Entry<Integer, String>> iterator = fileMap.entrySet().iterator();
		while( iterator.hasNext())
		{
			Entry<Integer, String> data= iterator.next();
			System.out.println("debug file number is " + data.getValue() +"  file name is" + data.getKey());
		} 
	}
	
	public void parseFile(FileChannel fc ,long pageStart ,int pageSize)
	{
	    try {
	    	//System.out.println("bplusTreeHead  is " + pageStart);
	    	MappedByteBuffer temp = fc.map(FileChannel.MapMode.READ_ONLY, pageStart, pageSize);
	    	 type = temp.getInt();
	    	bplusTreeStartPos = temp.getLong();
	    	//System.out.println("bplusTreeStartPos  is " + bplusTreeStartPos);
	    	rootPage = temp.getInt();
	    	//System.out.println("rootPage  is " + rootPage);
	    	bPlusPageSize = temp.getInt();
	    	//System.out.println("bPlusPageSize  is " + bPlusPageSize);
	    	temp.get(otherInfor);
	    	dataPageStartPos = Bytes.toLong(otherInfor, 0, 8);
	    	dataPageSize   = Bytes.toInt(otherInfor,8,4);
			//System.out.println("read number is " + rootPage);
			
			
			int fileCount= temp.getInt();
			//System.out.println("file map number is " + fileCount);
			for(int i = 0 ; i < fileCount ; i++)
			{
				int fileNameSize = temp.getInt();
				//System.out.println("fileNameSize  is " + fileNameSize);
				byte[] data = new byte[fileNameSize];
				temp.get(data);
				String fileNameString = new String(data);
				int fileNumber = temp.getInt();
				fileMap.put(fileNumber, fileNameString);
			}
			//读最小值
			int minNumber  = temp.getInt();
			for( int i = 0 ; i < minNumber ;i++)
			{
				int filedLength = temp.getInt();
				byte [] filed = new byte[filedLength];
				temp.get(filed);
				minData.add(filed);
			}
			//读最大值
			int maxNumber  = temp.getInt();
			for( int i = 0 ; i < maxNumber ;i++)
			{
				int filedLength = temp.getInt();
				byte [] filed = new byte[filedLength];
				temp.get(filed);
				maxData.add(filed);
			}
			
			DirectByteBufferCleaner.clean(temp);
			//temp.clear();
			
			temp = null;
			//this.printAllFileName();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
