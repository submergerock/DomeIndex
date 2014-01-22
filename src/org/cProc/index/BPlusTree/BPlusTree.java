package org.cProc.index.BPlusTree;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.cProc.GernralRead.ReadData.DataType;
import org.cProc.GernralRead.ReadData.ParseIndexFormat;
import org.cProc.index.BPlusTree.Data.Data;
import org.cProc.index.BPlusTree.Data.MulitFiledData;
import org.cProc.index.BPlusTree.Data.ReadWriteIndexDataProxy;
import org.cProc.tool.Bytes;




//该B+树的叶子节点的第一个节点为最小值，其偏移量没有任何意义
public class BPlusTree<T extends Data> {
	private int  diskSize = 0;
    public int n;   
    BEntry<T> root;   
    static boolean DEBUG=false;   
    static boolean DEBUG2=true;   
    HashMap<String,Integer>  fileMap = null;
	int fileNumber = 0;
	
	private ArrayList<byte []> minDataT = null;
	private ArrayList<byte []> maxDataT = null;
	
	
    public int getFileNumberCount()
    {
    	return fileNumber;
    }
	
    public BPlusTree(int n , T minValue )   
    {   
        this.n=n;   
        root=new BEntry<T>(n , minValue,new PageCountHelper());   
        root.addRootListener(this);   
        root.isRoot=true;   
        ArrayList<byte[]> list =  minValue.getArrayListData();
        int indexSize =0;
        for(int i = 0 ; i < list.size() ; i++)
        {
        	indexSize = indexSize+list.get(i).length;
        }
        diskSize = getSuitablePageSize(indexSize, n);
    }   
    public  void insert(T index ,int fileNumber , long offset)   
    {   
        BNode<T> node=findNode(index);   
        
        if (node==null)   
        {   
        
            BEntry<T> entry=findEntry(index);   
            
            BNode<T> newNode=new BNode<T>(index);  
           
            newNode.setOffset(fileNumber, offset);
           
            entry.insert(newNode);   
           
        }          
    }   
    
    void delete(T index)   
    {   
        BNode<T> node=findNode(index);   
        if (node!=null)   
        {   
            BEntry entry=node.inWhichEntry;   
            //System.out.println("   "+node.inWhichEntry);    
            entry.delete(node);   
        }   
    }   
   
    
    public void writeFiles(HashMap<String,Integer> fileMap)
    {
    	this.fileMap = fileMap;
    	if(this.fileMap != null )
    	{
    		fileNumber = fileMap.entrySet().size();
    	}
   
    }
    
    public void changePageNumber()
    {
    	System.out.println("start change number*********");
    	 int number = 1;
    	 int parentNumber = 0;
    	 
    	 //this list is used for queue,FIFO;
    	 ArrayList<BEntry<T>> list = new  ArrayList<BEntry<T>>();
		 root.setPageNumber(number);
		 number++;
    	 list.add(this.root);
 
    	 while(!list.isEmpty())
    	 {
    		 BEntry<T> entry = list.get(0);
    		 parentNumber = entry.pageNumber;
    		 for( int i = 0 ; i < entry.nodes.size() ; i++)
    		 {	
    			if( ( (BNode<T>)entry.nodes.get(i)).point  != null)
    			{	
    				(( (BNode<T>)entry.nodes.get(i)).point).setPageNumber(number);
    				 number++;
    				list.add( ( (BNode<T>)entry.nodes.get(i)).point);
    			}
    		 }
    		 list.remove(0);
    	 }
    	
    }
    
    
    public void setMinValue(ArrayList<byte []> data)
    {
    	minDataT = data;
    }
    
    public void setMaxValue(ArrayList<byte []> data)
    {
    	maxDataT = data;
    }
    
    //为了合并BplusTree,写树的非递归调用，广度遍历
    public void writeToFile(FileChannel fc , long startPos ,int pageSize)
    {
    	this.changePageNumber();
    	ArrayList<BEntry<T> > queueArrayList = new ArrayList<BEntry<T> >();
    	queueArrayList.add(root);
    	int pageCount = 0;
    	while(!queueArrayList.isEmpty())
    	{
    		BEntry<T> currentEntry = queueArrayList.remove(0);
    		for(int i = 0 ; i < currentEntry.nodes.size() ; i++)
    		{
    			if( ((BNode<T>)currentEntry.nodes.get(i)).point != null)
    			{
    				queueArrayList.add( ((BNode<T>)currentEntry.nodes.get(i)).point );
    			}
    		}
    		try {
				currentEntry.writeNORecur(fc, startPos+pageCount*pageSize, pageSize);
				pageCount++;
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}

    }
       
    public void writeToFile(String name)
    {
    	this.changePageNumber();
    	try {
    		File file = new File(name);
        	if(!file.exists())
        	{
        		file.createNewFile();
        	}
        	DataInputStream input = new DataInputStream(new FileInputStream(file));
            FileChannel fc = new RandomAccessFile(name, "rw").getChannel();  
            
            
            //写信息头Page
            ByteBuffer temp = ByteBuffer.allocate (BTreeType.headPage);  //1024���ֽ�
            temp.put(Bytes.toBytes(BTreeType.BPlusTreeType));      //写入树的类型
            temp.put(Bytes.toBytes((long)BTreeType.headPage));   //写入树的起始位置
            temp.put(Bytes.toBytes(this.root.pageNumber));           //写入根的编号
            System.out.println("*************************************pageSize is " +diskSize);
            temp.put(Bytes.toBytes(diskSize));           							//页的大小
            for(int i = 0 ; i < BTreeType.headOtherInfor ; i++)
            {
            	temp.put((byte)0);
            }
            //写入文件的影射
            temp.put(Bytes.toBytes(fileNumber));                             
          
            if(fileNumber != 0)
            {
                Iterator<Entry<String, Integer>>  iter= fileMap.entrySet().iterator();
                while(iter.hasNext())
                {
                	Entry<String, Integer> entry = iter.next();
                	String key 	   = entry.getKey();
                	Integer value = entry.getValue();
                	 temp.put(Bytes.toBytes(key.length()));
                	 temp.put(Bytes.toBytes(key));
                	 temp.put(Bytes.toBytes(value));
                	
                }
            }
            
          //写入最小值
            if(minDataT != null)
            {
                temp.put(Bytes.toBytes(minDataT.size()));
                for( int i = 0 ; i < minDataT.size() ; i++)
                {		
                		temp.put(Bytes.toBytes(minDataT.get(i).length));
                		temp.put(minDataT.get(i));
                }
            }
            else {
            	  temp.put(Bytes.toBytes(0));
			}
            
            //写入最大值
            if(maxDataT != null)
            {
            	   temp.put(Bytes.toBytes(maxDataT.size()));
                   for( int i = 0 ; i < maxDataT.size() ; i++)
                   {
                	   temp.put(Bytes.toBytes(maxDataT.get(i).length));
                   		temp.put(maxDataT.get(i));
                   }
            }
            else {
            	  temp.put(Bytes.toBytes(0));
			}
         

            temp.flip();
            fc.write(temp, 0);
            
            //写BplusTree
            long bplussPageSizeStart = BTreeType.headPage;
            System.out.println("diskSize is " +diskSize);
            writeToFile(fc,bplussPageSizeStart,diskSize);
          //  root.write(fc); 

            fc.close();
            temp = null;
		} catch ( Exception e) {
			e.printStackTrace();
		}
    }
    
    public void print()   
    {   
        root.print(0);   
    }   
    BNode<T> findNode(T index)   
    { 
        return (root.findEntry(index)).findNode(index);   
    }   
    BEntry<T> findEntry(T index)   
    {   
        return root.findEntry(index);   
    }   
       
    void RootChanged(BEntry<T> entry)   
    {   
        //System.out.println("root"+root.info());    
        //System.out.println();    
        this.root=entry;   
        //System.out.println("new root is"+root.info());    
        entry.isRoot=true;   
        entry.addRootListener(this);   
    }   
    String printdebug()   
    {   
        StringBuffer sb=new StringBuffer();   
        root.printdebug(0,sb);   
        return sb.toString();   
    }   
    void travel()   
    {   
        this.print();   
        System.out.println("++++++++++++++++++++++++");   
        for (BEntry entry=root.leftMost();entry!=null;entry=entry.next )   
        {   
            entry.print(0);   
        }   
        System.out.println("-------------------------------------------------");   
    }   
//    void travel2()   
//    {   
//        this.print();   
//        System.out.println("++++++++++++++++++++++++");   
//        for (BEntry entry=root.rightMost();entry!=null;entry=entry.previous )   
//        {   
//            entry.print(0);   
//        }   
//        System.out.println("-------------------------------------------------");   
//    }   
    
    
    private  int getSuitablePageSize(int Tsize ,int m)
    {
    	 //1 : 是否是叶子  4 : 上一个   4： 下一个   4：总共有多少个；
    	int byteSize = 1 ;
    	int intSize = 4 ;
    	int longSize = 8;
    	int addMore = 8;
    	int nodeMaxSize = (Tsize+intSize+12+8)*m+byteSize+intSize+intSize+intSize;
    	int pageSize = (((nodeMaxSize/1024)+1))*1024;
    	return   pageSize;
    }
    
    public static void main(String[] args)    
    {   
    	
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
		ArrayList<byte []> dataList = new ArrayList<byte []>();
		byte a = 0 ;
//		dataList.add(Bytes.toBytes(Integer.MIN_VALUE));
//		dataList.add(Bytes.toBytes(Long.MIN_VALUE));
//		dataList.add(Bytes.toBytes(Long.MIN_VALUE));
		dataList.add(DataType.getUnsginedMinBytesReturnNot8Byte(Byte.class));
//		dataList.add(Bytes.toBytes( Byte.MIN_VALUE));
//		dataList.add(Bytes.toBytes(Integer.MIN_VALUE));
//		dataList.add(Bytes.toBytes(Long.MIN_VALUE));
//		dataList.add(Bytes.toBytes(Long.MIN_VALUE));
//		
		int  m = 301;
        BPlusTree<MulitFiledData> b=new BPlusTree<MulitFiledData>(m, new MulitFiledData(dataList,proxy));   
   
/*  
        b.insert(10);  
        if (DEBUG)b.travel();  
        b.insert(50);  
        if (DEBUG)b.travel();  
        b.insert(20);  
        if (DEBUG)b.travel();  
        b.insert(40);  
        if (DEBUG)b.travel();  
        b.insert(100);  
        if (DEBUG)b.travel();  
        b.insert(120);  
        if (DEBUG)b.travel();  
        b.insert(150);  
        if (DEBUG)b.travel();  
  
        b.delete(120);  
        if (DEBUG2)b.travel();  
        b.delete(100);  
        if (DEBUG2)b.travel();  
        b.delete(20);  
        if (DEBUG2)b.travel();  
        b.delete(40);  
        if (DEBUG2)b.travel();  
        b.delete(150);  
        if (DEBUG2)b.travel();  
        b.delete(50);  
        if (DEBUG2)b.travel();*/   
          long startTime =  System.currentTimeMillis();
          int count = 0;
          int number = 0;
        for(int j=0;j<2;j++){   
            for (int i=0;i<100;i++ )   
            {   
                int x=  (int)(Math.random()*200)+100;   
//                System.out.println("adding "+x+" ");   
                if( x >=211 && x <= 254)
                {
                	count++;
                }
                ArrayList<byte []> tempDataList = new ArrayList<byte []>();
//                tempDataList.add(Bytes.toBytes(x));
//                tempDataList.add(Bytes.toBytes((long)x));
//                tempDataList.add(Bytes.toBytes((long)x));
                tempDataList.add(Bytes.toBytes((byte)x));
//                tempDataList.add(Bytes.toBytes((byte)x));
//                tempDataList.add(Bytes.toBytes(x));
//                tempDataList.add(Bytes.toBytes((long)x));
//                tempDataList.add(Bytes.toBytes((long)x));
                
                if(number == 23)
                {
                	  System.out.println(" number is " + number);
                }
                b.insert(new MulitFiledData(tempDataList,proxy),1,x);   
                number++;
//                System.out.println(" number is " + number);
//                b.print();   
//                if(number%2 == 0)
//                {
//                	b.print();   
//					number++;
//                }
 //               //b.travel();       
            }          
//            for (int i=0;i<20 ;i++ )   
//            {   
//                int x=(int)(Math.random()*30);   
//                System.out.println("deleting "+x);   
//                b.delete(new MulitFiledData(x,i+""+x));   
//                b.print();     
 //           }                 
        }       
        long endTime =  System.currentTimeMillis();
        System.out.println("****************count number is " + count);
     
        HashMap<String,Integer> fileMap = new  HashMap<String,Integer>();
        fileMap.put("2123", 1);
        fileMap.put("2456", 2);
        b.writeFiles(fileMap);
        
        long writestartTime =  System.currentTimeMillis();
        b.writeToFile("C:\\dingli\\test\\btreedata1"+BTreeType.orgName);
        b.print();    
        long writeendTime =  System.currentTimeMillis();
        System.out.println("create tree time is " + (endTime -  startTime));
        System.out.println("write tree time is " + (writeendTime -  writestartTime));
        System.out.println("****************count number is " + count);
    }   

}
