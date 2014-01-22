package org.cProc.index.BPlusTree.Data;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public  abstract class Data implements Comparable , CProWritable{

	public  abstract void read(ByteBuffer buffer);
	public   abstract void write(ByteBuffer buffer);
	 public  abstract int compareTo(Object o) ;
	 public  abstract Data getInstance() ;
	  public abstract byte[] getIndexFiled(int pos);
	  public abstract int getByteSize();
	  public abstract ArrayList<byte[]> getArrayListData();
	  
	  public  abstract ReadWriteIndexDataProxy getIndexProxy();
	  public  abstract byte[] getFileValue(int i);
	

}
