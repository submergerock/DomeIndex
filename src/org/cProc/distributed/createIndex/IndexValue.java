package org.cProc.distributed.createIndex;

import org.cProc.index.BPlusTree.Data.MulitFiledData;



public class IndexValue implements java.lang.Comparable<IndexValue>{

	private long offset;
	private int fileNumber;
	private MulitFiledData data = null;
	
	
	public IndexValue(MulitFiledData data,int fileNumber,long offset)
	{
		this.data = data;
		this.fileNumber = fileNumber;
		this.offset   = offset;
	}
	
	
	
	
	
	@Override
	public int compareTo(IndexValue o) {
		if(o.getClass()!=this.getClass())
			return -100;
		 MulitFiledData otherData =(MulitFiledData)o.getData();
		 return  this.data.getIndexProxy().compare(this.data.getArrayListData(), otherData.getArrayListData()) ;

	}





	public long getOffset() {
		return offset;
	}





	public void setOffset(long offset) {
		this.offset = offset;
	}





	public int getFileNumber() {
		return fileNumber;
	}





	public void setFileNumber(int fileNumber) {
		this.fileNumber = fileNumber;
	}





	public MulitFiledData getData() {
		return data;
	}





	public void setData(MulitFiledData data) {
		this.data = data;
	}
	
	
	
	

}
