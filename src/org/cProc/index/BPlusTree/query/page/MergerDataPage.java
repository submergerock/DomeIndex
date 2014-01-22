package org.cProc.index.BPlusTree.query.page;

import java.util.ArrayList;

public class MergerDataPage <Data>{
	
	
	ArrayList<Data> list  = new ArrayList<Data>();
	public ArrayList<Integer> fileNumberList  = new ArrayList<Integer>();
	ArrayList<Long> offsetList  = new ArrayList<Long>();
	private long offset = 0;
	public MergerDataPage(long offset)
	{
		this.offset = offset;
	}
	public void addOneData(Data  data)
	{
		list.add(data);
	}
	
	public long getPageOffset()
	{
		return offset;
	}
	
	public int getFileNumberOfDataPos(int pos)
	{
		return fileNumberList.get(pos);
	}
	public long getFileOffsetOfDataPos(int pos)
	{
		return offsetList.get(pos);
	}
	
	
	public void addOneDataFileNumber(int fileNumber)
	{
		fileNumberList.add(fileNumber);
	}
	
	public void addOneDataOffset(long  offset)
	{
		offsetList.add(offset);
	}
	
	public ArrayList<Data>  getDataList()
	{
		return list;
	}
	
	public ArrayList<Integer>  getFileNumberList()
	{
		return fileNumberList;
	}
	
	public ArrayList<Long>  getOffsetList()
	{
		return offsetList;
	}
}
