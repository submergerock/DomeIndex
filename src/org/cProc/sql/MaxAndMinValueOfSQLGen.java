package org.cProc.sql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.cProc.GernralRead.ReadData.DataType;
import org.cProc.GernralRead.ReadData.ParseIndexFormat;
import org.cProc.GernralRead.ReadData.ParseTableFormat;
import org.cProc.GernralRead.TableAndIndexTool.TableAndIndexTool;
import org.cProc.function.SupportFunction;
import org.cProc.index.BPlusTree.Data.MulitFiledData;
import org.cProc.index.BPlusTree.Data.ReadWriteIndexDataProxy;
import org.cProc.tool.Bytes;


import com.sun.corba.se.impl.orbutil.graph.Node;


/**
 * 类名: MaxAndMinValueOfSQLGen
 * 
 * @Comments     该类作用： 1.根据指定的SQL，通过计算条件，得到查询的范围。
 * 例如：    A > 0 and A <10 or( A>-100 and A < 2)  得到的结果范围为：  A > -100 and A < 10
 * 								
 * @author gyy 
 * @date 2011.11.08
 * @Version: 版本1.0
 */
public class MaxAndMinValueOfSQLGen {
	
	
	
	public static final Log LOG = LogFactory.getLog(MaxAndMinValueOfSQLGen.class.getName());
	
	
	
    CreateTestLexer lexer =  null;
    CommonTokenStream tokens = null;
    CreateTestParser parser = null;
    CommonTree tree = null;
    ArrayList<String> showColumn = new ArrayList<String>();
    private  ParseTableFormat tableFormat = null;
    private static final String typeAndValueSeprator = "@#@#@#@#-_-!";
    private boolean filter = false;
    private HashMap<String, String> maxValue = new HashMap<String, String>();
    private  HashMap<String, String> minValue = new HashMap<String, String>();
    private final int TYPEPOS = 1;
    private final int VALUEPOS = 0;
    private Set condtionColumnSet = new TreeSet<String>();
    private HashMap<String, byte[]> conditonMaxBytes = new HashMap<String, byte[]>();
    private  HashMap<String, byte[]> conditonMinBytes  = new HashMap<String, byte[]>();
    private boolean haveUnsignedMax = false;


    
    private IndexChooser indexChooser = null;
    
    private void InitTableFormat(String tableName, String appName,FileSystem fs )
    {
    		String tableFormatFile = TableAndIndexTool.getTableFormat(tableName, appName);
    		tableFormat  = new ParseTableFormat(tableFormatFile,fs);
    }
    
    public ParseTableFormat getParseTableFormat()
    {
    	return tableFormat;
    }
    
    public boolean getHaveUnsignedMax()
    {
    	return haveUnsignedMax;
    }

    
    public MaxAndMinValueOfSQLGen(String sql, FileSystem fs)
    {
    	indexChooser = new IndexChooser(sql);
        lexer = new CreateTestLexer(new ANTLRStringStream(sql));//"select start_time_s  from M where start_time_s  in (4, 6, 7) "));//4^2*2/4-(6+2)+4
        tokens = new CommonTokenStream(lexer);
        parser = new CreateTestParser(tokens);
        try {
			this.tree = parser.statement().tree;
//			if(LOG.isDebugEnabled())
//					LOG.debug("grammer tree is " + tree.toStringTree());
			 String tableName = tree.getChild(0).getChild(0).getText();
			 String appName = tree.getChild(1).getChild(0).getText();
//	    	 rp= new RowParse(tableName);
	    	 for(int i = 0 ; i < tree.getChild(2).getChildCount() ; i++)
	    	 {
	    		 showColumn.add(tree.getChild(2).getChild(i).getText());
	    	 }
	    	 if(tree.getChildCount() > 3)
	    	 {
	    		 if(tree.getChild(3).getType() == CreateTestParser.TOK_ORCONDITION)
	    		 {
	    			 filter = true;
	    		 }
	    	 }
	    	 InitTableFormat(tableName, appName,fs);
			 genMaxAndMinValueOfSQL();
			 haveUnsignedMax();
			// PrintMaxAndMin(maxValue,minValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void ShowCloumn()
    {
    	for(int i = 0 ; i  < showColumn.size(); i++)
    	{
    		LOG.info("showColumn is  " + showColumn.get(i));
    	}
    }
    
    //检查是否存在unsigned max的值，
     //例如：byte： 255  ： 仅仅处理==的情况    
    private void haveUnsignedMax()
    {
    	try {
			haveUnsignedMax  = haveUnsignedMax((CommonTree)tree.getChild(3));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private boolean haveUnsignedMax(CommonTree tree) throws Exception
    {
		   boolean tempReslut = false;
		   
		   
		   
		   switch(tree.getToken().getType())
		   {
		   case CreateTestParser.TOK_ORCONDITION:
			   //or的子节点中存在一个真值，则这个值为真.
			   //这个真值的产生是因为：条件里包含一个非索引字段的列；
			   	for(int i = 0 ; i <  tree.getChildCount(); i++)
			   	{	
			   		tempReslut = haveUnsignedMax((CommonTree)tree.getChild(i));
			   		if( tempReslut )
			   		{
			   			return true;
			   		}
			   	}
		   		return false;
		   case CreateTestParser.TOK_ANDCONDITION:
			   	for(int i = 0 ; i <  tree.getChildCount(); i++)
			   	{
			   		tempReslut = haveUnsignedMax((CommonTree)tree.getChild(i));
			   		if( tempReslut )
			   		{
			   			return true;
			   		}
			   	}
		   		return false;
		   case CreateTestParser.TOK_ATOMCONDITION:
			   	  return haveUnsignedMaxAtom((CommonTree)tree);
		    default:
		    	throw new Exception("AND or OR is error " + tree.getText());
		   }
    }
    
    private boolean haveUnsignedMaxAtom(CommonTree tree)
    {
		  String columName = tree.getChild(0).getText();
		  		  
		  Class type = tableFormat.getColumnType(columName); 
		  
		  
		  if(type==String.class)
			  return true;
		  
			
		  if(tree.getChild(1).getType() == CreateTestLexer.EQUAL)
		  {
			  long value =  Long.parseLong(tree.getChild(2).getText());
			  if(type == Byte.class)
			  {
				  if(value == Bytes.toLong(DataType.getUnsginedMaxBytes(Byte.class)))
				  {
					  return true;
				  }
			  }
			  else if(type == Short.class)
			  {
				  if(value == Bytes.toLong(DataType.getUnsginedMaxBytes(Byte.class)))
				  {
					  return true;
				  }
				  
			  }else if(type == Integer.class)
			  {
				  if(value == Bytes.toLong(DataType.getUnsginedMaxBytes(Byte.class)))
				  {
					  return true;
				  }
				  
			  }
		  }
		  return false;
    }
    
    private void  PrintMaxAndMin(HashMap<String, String> localMaxValue ,HashMap<String, String> localMinValue)
    {
    		////System.out.println("maxValue is: ");
    	   Iterator<Entry<String, String >>  maxIterator = localMaxValue.entrySet().iterator();
		 	//上限和上限之间求最大值
		    while(maxIterator.hasNext())
		    {
		    	Entry<String, String > dataOR = maxIterator.next();
		    	System.out.println(dataOR.getKey()+"   "+dataOR.getValue());
		    }
		    
			System.out.println("minValue is: ");
	    	   Iterator<Entry<String, String >> minIterator = localMinValue.entrySet().iterator();
			 	//上限和上限之间求最大值
			    while(minIterator.hasNext())
			    {
			    	Entry<String, String > dataOR = minIterator.next();
			    	System.out.println(dataOR.getKey()+"   "+dataOR.getValue());
			    }
    }
    
    //根据SQL,获取上限和下限的部分
    //思路： 
    //如果是OR：
    // 先获取OR的前半部分集合A，再获取下一个部分集合B，求法为：
    // 遍历A，
     //情况1:如果M字段在A中出现，且在B中出现，则求在M的最小集里求 A和B的最小值，在M的最大集里求A和B的最大值
    //情况2：如果N在A中出现，在B中不出现，则直接在N的最小集里放入类型的最小值，在N的最大集里放类型最大值
    //情况3：如果H在A中没有，在B中出现，则直接在H的最小集里放入类型的最小值，在H的最大集里放类型最大值
	private  void getMaxAndMinIndexValue(CommonTree tree,HashMap<String, String> localMaxValue ,HashMap<String, String> localMinValue) 
	throws Exception
	   {
	
		   switch(tree.getToken().getType())
		   {
		   case CreateTestParser.TOK_ORCONDITION:
			  	//System.out.println("******************TOK_ORCONDITION***************************");
			   //or的过程：先获取第一个，然后再将第一个的传递给其他人；
			   
			   getMaxAndMinIndexValue((CommonTree)tree.getChild(0),localMaxValue,localMinValue);		
			   //System.out.println("the first*********************");
			   if( tree.getChildCount() == 1)
			   {
				   return ;
			   }
			   	for(int i = 1 ; i <  tree.getChildCount(); i++)
			   	{	
			   		HashMap<String, String>  tempMaxValue = new HashMap<String, String>();
			   	   HashMap<String, String>  tempMinValue = new HashMap<String, String>();
			   		 getMaxAndMinIndexValue((CommonTree)tree.getChild(i),tempMaxValue,tempMinValue);							   	  
			   	     //PrintMaxAndMin(localMaxValue,localMinValue);

				   	//PrintMaxAndMin(tempMaxValue,tempMinValue);

						   	//对于同一列
						 	//上限和上限之间求最大值
				   			Iterator<Entry<String, String >>  maxIterator = tempMaxValue.entrySet().iterator();
				   			TreeSet<String> localSet = new  TreeSet<String>();
				   			Iterator<Entry<String, String >>  localMaxIterator = localMaxValue.entrySet().iterator();
						    while(localMaxIterator.hasNext())
						    {
						    	localSet.add(localMaxIterator.next().getKey());
						    }
				   			
				   			TreeSet<String> tempSet = new  TreeSet<String>();
						    while(maxIterator.hasNext())
						    {
						    	Entry<String, String > dataOR = maxIterator.next();
						    	String keyOR  = dataOR.getKey();
						    	tempSet.add(keyOR);
						    	//情况2： 找到集合 (temp - local)
						    	if( null == localMaxValue.get(keyOR))
						    	{
						    		//获取该value类型的最大值： 原来的里面没有涉及的字段
						    		localMaxValue.put(keyOR, getTypeMaxValue(dataOR.getValue()));
						    		tempSet.add(keyOR);
						    	}
						    	else {
						    	//	情况1:
						    	 String value =	getMaxValue( localMaxValue.get(keyOR) , dataOR.getValue());
						    	 localMaxValue.put(keyOR, value);
								}
						    }
						    //情况3： 找到集合 (local - temp)
						    localSet.removeAll(tempSet);
						    Iterator<String>localSubTempMax = localSet.iterator();
						    while(localSubTempMax.hasNext())
						    {
						    	String columnName = localSubTempMax.next();
						    	localMaxValue.put(columnName, getTypeMaxValue(localMaxValue.get(columnName)));
						    }
						    localSubTempMax = null;
						    					    
						  	//下限和下限之间求最小值
						    Iterator<Entry<String, String >>  minIterator = tempMinValue.entrySet().iterator();
						    while(minIterator.hasNext())
						    {
						    	Entry<String, String > dataOR = minIterator.next();
						    	String keyOR  = dataOR.getKey();
						    	//情况2：
						    	if( null == localMinValue.get(keyOR))
						    	{
						    		//获取该value类型的最小值： 原来的里面没有涉及的字段
						    	//	System.out.println("@gyy:"+getTypeMinValue(dataOR.getValue()));
						    		localMinValue.put(keyOR, getTypeMinValue(dataOR.getValue()));
						    	}
						    	else {
						    	//情况1:
						    	 String value =	getMinValue( localMinValue.get(keyOR) , dataOR.getValue());
						    	 localMinValue.put(keyOR, value);
								}
						    }
						    
						    //情况3： 找到集合 (local - temp)
						    Iterator<String>localSubTempMin = localSet.iterator();
						    while(localSubTempMin.hasNext())
						    {
						    	String columnName = localSubTempMin.next();
						    	localMinValue.put(columnName, getTypeMinValue(localMinValue.get(columnName)));
						    }
						    localSubTempMin = null;
						
						 	//PrintMaxAndMin(localMaxValue,localMinValue);
						   	tempMaxValue = null;
						   	tempMinValue =null;
			   	}
			    return ;
		   case CreateTestParser.TOK_ANDCONDITION:
			   HashMap<String, String>  andTempMaxValue = new HashMap<String, String>();
			   HashMap<String, String>  andTempMinValue = new HashMap<String, String>();
			   	for(int i = 0 ; i <  tree.getChildCount(); i++)
			   	{
			   		getMaxAndMinIndexValue((CommonTree)tree.getChild(i),andTempMaxValue,andTempMinValue);
			   	}
			   	//对于同一列
			   	//上限和上限之间求最小值
			   	//下限和下限之间求最大值
			    Iterator<Entry<String, String >>  maxIteratorAnd = andTempMaxValue.entrySet().iterator();
			    while(maxIteratorAnd.hasNext())
			    {
			    	Entry<String, String > dataOR = maxIteratorAnd.next();
			    	String keyOR  = dataOR.getKey();
			    	if( null == localMaxValue.get(keyOR))
			    	{
			    		localMaxValue.put(keyOR, dataOR.getValue());
			    	}
			    	else {
			    	 String value =	getMinValue( localMaxValue.get(keyOR) , dataOR.getValue());
			    	 localMaxValue.put(keyOR, value);
					}
			    }
			  	//下限和下限之间求最大值
			    Iterator<Entry<String, String >>  minIteratorAnd = andTempMinValue.entrySet().iterator();
			    while(minIteratorAnd.hasNext())
			    {
			    	Entry<String, String > dataOR = minIteratorAnd.next();
			    	String keyOR  = dataOR.getKey();
			    	if( null == localMinValue.get(keyOR))
			    	{
			    		localMinValue.put(keyOR, dataOR.getValue());
			    	}
			    	else {
			    	 String value =	getMaxValue( localMinValue.get(keyOR) , dataOR.getValue());
			    	 localMinValue.put(keyOR, value);
					}
			    }			   
			    return ;
		   case CreateTestParser.TOK_ATOMCONDITION:
			   	  getMaxAndMinIndexValueAtomProcess( tree , localMaxValue, localMinValue);
			   	  return ;
		    default:
		    	throw new Exception("AND or OR is error " + tree.getText());
		   }
	   }
	      
	 private void  getMaxAndMinIndexValueAtomProcess(CommonTree tree, HashMap<String, String> localMaxValue ,HashMap<String, String> localMinValue) 
	 throws Exception
	   {
		 Class type = null;
		 if( tree.getChild(0).getType()  == CreateTestParser.TOK_FUN)
		 {
			String funtionName =  tree.getChild(0).getChild(0).getText();
			type = SupportFunction.functionReturnValueMap.get(funtionName);
		 }
		 else {
			  String columName = tree.getChild(0).getText();
			   type =  tableFormat.getColumnType(columName);
		}
		  if( type == String.class)
		  {
			   return ;
		  }
		  else if(type == Long.class)
		  {
			   getMaxAndMinIndexValueAtomLongProcess(tree,localMaxValue,localMinValue);
			   return ;
		  }
		  else if(type == Integer.class)
		  {
			  getMaxAndMinIndexValueAtomIntProcess(tree,localMaxValue,localMinValue);
			   return ;
		  }
		  else if(type == Byte.class)
		  {
			  getMaxAndMinIndexValueAtomByteProcess(tree,localMaxValue,localMinValue);
			  return ;
		  }
		  else if(type == Short.class)
		  {
			  getMaxAndMinIndexValueAtomShortProcess(tree,localMaxValue,localMinValue);
			  return ;
		  }
		
	    	 throw  new Exception("atomProcess is error " + tree.getText());
	   }
	   
	 private void  getMaxAndMinIndexValueAtomIntProcess(CommonTree tree, HashMap<String, String> localMaxValue ,HashMap<String, String> localMinValue) 
	 throws Exception
	   {
		  String columName = "";
			 if( tree.getChild(0).getType()  == CreateTestParser.TOK_FUN)
			 {
				 	int childCount =  tree.getChild(0).getChildCount();
					StringBuffer column = new StringBuffer();
					column.append(tree.getChild(0).getChild(0).getText());
					column.append("(");
					for(int i = 1 ; i < childCount  ; i++)
					{
						column.append(tree.getChild(0).getChild(i).getText());
						column.append(",");
					}
					column.deleteCharAt(column.length()-1);
					column.append(")");
					columName = column.toString();
			 }
			 else {
				 columName = tree.getChild(0).getText();
			}
		  int sign   			  =  tree.getChild(1).getType();
		  switch(sign)
		  {
		  case CreateTestLexer.EQUAL :
			      long temp = Long.parseLong(tree.getChild(2).getText());
			      localMaxValue.put(columName, temp+typeAndValueSeprator+DataType.IntType);
			      localMinValue.put(columName, temp+typeAndValueSeprator+DataType.IntType);
			     return ;
		
		  case CreateTestLexer.NOTEQUAL :
		      long tempNot = Long.parseLong(tree.getChild(2).getText());
		      
		      localMaxValue.put(columName,  Bytes.toLong(DataType.getUnsginedMaxBytes(Integer.class))+typeAndValueSeprator+DataType.IntType);
		      localMinValue.put(columName,  Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Integer.class))+typeAndValueSeprator+DataType.IntType);
		      return ;
		      
		  case CreateTestLexer.LESS :
		      long tempLess = Long.parseLong(tree.getChild(2).getText());
		      
		      localMaxValue.put(columName, tempLess+typeAndValueSeprator+DataType.IntType);
		      localMinValue.put(columName,  Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Integer.class))+typeAndValueSeprator+DataType.IntType);
		      return ;
		      
		  case CreateTestLexer.MORE :
		      long tempMore = Long.parseLong(tree.getChild(2).getText());
		      localMaxValue.put(columName,  Bytes.toLong(DataType.getUnsginedMaxBytes(Integer.class))+typeAndValueSeprator+DataType.IntType);
		      localMinValue.put(columName, tempMore+typeAndValueSeprator+DataType.IntType);
		      return ;
		      
		  case CreateTestLexer.IN :
			  if(tree.getChild(2).getType() == CreateTestLexer.NOT )
			  {
				  localMaxValue.put(columName, Bytes.toLong(DataType.getUnsginedMaxBytes(Integer.class))+typeAndValueSeprator+DataType.IntType);
				  localMinValue.put(columName, Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Integer.class))+typeAndValueSeprator+DataType.IntType);
			      return ;
			  }
			  else
			  {
				   long maxInt = Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Integer.class));
				   long  minInt = Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Integer.class));
				  	for(int i =2 ; i < tree.getChildCount() ; i++)
				  	{
				  	     long tempIN = Long.parseLong(tree.getChild(i).getText());
				  	     if( maxInt < tempIN)
				  	     {
				  	    	maxInt = tempIN;
				  	     }
				  	   if( minInt > tempIN)
				  	     {
				  		   minInt = tempIN;
				  	     } 
				  	}
				  	 localMaxValue.put(columName, maxInt+typeAndValueSeprator+DataType.IntType);
					 localMinValue.put(columName, minInt+typeAndValueSeprator+DataType.IntType);
				  	 return ;
				 
			  }
		  case CreateTestLexer.IS :
			  if(tree.getChildCount() == 3 &&  tree.getChild(2).getType() == CreateTestLexer.NOT)
			  {
			      localMaxValue.put(columName,  Bytes.toLong(DataType.getUnsginedMaxBytes(Integer.class))+typeAndValueSeprator+DataType.IntType);
			      localMinValue.put(columName,  Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Integer.class))+typeAndValueSeprator+DataType.IntType);
			  }
			  else {
				 // localMaxValue.put(columName, NullPosConst.NULL_VALUE+typeAndValueSeprator+DataType.IntType);
			     // localMinValue.put(columName, NullPosConst.NULL_VALUE+typeAndValueSeprator+DataType.IntType);  
			  }
			  return ;
		    default:
		    	 throw  new Exception("atom is error " + tree.getText());
		  }
	   }
	 
	 
	 private void  getMaxAndMinIndexValueAtomLongProcess(CommonTree tree, HashMap<String, String> localMaxValue ,HashMap<String, String> localMinValue) 
	 throws Exception
	   {
		  String columName = "";
		 if( tree.getChild(0).getType()  == CreateTestParser.TOK_FUN)
		 {
			 	int childCount =  tree.getChild(0).getChildCount();
				StringBuffer column = new StringBuffer();
				column.append(tree.getChild(0).getChild(0).getText());
				column.append("(");
				for(int i = 1 ; i < childCount  ; i++)
				{
					column.append(tree.getChild(0).getChild(i).getText());
					column.append(",");
				}
				column.deleteCharAt(column.length()-1);
				column.append(")");
				columName = column.toString();
		 }
		 else {
			 columName = tree.getChild(0).getText();
		}
		  int sign   			  =  tree.getChild(1).getType();
		  switch(sign)
		  {
		  case CreateTestLexer.EQUAL :
			  	 long temp =Long.parseLong(tree.getChild(2).getText());
			      localMaxValue.put(columName, temp+typeAndValueSeprator+DataType.LongType);
			      localMinValue.put(columName, temp+typeAndValueSeprator+DataType.LongType);
			     return ;
		
		  case CreateTestLexer.NOTEQUAL :
		      long tempNot =Long.parseLong(tree.getChild(2).getText());
		      localMaxValue.put(columName, Bytes.toLong(DataType.getUnsginedMaxBytes(Long.class))+typeAndValueSeprator+DataType.LongType);
		      localMinValue.put(columName, Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Long.class))+typeAndValueSeprator+DataType.LongType);
		      return ;
		      
		  case CreateTestLexer.LESS :
		      long tempLess =Long.parseLong(tree.getChild(2).getText());
		      localMaxValue.put(columName, tempLess+typeAndValueSeprator+DataType.LongType);
		      localMinValue.put(columName, Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Long.class))+typeAndValueSeprator+DataType.LongType);
		      return ;
		      
		  case CreateTestLexer.MORE :
		    
		      long tempMore =Long.parseLong(tree.getChild(2).getText());
		      localMaxValue.put(columName, Bytes.toLong(DataType.getUnsginedMaxBytes(Long.class))+typeAndValueSeprator+DataType.LongType);
		      localMinValue.put(columName, tempMore+typeAndValueSeprator+DataType.LongType);
		      return ;
		      
		  case CreateTestLexer.IN :
			  if(tree.getChild(2).getType() == CreateTestLexer.NOT )
			  {
				  localMaxValue.put(columName, Bytes.toLong(DataType.getUnsginedMaxBytes(Long.class))+typeAndValueSeprator+DataType.LongType);
				  localMinValue.put(columName, Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Long.class))+typeAndValueSeprator+DataType.LongType);
			      return ;
			  }
			  else
			  {
				   long maxLong = Long.MIN_VALUE ;
				   long minLong = Long.MAX_VALUE;
				  	for(int i =2 ; i < tree.getChildCount() ; i++)
				  	{
				  	  long tempIN =Long.parseLong(tree.getChild(i).getText());
				  	 
				  	     if( maxLong < tempIN)
				  	     {
				  	    	maxLong = tempIN;
				  	     }
				  	   if( minLong > tempIN)
				  	     {
				  		 minLong = tempIN;
				  	     } 
				  	}
				  	 localMaxValue.put(columName, maxLong+typeAndValueSeprator+DataType.LongType);
					 localMinValue.put(columName, minLong+typeAndValueSeprator+DataType.LongType);
				  	 return ;
				 
			  }
		  case CreateTestLexer.IS :
			  if(tree.getChildCount() == 3 && tree.getChild(2).getType() == CreateTestLexer.NOT)
			  {
			      localMaxValue.put(columName,  Bytes.toLong(DataType.getUnsginedMaxBytes(Long.class))+typeAndValueSeprator+DataType.LongType);
			      localMinValue.put(columName,  Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Long.class))+typeAndValueSeprator+DataType.LongType);
			  }
			  else {
				 // localMaxValue.put(columName, NullPosConst.NULL_VALUE+typeAndValueSeprator+DataType.LongType);
			     // localMinValue.put(columName, NullPosConst.NULL_VALUE+typeAndValueSeprator+DataType.LongType);  
			  }
			  return ;
		    default:
		    	 throw  new Exception("atom is error " + tree.getText());
		  }
	   }
	 
	 private void  getMaxAndMinIndexValueAtomShortProcess(CommonTree tree, HashMap<String, String> localMaxValue ,HashMap<String, String> localMinValue) 
	 throws Exception
	   {
		  String columName = "";
			 if( tree.getChild(0).getType()  == CreateTestParser.TOK_FUN)
			 {
				 	int childCount =  tree.getChild(0).getChildCount();
					StringBuffer column = new StringBuffer();
					column.append(tree.getChild(0).getChild(0).getText());
					column.append("(");
					for(int i = 1 ; i < childCount  ; i++)
					{
						column.append(tree.getChild(0).getChild(i).getText());
						column.append(",");
					}
					column.deleteCharAt(column.length()-1);
					column.append(")");
					columName = column.toString();
			 }
			 else {
				 columName = tree.getChild(0).getText();
			}
		  int sign   			  =  tree.getChild(1).getType();
		  switch(sign)
		  {
		  case CreateTestLexer.EQUAL :
			      int temp = Integer.parseInt(tree.getChild(2).getText());
			      localMaxValue.put(columName, temp+typeAndValueSeprator+DataType.ShortType);
			      localMinValue.put(columName, temp+typeAndValueSeprator+DataType.ShortType);
			     return ;
		
		  case CreateTestLexer.NOTEQUAL :
			  int tempNot = Integer.parseInt(tree.getChild(2).getText());
		   
		      localMaxValue.put(columName, Bytes.toLong(DataType.getUnsginedMaxBytes(Short.class))+typeAndValueSeprator+DataType.ShortType);
		      localMinValue.put(columName, Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Short.class))+typeAndValueSeprator+DataType.ShortType);
		      return ;
		      
		  case CreateTestLexer.LESS :
		   
		      int tempLess = Integer.parseInt(tree.getChild(2).getText());
		      localMaxValue.put(columName, tempLess+typeAndValueSeprator+DataType.ShortType);
		      localMinValue.put(columName, Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Short.class))+typeAndValueSeprator+DataType.ShortType);
		      return ;
		      
		  case CreateTestLexer.MORE :
			   int tempMore = Integer.parseInt(tree.getChild(2).getText());

		      localMaxValue.put(columName, Bytes.toLong(DataType.getUnsginedMaxBytes(Short.class))+typeAndValueSeprator+DataType.ShortType);
		      localMinValue.put(columName, tempMore+typeAndValueSeprator+DataType.ShortType);
		      return ;
		      
		  case CreateTestLexer.IN :
			  if(tree.getChild(2).getType() == CreateTestLexer.NOT )
			  {
				  localMaxValue.put(columName, Bytes.toLong(DataType.getUnsginedMaxBytes(Short.class))+typeAndValueSeprator+DataType.ShortType);
				  localMinValue.put(columName, Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Short.class))+typeAndValueSeprator+DataType.ShortType);
			      return ;
			  }
			  else
			  {
				   long maxShort =Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Short.class)) ;
				   long minShort = Bytes.toLong(DataType.getUnsginedMaxBytes(Short.class));
				  	for(int i =2 ; i < tree.getChildCount() ; i++)
				  	{
				  		 int tempIN = Integer.parseInt(tree.getChild(2).getText());
				  	     if( maxShort < tempIN)
				  	     {
				  	    	maxShort = tempIN;
				  	     }
				  	   if( minShort > tempIN)
				  	     {
				  		 minShort = tempIN;
				  	     } 
				  	}
				  	 localMaxValue.put(columName, maxShort+typeAndValueSeprator+DataType.ShortType);
					  localMinValue.put(columName, minShort+typeAndValueSeprator+DataType.ShortType);
				  	 return ;
				 
			  }
		  case CreateTestLexer.IS :
			  if(tree.getChildCount() == 3 && tree.getChild(2).getType() == CreateTestLexer.NOT)
			  {
			      localMaxValue.put(columName,  Bytes.toLong(DataType.getUnsginedMaxBytes(Short.class))+typeAndValueSeprator+DataType.ShortType);
			      localMinValue.put(columName,  Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Short.class))+typeAndValueSeprator+DataType.ShortType);
			  }
			  else {
				 // localMaxValue.put(columName, NullPosConst.NULL_VALUE+typeAndValueSeprator+DataType.ShortType);
			     // localMinValue.put(columName, NullPosConst.NULL_VALUE+typeAndValueSeprator+DataType.ShortType);  
			  }
			  return ;
		    default:
		    	 throw  new Exception("atom is error " + tree.getText());
		  }
	   }
	 
	 //255----(-1)
	 //254----(-2)
	 //253----(-3)
	 //128----(-128)
//	 private byte changeFromByteOverByteMin(int value)
//	 {
//		 byte reslut = (byte)(value - Byte.MAX_VALUE*2 -2); 
//		 return reslut;
//	 }
//	 private short changeFromShortOverShortMax(int value)
//	 {
////		 short reslut = (short)(value -Short.MAX_VALUE*2 -2); 
////		 return reslut;
//	 }
	 
	 private void  getMaxAndMinIndexValueAtomByteProcess(CommonTree tree, HashMap<String, String> localMaxValue ,HashMap<String, String> localMinValue) 
	 throws Exception
	   {
		  String columName = "";
			 if( tree.getChild(0).getType()  == CreateTestParser.TOK_FUN)
			 {
				 	int childCount =  tree.getChild(0).getChildCount();
					StringBuffer column = new StringBuffer();
					column.append(tree.getChild(0).getChild(0).getText());
					column.append("(");
					for(int i = 1 ; i < childCount  ; i++)
					{
						column.append(tree.getChild(0).getChild(i).getText());
						column.append(",");
					}
					column.deleteCharAt(column.length()-1);
					column.append(")");
					columName = column.toString();
			 }
			 else {
				 columName = tree.getChild(0).getText();
			}
		  int sign   			  =  tree.getChild(1).getType();
		  switch(sign)
		  {
		  case CreateTestLexer.EQUAL :
			      int temp = Integer.parseInt(tree.getChild(2).getText());
//			      if(temp > Byte.MAX_VALUE)
//			      {
//			    	 temp = changeFromByteOverByteMin(temp);
//			      }
			      localMaxValue.put(columName, temp+typeAndValueSeprator+DataType.ByteType);
			      localMinValue.put(columName, temp+typeAndValueSeprator+DataType.ByteType);
			     return ;
		
		  case CreateTestLexer.NOTEQUAL :
		   
		      int tempNot = Integer.parseInt(tree.getChild(2).getText());
//		      if(tempNot > Byte.MAX_VALUE)
//		      {
//		    	  tempNot = changeFromByteOverByteMin(tempNot);
//		      }
		      localMaxValue.put(columName,  Bytes.toLong(DataType.getUnsginedMaxBytes(Byte.class))+typeAndValueSeprator+DataType.ByteType);
		      localMinValue.put(columName, Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Byte.class))+typeAndValueSeprator+DataType.ByteType);
		      return ;
		      
		  case CreateTestLexer.LESS :
		
		      int tempLess = Integer.parseInt(tree.getChild(2).getText());
		      
//		      if(tempLess > Byte.MAX_VALUE)
//		      {
//		    	  tempLess = changeFromByteOverByteMin(tempLess);
//		      }
		      localMaxValue.put(columName, tempLess+typeAndValueSeprator+DataType.ByteType);
		      localMinValue.put(columName, Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Byte.class))+typeAndValueSeprator+DataType.ByteType);
		      return ;
		      
		  case CreateTestLexer.MORE :
			     int tempMore = Integer.parseInt(tree.getChild(2).getText());
		    
//			      if(tempMore > Byte.MAX_VALUE)
//			      {
//			    	  tempMore = changeFromByteOverByteMin(tempMore);
//			      }
		      localMaxValue.put(columName, Bytes.toLong(DataType.getUnsginedMaxBytes(Byte.class))+typeAndValueSeprator+DataType.ByteType);
		      localMinValue.put(columName, tempMore+typeAndValueSeprator+DataType.ByteType);
		      return ;
		      
		  case CreateTestLexer.IN :
			  if(tree.getChild(2).getType() == CreateTestLexer.NOT )
			  {
				  localMaxValue.put(columName, Bytes.toLong(DataType.getUnsginedMaxBytes(Byte.class))+typeAndValueSeprator+DataType.ByteType);
				  localMinValue.put(columName, Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Byte.class))+typeAndValueSeprator+DataType.ByteType);
			      return ;
			  }
			  else
			  {
				   long maxByte = Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Byte.class));
				   long minByte = Bytes.toLong(DataType.getUnsginedMaxBytes(Byte.class));
				  	for(int i =2 ; i < tree.getChildCount() ; i++)
				  	{
				  		 int tempIN = Integer.parseInt(tree.getChild(i).getText());
//					     if(tempIN > Byte.MAX_VALUE)
//					     {
//					    	  tempIN = changeFromByteOverByteMin(tempIN);
//					     }
				  	     if( maxByte < tempIN)
				  	     {
				  	    	 maxByte = tempIN;
				  	     }
				  	   if( minByte > tempIN)
				  	     {
				  		     minByte = tempIN;
				  	     } 
				  	}
				  	 localMaxValue.put(columName, maxByte+typeAndValueSeprator+DataType.ByteType);
					 localMinValue.put(columName, minByte+typeAndValueSeprator+DataType.ByteType);
				  	 return ;
				 
			  }
		  case CreateTestLexer.IS :
			  if(tree.getChildCount() == 3 && tree.getChild(2).getType() == CreateTestLexer.NOT)
			  {
			      localMaxValue.put(columName,  Bytes.toLong(DataType.getUnsginedMaxBytes(Byte.class))+typeAndValueSeprator+DataType.ByteType);
			      localMinValue.put(columName,  Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Byte.class))+typeAndValueSeprator+DataType.ByteType);
			  }
			  else {
				 // localMaxValue.put(columName, NullPosConst.NULL_VALUE+typeAndValueSeprator+DataType.ByteType);
			     // localMinValue.put(columName, NullPosConst.NULL_VALUE+typeAndValueSeprator+DataType.ByteType);  
			  }
			  return ;
		    default:
		    	 throw  new Exception("atom is error " + tree.getText());
		  }
	   }
	 
	   
	 public void genMaxAndMinValueOfSQL()
	 {
		   try {
			  
			getMaxAndMinIndexValue((CommonTree)tree.getChild(3),maxValue,minValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
	 }
   
//	   void printMaxAndMinValue()
//	   {
//		   System.out.println("max values is :");
//		    Iterator<Entry<String, String >>  maxIterator = maxValue.entrySet().iterator();
//		    while(maxIterator.hasNext())
//		    {
//		    	Entry<String, String > data = maxIterator.next();
//		    	 System.out.println(data.getKey()+"    "+ data.getValue());
//		    	 System.out.println(data.getKey()+"    "+
//		    	Long.toHexString(Long.parseLong(data.getValue().split(typeAndValueSeprator)[0])));
//
//		    	 condtionColumnSet.add(data.getKey());
//		    }
//		    System.out.println("min values is :");
//		    Iterator<Entry<String, String >>  minIterator = minValue.entrySet().iterator();
//		    while(minIterator.hasNext())
//		    {
//		    	Entry<String, String > data = minIterator.next();
//		    	 System.out.println(data.getKey()+"    "+
//		 		    	Long.toHexString(Long.parseLong(data.getValue().split(typeAndValueSeprator)[0])));
//
//		    	 System.out.println(data.getKey()+"    "+ data.getValue());
//		    	 condtionColumnSet.add(data.getKey());
//		    }
//		    
//		    System.out.println("condtionColumnSet  is " + condtionColumnSet);
//	   }
	   
	   void createMaxAndMinValueBytes()
	   {
		   
	   }
	   
	   
	   public String getColumnMaxValue(String columnName)
	   {
			   try {
				   String value = maxValue.get(columnName);
				   String[] values= value.split(typeAndValueSeprator);
				   return values[VALUEPOS];
			} catch ( Exception e) {
				e.printStackTrace();
				return null;
			}
	   }
	   
	   public String getColumnMinValue(String columnName)
	   {
			   try {
				   String value = minValue.get(columnName);
				   String[] values= value.split(typeAndValueSeprator);
				   return values[VALUEPOS];
			} catch ( Exception e) {
				e.printStackTrace();
				return null;
			}
	   }
	   
	   
	   private String getTypeMaxValue (String A)
	   {
		   String[] valuesOfA = A.split(typeAndValueSeprator);
		   if(valuesOfA[TYPEPOS].equals(DataType.IntType))
		   {
			 // long intA = Long.parseLong(valuesOfA[VALUEPOS]);
			  return Bytes.toLong(DataType.getUnsginedMaxBytes(Integer.class))+typeAndValueSeprator+DataType.IntType;
		   }
		   else if(valuesOfA[TYPEPOS].equals(DataType.LongType)){
			   //long intA = Long.parseLong(valuesOfA[VALUEPOS]);
			   return Bytes.toLong(DataType.getUnsginedMaxBytes(Long.class))+typeAndValueSeprator+DataType.LongType;
		   }
		   else if(valuesOfA[TYPEPOS].equals(DataType.ShortType)){
			   //long intA = Long.parseLong(valuesOfA[VALUEPOS]);
			   return Bytes.toLong(DataType.getUnsginedMaxBytes(Short.class))+typeAndValueSeprator+DataType.ShortType;
		   }
		   else if(valuesOfA[TYPEPOS].equals(DataType.ByteType)){
			   //long intA = Long.parseLong(valuesOfA[VALUEPOS]);
			   return Bytes.toLong(DataType.getUnsginedMaxBytes(Byte.class))+typeAndValueSeprator+DataType.ByteType;
		   }
		   return null;
	   }
	   
	   private String getTypeMinValue (String A)
	   {
		   String[] valuesOfA = A.split(typeAndValueSeprator);
		   if(valuesOfA[TYPEPOS].equals(DataType.IntType))
		   {
			  //long intA = Long.parseLong(valuesOfA[VALUEPOS]);
			  return Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Integer.class))+typeAndValueSeprator+DataType.IntType;
		   }
		   else if(valuesOfA[TYPEPOS].equals(DataType.LongType)){
			   //long intA = Long.parseLong(valuesOfA[VALUEPOS]);
			   return Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Long.class))+typeAndValueSeprator+DataType.LongType;
		   }
		   else if(valuesOfA[TYPEPOS].equals(DataType.ShortType)){
			   //long intA = Long.parseLong(valuesOfA[VALUEPOS]);
			   return Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Short.class))+typeAndValueSeprator+DataType.ShortType;
		   }
		   else if(valuesOfA[TYPEPOS].equals(DataType.ByteType)){
			   //long intA = Long.parseLong(valuesOfA[VALUEPOS]);
			   return Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Byte.class))+typeAndValueSeprator+DataType.ByteType;
		   }
		   return null;
	   }
	   
	   
	   private String getMaxValue(String A ,String B)
	   {
		   String[] valuesOfA = A.split(typeAndValueSeprator);
		   String[] valuesOfB = B.split(typeAndValueSeprator);
		   if(valuesOfA[TYPEPOS].equals(DataType.IntType))
		   {
			  long longA = Long.parseLong(valuesOfA[VALUEPOS]);
			  long longB = Long.parseLong(valuesOfB[VALUEPOS]);
			  if(longA > longB)
			  {
				  return longA+typeAndValueSeprator+DataType.IntType;
			  }
			  return longB+typeAndValueSeprator+DataType.IntType;
		   }
		   else if (valuesOfA[TYPEPOS].equals(DataType.LongType))
		   {
				  long longA = Long.parseLong(valuesOfA[VALUEPOS]);
				  long longB =Long.parseLong(valuesOfB[VALUEPOS]);
				  if(longA > longB)
				  {
					  return longA+typeAndValueSeprator+DataType.IntType;
				  }
				  return longB+typeAndValueSeprator+DataType.IntType;
		   }
		   else if (valuesOfA[TYPEPOS].equals(DataType.ShortType)) 
		   {
				  int intA = Integer.parseInt(valuesOfA[VALUEPOS]);
				  int intB = Integer.parseInt(valuesOfB[VALUEPOS]);
				  if(intA > intB)
				  {
					  return intA+typeAndValueSeprator+DataType.IntType;
				  }
				  return intB+typeAndValueSeprator+DataType.IntType;
		   }
		   else  if (valuesOfA[TYPEPOS].equals(DataType.ByteType)) 
		   {
				  int intA = Integer.parseInt(valuesOfA[VALUEPOS]);
				  int intB = Integer.parseInt(valuesOfB[VALUEPOS]);
				  if(intA > intB)
				  {
					  return intA+typeAndValueSeprator+DataType.IntType;
				  }
				  return intB+typeAndValueSeprator+DataType.IntType;
		   }
		   return null;
	   }
	   
	   private String getMinValue(String A ,String B)
	   {
//		   System.out.println("string A :" + A);
//		   System.out.println("string A :" + B);
		   String[] valuesOfA = A.split(typeAndValueSeprator);
		   String[] valuesOfB = B.split(typeAndValueSeprator);
		   if(valuesOfA[TYPEPOS].equals(DataType.IntType))
		   {
			  long intA = Long.parseLong(valuesOfA[VALUEPOS]);
			  long intB = Long.parseLong(valuesOfB[VALUEPOS]);
			  if(intA < intB)
			  {
				  return intA+typeAndValueSeprator+DataType.IntType;
			  }
			  return intB+typeAndValueSeprator+DataType.IntType;
		   }
		   else if (valuesOfA[TYPEPOS].equals(DataType.LongType))
		   {
				  long longA = Long.parseLong(valuesOfA[VALUEPOS]);
				  long longB =Long.parseLong(valuesOfB[VALUEPOS]);
				  if(longA < longB)
				  {
					  return longA+typeAndValueSeprator+DataType.IntType;
				  }
				  return longB+typeAndValueSeprator+DataType.IntType;
		   }
		   else if (valuesOfA[TYPEPOS].equals(DataType.ShortType)) 
		   {
				  int intA = Integer.parseInt(valuesOfA[VALUEPOS]);
				  int intB = Integer.parseInt(valuesOfB[VALUEPOS]);
				  if(intA < intB)
				  {
					  return intA+typeAndValueSeprator+DataType.IntType;
				  }
				  return intB+typeAndValueSeprator+DataType.IntType;
		   }
		   else  if (valuesOfA[TYPEPOS].equals(DataType.ByteType)) 
		   {
				  int intA = Integer.parseInt(valuesOfA[VALUEPOS]);
				  int intB = Integer.parseInt(valuesOfB[VALUEPOS]);
				  if(intA < intB)
				  {
					  return intA+typeAndValueSeprator+DataType.IntType;
				  }
				  return intB+typeAndValueSeprator+DataType.IntType;
		   }
		   return null;
	   }
	   
	   public  MulitFiledData getMaxMulitFiledData(ReadWriteIndexDataProxy proxy)
	   {
		   ArrayList<byte[]> dataListMax = new ArrayList<byte[]>();
		   ParseIndexFormat format = proxy.getIndexFormat();
		   ArrayList<String> columnNameList= format.getIndexColumnName();
		   ArrayList<Class> columnTypeList= format.getIndexColumnType();
		   int size = columnNameList.size();
		   for(int i =0 ; i < size ; i ++)
		   {
			
			  String temp = maxValue.get(columnNameList.get(i));
			  if(temp == null)
			  {
				  dataListMax.add(DataType.getUnsginedMaxBytes(columnTypeList.get(i)));
			  }
			  else 
			  {
				  String[] temps =temp.split(typeAndValueSeprator);
				 dataListMax.add(DataType.getBytes(columnTypeList.get(i), temps[VALUEPOS]));
			  }
		   }
		   
		   MulitFiledData data = new MulitFiledData(dataListMax,proxy,false);
		   return data;
		
	   }
	   
	   public  HashMap<String, byte[]> getConditonMinBytes( )
	   {		   
		   if(conditonMinBytes.size() > 0)
		   {
			   return  conditonMinBytes;
		   }
		   
		   ArrayList<String> columnNameList= tableFormat.getColumnName();
		   HashMap<String,Class> columnTypeList= tableFormat.getColumnNameAndTypeHashMap();
		   
		    Iterator<Entry<String, String >>  maxIterator = minValue.entrySet().iterator();
		    while(maxIterator.hasNext())
		    {
		    	Entry<String, String > data = maxIterator.next();
		    	 condtionColumnSet.add(data.getKey());
		    	 String[] temps =data.getValue().split(typeAndValueSeprator);
		    	 conditonMinBytes.put(data.getKey(), DataType.getBytes(columnTypeList.get(data.getKey()) , temps[VALUEPOS]));
		    }
		   
		   return conditonMinBytes;
	   }
	   
	   public  HashMap<String, byte[]> getConditonMaxBytes( )
	   {
		   if(conditonMaxBytes.size() > 0)
		   {
			   return  conditonMaxBytes;
		   }
		   ArrayList<String> columnNameList= tableFormat.getColumnName();
		   HashMap<String,Class> columnTypeList= tableFormat.getColumnNameAndTypeHashMap();

		    Iterator<Entry<String, String >>  maxIterator = maxValue.entrySet().iterator();
		    while(maxIterator.hasNext())
		    {
		    	Entry<String, String > data = maxIterator.next();
		    	 condtionColumnSet.add(data.getKey());
		    	 String[] temps =data.getValue().split(typeAndValueSeprator);
		    	 conditonMaxBytes.put(data.getKey(), DataType.getBytes(columnTypeList.get(data.getKey()) , temps[VALUEPOS]));
		    }
		   return conditonMaxBytes;
	   }
	   
	   public Set<String> getConditonSet()
	   {
		   if(condtionColumnSet.size() > 0)
		   {
		   return  condtionColumnSet;
		   }
		    Iterator<Entry<String, String >>  maxIterator = maxValue.entrySet().iterator();
		    while(maxIterator.hasNext())
		    {
		    	Entry<String, String > data = maxIterator.next();
		    	 condtionColumnSet.add(data.getKey());
		    }
		   return condtionColumnSet;
	   }
	   
	   
	   public  MulitFiledData getMinMulitFiledData(ReadWriteIndexDataProxy proxy)
	   {
		   ArrayList<byte[]> dataListMin = new ArrayList<byte[]>();
		   ParseIndexFormat format = proxy.getIndexFormat();
		   ArrayList<String> columnNameList= format.getIndexColumnName();
		   ArrayList<Class> columnTypeList= format.getIndexColumnType();
		   int size = columnNameList.size();
		   for(int i =0 ; i < size ; i ++)
		   {
			  String temp = minValue.get(columnNameList.get(i));
			  if(temp == null)
			  {
				  dataListMin.add(DataType.getUnsginedMinBytesReturnNot8Byte(columnTypeList.get(i)));
			  }
			  else 
			  {
				 String[] temps =temp.split(typeAndValueSeprator);
				 dataListMin.add(DataType.getBytes(columnTypeList.get(i), temps[VALUEPOS]));
			  }
		   }
		   MulitFiledData data = new MulitFiledData(dataListMin,proxy ,false);
		   return data;
	   }

   
    public static void main(String[] args) throws Exception  {
    	
    	ParseIndexFormat readIndexFormat = null;
    	ParseTableFormat readTableFormat =null;
		String path ="/afd/tableInfo/nur/indexfile/calledIndex/index.frm" ;
//		String tablePath ="/gentest/bssap/table.frm" ;
		Configuration conf = new Configuration();
		conf.set("fs.default.name","hdfs://192.168.1.12:9000");
		FileSystem fs = null;
		try {
			 fs = FileSystem.get(conf);
			if(fs != null)
			{
				readIndexFormat = new ParseIndexFormat(path,fs);
//				readTableFormat  = new ParseTableFormat(tablePath,fs);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		ReadWriteIndexDataProxy proxy = new ReadWriteIndexDataProxy(readIndexFormat);
    	
    	long  startTime = System.currentTimeMillis();
//    	String sql = "select all  from bssap   on gentest where   start_time_s == 1321340888  and start_time_ns > 1 and start_time_ns < 1158543";

//select all from bssap on cdr where  calling_number >= 0 and calling_number <= 9223372036854775807 and start_time_s >= 0 and start_time_s <= 4294967295 and called_number >= 0 and called_number <= 9223372036854775807 and cdr_type >= 0 and cdr_type <= 255 and imsi >= 0 and imsi <= 9223372036854775807 and city_code >= 0 and city_code <= 255 and  (   cdr_result >= 0 ) !!!gyy!!!select all from bssap on cdr where  cdr_result >= 0
    	
    	String sql ="select all from nur on afd where start_time>=0 ";
    	MaxAndMinValueOfSQLGen rf = new MaxAndMinValueOfSQLGen(sql,fs);
    	long  endTime = System.currentTimeMillis();
    	  startTime = System.currentTimeMillis();
    	  endTime = System.currentTimeMillis();
    	  //rf.printMaxAndMinValue();
    	  
   }

    
}
