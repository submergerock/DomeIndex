package org.cProc.index.BPlusTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.cProc.GernralRead.ReadData.DataType;
import org.cProc.GernralRead.ReadData.ParseTableFormat;
import org.cProc.tool.Bytes;


//这部分是BPlusTree与业务表的结合处。 
 //通过输入SQL解析出来的最小范围和最大范围与BPlusTree里存放的最小数据（完整的数据）和最大数据比较（完整的数据）
//每个字段满足这样的情况则返回true:
// 条件范围与最大最小范围有交集
// 判断方法：    (最小<=条件范围的下界 <= 最大  or  最下 <= 条件范围的下界 <= 最大)
//						 or ( 条件范围的下界 <= 最小 and 条件范围的上界  >= 最大)
//，如果为false，则函数返回false
public class BPlusTreeMaxAndMinCompare {
	
	public static boolean BPlusTreeMaxAndMinCompare( ArrayList<byte[]> maxList, ArrayList<byte[]> minList ,
															HashMap<String, byte[]> condtionMax,HashMap<String, byte[]> condtionMin,
															ParseTableFormat tableFormat)
	{
//		System.out.println("maxListSize is " + maxList.size() );
			if(maxList.size() == 0)
			{
				return true;
			}
		   Iterator<Entry<String, byte[] >>  maxIterator = condtionMin.entrySet().iterator();
		    while(maxIterator.hasNext())
		    {
		    	Entry<String, byte[] > data = maxIterator.next();
		    	String columnName = data.getKey();
		    	Class typeClass = tableFormat.getColumnType(columnName);
		    	byte[] dataMaxValue =   maxList.get( tableFormat.getColunmPos(columnName));
		    	byte[] dataMinValue =   minList.get( tableFormat.getColunmPos(columnName));
		    	
		    	byte[] condtionMaxVale = condtionMax.get(columnName);
		    	byte [] condtionMinVale = condtionMin.get(columnName);
		    	
//		    	System.out.println("colunmnName is " + columnName);
//		    	if(columnName.equals("city_code"))
//		    	{
//		    		
//		    		continue;
//		    		
//		    	}
		    	
		    	//当最小值为异常值时，即最小值为：-1,-1,-1...时
		    	int count = 0 ;
		    	for(int i = 0 ; i <dataMinValue.length ; i++)
		    	{
		    		if(dataMinValue[i] != -1)
		    		{	
		    			break;
		    		}
		    		count++;
		    	}
		    	if(count == dataMinValue.length)
		    	{
		    		continue;
		    	}
		    	//如果模糊索引的最大值和最小值有异常。这里仅仅处理最小值比最大值大的情况；
		    	if(DataType.compare(typeClass, dataMinValue, dataMaxValue) > 0)
		    	{
		    		continue;
		    	}
		    	
		    	// 判断方法：    (最小<=条件范围的下界 <= 最大  or  最下 <= 条件范围的下界 <= 最大) 
		    	//						 and ( 条件范围的下界 <= 最小 and 条件范围的上界  >= 最大)
		    	//，如果为false，则函数返回false
		    	
				if (!( // !
				((DataType.compare(typeClass, condtionMinVale, dataMinValue) >= 0 && DataType
						.compare(typeClass, condtionMinVale, dataMaxValue) <= 0) || (DataType
						.compare(typeClass, condtionMaxVale, dataMinValue) >= 0 && DataType
						.compare(typeClass, condtionMaxVale, dataMaxValue) <= 0)) // or1
				|| (DataType.compare(typeClass, dataMinValue, condtionMinVale) >= 0 && DataType
						.compare(typeClass, condtionMaxVale, dataMaxValue) >= 0)) // !
				)
		    	{	
		    		return false;
		    	}
		    }
			return true;
		}
}
