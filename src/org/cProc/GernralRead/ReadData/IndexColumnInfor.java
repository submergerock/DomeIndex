package org.cProc.GernralRead.ReadData;

import java.sql.SQLClientInfoException;
import java.util.ArrayList;

public class IndexColumnInfor {
	/** 
	 * @author yangzhenyu
	 * @date 2012-5-8
	 * @version v1.0
	 * @TODO 
	 */
	private boolean isFunction = false;
	private String columnName ="";
	private String functionName ="";
	private  ArrayList<String> functionPara = null; 
	private Class  columnType=null;
	
	private static final String RPARENT =")";
	private static final String LPARENT ="(";
	private static final String COMMER=",";
	
	public IndexColumnInfor(String columnString,  Class columnType)
	{
		if(columnString.endsWith(RPARENT))
		{
			isFunction = true;
			functionPara = new ArrayList<String>();
			int lparentPos = columnString.indexOf(LPARENT);
			functionName =columnString.substring(0,lparentPos);
			String parmaList = columnString.substring(lparentPos+1 ,columnString.length()-1);
			String[] parma = parmaList.split(COMMER);
			this.columnName = parma[0];
			for(int i = 0 ; i < parma.length ; i++)
			{
				functionPara.add(parma[i]);
			}
			
		}
		else {
			this.isFunction = false;
			this.columnName = columnString;
		}
		this.columnType = columnType;
		
	}
	
	public boolean isFunction()
	{
		return isFunction;
	}
	
	public String getColumnName()
	{
		return columnName;
	}
	
	public String getFunctionName()
	{
		return functionName;
	}
	
	public ArrayList<String> getFunctionPara()
	{
		return functionPara;
	}
	
	public Class getFunctionReturnType()
	{
		return columnType;
	}
	
	
	public void printColumnInfor()
	{
		if(isFunction)
		{
			System.out.println(functionName);
			for(int i = 0 ; i < functionPara.size() ; i ++)
			{
				System.out.println(functionPara.get(i));
				
			}
			System.out.println("******"+columnName);
		}
		else {
			System.out.println(columnName);
		}
	}
	
	
	public static void main(String[] args)
	{
		String s1 ="Length(called_number)";
		String s2 ="start_time_s";
		IndexColumnInfor c1 = new IndexColumnInfor(s1, null);
		c1.printColumnInfor();
		System.out.println("***************************");
		IndexColumnInfor c2 = new IndexColumnInfor(s2, null);
		c2.printColumnInfor();

	
	}

	
	
}
