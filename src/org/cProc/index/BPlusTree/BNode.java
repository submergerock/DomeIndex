package org.cProc.index.BPlusTree;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.cProc.index.BPlusTree.Data.Data;
import org.cProc.tool.Bytes;



public class BNode<T extends  Data > {
	
		public  Object minIndex = null ;
	    public T index= null;   
	    public BEntry<T> point=null;   
	    
	    public  int fileNumber = -1 ;
	    public  long offset  =  -1;
	    
	    
	    BEntry<T> inWhichEntry;   
	    BNode(T index)   
	    {   
	        this.index=index;   
	    }   
	    void setPoint(BEntry<T> be)   
	    {   
	        this.point=be;   
	        if (be!=null)   
	            be.parent=this;        
	    }   
	    void insertLink(BNode middle)   
	    {   
	        BEntry<T> former=this.point.rightMost();   
	        BEntry<T> middleFormer=middle.point.leftMost();   
	        BEntry<T> middleLatter=middle.point.rightMost();   
	        if (middleFormer==middleLatter)   
	            return;   
	           
	        middleLatter.setNext(former.next);   
	        former.setNext(middleFormer);   
	    }   
	    
	    public  void write(ByteBuffer  buffer , FileChannel fc) throws Exception
	    {
	    	if(point != null)  //��Ҷ�ӽڵ�
	    	{
	    		//不可能为叶子了
//	    		buffer.put(Bytes.toBytes(point.pageNumber) );	
//	    		index.write(buffer);
//	    		point.writeNORecur(fc);
	    	}
	    	else  //Ҷ�ӽڵ�
	    	{
	    		index.write(buffer);
	    		buffer.put(Bytes.toBytes(fileNumber));	
	    		buffer.put(Bytes.toBytes(offset));	
			}

	    } 
	    
	    void print()   
	    {   
	        if ( minIndex != null && index.compareTo(minIndex) >= 0)   
	            System.out.println("node"+index);   
	    }   
	   
	    public void setMinNode(T min)
	    {
	    	minIndex = min;
	    }
	    
	    public static void main(String[] args)    
	    {   
	        System.out.println("Hello World!");   
	    }   
	    
	    public void setInfor( BNode node )
	    {
	    	if(node != null)
	    	{
	    		this.fileNumber = node.fileNumber;
	    		this.offset 	       = node.offset;
	    	}
	    }
	    
	    public void setOffset(int fileNumber , long offset)
	    {
	    	this.fileNumber = fileNumber;
	    	this.offset = offset;
	    }

}
