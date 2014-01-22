package org.cProc.index.BPlusTree.query;

import java.util.ArrayList;
import java.util.HashMap;

import org.cProc.index.BPlusTree.Data.Data;
import org.cProc.sql.IndexFilter;


public interface QueryInterface <T extends Data>{

    public  final String  fileNameAndOffsetSeprater="#";
    public  final  String OffsetAndOffsetSeprater=",";

	public abstract void setTemplate( T  template);

	public   abstract T getTemplate( );
	public   abstract String  getIndexFile();
	
	//遍历所有值
	public abstract void startFindSeqDataOfAll();
	public  abstract T getNextData();
	public  abstract int getCurrentFileNumber();
	public  abstract long getCurrentOffset();
	public abstract boolean dataIsOver();
	
	
	//查询范围
	public abstract boolean findRangeData(T startIndex , T endIndex);
	
	//获取查询后的信息
	public abstract int getReslutSize();   //获取结果的总数
	public abstract ArrayList<T> getIndexReuslt();  					 //获取结果的Index值
	public abstract ArrayList<Integer> getFileNumberReuslt();  //获取结果的文件编号
	public abstract ArrayList<Long> getOffsetReuslt();				//获取结果的偏移量
	public abstract void  printReslut();											//打印结果
	public abstract String getFileName(int fileNumber);       //将文件编号转化成文件名；
	//format: fileName#offset,offset,offset
	public abstract ArrayList<String> getSortFileOffsetResult(); //将结果集排序
	
	//最大值与最小值
	public  abstract ArrayList<byte []> getMaxData();
	public   abstract ArrayList<byte []> getMinData();
	
	//过滤索引
	public abstract void setIndexFilter(IndexFilter<T > indexFilter);
	
	//获取文件名以及编号的对应信息
	public  abstract  HashMap<Integer,String>   getFileMap();
	
	public abstract void queryClose(); 
//    //从0开始
//    public  abstract long getBPlusTreePagePos( int number);
//    
//    public abstract  long getBPlusTreeHeadPos();
//    
//    //设置BPlusTree在文件里的存放位置,可以从0开始,也可以从任意位置开始
//    public abstract void setBplusTreeStartPos(long pos);
	
}
