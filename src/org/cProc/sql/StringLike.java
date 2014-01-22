package org.cProc.sql;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * String 的like功能的sql转化
 * like的正常写法为  %value      value%         %value%    va%lue
 * @author zxc
 * 
 */
public class StringLike {

	private static final String LIKE_STR_1="%";
	
	
	private static final String LIKE_STR_2="'";
	
	
	public static final int OP_CODE_START_OR_MID_END = 1;
	
	public static final int OP_CODE_MID = 2;
	
	public static final int OP_CODE_EQUAL = 0;
	
	
	
	public  boolean getValue(String src,String value) {
		
		value = StringUtils.substringBetween(value, LIKE_STR_2);
		int count=  StringUtils.countMatches(value, LIKE_STR_1);
		if(count==OP_CODE_START_OR_MID_END)
		{
			if(StringUtils.startsWith(value, LIKE_STR_1))
			{//%开头 
				
				return StringUtils.endsWith(src, StringUtils.substring(value, 1));
			}else if(StringUtils.endsWith(value, LIKE_STR_1))
			{//%结尾 
				
				return StringUtils.startsWith(src, StringUtils.substring(value, 0, value.length()-1));
			}else
			{//mid
				String [] arr = StringUtils.split(LIKE_STR_1);
				
				return src.startsWith(arr[0]) && src.endsWith(arr[1]);
				
				
			}
			
		}else if(count==OP_CODE_MID)
		{
			return StringUtils.contains(src, StringUtils.substringBetween(value, LIKE_STR_1));
			
		}else if(count==OP_CODE_EQUAL)
		{
			return StringUtils.equals(src, value);
			
			
		}else// match
		{
			
			
		}
		
		
		
		return false;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StringLike like = new StringLike();
		long l = System.currentTimeMillis();
		
		for(int i=0;i<=10000000;i++)
			like.getValue("www.baidu.com","'%wwww.baidu.com'");
		
		System.out.println(System.currentTimeMillis()-l);
		System.out.println(StringUtils.substring("dshdsh1%", 0, "dshdsh1%".length()-1));
		
	}


}
