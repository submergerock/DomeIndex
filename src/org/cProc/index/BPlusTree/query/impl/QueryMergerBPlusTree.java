package org.cProc.index.BPlusTree.query.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.cProc.GernralRead.ReadData.ParseIndexFormat;
import org.cProc.index.BPlusTree.BTreeType;
import org.cProc.index.BPlusTree.Data.Data;
import org.cProc.index.BPlusTree.Data.MulitFiledData;
import org.cProc.index.BPlusTree.Data.ReadWriteIndexDataProxy;
import org.cProc.index.BPlusTree.query.QueryInterface;
import org.cProc.index.BPlusTree.query.page.HeadPage;
import org.cProc.index.BPlusTree.query.page.LeafPage;
import org.cProc.index.BPlusTree.query.page.MergerDataPage;
import org.cProc.index.BPlusTree.query.page.NotLeafPage;
import org.cProc.index.BPlusTree.query.page.Page;
import org.cProc.index.BPlusTree.query.page.PageLoadProxy;
import org.cProc.sql.IndexFilter;
import org.cProc.tool.Bytes;

public class QueryMergerBPlusTree <T extends Data>  implements QueryInterface<T>{

    private  long bplusTreeStartPos = 0;
	private  T  template = null;
	private PageLoadProxy proxy = null;  
	private  FileChannel fc = null;
	private  String indexFile = "";
	
    private int  currentPosInPage = 0;
    private boolean  traversalOver = false;
    private  int currentFileNumber = -1;
    private  long currentOffset= -1;
    private  MergerDataPage currentPage  = null;
	
    private IndexFilter  indexFilter = null;
    
    private  ArrayList<Integer> fileNumberList = new  ArrayList<Integer>();
    private  ArrayList<Long> offsetList = new  ArrayList<Long>();
    private  ArrayList<T> reslutList = new  ArrayList<T>();
    private FileInputStream fileStream = null;
	private File file=null;
	
	public QueryMergerBPlusTree(String name)
	{
		 this.indexFile = name;    
	    	try {
	    		 file = new File(name);
	        	if(!file.exists())
	        	{
	        		file.createNewFile();
	        	}	
	        	fileStream = new FileInputStream(name);
	             fc = fileStream.getChannel();  
	    	}catch (Exception e) {
			}
	    	 proxy = new PageLoadProxy(this,fc);
	    	 proxy.loadPage(HeadPage.HEADSTART);
	}
	
	public void setTemplate( T  template)
	{
		this.template = template;
	}
	public   T getTemplate( )
	{
		  return (T)template.getInstance();
	}
	
	//遍历所有值
	public void startFindSeqDataOfAll()
	{
	    currentPage  = proxy.getMergerDataPage(proxy.getDataPageStart(), BTreeType.MegerBTreeType,true);
	    if(currentPage == null)
	    {
	    	traversalOver = true;
	    }
	}
	
	public T getNextData()
	{
//	    System.out.println("currentPage  is "+ currentPage.getPageOffset()+"currentPage.getDataList() is" + currentPage.getDataList() +"  currentPos is " + currentPosInPage);
		T reslut =  (T)currentPage.getDataList().get(currentPosInPage);
		currentFileNumber = currentPage.getFileNumberOfDataPos(currentPosInPage);
		currentOffset 			=  currentPage.getFileOffsetOfDataPos(currentPosInPage);
		if(currentPosInPage == currentPage.getDataList().size() -1) //当已经为最后一个的时候
		{
			proxy.removePage(currentPage.getPageOffset(), BTreeType.MegerBTreeType);
			currentPage=	proxy.getMergerDataPage(proxy.getNextMergeDataPagePos(currentPage.getPageOffset(), BTreeType.MegerBTreeType),
					BTreeType.MegerBTreeType,true);
			if(currentPage != null)
			{
				currentPosInPage = 0;
			}
			else {
				traversalOver = true;
			}
		}
		else {
			currentPosInPage++;
		}

		return reslut;
	}
	
	public int getCurrentFileNumber()
	{
		return currentFileNumber;
	}
	
	public long getCurrentOffset()
	{
		return currentOffset;
	}
	
	public boolean dataIsOver()
	{
		return traversalOver;
	}
	
	
	//查询范围
	private  boolean findRangeData(Page  page ,T startIndex , T endIndex)
	{		
			Page<T> tempPage = page;
			if(!tempPage.isLeaf())
			{	
				NotLeafPage<T> parentLeaf = null;
				
				findLeaf:  while( !tempPage.isLeaf())  //当非叶子节点的时候
				{
					NotLeafPage<T> notLeaf = (NotLeafPage<T>)tempPage;
					ArrayList<T> indexList  =  notLeaf.getIndexList();

					boolean find = false;
					for(int i = 0 ; i < indexList.size()-1 ; i++)
					{
						//存在几种情况：  假设X = A
						//1：  A  A； 则：需要判断    x>= before and x <= after ;  
						//2  :  A  A+1;则需要判断  X>=before and  X < after
						//3:  A-1  A+1; 则需要判断 X > before and  x < after
						if( ( startIndex.compareTo(indexList.get(i)) >= 0 &&   startIndex.compareTo(indexList.get(i+1)) <= 0)
//							||  ( startIndex.compareTo(indexList.get(i)) >= 0 &&   startIndex.compareTo(indexList.get(i+1)) < 0)
//							||   ( startIndex.compareTo(indexList.get(i)) > 0 &&   startIndex.compareTo(indexList.get(i+1)) < 0)
							)
						{	
							parentLeaf = notLeaf;
							tempPage = proxy.getPage(notLeaf.getChildNumber(i));
							if(tempPage.isLeaf())
							{
								break findLeaf;
							}
							else {
								find = true;
								break;
							}
						}
					}
					if(!find &&indexList.size() > 0)
					{		
						parentLeaf = notLeaf;
						int number =  notLeaf.getChildNumber(indexList.size() -1);
						tempPage = proxy.getPage( notLeaf.getChildNumber(indexList.size() -1));
						
					}
				}	
			}
			//找到了叶子了 ,应该找到第一个 A,A满足  A > startIndex and next(A) <= startIndex;
			//如果是最后一个，如果A满足 A >startIndex ,则加入 
			LeafPage<T> leafPage = (LeafPage<T>)tempPage;
			ArrayList<T> indexList  =  leafPage.getIndexList();			
			LeafPage<T> tempLeafPage = (LeafPage<T>)leafPage;

			for(int k =  1; k <  indexList.size()-1 ; k++ )
			{
							if( indexList.get(k+1).compareTo(startIndex) >= 0  ) // 只要下一个大于等于startIndex,则添加
							{
//								DebugPrint.DebugPrint("find data : " + indexList.get(k) +" int the page: " +tempLeafPage.getPageNumber() +"  next page is: " +tempLeafPage.nextNumber
//										+"  pre page is " + tempLeafPage.preNumber , this);
								 this.addReslut(indexList.get(k) ,tempLeafPage.fileNumberList.get(k), tempLeafPage.offsetList.get(k),startIndex , endIndex );
								 return true;
							}
			}
			this.addReslut(indexList.get(indexList.size()-1) ,tempLeafPage.fileNumberList.get(indexList.size()-1), tempLeafPage.offsetList.get(indexList.size()-1),startIndex , endIndex);
			return true;
	}

	public String getIndexFile()
	{
		return indexFile;
	}

	//查询范围
	private boolean findRangeData(int number ,T startIndex , T endIndex)
	{
		 return findRangeData(proxy.getPage(number),startIndex , endIndex);
	}
	
	//查询范围
	public boolean findRangeData(T startIndex , T endIndex)
	{
		 return findRangeData(((HeadPage)proxy.getPage(HeadPage.HEADSTART)).getRootPagePos(),startIndex , endIndex);
	}
	
	private void addReslut(T data ,int fileNumber, long offset ,T start, T end)
	{
//		DebugPrint.DebugPrint("addReslut", this);
		long nextOffset = offset;
		MergerDataPage page = proxy.getMergerDataPage(nextOffset, BTreeType.MegerBTreeType,true);
		if(page == null)
		{
			return ;
		}
		ArrayList<Data> dataList = page.getDataList();
		boolean quit = false;
		while(!quit)
		{
			for(int i = 0 ; i < dataList.size() ; i++)
			{			
				if((dataList.get(i)).compareTo(start) >= 0 && (dataList.get(i)).compareTo(end)<=0)
				{
					if(indexFilter != null)
					{
						if(indexFilter.isDataOK(dataList.get(i) ))
						{
							reslutList.add((T)dataList.get(i));
							fileNumberList.add(page.getFileNumberOfDataPos(i));
							offsetList.add(page.getFileOffsetOfDataPos(i));
						}
					}
					else 
					{
						reslutList.add((T)dataList.get(i));
						fileNumberList.add(page.getFileNumberOfDataPos(i));
						offsetList.add(page.getFileOffsetOfDataPos(i));
					}
					
				}
				else {
					if((dataList.get(i)).compareTo(end) >0)
					{
						//System.out.println("end is "+ end +"   dataList.get(i)  is "+dataList.get(i) + "   dataList.get(i-1)  is "+dataList.get(i-1));
						quit = true;
						break;
					}
				}
			}
			//遍历完一个页,遍历下一个页;如果下一个页的位置等于BPlus的位置,则退出
		//	System.out.println("addNextDataPage");
			nextOffset = proxy.getNextMergeDataPagePos(nextOffset, BTreeType.MegerBTreeType) ;
			page = proxy.getMergerDataPage(nextOffset, BTreeType.MegerBTreeType,true);
			if (page == null) {
				if(page == null)
				{
					//System.out.println("page is null ,nextPagePos is " + nextOffset);
				}
				quit = true;
			} else {
				dataList = page.getDataList();
			}
				
		}

	}
	
	//获取查询后的信息
	public  int getReslutSize()   //获取结果的总数
	{
		return reslutList.size();
	}
	public  ArrayList<T> getIndexReuslt()  					 //获取结果的Index值
	{
		return reslutList;
	}
	public  ArrayList<Integer> getFileNumberReuslt()  //获取结果的文件编号
	{
		return this.fileNumberList;
	}
	public  ArrayList<Long> getOffsetReuslt()				//获取结果的偏移量
	{
		return this.offsetList;
	}
	
	
	public  void  printReslut()										//打印结果
	{
		
	}
	
	public  String getFileName(int fileNumber)      //将文件编号转化成文件名；
	{
		return  getFileMap().get(fileNumber);
	}
	
	//format: fileName#offset,offset,offset
	public  ArrayList<String> getSortFileOffsetResult() //将结果集排序
	{
		 // long  start_time = System.currentTimeMillis();
		//System.out.println("QueryMergerBPlusTree: &&&&&&&&&&&&&&&&data Sizie is " +offsetList.size() );
		ArrayList<Long> offsets = new ArrayList<Long>();
		 long  start_time = System.currentTimeMillis();
		 long modFileNumber = 10000000000l;
		for(int i = 0 ; i < fileNumberList.size() ;i++)
		{	
			offsets.add(fileNumberList.get(i)*modFileNumber+offsetList.get(i));
		}
			
		Collections.sort(offsets);

		HashMap<Integer, String> map = new HashMap<Integer, String>();
		
		int lastFileName =-1;
		ArrayList<String> reslut  = new  ArrayList<String>();
		StringBuffer sb = new StringBuffer(OffsetAndOffsetSeprater);
	
		for(int m = 0 ; m  < offsets.size() ; m++)
		{
			int  fileNumber =  (int)(offsets.get(m)/modFileNumber);
			long  offset = offsets.get(m)%modFileNumber;
			if( fileNumber !=lastFileName )
			{	
				lastFileName = fileNumber;
				reslut.add(sb.substring(0,sb.length()-1));
				sb = null;
				sb = new StringBuffer(this.getFileName(fileNumber));
				sb.append(fileNameAndOffsetSeprater);	
				sb.append(offset);
				sb.append(OffsetAndOffsetSeprater);
			}
			else {
				sb.append(offset);
				sb.append(OffsetAndOffsetSeprater);
			}
		}
		reslut.add(sb.substring(0,sb.length()-1));
     	reslut.remove(0);
		 long end_time = System.currentTimeMillis();
		 //DebugPrint.InforPrint("Query:getSortFileOffsetResult   2 cost time is "+ (end_time-start_time) +"  count is  " +fileNumberList.size());
		return  reslut;
	}
	
	//最大值与最小值
	public   ArrayList<byte []> getMaxData()
	{
		 	return   ((HeadPage)proxy.getPage(HeadPage.HEADSTART)).getMaxData();
	}
	public    ArrayList<byte []> getMinData()
	{
			return   ((HeadPage)proxy.getPage(HeadPage.HEADSTART)).getMinData();
	}
	
	//过滤索引
	public  void setIndexFilter(IndexFilter<T > indexFilter)
	{
		this.indexFilter = indexFilter;
	}
	
	//获取文件名以及编号的对应信息
	public    HashMap<Integer,String>   getFileMap()
	{
		HeadPage page = (HeadPage)proxy.getPage(HeadPage.HEADSTART);
		return page.getFileMap();
	}
	
    //从0开始
    public  long getBPlusTreePagePos( int number)
    {
    	HeadPage head = (HeadPage<T>)( proxy.getPage(0));
    	return bplusTreeStartPos + BTreeType.headPage+ (number -1) * head.bPlusPageSize ;
    }
    
    public long getBPlusTreeHeadPos()
    {
    	return bplusTreeStartPos ;
    }
    
    //设置BPlusTree在文件里的存放位置,可以从0开始,也可以从任意位置开始
    public void setBplusTreeStartPos(long pos)
    {
    	bplusTreeStartPos = pos;
    }
    
	public void queryClose()
	{
		//System.out.println();
		//proxy.close();
		if(file != null)
		{
			file = null;
		}
		if(fileStream != null)
		{
			try {
				if(fc != null)
				{
					//System.out.println("fc.close");
					fc.close();
					fc = null;
				}
				//System.out.println("fileStream.close();");
				fileStream.close();
				fileStream = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		proxy.close();
	}
	
	public static  void main(String[] args)
	{
		//2999:783872999
		
    	ParseIndexFormat readIndexFormat = null;
		String path ="C:\\dingli\\test\\index.frm" ;
		Configuration conf = new Configuration();
//		conf.set("fs.default.name","hdfs://192.168.1.8:9000");
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
		ArrayList<byte[]> dataList = new ArrayList<byte[]>();
		dataList.add(Bytes.toBytes(Integer.MIN_VALUE));
		dataList.add(Bytes.toBytes(Long.MIN_VALUE));
		dataList.add(Bytes.toBytes(Long.MIN_VALUE));
		dataList.add(Bytes.toBytes(Byte.MIN_VALUE));
		dataList.add(Bytes.toBytes(Byte.MIN_VALUE));
		dataList.add(Bytes.toBytes(Integer.MIN_VALUE));
		dataList.add(Bytes.toBytes(Long.MIN_VALUE));
		dataList.add(Bytes.toBytes(Long.MIN_VALUE));
		
		QueryMergerBPlusTree<MulitFiledData> query =  new QueryMergerBPlusTree<MulitFiledData>("C:\\dingli\\test\\reslut"+BTreeType.mergeName);
		query.setTemplate(new MulitFiledData(dataList,proxy));
		
		   ArrayList<byte[]> maxByteArrayList = query.getMaxData();
		   //System.out.println("max data****************");
		   for(int i = 0 ; i < maxByteArrayList.size() ; i++)
		   {
			   switch(maxByteArrayList.get(i).length)
			   {
			   	case 8:
			   	 System.out.println(Bytes.toLong(maxByteArrayList.get(i)));
			   	 break;
			   	case 4:
			   	 System.out.println(Bytes.toInt(maxByteArrayList.get(i)));
			   	 break;
			
			   	case 2:
			   	 System.out.println(Bytes.toShort(maxByteArrayList.get(i)));
			   	 break;
			   	case 1:
			   	 System.out.println(maxByteArrayList.get(i)[0]);
			   	 break;
			   	default:
			   	   System.out.println("It is a string ,no sense");
			   }
		   }
		   ArrayList<byte[]> minByteArrayList = query.getMinData();
		   System.out.println("min data****************");
		   for(int i = 0 ; i < minByteArrayList.size() ; i++)
		   {
			   switch(minByteArrayList.get(i).length)
			   {
			   	case 8:
			   	 System.out.println(Bytes.toLong(minByteArrayList.get(i)));
			   	 break;
			   	case 4:
			   	 System.out.println(Bytes.toInt(minByteArrayList.get(i)));
			   	 break;
			   	case 2:
			   	 System.out.println(Bytes.toShort(minByteArrayList.get(i)));
			   	 break;
			   	case 1:
			   	 System.out.println(minByteArrayList.get(i)[0]);
			   	 break;
			   	default:
			   	   System.out.println("It is a string ,no sense");
			   }
		   }
		
		   long start_time = System.currentTimeMillis();
//		   query.findData( new MulitFiledData(155,155));
//			ArrayList<byte[]> dataList1 = new ArrayList<byte[]>();
//			int start = 0;
//			int end = 1;
//			dataList1.add(Bytes.toBytes((long )start));
//			dataList1.add(Bytes.toBytes(start));
//			dataList1.add(Bytes.toBytes((long )start));
//			dataList1.add(Bytes.toBytes((byte)start));
//			dataList1.add(Bytes.toBytes((byte)start));
//			dataList1.add(Bytes.toBytes(start));
//			dataList1.add(Bytes.toBytes((long )start));
//			dataList1.add(Bytes.toBytes((long )start));
//			ArrayList<byte[]> dataList2 = new ArrayList<byte[]>();
//			dataList2.add(Bytes.toBytes((long )end));
//			dataList2.add(Bytes.toBytes(end));
//			dataList2.add(Bytes.toBytes((long )end));
//			dataList2.add(Bytes.toBytes((byte)end));
//			dataList2.add(Bytes.toBytes((byte)end));
//			dataList2.add(Bytes.toBytes(end));
//			dataList2.add(Bytes.toBytes((long )end));
//			dataList2.add(Bytes.toBytes((long )end));
//		   query.findRangeData(new MulitFiledData(dataList1,proxy), new MulitFiledData(dataList2,proxy));
//		   long end_time = System.currentTimeMillis();
//		   
//		   end_time = System.currentTimeMillis();
//		   start_time = System.currentTimeMillis();
//		   ArrayList<String> reslut = query.getSortFileOffsetResult();
//		   System.out.println("time is " + (end_time - start_time));
//		   System.out.println("size is " + reslut.size());
//		   for(int i =0 ; i < reslut.size() ; i++)
//		   {
//			   System.out.println(reslut.get(i));
//		   }
			  query.startFindSeqDataOfAll();
			  int count = 0 ; 
			  while(!query.dataIsOver() && count  < 10000000)
			  {
				  System.out.print(query.getNextData()+" ");
				  count ++;
			  }
			  query.queryClose();
//			  System.out.println();
////			  File file =new File("D:\\test\\gyyTestMerge.merg");
////			  System.out.println("delete is " +file.delete());
////			  System.out.println("delete is " +file.delete());
////			  System.out.println("delete is " +file.delete());
////			  System.out.println("delete is " +file.delete());
////			  System.out.println("delete is " +file.delete());
////			  System.out.println("delete is " +file.delete());
//			  try {
//				Thread.sleep(30000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

	}
	
}
