package org.cProc.index.BPlusTree.query.page;

import org.cProc.index.BPlusTree.Data.Data;

public class Page <T extends Data> {

	private int  pageNumber = -1;
	private int nextPage = -1;
	private  T template = null;
	
	public Page()
	{
		
	}
	
//	public int getType()
//	{
//		return 0 ;
//	}
//	
	public void   setTemplate(T   template)
	{
		this.template = template;
	}
	
	public void setPageNumber(int number)
	{
		this.pageNumber = number;
	}
	
	public int getPageNumber()
	{
		return pageNumber;
	}
	
	public  Data getTemplate()
	{
		return template.getInstance();
	}
	
	public  boolean isLeaf()
	{
		return false;
	}
}
