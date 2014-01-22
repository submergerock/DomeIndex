package org.cProc.index.BPlusTree.query.page;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.cProc.index.BPlusTree.Data.Data;


public class NotLeafPage<T extends Data>   extends Page{

	 public ArrayList<T> indexList  =  new ArrayList<T>();
	 public ArrayList<Integer> childNumberList  =  new ArrayList<Integer>();
	public NotLeafPage()
	{
		
	}
	
	public  boolean isLeaf()
	{
		return false;
	}
	
	public ArrayList<T> getIndexList()
	{
		return indexList;
	}
	public int getChildNumber(int pos)
	{
		return childNumberList.get(pos);
	}

//查询范围  先找到第一个元素，从底层遍历数据
//	public  boolean findRangeData(T startIndex, T endIndex , Query q )
//	{
//			for(int i = 0 ; i < indexList.size()-1 ; i++)
//			{
//				if( startIndex.compareTo(indexList.get(i)) >= 0 &&   startIndex.compareTo(indexList.get(i+1)) <= 0 )
//				{
//					  q.findRangeData(childNumberList.get(i), startIndex , endIndex);
//					  if(q.getReslutSize() != 0)
//					  {
//						  return true;
//					  }
//				}
//				if(startIndex.compareTo(indexList.get(i)) <  0)
//				{
//					return false;  
//				}
//			}
//			if(indexList.size() > 0)
//			{		
//						return q.findRangeData(childNumberList.get(indexList.size() -1), startIndex,endIndex);
//			}
//			
//			return false;
//
//	}
//	
////查询单个
//	public  boolean findIndex(T index , Query q )
//	{
//			for(int i = 0 ; i < indexList.size()-1 ; i++)
//			{
//				if( index.compareTo(indexList.get(i)) >= 0 &&   index.compareTo(indexList.get(i+1)) <= 0 )
//				{
//					  q.findData(childNumberList.get(i), index);
//					  if(q.getReslutSize() != 0)
//					  {
//						  return true;
//					  }
//				}
//				if(index.compareTo(indexList.get(i)) <  0)
//				{
//					return false;  //��Ҫ������ʣ�
//				}
//			}
//			if(indexList.size() > 0)
//			{		
//				if( index.compareTo(indexList.get( indexList.size() -1 )) >= 0 )
//				{
//					//
//						q.findData(childNumberList.get(indexList.size() -1), index);
//						return true;
//				}
//			}
//			
//			return false;
//
//	}
	
	
	//�Ѷ�ȡ�˵�һ���ֽڣ����Ƿ�ΪҶ�ӽڵ�Ĳ���
	public void parseFile(ByteBuffer buffer )
	{	
	    try {
	    	int indexSize = buffer.getInt();
	    	for(int i = 0 ; i < indexSize ; i++)
	    	{
	    		int childNumber = buffer.getInt();
	    		childNumberList.add(childNumber);
	    		T index =  (T)this.getTemplate();
	    		index.read(buffer);
	    		indexList.add(index);
	    	}
//	    	DebugPrint.DebugPrint("page number is " +this.getPageNumber() +" type is not leaf"  +" indexList size is  " +  this.indexList.size() , this);
	    	
	    	
//	    	for(int m = 0 ; m < this.indexList.size() ; m++)
//	    	{
//	    		System.out.println("index " +m+"  is " + this.indexList.get(m) +" point to  pageNumber is " +childNumberList.get(m) );
//	    	}
	    	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
