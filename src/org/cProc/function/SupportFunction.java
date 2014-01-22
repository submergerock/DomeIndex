package org.cProc.function;

import java.util.ArrayList;
import java.util.HashMap;

import org.cProc.tool.Bytes;

import com.sun.org.apache.bcel.internal.util.ByteSequence;

public class SupportFunction {
	/** 
	 * @author yangzhenyu
	 * @date 2012-5-8
	 * @version v1.0
	 * @TODO 
	 */
	// 均为有符号数
	//toInt  
	//toLong
	//toShort
	//toByte
	public  static HashMap<String, Class> functionReturnValueMap = new HashMap<String, Class>();
	static{
		functionReturnValueMap.put("STRINGTOLONG", Long.class);
		functionReturnValueMap.put("STRINGTOINT", Integer.class);
		functionReturnValueMap.put("LENGTH", Long.class);
	}
	
	public static final  String StringtoLong ="STRINGTOLONG";
	public static final String StringtoInt ="STRINGTOINT";
	public static final String length ="LENGTH";

	//for query
	//if  f(a,0,1) ,then funtionPara contain (a, 0 , 1);
	public  static byte[] functionCall(String functionName ,ArrayList<String> paras ,byte[] value)
	{
		if(functionName.equals(StringtoLong) )
		{
			return Bytes.toBytes(StringtoLong(Bytes.toString(value) , Integer.parseInt(paras.get(1)),  Integer.parseInt(paras.get(2))));
		}
		if(functionName.equals(length))
		{
			return Bytes.toBytes(length(Bytes.toString(value)));
		}
		return null;
	}
	
	//for create index 
	//if  f(a,0,1) ,then funtionPara contain (a , 0 , 1);
	public static  byte[]  supportFunction(String funtionName ,ArrayList<String> funtionPara, byte[] value , Class inputType , Class outputType)
	{
		if(funtionName.equals(StringtoLong))
		{
			return  Bytes.toBytes(StringtoLong(Bytes.toString(value),  
									Integer.parseInt(funtionPara.get(1)),  
									Integer.parseInt(funtionPara.get(2)))
									);
		}
		if(funtionName.equals(length))
		{
			return Bytes.toBytes(length(Bytes.toString(value)));
		}
		return null;
	}
	
	public static  byte[]  supportFunction(String funtionName ,ArrayList<String> funtionPara, ArrayList<byte[]> valueArray , 
																	ArrayList<Class> inputTypeArray , Class outputType)
	{
		return null;
	}
	
	
	private static long length(String value)
	{
	
		return value.length();
	}
	
	//start from 1;
	private static long StringtoLong(String value, int from ,int length)
	{

		String stringReslut = value.substring(from-1, from+length-1);
	try {
		long returnValue = Long.parseLong(stringReslut);
		stringReslut = null;
		
		return returnValue;
	} catch (Exception e) {
		int pos = 0 ;
		for(int i = 0 ; i < stringReslut.length() ; i++)
		{
			if(stringReslut.charAt(i) == 0)
			{
				pos++;
			}
		}
		long returnValue = 0;
		try {
			 returnValue = Long.parseLong(stringReslut.substring(pos,stringReslut.length()));
		} catch (Exception e2) {
			return Long.MAX_VALUE;
		}
		return returnValue;
		// TODO: handle exception
	}	
	
	}
	
	public static void main(String[] args)
	{
		System.out.println(StringtoLong("123123123",6,4));
	}
	
	
	
	
}
