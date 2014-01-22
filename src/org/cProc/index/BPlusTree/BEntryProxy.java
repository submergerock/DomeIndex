package org.cProc.index.BPlusTree;

import org.cProc.index.BPlusTree.Data.Data;



public class BEntryProxy<T extends Data> {
	  
    BEntry<T> host;   
    BEntryProxy(BEntry<T> host)   
    {   
        this.host=host;   
    }   
    void delete(BNode<T> node)   
    {          
        if (this.host.isRoot)   
        {   
            this.host.deleteFromVector(node);   
            if (this.host.nodes.size()==1)//��ڵ����    
            {   
                this.host.giveRootTo(this.host.firstNode.point);   
                this.host.isRoot=false;   
                this.host.firstNode.point=null;   
            }   
            return;   
        }   
        BNode<T> nodeParent=node.inWhichEntry.parent;   
        BNode<T> siblingParentNode=null;   
        //node.inWhichEntry.print(0);    
        BEntry<T> entryParent=nodeParent.inWhichEntry;   
        this.host.deleteFromVector(node);   
           
           
        //System.out.println(entryParent.info());    
        //System.out.println(nodeParent.index);    
        if (this.host.notEnough())   
        {   
            int parentNo=entryParent.nodes.indexOf(nodeParent);   
            if (parentNo==0)   
            {   
                siblingParentNode=(BNode)(entryParent.nodes.elementAt(parentNo+1));    
            }   
            else   
            {                  
                siblingParentNode=(BNode)(entryParent.nodes.elementAt(parentNo-1));    
            }   
            BEntry<T> siblingEntry=siblingParentNode.point;   
            if (siblingEntry.justEnough())//��    
            {   
                if (siblingEntry.isLeaf())   
                {                      
                    //System.out.println(siblingParentNode.index);    
                    if (parentNo==0)   
                    {                          
                        for (int i=1;i<siblingEntry.nodes.size() ;i++ )   
                        {   
                            BNode<T> tempnode=(BNode<T>)(siblingEntry.nodes.elementAt(i));   
                            this.host.addToVector(tempnode);   
                        }   
                           
                        this.host.setNext(siblingEntry.next);   
                        siblingEntry.setNext(null);   
                        nodeParent.inWhichEntry.delete(siblingParentNode);   
                    }   
                    else   
                    {   
                        for (int i=1;i<this.host.nodes.size() ;i++ )   
                        {   
                            BNode<T> tempnode=(BNode<T>)(this.host.nodes.elementAt(i));   
                            siblingEntry.addToVector(tempnode);   
                        }   
                        siblingEntry.setNext(this.host.next);   
                        this.host.setNext(null);   
                        nodeParent.inWhichEntry.delete(this.host.parent);   
                    }                          
                       
                }   
                else//if (siblingEntry.isLeaf()) not leaf    
                {   
                    if (parentNo==0)   
                    {   
                        siblingParentNode.setPoint(siblingEntry.firstNode.point);   
                        //siblingEntry.firstNode.setPoint(this.firstNode.point);    
                           
                        entryParent.delete(siblingParentNode);   
                        this.host.addToVector(siblingParentNode);   
                        for (int i=1;i<siblingEntry.nodes.size() ;i++ )   
                        {   
                            BNode<T> tempnode=(BNode<T>)(siblingEntry.nodes.elementAt(i));                               
                            this.host.addToVector(tempnode);   
                        }   
                    }   
                    else   
                    {   
                        this.host.parent.setPoint(this.host.firstNode.point);   
                        entryParent.delete(this.host.parent);   
                        siblingEntry.addToVector(this.host.parent);   
                        for (int i=1;i<this.host.nodes.size() ;i++ )   
                        {   
                            BNode<T> tempnode=(BNode<T>)(this.host.nodes.elementAt(i));   
                            siblingEntry.addToVector(tempnode);   
                        }   
                    }   
                       
                }   
            }   
            else//��    
            {   
                if(parentNo==0)//����    
                {   
                    if (this.host.isLeaf())//Ҷ�ӽڵ�,��ڶ���    
                    {   
                        BNode<T> firstnode=(BNode<T>)(siblingEntry.nodes.elementAt(1));//considing the node -1 ,actually firstnode is the 2nd node     
                        siblingEntry.delete(firstnode);   
                        BNode<T> second=(BNode<T>)(siblingEntry.nodes.elementAt(1));//it is actually the 3nd node last moment.    
                        this.host.addToVector(firstnode);   
                        siblingParentNode.index=second.index;   
                    }   
                    else   
                    {   
                        BNode<T> firstnode=(BNode<T>)(siblingEntry.nodes.elementAt(1));   
                        siblingEntry.delete(firstnode);   
                        //swap the firstNode.point with the firstnode.point    
                        BEntry temppoint=siblingEntry.firstNode.point;   
                        siblingEntry.firstNode.setPoint(firstnode.point);   
                        firstnode.setPoint(temppoint);   
                        //swap the firstnode.index with the siblingParentNode.index    
                        T temp=firstnode.index;   
                        firstnode.index=siblingParentNode.index;   
                        siblingParentNode.index=temp;   
   
                        this.host.addToVector(firstnode);   
                    }   
                }   
                else     
                {   
                    BNode<T> lastnode=(BNode<T>)(siblingEntry.nodes.lastElement());   
   
                    siblingEntry.delete(lastnode);   
                    this.host.addToVector(lastnode);   
                    //swap the index of lastnode with nodeParent    
                    T tempint=nodeParent.index;   
                    nodeParent.index=lastnode.index;   
                    lastnode.index=tempint;   
                    //swap lastnode.point with this.firstnode.point,because lastnode's point should be the first.point    
                    BEntry<T> temp=lastnode.point;   
                    lastnode.setPoint(this.host.firstNode.point);   
                    this.host.firstNode.setPoint(temp);   
                }   
            }   
               
        }   
    }   
   
    void insert(BNode node )   
    {   
        if (!host.isFull())   
        {   
            host.addToVector(node);   
            
        }   
        else   
        {   
        	if(host.isLeaf())
        	{
        	BNode parentNode = host.parent;
        	if(parentNode != null )
        	{
        		BEntry<T> parentEntry =parentNode.inWhichEntry;
        		int pos = parentEntry.nodes.indexOf(parentNode);
        		
        		//寻找右兄弟
        		if( pos != (parentEntry.nodes.size() -1) ) //该节点非最右边
        		{
        			BNode rightNode = (BNode)parentEntry.nodes.get(pos+1);
        			BEntry<T> rightEntry =rightNode.point;
        			//添加入右兄弟
        			if(!rightEntry.isFull())
        			{
        				//将新的节点放到此Entry中；
        				host.addToVector(node);
        				//将本节点的最后一个移动到右兄弟中；
        				BNode<T>  lastNode =host.removeLast();
        				//将父节点指向右兄弟节点的node的值更新的最小值
        				rightEntry.firstNode.index = lastNode.index;
        				rightEntry.addToVector(lastNode);
        				rightNode.index = lastNode.index;
        				return ;
        			}
        		}
        		//寻找左兄弟
        		if(pos > 0)  //该节点非最左边
        		{
        			BNode<T> leftNode = (BNode)parentEntry.nodes.get(pos-1);
        			BEntry<T> leftEntry =leftNode.point;
        			//添加入左兄弟
        			if(!leftEntry.isFull())
        			{  
        				host.addToVector(node);
        				//将本节点的第一个移动到左兄弟中；
        				BNode<T>  minNode =host.removeFirst();
        				BNode<T>  firstNode =host.removeFirst();
        				//将新的节点放到此Entry中；
        				leftEntry.addToVector(firstNode); 
        				minNode.index =  ((BNode<T>)host.nodes.get(0)).index;
        				//设置最小值
        				host.insertMin(minNode);
        				
        				//将父节点指向该节点的node的值更新的最小值
        				parentNode.index = minNode.index;
        				return ;
        			}
        		}        		
        	}
        	}
        
            host.addToVector(node);   
            BEntry<T> newEntry=new BEntry<T>(host.n,host.SMALL , host.count);   
               
            BNode<T> midNode=(BNode<T>)(host.nodes.elementAt(host.leafhalf));   
            int nodesize=host.nodes.size();   
            for (int i=host.leafhalf;i<nodesize ;i++ )   
            {   
                BNode<T> tempnode=(BNode<T>)host.nodes.elementAt(i);   
                newEntry.addToVector(tempnode);   
            }   
            for (int i=host.leafhalf;i<nodesize ;i++ )   
            {   
                host.nodes.remove(host.leafhalf);   
            }   
            if(host.isLeaf())   
            {   
                BNode<T> tempnode=new BNode<T>(midNode.index);   
               // this.lastNode().setPoint(newEntry);    
                tempnode.setInfor(midNode);
                midNode=tempnode;  
                host.link2(newEntry);   
                newEntry.firstNode.index =  midNode.index;
                newEntry.firstNode.setInfor(midNode);
            }   

               
            midNode.setPoint(newEntry);   
            if (host.isRoot)   
                host.createNewRootFor(midNode);   
            else   
            {    
            	//如果该节点和
                host.parent.inWhichEntry.insert(midNode, host.parent);   
            }   
           
        }   
    }   
    
    
    //肯定不会是子节点，是分裂时采用到的函数
    void insert(BNode node ,BNode before )   
    {
        if (!host.isFull())   
        {   
            host.addToVector(node,before);   
            
        }   
        else   
        {           
            host.addToVector(node ,before);   
            BEntry<T> newEntry=new BEntry<T>(host.n,host.SMALL , host.count);   
            BNode<T> midNode=(BNode<T>)(host.nodes.elementAt(host.leafhalf));   
            int nodesize=host.nodes.size();   
            for (int i=host.leafhalf;i<nodesize ;i++ )   
            {   
                BNode<T> tempnode=(BNode<T>)host.nodes.elementAt(i);   
                newEntry.addToVector(tempnode);   
            }   
            for (int i=host.leafhalf;i<nodesize ;i++ )   
            {   
                host.nodes.remove(host.leafhalf);   
            }   

            	newEntry.firstNode.index = midNode.index;
                newEntry.firstNode.setPoint(midNode.point);             
               newEntry.nodes.remove(midNode);   
  
            midNode.setPoint(newEntry);   
            if (host.isRoot)   
                host.createNewRootFor(midNode);   
            else   
            {    
            	//如果该节点和
                host.parent.inWhichEntry.insert(midNode, host.parent);   
            }   
        }
    }
    
    public static void main(String[] args)    
    {   
        System.out.println("Hello World!");   
    }   

}
