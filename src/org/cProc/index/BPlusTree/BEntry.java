package org.cProc.index.BPlusTree;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Vector;

import org.cProc.index.BPlusTree.Data.Data;
import org.cProc.tool.Bytes;




public class BEntry< T extends Data > {
	public static final byte  LEAF = 1;
	public static final byte   NOTLEAF=0;
	
	
    int n;   
    int leafhalf;   
    Vector nodes;   
    BNode<T> firstNode;   
    BEntry<T> next;   
    BEntry<T> previous;   
    int level;   
    boolean isRoot=false;   
    BNode<T> parent=null;   
    String name;   
     T SMALL= null;   
    BPlusTree<T> bp;   
    BEntryProxy<T> entryproxy=new BEntryProxy<T>(this);   
    int pageNumber = -1;
    PageCountHelper  count = null;
    
    BEntry(int n , T minValue , PageCountHelper count)   
    {   
    	SMALL = minValue;
        this.n=n;   
        nodes=new Vector(n+1);   
        leafhalf=(n>>1)+1;//[n-1/2]+1    
        firstNode=new BNode<T>(SMALL);   
        this.addToVector(firstNode);   
        this.count = count;
        pageNumber = count.getNumber();
    }   
    BNode<T> lastNode()   
    {   
        return (BNode<T>)(this.nodes.lastElement());   
    }   
       
    BEntry<T> rightMost()   
    {   
        BEntry<T> entry=this;   
        while(true)   
        {              
            if (entry.isLeaf())   
                return entry;   
            else   
                entry=entry.lastNode().point;   
        }   
    }   
    BEntry<T> leftMost()   
    {   
        BEntry<T> entry=this;   
        while(true)   
        {                      
            if (entry.isLeaf())   
                return entry;   
            else   
                entry=entry.firstNode.point;   
        }   
    }   
    void link(BEntry<T> latter)   
    {   
        BEntry<T> formerLeaf=this.rightMost();   
        BEntry<T> latterLeaf=latter.leftMost();   
        formerLeaf.setNext(latterLeaf);   
    }   
    void link2(BEntry<T> latter)   
    {   
        latter.setNext(this.next);   
        this.setNext(latter);   
    }   
   
    void setNext(BEntry<T> entry)   
    {   
        this.next=entry;   
        if (entry==null)   
            return;   
           
           
        entry.previous=this;   
    }   
    void deleteFromVector(BNode<T> node)   
    {   
        nodes.remove(node);   
        node.inWhichEntry=null;   
    }   
    void addToVector(BNode<T> node)   
    {   
        int i=0;   
        for (;i<nodes.size();i++ )   
        {   
            BNode<T> tempNode=(BNode<T>)nodes.elementAt(i);   
            if (tempNode.index.compareTo(node.index) > 0)   
            {   
                nodes.add(i,node);   
                node.inWhichEntry=this;   
                return ;   
            }   
        }   
        nodes.add(i,node);   
        node.inWhichEntry=this;   
    }   
    
    
    void addToVector(BNode<T> node , BNode<T> before)   
    {   
        int i=0;   
        for (;i<nodes.size();i++ )   
        {   
            BNode<T> tempNode=(BNode<T>)nodes.elementAt(i);   
            if (tempNode == before )   
            {   
                nodes.add(i+1,node);   
                node.inWhichEntry=this;   
                return ;   
            }   
        }   
        nodes.add(i,node);   
        node.inWhichEntry=this;   
    }   
    
    
    BNode<T>  removeFirst()
    {
    	if(nodes.size() > 0 )
    	{
    		return ( BNode<T>)nodes.remove(0);
    	}
    	return null;
    }
    
      void insertMin(BNode<T> min)
    {
    	  nodes.insertElementAt(min, 0);
    }
    
    BNode<T>  removeLast()
    {
    	if(nodes.size() > 0 )
    	{

    		return ( BNode<T>)nodes.remove(nodes.size()-1);
    	    		
    	}
    	return null;
    }
    
    BNode<T> findNode(T index)   
    {   
        for (int i=0;i<nodes.size() ;i++ )   
        {   
            BNode<T> tempnode=(BNode<T>)nodes.elementAt(i);   
            if (tempnode.index.compareTo(index) > 0)   
            {   
                return null;   
            }   
            if (tempnode.index==index)   
                return tempnode;   
        }   
        return null;   
    }   
    BEntry<T> findEntry(T index)   
    {   
    	
    
        if (isLeaf())   
        {   
            return this;   
        } 
      
        for (int i=0;i<nodes.size() ;i++ )   
        {   
            BNode tempNode=(BNode)nodes.elementAt(i);   
            if (tempNode.index.compareTo(index) > 0)   
            {   
                BNode tempNode2=(BNode)nodes.elementAt(i-1);   
                if (tempNode2.point.isLeaf())   
                {   
                    return tempNode2.point;   
                }   
                else   
                {   
                    return tempNode2.point.findEntry(index);   
                }   
            }   
        }   
                BEntry entry=((BNode)(nodes.lastElement())).point;   
                if (entry.isLeaf())    
                    return entry;   
                else   
                    return entry.findEntry(index);   
    }   
    
    
    public void writeNORecur(FileChannel fc ,long startPage ,int pageSize) throws Exception
    {
    	//DebugPrint.InforPrint("page number is " +this.pageNumber + "  startPage  is " + startPage +"  pageSize is " + pageSize );
     	 ByteBuffer temp = ByteBuffer.allocate (pageSize);  //1024���ֽ�
    	 //page��ĵ�һ��bit������Ƿ�ΪҶ�ӽڵ�
    	 if(!isLeaf())  //��Ҷ�ӽڵ�
    	 {
    		temp.put(NOTLEAF);
    		temp.put(Bytes.toBytes(nodes.size()));
  	      for (int i=0;i<nodes.size() ;i++ )   
	      {   
	            BNode<T> node=(BNode<T>)nodes.elementAt(i);   
	            temp.put(Bytes.toBytes(node.point.pageNumber));
	            node.index.write(temp);	       

	       }    
    	 }
    	 else   //Ҷ�ӽڵ�
     	 {
    		// System.out.println("leaf number is " + this.pageNumber +" start   is " +startPage +"  pageSize is " + pageSize );
    		 temp.put(LEAF);
    		 if(previous != null)
    		 {
    			 temp.put(Bytes.toBytes(previous.pageNumber));
    		 }
    		 else {
    			 temp.put(Bytes.toBytes((int)0));
			}
    		 if(next != null)
    		 {	
    			 temp.put(Bytes.toBytes(next.pageNumber));
    		 }
    		 else {

    			 temp.put(Bytes.toBytes((int)0));
 			}
    		 temp.put(Bytes.toBytes(nodes.size()));   //��һ��Ҷ�ӽڵ�
    	      for (int i=0;i<nodes.size() ;i++ )   
    	      {   
    	            BNode<T> node=(BNode<T>)nodes.elementAt(i);  
    	            node.write(temp,fc);
    	       }   
    	 }
        temp.flip();
        fc.write(temp, startPage);
        temp = null;
    }
    
    
    //已废弃不用
//    public  void write(FileChannel fc) throws Exception
//    {
//    	 ByteBuffer temp = ByteBuffer.allocate (diskSize);  //1024���ֽ�
//    	 //page��ĵ�һ��bit������Ƿ�ΪҶ�ӽڵ�
//    	 if(!isLeaf())  //��Ҷ�ӽڵ�
//    	 {
//    		temp.put(NOTLEAF);
//    		temp.put(Bytes.toBytes(nodes.size()));
//  	      for (int i=0;i<nodes.size() ;i++ )   
//	      {   
//	            BNode<T> node=(BNode<T>)nodes.elementAt(i);   
//	            temp.put(Bytes.toBytes(node.point.pageNumber));
//	            node.index.write(temp);
//	            node.point.write(fc);
//	            
//	       }    		
//    	 }
//    	 else   //Ҷ�ӽڵ�
//     	 {
//    		 temp.put(LEAF);
//    		 if(previous != null)
//    		 {
//    			 temp.put(Bytes.toBytes(previous.pageNumber));
//    		 }
//    		 else {
//    			 temp.put(Bytes.toBytes((int)0));
//			}
//    		 if(next != null)
//    		 {
//    			 temp.put(Bytes.toBytes(next.pageNumber));
//    		 }
//    		 else {
//    			 temp.put(Bytes.toBytes((int)0));
// 			}
//    		 temp.put(Bytes.toBytes(nodes.size()));   //��һ��Ҷ�ӽڵ�
//    	      for (int i=0;i<nodes.size() ;i++ )   
//    	      {   
//    	            BNode<T> node=(BNode<T>)nodes.elementAt(i);   
//    	            node.write(temp,fc);
//    	        }   
//    	 }
//
//        temp.flip();
////        byte[] array = temp.array();
////    	System.out.println("page is " +  pageNumber);
////        for( int i = 0 ; i < array.length ; i++)
////        {
////        	System.out.print(array[i]+" ");
////        }
////    	System.out.println();
//        fc.write(temp, pageNumber*diskSize);
//        temp = null;
//    }
    
    String info()   
    {   
        StringBuffer s=new StringBuffer();   
        s.append("Entry"+pageNumber+"[");   
        for (int i=0;i<nodes.size() ;i++ )   
        {   
            BNode<T> node=(BNode<T>)nodes.elementAt(i);   
  //          if (node.index.compareTo(SMALL) > 0)   
                s=s.append(node.index);   
                if(this.isLeaf())
                {
                	 s=s.append(":"+node.fileNumber+",");   
                }
                else {
                	 s=s.append(",");   
				}
        }   
        s.append("]"  );   
        if(next != null)
        {
        	 s.append("    next page is " + next.pageNumber );   
        }
        if(previous != null)
        {
        	 s.append("    prev page is " + previous.pageNumber );   
        }
        if(this.isLeaf())
        {
        	 s.append("    is  leaf"  );   
        }
        else {
        	 s.append("    not  leaf"  );   
		}
        return s.toString();   
    }   
   
    void print(int tab)   
    {   
        for (int i=0;i<tab ;i++ )   
        {   
            System.out.print("\t");   
        }   
        System.out.println(info());   
        if (this.isLeaf())   
        {   
            return;   
        }   
        for (int i=0;i<nodes.size() ;i++ )   
        {   
            BNode<T> node=(BNode<T>)nodes.elementAt(i);   
            node.print();    
            if (node.point!=null)   
            {   
                node.point.print(tab+1);   
            }   
               
        }   
    }   
   
    void printdebug(int tab,StringBuffer sb)   
        {   
           for (int i=0;i<tab ;i++ )   
            {   
                   sb=sb.append("\t");   
            }   
           sb.append(info()+"\n");   
           for (int i=0;i<nodes.size() ;i++ )   
            {   
                    BNode<T> node=(BNode<T>)nodes.elementAt(i);   
                    //node.print();    
                    if (node.point!=null)   
                    {   
                            node.point.printdebug(tab+1,sb);   
                    }   
            }   
        }   
    boolean isLeaf()   
    {   
        return (firstNode.point==null);   
    }   
    boolean isFull()   
    {   
        return (nodes.size()==n);   
    }   
    boolean notEnough()   
    {   
        return (nodes.size()<leafhalf);   
    }   
    boolean justEnough()   
    {   
        return (nodes.size()==leafhalf);   
    }   
    void delete(BNode node)   
    {   
        entryproxy.delete(node);   
    }   
    void insert(BNode node)   
    {   
        entryproxy.insert(node);   
    }   
    
    void insert(BNode node ,BNode before )
    {
    	 entryproxy.insert(node ,before);   
    }
    void createNewRootFor(BNode node)   
    {   
        BEntry<T> entry=new BEntry<T>(n,this.SMALL,count);   
        entry.firstNode.setPoint(this);   
        entry.addToVector(node);   
        this.isRoot=false;   
        giveRootTo(entry);   
    }   
    void giveRootTo(BEntry< T> be)   
    {   
        this.isRoot=false;             
        bp.RootChanged(be);   
    }   
    void addRootListener(BPlusTree bp)   
    {   
        this.bp=bp;   
    }   
    
//    public void setPageNumber( PageCountHelper number)
//    {
//    	this.pageNumber = number.getNumber();
//    	
//    	for( int i = 0  ;   i  < nodes.size() ; i++)
//    	{
//    		Object  obj = nodes.get(i);
//    		
//    		if(obj != null && ((BNode)obj).point != null )
//    			((BNode)obj).point.setPageNumber(number);
//    	}
//    	
//    }
    
    public void setPageNumber(  int number)
    {
    	this.pageNumber = number;   	
    }

    
    
}


