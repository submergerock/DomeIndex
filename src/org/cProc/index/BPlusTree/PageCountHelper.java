package org.cProc.index.BPlusTree;

public class PageCountHelper {
	
	int  count = 0 ;
	public PageCountHelper()
	{
		
	}
	
	synchronized int getNumber()
	{
		return ++count;
	}


}
