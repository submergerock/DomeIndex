package org.cProc.index.BPlusTree.query.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.cProc.index.BPlusTree.query.page.NotLeafPage;
import org.cProc.index.BPlusTree.query.page.Page;
import org.cProc.index.BPlusTree.query.page.PageLoadProxy;
import org.cProc.sql.IndexFilter;
import org.cProc.tool.Bytes;






public class Query<T extends Data>  implements QueryInterface<T> {

	
	private static Log Log4jlogger = LogFactory.getLog(Query.class); 
	
	
	
    private  Page  rootPage  = null;
    private  String  indexFile ="";
    private  ArrayList<T> reslutList = new  ArrayList<T>();
    private  ArrayList<Integer> fileNumberList = new  ArrayList<Integer>();
    private  ArrayList<Long> offsetList = new  ArrayList<Long>();
   // HashMap<Integer , Page> pageMap = new HashMap<Integer , Page>();
    FileChannel  fc = null;
    private  T  template = null;

    private IndexFilter  indexFilter = null;
    
    private FileInputStream fileStream = null;
    
    
    private LeafPage<T> seqLeafPage = null;
    private int  currentPosInPage = 0;
    private boolean  traversalOver = false;
    private  int currentFileNumber = -1;
    private  long currentOffset= -1;
    private  long bplusTreeStartPos = 0;
  
 	PageLoadProxy proxy = null;
    
    //浠�寮�
    public  long getBPlusTreePagePos( int number)
    {
    	HeadPage head = (HeadPage<T>)( proxy.getPage(0));
    	return bplusTreeStartPos + BTreeType.headPage+ (number -1) * head.bPlusPageSize ;
    }
    
    public int getType()
    {
    	HeadPage head = (HeadPage<T>)( proxy.getPage(0));
    	return head.type;
    }
    public long getBPlusTreeHeadPos()
    {
    	return bplusTreeStartPos ;
    }
    
    //璁剧疆BPlusTree鍦ㄦ枃浠堕噷鐨勫瓨鏀句綅缃�鍙互浠�寮�,涔熷彲浠ヤ粠浠绘剰浣嶇疆寮�
    public void setBplusTreeStartPos(long pos)
    {
    	bplusTreeStartPos = pos;
    }
       
    
	public Query(String name)
	{
		 this.indexFile = name;    
	    	try {
	    		File file = new File(name);
	        	if(!file.exists())
	        	{
	        		file.createNewFile();
	        	}	    
	        	 fileStream = new FileInputStream(name);
	             fc = fileStream.getChannel();  
	    	}catch (Exception e) {
	    		e.printStackTrace();
			}
	    	 proxy = new PageLoadProxy(this,fc);
	    	proxy.loadPage(HeadPage.HEADSTART);
		 
	}
	
	public   HashMap<Integer,String>   getFileMap()
	{
		HeadPage page = (HeadPage)this.getPage(HeadPage.HEADSTART);
		return page.fileMap;
	}
	
	public void setTemplate( T  template)
	{
		this.template = template;
	}
	public   T getTemplate( )
	{
		 return (T)template.getInstance();
	}
			
	//鏌ヨ鑼冨洿
	private  boolean findRangeData(Page  page ,T startIndex , T endIndex)
	{		
			Page<T> tempPage = page;
			if(!tempPage.isLeaf())
			{	
				NotLeafPage<T> parentLeaf = null;
				
				findLeaf:  while( !tempPage.isLeaf())  //褰撻潪鍙跺瓙鑺傜偣鐨勬椂鍊�
				{
					NotLeafPage<T> notLeaf = (NotLeafPage<T>)tempPage;
					ArrayList<T> indexList  =  notLeaf.getIndexList();
					boolean find = false;
					for(int i = 0 ; i < indexList.size()-1 ; i++)
					{
						//瀛樺湪鍑犵鎯呭喌锛� 鍋囪X = A
						//1锛� A  A锛�鍒欙細闇�鍒ゆ柇    x>= before and x <= after ;  
						//2  :  A  A+1;鍒欓渶瑕佸垽鏂� X>=before and  X < after
						//3:  A-1  A+1; 鍒欓渶瑕佸垽鏂�X > before and  x < after
						if( ( startIndex.compareTo(indexList.get(i)) >= 0 &&   startIndex.compareTo(indexList.get(i+1)) <= 0)
//							||  ( startIndex.compareTo(indexList.get(i)) >= 0 &&   startIndex.compareTo(indexList.get(i+1)) < 0)
//							||   ( startIndex.compareTo(indexList.get(i)) > 0 &&   startIndex.compareTo(indexList.get(i+1)) < 0)
							)
						{	
							parentLeaf = notLeaf;
							tempPage = getPage(notLeaf.getChildNumber(i));
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
					//find the not-leaf, but this not-leaf have no one index,so return ;
					if(!find && indexList.size() == 0)
					{
						return true;
					}
					if(!find &&indexList.size() > 0)
					{		
						parentLeaf = notLeaf;
						int number =  notLeaf.getChildNumber(indexList.size() -1);
						tempPage = getPage( notLeaf.getChildNumber(indexList.size() -1));
					}
				}	
			}
			//鎵惧埌浜嗗彾瀛愪簡 :
			LeafPage<T> leafPage = (LeafPage<T>)tempPage;
			ArrayList<T> indexList  =  leafPage.getIndexList();
			int startPos = 0;
					LeafPage<T> tempLeafPage = (LeafPage<T>)leafPage;
					do {
						if(0 ==startPos)
						{
							startPos = 1;
						}
						for(int k =  startPos; k <  indexList.size() ; k++ )
						{
							
							
//							Log4jlogger.info("find data : " + indexList.get(k) +" int the page: " +tempLeafPage.getPageNumber() +"  next page is: " +tempLeafPage.nextNumber
//									+"  pre page is " + tempLeafPage.preNumber );
							
							
							if( indexList.get(k).compareTo(startIndex) >= 0 && indexList.get(k).compareTo(endIndex) <= 0)
							{
								
								 this.addReslut(indexList.get(k) ,tempLeafPage.fileNumberList.get(k), tempLeafPage.offsetList.get(k) );
							}
							else if(indexList.get(k).compareTo(endIndex) > 0){
								return true;
							}
						}
						if(tempLeafPage.nextNumber != 0)
						{
							Page nextPage = this.getPage(tempLeafPage.nextNumber);
							try {
								tempLeafPage = (LeafPage)this.getPage(tempLeafPage.nextNumber);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							startPos = 1;
							indexList  =  tempLeafPage.getIndexList();
						} 
						else {
							return true;
						}
					} while (true);
	}
	

	//鏌ヨ鑼冨洿
	private boolean findRangeData(int number ,T startIndex , T endIndex)
	{
		 return findRangeData(getPage(number),startIndex , endIndex);
	}
	
	//鏌ヨ鑼冨洿
	public boolean findRangeData(T startIndex , T endIndex)
	{
		 return findRangeData(((HeadPage)getPage(HeadPage.HEADSTART)).getRootPagePos(),startIndex , endIndex);
	}
	
	
	private  Page getPage(int number)
	{
		Page page =  proxy.getPage(number);
		if(page == null)
		{
			proxy.loadPage(number);
			page =   proxy.getPage(number);
		}
		return page;
	}
	
	public void startFindSeqDataOfAll()
	{
		Page page = getPage(((HeadPage)getPage(HeadPage.HEADSTART)).getRootPagePos());
		while( !page.isLeaf())
		{
			NotLeafPage<T> notLeafPage =  (NotLeafPage)page;
			 int pageNumber = notLeafPage.childNumberList.get(0);
			 page = getPage(pageNumber);
		}
		//page 涓烘渶宸﹁竟鐨勫彾瀛愯妭鐐�
		seqLeafPage = (LeafPage<T>)page;
		if(seqLeafPage.getIndexList().size() == 1)  //鍖呭惈涓�釜榛樿鐨勬渶灏忓�
		{
			traversalOver = true;
		}
	}
	
	public T getNextData()
	{
		if(currentPosInPage == 0)
		{
			currentPosInPage++;
		}
		T reslut =  seqLeafPage.getIndexList().get(currentPosInPage);
		currentFileNumber = seqLeafPage.getFileNumberOfDataPos(currentPosInPage);
		currentOffset 			=  seqLeafPage.getFileOffsetOfDataPos(currentPosInPage);
		if(currentPosInPage == seqLeafPage.getIndexList().size() -1) //褰撳凡缁忎负鏈�悗涓�釜鐨勬椂鍊�
		{
			proxy.removePage(seqLeafPage.getPageNumber());
			if(seqLeafPage.nextNumber != 0)
			{
				currentPosInPage = 0;
				seqLeafPage = (LeafPage<T>)getPage(seqLeafPage.nextNumber);
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
	
	public void setIndexFilter(IndexFilter<T> indexFilter)
	{
		this.indexFilter = indexFilter;
	}
	
	private void addReslut(T data ,int fileNumber, long offset)
	{
		if(indexFilter != null)
		{
			if(indexFilter.isDataOK(data))
			{
				this.reslutList.add(data);
				//Log4jlogger.info("add"+data);
				this.fileNumberList.add(fileNumber);
				this.offsetList.add(offset);
			}
		}
		else 
		{
			this.reslutList.add(data);
			//Log4jlogger.info(""+data);
			this.fileNumberList.add(fileNumber);
			this.offsetList.add(offset);
		}
	}
	
	public int getReslutSize()
	{
		return reslutList.size();
	}
	
	public  ArrayList<T> getIndexReuslt()
	{
		return reslutList;
	}
	
	public  ArrayList<Integer> getFileNumberReuslt()
	{
		return this.fileNumberList;
	}
	
	public  ArrayList<Long> getOffsetReuslt()
	{
		return this.offsetList;
	}
	
	public void  printReslut()
	{
		for(int i = 0 ; i < reslutList.size() ; i++)
		{
			System.out.println("data : " + reslutList);
		}
	}
	public  String getFileName(int fileNumber)
	{
		return 	((HeadPage)getPage(HeadPage.HEADSTART)).getFileName(fileNumber);
	}
	
	public  void printlnAllFileName()
	{
			((HeadPage)getPage(HeadPage.HEADSTART)).printAllFileName();
	}
	
	//format: fileName#offset,offset,offset
	public ArrayList<String> getSortFileOffsetResult()
	{
		try {
		 // long  start_time = System.currentTimeMillis();
//		System.out.println("Query: &&&&&&&&&&&&&&&&data Sizie is " +offsetList.size() );
		ArrayList<Long> offsets = new ArrayList<Long>();
		 long  start_time = System.currentTimeMillis();
		 long modFileNumber = 10000000000l;
		for(int i = 0 ; i < fileNumberList.size() ;i++)
		{	
			offsets.add(fileNumberList.get(i)*modFileNumber+offsetList.get(i));
		}
		//System.out.println("getSortFileOffsetResult:fileNumber is " + offsets.size());	
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
//				System.out.println("fileNumber is " + fileNumber);
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
		} catch (Exception e) {
			e.printStackTrace();
			return new  ArrayList<String>();
			
		}
	}
	
	public   ArrayList<byte []> getMaxData()
	{
		return   ((HeadPage)getPage(HeadPage.HEADSTART)).getMaxData();
	}
	
	public   ArrayList<byte []> getMinData()
	{
		return   ((HeadPage)getPage(HeadPage.HEADSTART)).getMinData();
	}
	
	
	public  String  getIndexFile()
	{
		return  indexFile;
	}
	
	public void queryClose()
	{
		if(fileStream != null)
		{
			try {
				if(fc != null)
				{
					fc.close();
				}
				fileStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		proxy.close();
	}
	
	public static void main(String[] args)
	{
		//2999:783872999
	
    	ParseIndexFormat readIndexFormat = null;
		String path ="C:\\dingli\\test\\callingIndex\\index.frm" ;
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
		dataList.add(Bytes.toBytes(Long.MIN_VALUE));
		dataList.add(Bytes.toBytes(Integer.MIN_VALUE));
		dataList.add(Bytes.toBytes(Long.MIN_VALUE));
		dataList.add(Bytes.toBytes(Byte.MIN_VALUE));
		dataList.add(Bytes.toBytes(Byte.MIN_VALUE));
		dataList.add(Bytes.toBytes(Integer.MIN_VALUE));
		dataList.add(Bytes.toBytes(Long.MIN_VALUE));
		dataList.add(Bytes.toBytes(Long.MIN_VALUE));
		
		//   Query<MulitFiledData> query =  new Query<MulitFiledData>("D:\\test\\btreedata0"+BTreeType.orgName);
		   Query<MulitFiledData> query =  new Query<MulitFiledData>(
				   "C:\\dingli\\test\\callingIndex\\data\\2150\\2\\"
				   +"1300532408_1300536627_3_9c18a4b0-d73e-4974-b2a1-9502221b9a0c.org"
				   //+BTreeType.orgName
				   );
		  
		   query.setTemplate(new MulitFiledData(dataList,proxy));
		   
		   ArrayList<byte[]> maxByteArrayList = query.getMaxData();
//		   System.out.println("max data****************");
		   int count = 0;
		   for(int i = 0 ; i < maxByteArrayList.size() && count < 5 ; i++)
		   {
			   count++;
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
		    count = 0;
		   for(int i = 0 ; i < minByteArrayList.size() && count < 5 ; i++)
		   {
			   count++;
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
			ArrayList<byte[]> dataList1 = new ArrayList<byte[]>();
			dataList1.add(Bytes.toBytes((long )211));
			dataList1.add(Bytes.toBytes(211));
			dataList1.add(Bytes.toBytes((long )211));
			dataList1.add(Bytes.toBytes((byte)211));
			dataList1.add(Bytes.toBytes((byte)211));
			dataList1.add(Bytes.toBytes(211));
			dataList1.add(Bytes.toBytes((long )211));
			dataList1.add(Bytes.toBytes((long )211));
			ArrayList<byte[]> dataList2 = new ArrayList<byte[]>();
			dataList2.add(Bytes.toBytes((long )254));
			dataList2.add(Bytes.toBytes(254));
			dataList2.add(Bytes.toBytes((long )254));
			dataList2.add(Bytes.toBytes((byte)254));
			dataList2.add(Bytes.toBytes((byte)254));
			dataList2.add(Bytes.toBytes(254));
			dataList2.add(Bytes.toBytes((long )254));
			dataList2.add(Bytes.toBytes((long )254));
//		   query.findRangeData(new MulitFiledData(dataList1,proxy), new MulitFiledData(dataList2,proxy));
		   long end_time = System.currentTimeMillis();
		  
		   System.out.println("*******************************");
		   query.printlnAllFileName();
		   System.out.println("*******************************");
//		   ArrayList<MulitFiledData> list = query.getIndexReuslt();
//		   ArrayList<Integer> fileNumberList = query.getFileNumberReuslt();
//		   ArrayList<Long> offsetList = query.getOffsetReuslt();
//			for(int i = 0 ; i < list.size() ; i++)
//			{
//				System.out.println("data : "+i + "  "+  list.get(i) +" file name is :" + fileNumberList.get(i) +" file name is :" + query.getFileName( fileNumberList.get(i)) +"  offset is :  " + offsetList.get(i));
//			}
//			System.out.println("count is " + list.size());
//			System.out.println("time is " + (end_time - start_time));
//		   start_time = System.currentTimeMillis();
//		   ArrayList<String> reslut = query.getSortFileOffsetResult();
//		   end_time = System.currentTimeMillis();
//		   System.out.println("time is " + (end_time - start_time));
//		   System.out.println("size is " + reslut.size());
//		   for(int i =0 ; i < reslut.size() ; i++)
//		   {
//			   System.out.println(reslut.get(i));
//		   }
		  query.startFindSeqDataOfAll();
		  int countAll = 0 ;
		  while(!query.dataIsOver() )
		  {
			  System.out.println(query.getNextData()+" ");
			  countAll++;
		  }
		  System.out.println("countAll is " + countAll);
		  query.queryClose();

	}
	
	
	
}
