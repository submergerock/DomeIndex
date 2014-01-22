package org.cProc.tool;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TimeUtils {

	
	
	
	
	
	public synchronized static String getCurrentTimePath()
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		return format.format(new Date())+"_"+java.util.UUID.randomUUID().toString();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> list   = new ArrayList<String>();
		list.add("111");
		list.add("222");
		list.add("333");
		
		for(String str:list)
		{
			if(str.equals("222"))
				list.remove("222");
		}
		System.out.println(list);
	}

}
