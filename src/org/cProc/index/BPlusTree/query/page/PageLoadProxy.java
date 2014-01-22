package org.cProc.index.BPlusTree.query.page;

import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cProc.gc.DirectByteBufferCleaner;
import org.cProc.index.BPlusTree.BEntry;
import org.cProc.index.BPlusTree.BTreeType;
import org.cProc.index.BPlusTree.Data.Data;
import org.cProc.index.BPlusTree.query.QueryInterface;




public class PageLoadProxy<T extends Data> {
	
	
	private static Log Log4jlogger = LogFactory.getLog(PageLoadProxy.class); 
	
	
	
	 HashMap<Integer , Page> pageMap = new HashMap<Integer , Page>();
	 HashMap<Long , MergerDataPage> dataPageMap = new HashMap<Long , MergerDataPage>();
	 HeadPage head = null;
	 QueryInterface query = null;
	 FileChannel fc = null;
	 private final int maxPageCount = 10000;
	 private final int realsePageCount = 3000;
	 
	 private final int dataMaxPageCount = 10000;
	 private final int dataRealsePageCount = 3000;
	 public int dataNumber = 0;
	 Thread thread = null;
	 AheadReadThread  aheadReadThread = null;
	 boolean isClose = false;
	 
	 public PageLoadProxy( QueryInterface query ,FileChannel fc)
	 {
		 this.query = query;
		 this.fc = fc;
		 aheadReadThread = new AheadReadThread(this);
		 thread = new Thread(aheadReadThread);
		 thread.start();
	 }
	 
	 
	 synchronized public void close()
	 {
		 Iterator<Entry<Integer , Page>> iterator =pageMap.entrySet().iterator();		 
		 pageMap = null;
		 dataPageMap = null;
		 query = null;
		 head  = null;
		 if(aheadReadThread != null)
		 {
			 aheadReadThread.setNextMergeDataPage(0);
		 }
		 isClose = true;
	 }
	public  void removePage(int number) {
		pageMap.remove(number);
	}
	
	synchronized public boolean isColse()
	{
		return isClose;
	}
	
	public  Page getPage(int number)
	{
		Page page =  pageMap.get(number);
		if(page == null)
		{
//			DebugPrint.InforPrint(" load page Number is " + number);
			
			if(pageMap.size()  >= maxPageCount)
			{	int realseCount = 0;
				 Iterator<Entry<Integer , Page>> iterator =pageMap.entrySet().iterator();
				 while(iterator.hasNext() && realseCount > realsePageCount)
				 {
					 realseCount++;
					 Entry<Integer , Page> entry =   iterator.next();
					 if(entry.getKey() != 0)
					 {
						 realseCount++;
						  pageMap.remove(entry.getKey());
					 }
				 }
				
			}
			loadPage(number);
			page =  pageMap.get(number);
		}
		return page;
	}
	
	public long getDataPageStart()
	{
		return BTreeType.headPage;
	}
	
	
	synchronized public  MergerDataPage getMergerDataPage(long pageOffset,int  pageType ,boolean ahead)
	{
		//System.out.println("getMergerDataPage***************one"+"**8pageOffset is "+ pageOffset);
		if(this.isColse())
		{
			return null;
		}
		if(pageOffset >= head.getBPlusTreePageStartPos())
		{
			return null;
		}
		 if(dataPageMap.get(pageOffset) == null)
		 {
				if(dataPageMap.size()  >= dataMaxPageCount)
				{	int realseCount = 0;
				Iterator<Entry<Long , MergerDataPage>> iterator =dataPageMap.entrySet().iterator();
					 while(iterator.hasNext() && realseCount > dataRealsePageCount)
					 {
						 realseCount++;
						 Entry<Long , MergerDataPage> entry =   iterator.next();
						 if(entry.getKey() != 0)
						 {
							  realseCount++;
							  dataPageMap.remove(entry.getKey());
						 }
					 }
				}
			 dataPageMap.put(pageOffset, loadDataPage(pageOffset,pageType));
		 }
		 MergerDataPage page = dataPageMap.get(pageOffset);
		 //System.out.println("page data  is " + page.fileNumberList.size() + "   load page is " + pageOffset+"  dataNumber  is " + dataNumber);
		 if(page == null)
		 {
			 return page;
		 }
		 dataNumber= dataNumber+page.fileNumberList.size();
		 
		 //预读
		 if(ahead)
		 {
		 aheadReadThread.setNextMergeDataPage(getNextMergeDataPagePos(pageOffset, pageType));
		 }
		 return page;
	}
	
	//预读

	
	public long getNextMergeDataPagePos(long pageOffset,int  pageType)
	{
		return pageOffset+head.dataPageSize;
	}
	
	//从0开始
	public void  removePage(long pageOffset ,int pageType)
	{
		dataPageMap.remove(pageOffset);
	}
	

	public MergerDataPage loadDataPage(long pageOffset ,int  pageType)
	{
		try {
			
			MappedByteBuffer temp = fc.map(FileChannel.MapMode.READ_ONLY, pageOffset, head.dataPageSize);
			int  dataNumber = temp.getInt();
			
			MergerDataPage<Data>  dataPage = new MergerDataPage<Data>(pageOffset);
			for( int i = 0 ; i < dataNumber ; i++)
			{
				Data data = query.getTemplate();
				data.read(temp);
				int fileNumber = temp.getInt();
				long offset = temp.getLong();
				dataPage.addOneData(data);
				dataPage.addOneDataFileNumber(fileNumber);
				dataPage.addOneDataOffset(offset);
			}
			DirectByteBufferCleaner.clean(temp);
			temp = null;
			return  dataPage;
		} catch (Exception e) {
			e.printStackTrace();
			return  null;
		}
		
	}
	
	public void loadPage(int number )
	{
		if(number == HeadPage.HEADSTART)  //ͷ�ڵ�
		{			
		
			head = new HeadPage();
			head.parseFile(fc,0,BTreeType.headPage);
			//Log4jlogger.info("root is in the pos :" +head.getRootPagePos());
			pageMap.put(HeadPage.HEADSTART, head);
		}
		else  //����ڵ� 
		{
			byte isLeaf = -1;
		    try {
				String indexFile = query.getIndexFile();
//				System.out.println(" start fc.map : the indexfile is "+indexFile + " thread info is " +Thread.currentThread().getName() );
				long startPos  = head.getBPlusTreePagePos(number);
				long size = head.bPlusPageSize;
				if(startPos+head.bPlusPageSize > fc.size() )
				{
					size = fc.size() - startPos;
				}
		    	MappedByteBuffer temp = fc.map(FileChannel.MapMode.READ_ONLY,startPos ,size);
		 
		    	if(temp == null)
		    	{
		    		return ;
		    	}
		    	isLeaf = temp.get(); 
		    	if(isLeaf  == BEntry.LEAF)   //ΪҶ�ӽڵ�
		    	{
		    		LeafPage<T> page = new LeafPage<T>();
		    		page.setTemplate(query.getTemplate());
		    		page.setPageNumber(number);
		    		page.parseFile(temp);
		    		pageMap.put(number, page);
		    	}
		    	else if(isLeaf  == BEntry.NOTLEAF)  //Ϊ��Ҷ�ӽڵ�
		    	{
		    		NotLeafPage<T>  page = new NotLeafPage<T>();
		    		page.setPageNumber(number);
		    		page.setTemplate(query.getTemplate());
		    		page.parseFile(temp);
		    		pageMap.put(number, page);
		    	}
		    	else {
		    		Log4jlogger.info("this page is error");
				}
		    	DirectByteBufferCleaner.clean(temp);
		    	temp = null;
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}
	
}
