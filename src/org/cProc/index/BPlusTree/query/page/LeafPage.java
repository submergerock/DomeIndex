package org.cProc.index.BPlusTree.query.page;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.cProc.index.BPlusTree.Data.Data;


public class LeafPage <T extends Data> extends Page{
	
	
	public  ArrayList<T> indexList  =  new ArrayList<T>();
	public ArrayList<Integer> fileNumberList  =  new ArrayList<Integer>();
	public   ArrayList<Long> offsetList  =  new ArrayList<Long>();
	
	public int preNumber    = 0;
	public int nextNumber  = 0;
	public LeafPage()
	{
		
	}
	

	
	public  boolean isLeaf()
	{
		return true;
	}
	
	public int  getFileNumberOfDataPos(int pos)
	{
		return fileNumberList.get(pos);
	}
	
	public  long getFileOffsetOfDataPos(int pos)
	{
		return offsetList.get(pos);
	}
	
	public ArrayList<T> getIndexList()
	{
		return indexList;
	}

//	//单个查询
//	public  boolean  findIndex(T index , Query  q)
//	{
//			for(int i = 0 ; i < indexList.size() ; i++)
//			{
//				if(indexList.get(i).compareTo(index) == 0)
//				{
//					findAllIndex(i,index , q);
//				   return  true ;
//				}
//			}
//			return false;
//	}

//	//单个查询
//	private void   findAllIndex( int startPos ,T index, Query q)
//	{
//	
//		ArrayList<T> tempList = indexList;
//		for(int k =  startPos; k <  indexList.size() ; k++ )
//		{
//			if(indexList.get(k).compareTo(index) == 0)
//			{
//				DebugPrint.DebugPrint("find data : " + indexList.get(k) +" int the page: " +this.getPageNumber() +"  next page is: " +nextNumber
//						+"  pre page is " + preNumber,this);
//				 q.addReslut(indexList.get(k) ,fileNumberList.get(k), offsetList.get(k) );
//			}
//			else if(indexList.get(k).compareTo(index) > 0){
//				return ;
//			}
//		}
//		if(nextNumber != 0)
//		{
//			LeafPage page = (LeafPage)q.getPage(nextNumber);
//			page.findAllIndex(0, index, q);
//		}
//		return ;
//	}
	
//	//范围查询
//	public  boolean  findRangeData(T startIndex , T endIndex , Query  q)
//	{
//			for(int i = 0 ; i < indexList.size() ; i++)
//			{
//				if(indexList.get(i).compareTo(startIndex) >= 0 &&  indexList.get(i).compareTo(endIndex) <= 0)
//				{
//					findAllIndex(i,startIndex , endIndex,q);
//					
//				   return  true ;
//				}
//			}
//
//			return false;
//	}
//	//范围查询
//	public void   findAllIndex( int startPos ,T startIndex,  T endIndex,Query q)
//	{
//		
//		ArrayList<T> tempList = indexList;
//		if(0 ==startPos)
//		{
//			startPos = 1;
//		}
//		for(int k =  startPos; k <  indexList.size() ; k++ )
//		{
//			if( indexList.get(k).compareTo(endIndex) <= 0)
//			{
//				DebugPrint.DebugPrint("find data : " + indexList.get(k) +" int the page: " +this.getPageNumber() +"  next page is: " +nextNumber
//						+"  pre page is " + preNumber , this);
//				 q.addReslut(indexList.get(k) ,fileNumberList.get(k), offsetList.get(k) );
//
//			}
//			else if(indexList.get(k).compareTo(endIndex) > 0){
//				return ;
//			}
//		}
//		if(nextNumber != 0)
//		{
//			LeafPage page = (LeafPage)q.getPage(nextNumber);
//			page.findAllIndex(0, startIndex, endIndex,q);
//		}  
//		return ;
//	}
	
		
	//�Ѷ�ȡ�˵�һ���ֽڣ����Ƿ�ΪҶ�ӽڵ�Ĳ���
	public void parseFile(ByteBuffer buffer )
	{
		    try {
		    	//��ȡ�ж��ٸ�����ֵ
		    	preNumber = buffer.getInt();
		    	nextNumber = buffer.getInt();
		    	int indexSize = buffer.getInt();
		    	
		    	for(int i = 0 ; i < indexSize ; i++)
		    	{
		    		//��ȡ����ֵ		
		    		T index =  (T)this.getTemplate();
		    		index.read(buffer);
		    		indexList.add(index);
		    		fileNumberList.add(buffer.getInt());   //�ļ����
		    		offsetList.add(buffer.getLong());  //ƫ����
		    	}

		    	
//		    	for(int m = 0 ; m < this.indexList.size() ; m++)
//		    	{
//		    		DebugPrint.DebugPrint("index " +m+"  is " + this.indexList.get(m) + "  " +fileNumberList.get(m)  + "  " +offsetList.get(m)  );
//		    	}
		    	
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

}
