package org.cProc.index.BPlusTree.MergeTree;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.cProc.GernralRead.ReadData.DataType;
import org.cProc.GernralRead.ReadData.ParseIndexFormat;
import org.cProc.index.BPlusTree.BPlusTree;
import org.cProc.index.BPlusTree.BTreeType;
import org.cProc.index.BPlusTree.Data.Data;
import org.cProc.index.BPlusTree.Data.MulitFiledData;
import org.cProc.index.BPlusTree.Data.ReadWriteIndexDataProxy;
import org.cProc.index.BPlusTree.query.QueryInstanceFactory;
import org.cProc.index.BPlusTree.query.QueryInterface;
import org.cProc.index.BPlusTree.query.page.PageLoadProxy;
import org.cProc.tool.Bytes;




public class MergerBPlusTree <T extends Data> {
	
	
	
	private static Log Log4jlogger = LogFactory.getLog(PageLoadProxy.class); 
	
	
	
	private  int HEADPAGETYPE = 0;
	private int  DATAPAGETYPE  = 1;
	private int  BPLUSSPAGETYPE  = 2;
	
	private int PAGESIZE=  1024*32; // 数据页的大小
	private int BPLUSSPAGESIZE = 0; 
	private int  dataPageCount = 0;
	private T template = null;
	private  ArrayList<QueryInterface<T>> queryList = new  ArrayList<QueryInterface<T>>();
	private  HashMap<QueryInterface<T>, Integer> fileMap = new HashMap<QueryInterface<T>, Integer>();
	private ArrayList<T>  firstDataOfPage = new ArrayList<T>();
	private String fileName = "";
	
	private RandomAccessFile fileStream = null;
	 private  FileChannel fc = null;
	 
	private ByteBuffer currentBuffer = null;
	private int dataPage = 0;
	private int pageCountIndex = 0;
	private int bplusPageCountIndex = 0;
	private int PationSize = 10;
	private  int fileNumber = 0;
	private ArrayList<byte[]> maxDataArrayList = null;
	private ArrayList<byte[]> minDataArrayList =null;
	
	private int btree_m = 101;
    BPlusTree<T> btree= null;
	private long countNumber = 0;
	private long onePageCount = 0;

    private  int getSuitablePageSize(int Tsize ,int m)
    {
    	 //1 : 是否是叶子  4 : 上一个   4： 下一个   4：总共有多少个；
    	int byteSize = 1 ;
    	int intSize = 4 ;
    	int longSize = 8;
    	int nodeMaxSize = (Tsize+intSize+12)*m+byteSize+intSize+intSize+intSize;
    	int pageSize = (((nodeMaxSize/1024)+1))*1024;
    	return   pageSize;
    }
    public int getFileNumberCount()
    {
    	return fileNumber;
    }
	public  MergerBPlusTree(String fileName)
	{

		this.fileName = fileName;
    	try {
    		File file = new File(fileName);
        	if(!file.exists())
        	{
        		file.createNewFile();
        	}
 //       	DataInputStream input = new DataInputStream(new FileInputStream(file));
         	 fileStream = new RandomAccessFile(fileName, "rw");
         	 fc =fileStream.getChannel();


 			currentBuffer =  ByteBuffer.allocate(PAGESIZE-4);
			pageCountIndex = 0;
			dataPageCount++;
    	}
          catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setTemplate(T  template)
	{
		 this.template = template;
		 btree = new BPlusTree<T>(btree_m, template);   
		BPLUSSPAGESIZE = getSuitablePageSize(template.getByteSize(), 301);
	}
		
	public void MergeOver()
	{
		try {
			if( fileStream != null)
			{	
				//System.out.println("MergerBPlusTree  fileStream Close ");
				fileStream.close();
			}
			if(fc != null)
			{
				//System.out.println("MergerBPlusTree  fc  close");
				fc.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public  void MergeBPlusTree(String firstTree , String secondTree)
	{
		ArrayList<String> list = new  ArrayList<String>();
		list.add(firstTree);
		list.add(secondTree);
		MergeBPlusTree(list);
	}
	
	public  void MergeBPlusTree(ArrayList<String> fileNames)
	{
		 ArrayList<QueryInterface<T>> tempQueryList = new  ArrayList<QueryInterface<T>>();
		 
		 QueryInstanceFactory<T> factory = new QueryInstanceFactory();
		for(int i = 0 ; i < fileNames.size() ; i++)
		{
			 //检测文件是原始的BPlusTree还是合并后的BPlusTree,并返回实例
			QueryInterface<T> query =   factory.getQueryInstance(fileNames.get(i));  //new Query<T>(fileNames.get(i));
			addMaxData(query.getMaxData());
			addMinData(query.getMinData());
			if(query == null)
			{
				Log4jlogger.info("  MergeBPlusTree  : query is " + query);
			}
			query.setTemplate(this.template);
			query.startFindSeqDataOfAll();
			queryList.add(query);
			tempQueryList.add(query);
		}
		MegerFileMap();
		boolean isOver = false;
		int countNumber = 0;
		ArrayList<T> dataList = new  ArrayList<T>();
		 for(int i = 0 ;  i< tempQueryList.size(); i++)
		 {
				if(!tempQueryList.get(i).dataIsOver())
				{
					T data =  tempQueryList.get(i).getNextData();
					dataList.add(data);

				}
				else
				{
					//该文件已遍历完毕，退出
					tempQueryList.remove(i);
				}
		 } 
		while(!isOver)
		{
			T minData = null;
			if(dataList.size() == 0 )
			{		
				writeOver();
				writeBPlusTree();
				writeHeader();
				MergeOver();
				for(int i = 0 ; i < queryList.size(); i++)
				{
					QueryInterface<T> query  = queryList.get(i);
					query.queryClose();
				}
				return ;
			}
			//找到最小值
			minData = dataList.get(0);
			int minpos = 0;
			for(int i = 1 ; i < dataList.size() ; i++)
			{
				if(minData.compareTo(dataList.get(i)) > 0)
				{
					minData = dataList.get(i) ;
					minpos = i;
				}
			}
			//将最小值写入
			QueryInterface<T>  tempQuery = tempQueryList.get(minpos);
			countNumber++;
			writeOneData(minData, 
					getMapFileNumber(tempQuery,tempQuery.getCurrentFileNumber()),
					tempQuery.getCurrentOffset());
			dataList.remove(minpos);
			//获取最小值Query的下一个值；如果已为空，则将query移出
			QueryInterface<T> minQuery = tempQueryList.get(minpos);
			if(minQuery.dataIsOver())
			{
				tempQueryList.remove(minpos);
			}
			else
			{
				dataList.add(minpos, minQuery.getNextData());
			}
		}
	
	
		
	}
	
	
	private void addMaxData(ArrayList<byte[]> maxData)
	{
		if(maxData.size() == 0)
		{
			return ;
		}
		if(maxDataArrayList == null  )
		{
			maxDataArrayList = new ArrayList<byte[]>();
			for(int i = 0 ; i<maxData.size() ; i++)
			{
				maxDataArrayList.add(maxData.get(i));
			}
		}
		else
		{
			for(int i = 0 ; i <maxData.size() ; i++)
			{
				switch(maxData.get(i).length)
				{
					case 1:
//								byte byteA = maxData.get(i)[0]; 
//								byte byteB = maxDataArrayList.get(i)[0]; 
					//			if(byteB < byteA)
								if(DataType.compare(Byte.class,  maxDataArrayList.get(i),maxData.get(i)) < 0)
								{
									maxDataArrayList.remove(i);
									maxDataArrayList.add(i,  maxData.get(i));
								}
								break;
					case 2:
//						short shortA = Bytes.toShort(maxData.get(i)); 
//						short shortB = Bytes.toShort(maxDataArrayList.get(i)); 
					//	if(shortB < shortA)
						if(DataType.compare(Short.class,  maxDataArrayList.get(i),maxData.get(i)) < 0)	
						{
							maxDataArrayList.remove(i);
							maxDataArrayList.add(i,  maxData.get(i));
						}
						break;
					case 4:
//						int intA =Bytes.toInt(maxData.get(i)); 
//						int intB =  Bytes.toInt(maxDataArrayList.get(i)); 
						
					//	if(intB < intA)
						if(DataType.compare(Integer.class,  maxDataArrayList.get(i),maxData.get(i)) < 0)	
						{
							maxDataArrayList.remove(i);
							maxDataArrayList.add(i,  maxData.get(i));
						}
						break;
					case 8:
//						long longA =Bytes.toLong(maxData.get(i)); 
//						long longB = Bytes.toLong(maxDataArrayList.get(i)); 
					//	if(longB < longA)
						if(DataType.compare(Long.class,  maxDataArrayList.get(i),maxData.get(i)) < 0)		
						{
							maxDataArrayList.remove(i);
							maxDataArrayList.add(i,  maxData.get(i));
						}
						break;
				}
			}
		}
	}
	
	private void addMinData(ArrayList<byte[]> minData)
	{
		if(minData.size() == 0)
		{
			return ;
		}
		if(minDataArrayList == null)
		{
			minDataArrayList = new ArrayList<byte[]>();
			for(int i = 0 ; i<minData.size() ; i++)
			{
				minDataArrayList.add(minData.get(i));
			}
		}
		else {
			for(int i = 0 ; i <minData.size() ; i++)
			{
				switch(minData.get(i).length)
				{
					case 1:
								byte byteA = minData.get(i)[0]; 
								byte byteB = minDataArrayList.get(i)[0]; 
								if(byteB > byteA)
								{
									minDataArrayList.remove(i);
									minDataArrayList.add(i,  minData.get(i));
								}
								break;
					case 2:
						short shortA = Bytes.toShort(minData.get(i)); 
						short shortB = Bytes.toShort(minDataArrayList.get(i)); 
						if(shortB > shortA)
						{
							minDataArrayList.remove(i);
							minDataArrayList.add(i,  minData.get(i));
						}
						break;
					case 4:
						int intA =Bytes.toInt(minData.get(i)); 
						int intB =  Bytes.toInt(minDataArrayList.get(i)); 
						if(intB > intA)
						{
							minDataArrayList.remove(i);
							minDataArrayList.add(i,  minData.get(i));
						}
						break;
					case 8:
						long longA =Bytes.toLong(minData.get(i)); 
						long longB = Bytes.toLong(minDataArrayList.get(i)); 
						if(longB > longA)
						{
							minDataArrayList.remove(i);
							minDataArrayList.add(i,  minData.get(i));
						}
						break;
				}
			}
		
		}
	}
	
	public int getMapFileNumber( QueryInterface<T> query, int fileNumber)
	{
		return  fileMap.get(query)+fileNumber;
	}

	
	public void writeOneData(T data , int fileNumber , long offset )
	{
//		DebugPrint.InforPrint("data is " + data +" fileNumber is " +fileNumber +"  offset is " + offset);
		if(currentBuffer.remaining() >= (data.getByteSize()+12))
		{
			if(pageCountIndex == 0)
			{
				firstDataOfPage.add(data);
			}
			data.write(currentBuffer);
			currentBuffer.putInt(fileNumber);
			currentBuffer.putLong(offset);
			pageCountIndex++;
			countNumber++;
			onePageCount++;
		}
		else {
			currentBuffer.flip();
			ByteBuffer  writeBuffer = ByteBuffer.allocate(PAGESIZE);
			writeBuffer.putInt(pageCountIndex);
			writeBuffer.put(currentBuffer);
			writeBuffer.flip();
			try {
		//		System.out.println("page data is " + onePageCount +" Page pos is " + getNextPagePos(DATAPAGETYPE)+ "  pageCountIndex  is "+pageCountIndex);
				onePageCount = 0;
				fc.write(writeBuffer, getNextPagePos(DATAPAGETYPE));
				dataPageCount++;
//				System.out.println("write One Page");
			} catch (IOException e) {
				e.printStackTrace();
			}
			currentBuffer = ByteBuffer.allocate(PAGESIZE-4); 
			pageCountIndex = 0;
			writeOneData(data ,  fileNumber ,  offset );
		}
	}
	
	public void writeOver()
	{
		//System.out.println("all count is " + (countNumber-1));
		currentBuffer.flip();
		ByteBuffer  writeBuffer = ByteBuffer.allocate(PAGESIZE);
		writeBuffer.putInt(pageCountIndex);
		writeBuffer.put(currentBuffer);
		writeBuffer.flip();
		try {
			fc.write(writeBuffer, getNextPagePos(DATAPAGETYPE));
			//System.out.println("page data is " + onePageCount +"write the last  Page : the pos is " +getNextPagePos(DATAPAGETYPE) +" page Size  is " + PAGESIZE +" page Count is " + dataPageCount);
//			DebugPrint.InforPrint("bPlusPage start at " +(dataPageCount*PAGESIZE+BTreeType.headPage));
		} catch (IOException e) {
			e.printStackTrace();
		}
		currentBuffer = null;
		pageCountIndex = 0;
	}
	
	private void MegerFileMap()
	{
		int fileNumber = 0;
		for(int i = 0 ; i < queryList.size() ; i++ )
		{
			QueryInterface<T> query = queryList.get(i);
			 HashMap<Integer,String> map = query.getFileMap();
			 fileMap.put(query, fileNumber);
			 fileNumber = fileNumber + map.size();
		}
	}
	
	public void PrintFirstData()
	{
		for(int i = 0 ;  i < firstDataOfPage.size() ; i++)
		{
			System.out.println("data is  " + firstDataOfPage.get(i));
		}
	}
	
	
	
	public void writeHeader()
	{
		ByteBuffer  writeBuffer = ByteBuffer.allocate(BTreeType.headPage);
		writeBuffer.putInt(BTreeType.MegerBTreeType);     //文件的类型：是合并后的，还是原始的
		writeBuffer.putLong(getPagePos(BPLUSSPAGETYPE,0));  // BPlusTree的开始位置
		//System.out.println("bplus start at " + getPagePos(BPLUSSPAGETYPE,0));
		writeBuffer.putInt(1);  				//   //写入根的编号
		writeBuffer.putInt(BPLUSSPAGESIZE);  //  BPlusTree数据页的大小
	
		writeBuffer.putLong((long)BTreeType.headPage);  //数据页的起始位置
		writeBuffer.putInt(PAGESIZE);  //  数据页的大小
		for( int i = 0 ; i< 68 ;i++)
		{
			writeBuffer.put((byte)0);
		}
		int fileNumberCount = 0;
		for(int i = 0 ; i < queryList.size(); i++)
		{
			QueryInterface<T> query  = queryList.get(i);
			fileNumberCount = fileNumberCount +query.getFileMap().size();
		}
		Log4jlogger.info( " fileNumberCount  is " + fileNumberCount);
		fileNumber = fileNumberCount;
		writeBuffer.putInt(fileNumberCount);   // 写入文件的总数
		for(int i = 0 ; i < queryList.size(); i++)
		{
			QueryInterface<T> query  = queryList.get(i);
			HashMap<Integer,String> map =query.getFileMap();
			 int fileNumber = fileMap.get(query);
			 map.entrySet().iterator();
			 Iterator<Entry<Integer,String>>   iterator = map.entrySet().iterator();
			 while(iterator.hasNext())
			 {
				 Entry<Integer,String> dataEntry = iterator.next();
				 fileNumber =   fileMap.get(query) + dataEntry.getKey();
				 String fileName = dataEntry.getValue();
				 //写入单个文件的长度
				 writeBuffer.putInt(fileName.getBytes().length);
				 //写入单个文件的内容
				 writeBuffer.put(fileName.getBytes());
				 //写入单个文件的编号
				 writeBuffer.putInt(fileNumber);
				 System.out.println(System.currentTimeMillis()+"@gyySourcetoMerg:"+"fileName:"+fileName);
			 }
		
		}
				
        //写入最小值
        if(minDataArrayList != null)
        {
        	writeBuffer.put(Bytes.toBytes(minDataArrayList.size()));
            for( int i = 0 ; i < minDataArrayList.size() ; i++)
            {		
            	writeBuffer.put(Bytes.toBytes(minDataArrayList.get(i).length));
            	writeBuffer.put(minDataArrayList.get(i));
            }
        }
        else {
        	writeBuffer.put(Bytes.toBytes(0));
		}
        
        //写入最大值
        if(maxDataArrayList != null)
        {
        	writeBuffer.put(Bytes.toBytes(maxDataArrayList.size()));
               for( int i = 0 ; i < maxDataArrayList.size() ; i++)
               {
            	   writeBuffer.put(Bytes.toBytes(maxDataArrayList.get(i).length));
            	   writeBuffer.put(maxDataArrayList.get(i));
               }
        }
        else {
        	writeBuffer.put(Bytes.toBytes(0));
		}

		
		writeBuffer.flip();
		try {
			fc.write(writeBuffer, getNextPagePos(HEADPAGETYPE));
//			DebugPrint.DebugPrint("write Head",this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//将其写成一个BPlusTree
	public void writeBPlusTree()
	{	
		Log4jlogger.info("btree write start");
		for(int i = 0 ;  i < firstDataOfPage.size() ; i++)
		{
				btree.insert(firstDataOfPage.get(i), 0, getPagePos(DATAPAGETYPE,i));
		}
		Log4jlogger.info("b plus tree pos is " + getPagePos(BPLUSSPAGETYPE,0));
		btree.writeToFile(fc , getPagePos(BPLUSSPAGETYPE,0) ,BPLUSSPAGESIZE);
		Log4jlogger.info("btree write over");
	//	btree.print();
	}

	public long getNextPagePos( int type )
	{
		if(HEADPAGETYPE == type)
		{
			return 0l;
		}
		if(DATAPAGETYPE == type)
		{
			return  (dataPageCount-1)*PAGESIZE+BTreeType.headPage; // 写第一个的时候,dataPageCount已经为1了
		}
		if(BPLUSSPAGETYPE == type)
		{
			//写第一个的时候,dataPageCount已经包含了所有了dataPage;且第一个bplusPage已经为1;
			return dataPageCount*PAGESIZE+BTreeType.headPage+(bplusPageCountIndex-1)*BPLUSSPAGESIZE;    
		}
		return -1000;
	}
	
	//number从0开始；
	public long getPagePos( int type  , int number)
	{
		if(HEADPAGETYPE == type)
		{
			return 0l;
		}
		if(DATAPAGETYPE == type)
		{
			return number*PAGESIZE+BTreeType.headPage;
		}
		if(BPLUSSPAGETYPE == type)
		{	
//			DebugPrint.InforPrint("dataPageCount  is " + dataPageCount);
//			DebugPrint.DebugPrint("HEADSIZE  is " + BTreeType.headPage, this);
			return dataPageCount*PAGESIZE+BTreeType.headPage+number*BPLUSSPAGETYPE;
		}
		return -1000;
	}
	
	
	public static  void main(String[] args)
	{
		//System.out.println("hello");
		
    	ParseIndexFormat readIndexFormat = null;
		String path ="C:\\dingli\\CDR_direct\\tableInfor\\bssap\\indexfile\\callingIndex\\index.frm" ;
		Configuration conf = new Configuration();
		try {
			FileSystem fs = FileSystem.get(conf);
			if(fs != null)
			{
				readIndexFormat = new ParseIndexFormat(path,fs);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		ReadWriteIndexDataProxy proxy = new ReadWriteIndexDataProxy(readIndexFormat);
		ArrayList<byte[]> dataList2 = new ArrayList<byte[]>();
		
		dataList2.add(DataType.getUnsginedMinBytesReturnNot8Byte(Long.class));
		dataList2.add(DataType.getUnsginedMinBytesReturnNot8Byte(Integer.class));
		dataList2.add(DataType.getUnsginedMinBytesReturnNot8Byte(Long.class));
		dataList2.add(DataType.getUnsginedMinBytesReturnNot8Byte(Byte.class));
		dataList2.add(DataType.getUnsginedMinBytesReturnNot8Byte(Byte.class));
		dataList2.add(DataType.getUnsginedMinBytesReturnNot8Byte(Integer.class));
		dataList2.add(DataType.getUnsginedMinBytesReturnNot8Byte(Short.class));
		
		String fileNameString = "C:\\dingli\\test\\data\\reslut30"+BTreeType.mergeName;
		MergerBPlusTree<MulitFiledData> table = new MergerBPlusTree<MulitFiledData>(fileNameString);
		table.setTemplate(new MulitFiledData(dataList2, proxy));
		ArrayList<String> list = new  ArrayList<String>();

		String pathString ="C:\\dingli\\test\\data\\1\\";
		File file = new File(pathString);
		String [] filesStrings = file.list();
		for(int i = 0 ; i < 20 ; i++)
		{
			System.out.println(filesStrings[i]);
			list.add(pathString+filesStrings[i]);
		}
		table.MergeBPlusTree(list);
	//	table.PrintFirstData();
//		File  file = new File(fileNameString);
//		System.out.println(file.delete());
	}
	
}
