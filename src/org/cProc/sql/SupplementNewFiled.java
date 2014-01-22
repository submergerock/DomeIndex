package org.cProc.sql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
import org.cProc.tool.Bytes;

import testReadFile.ReadAndFilterFromLocalDiskThread;



/**
 * 类名: RowFilter
 * @Comments     该类作用： 1.根据SQL以及索引，来判断SQL的条件是否满足索引的项，如果不满足索引的项，则添加缺少的项；
 * 添加方式：   A > integer.Min and A < integer.max
 * @author gyy 
 * @date 2011.11.08
 * @Version: 版本1.0
 */
	public class SupplementNewFiled   {
		
		
		public static final Log LOG = LogFactory.getLog(SupplementNewFiled.class.getName());
		
		
		public static final String SEPRATEOLDSQLANDNEWSQL = "!!!gyy!!!";
	    CreateTestLexer lexer =  null;
	    CommonTokenStream tokens = null;
	    CreateTestParser parser = null;
	    CommonTree tree = null;
	    ArrayList<String> showColumn = new ArrayList<String>();
	    private ArrayList<String> conditionFiledList = new  ArrayList<String>();
//	    private ArrayList<Class> conditionFiledTypeList = new  ArrayList<Class>();
	    private HashMap<String, Class> conditionMap = new HashMap<String, Class>();
	    private boolean filter = false;
	    String sql ="";
	    ParseIndexFormat indexFormat = null;
	    private static  String andString =" and ";
	    private static  String whereString ="where";
	    private static  String LParentString =" ( ";
	    private static  String RParentString =" ) ";
	    public SupplementNewFiled(String psql , ParseIndexFormat indexFormat )
	    {
	    	this.indexFormat  = indexFormat;
	    	this.sql = psql;
	        lexer = new CreateTestLexer(new ANTLRStringStream(sql));//"select start_time_s  from M where start_time_s  in (4, 6, 7) "));//4^2*2/4-(6+2)+4
	        tokens = new CommonTokenStream(lexer);
	        parser = new CreateTestParser(tokens);
	        try {
				this.tree = parser.statement().tree;
				 String tableName = tree.getChild(0).getChild(0).getText();
				 String appName = tree.getChild(1).getChild(0).getText();
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
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }

	     public void ShowCloumn()
	    {
	    	
	    	for(int i = 0 ; i  < showColumn.size(); i++)
	    	{
	    		LOG.debug("showColumn is  " + showColumn.get(i));
	    	}
	    }
	   
	    public  String supplementSQL()
	    {
	    	 if( !filter)
	    	 {
	    		 printConditionColumn();
	    		 return sql;
	    	 }
	    	 try {
				process((CommonTree)tree.getChild(3));
				
				 ArrayList<String> indexColumn = indexFormat.getIndexColumnName();
				 ArrayList<String> indexColumnStrings = new ArrayList<String>();
				 indexColumnStrings.addAll(indexColumn);
				 //求集合（索引的字段-conditionList）
				 indexColumnStrings.removeAll(conditionFiledList);
				 StringBuffer addSQL =  new StringBuffer();
				 for( int  i = 0 ; i < indexColumnStrings.size()-1 ; i++ )
				 {
					 System.out.println("********"+indexColumnStrings.get(i));
					 addDefalutFiled(addSQL,indexColumnStrings.get(i));
				 }
				 if(addSQL.length() ==0)
				 {
					 return sql;
				 }
				 String addConditon = addSQL.substring(0, addSQL.length()-andString.length());
				 
				 return  addCondtion(sql,addConditon);
			
			} catch (Exception e1) {
				e1.printStackTrace();
				return sql;
			}
	    }
	    
	    private  static String addCondtion(String sql , String conditon)
	    {
	    	//取前面的部分
	    	String beforeWhereString = sql.substring(0, sql.indexOf(whereString)+whereString.length());
	    	//找到条件的部分
	    	String afterWhereString =  sql.substring(sql.indexOf(whereString)+whereString.length(), sql.length());
	    	System.out.println("before is " + beforeWhereString);
	    	System.out.println("after is " + afterWhereString);
	    	//取出条件部分： 新条件 and ( 旧条件 )
	    	//System.out.println("condtion is " + conditon);
	    	return beforeWhereString +"  " + conditon+ andString+ LParentString +afterWhereString+RParentString;
	    }
	    private void  addDefalutFiled(StringBuffer sb ,String filed)  throws Exception
	    {
	    		Class type =  indexFormat.getColunmType(filed);
				  if( type == String.class)
				  {

					  sb.append( filed+" >= " + " '' " +andString); 
					  return ;
				  }
				  else if(type == Integer.class)
				  {
					  sb.append(filed+" >= " + Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Integer.class)) +" and "+filed+" <= " +
							  Bytes.toLong(DataType.getUnsginedMaxBytes(Integer.class)) +andString); 
					  return ;
				  }
				  else if(type == Long.class)
				  {
					  sb.append(filed+" >= " + Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Long.class)) 
							  +" and "+filed+" <= " +
							  Bytes.toLong(DataType.getUnsginedMaxBytes(Long.class)) +andString); 
					  return ;
				  }
				  else if(type == Byte.class)
				  {
					  sb.append(filed+" >= " + Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Byte.class))
							  +" and "+filed+" <= " +Bytes.toLong(DataType.getUnsginedMaxBytes(Byte.class)) +andString); 
					  return ;
				  }
				  else if(type == Short.class)
				  {
					  sb.append(filed+" >= " + Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(Short.class)) +" and "+filed+" <= " +
							  Bytes.toLong(DataType.getUnsginedMaxBytes(Short.class)) +andString); 
					  return ;
				  }
	    	throw new Exception("index Filed  is error " +filed);
	  
	    }
	    
		private  boolean process(CommonTree tree) throws Exception
		   {
			   boolean reslut = false;
			   boolean tempReslut = false;
			   switch(tree.getToken().getType())
			   {
			   case CreateTestParser.TOK_ORCONDITION:
				   	for(int i = 0 ; i <  tree.getChildCount(); i++)
				   	{
				   		tempReslut = process((CommonTree)tree.getChild(i));
				   		if( tempReslut )
				   		{
				   			return true;
				   		}
				   	}
			   		return false;
			   case CreateTestParser.TOK_ANDCONDITION:
				   	for(int i = 0 ; i <  tree.getChildCount(); i++)
				   	{
				   		tempReslut = process((CommonTree)tree.getChild(i));
				   		if( !tempReslut )
				   		{
				   			return false;
				   		}
				   	}
			   		return true;
			   case CreateTestParser.TOK_ATOMCONDITION:
				   	  atomProcess( tree);
				   	  return true;
			    default:
			    	throw new Exception("AND or OR is error " + tree.getText());
			   }
		   }
		      
		 private void  atomProcess(CommonTree tree) throws Exception
		   {
			 String columName = "";
			 //deal with funciton
			 if(tree.getChild(0).getType() == CreateTestParser.TOK_FUN)
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
				 //deal with colum
				 columName = tree.getChild(0).getText();
			}
			  if(indexFormat.getColunmPos(columName) == -1)
			  {
				  return ;
			  }
			  conditionFiledList.add(columName);
			  
			  Class type =  indexFormat.getColunmType(columName);
			  if( type == String.class)
			  {
				  conditionMap.put(columName, type);
			  }
			  else if(type == Integer.class)
			  {
				  conditionMap.put(columName, type);
			  }
			  else if(type == Long.class)
			  {
				  conditionMap.put(columName, type);
			  }
			  else if(type == Byte.class)
			  {
				  conditionMap.put(columName, type);
			  }
			  else if(type == Short.class)
			  {
				  conditionMap.put(columName, type);
			  }
			  else {
				  throw  new Exception("atomProcess is error " + tree.getText());
			}
			
		   }

		 private  void printConditionColumn()
		 {
			 System.out.println("start:print column****************************************");
			 for( int  i = 0 ; i < conditionFiledList.size() ; i++ )
			 {
				 System.out.println("conditon Filed  " + i+"  is :" + conditionFiledList.get(i));
			 }
			 System.out.println("end:print column****************************************");
			 
		 }
		 
		 public static  String  getSQL(String sql,  ParseIndexFormat  readIndexFormat)
		 {
			  	SupplementNewFiled rf = new SupplementNewFiled(sql, readIndexFormat);
			  	return rf.supplementSQL()+SEPRATEOLDSQLANDNEWSQL+sql;
		 }
		 
		    public static void main(String[] args) throws Exception  {
		    	
		    	
//		    	String SQL ="select A from C where B == 1 ";
//		    	String addSQL =" A == 1 and C == 2";
//		    	
//		    	System.out.println(addCondtion(SQL , addSQL));
		  
		    	ParseIndexFormat readIndexFormat = null;
		    	ParseTableFormat readTableFormat =null;
				String path ="C:\\dingli\\test\\tableInfo\\bssap\\indexfile\\functionIndex\\index.frm" ;
				String tablePath ="C:\\dingli\\test\\tableInfo\\bssap\\table.frm" ;
				Configuration conf = new Configuration();
//				conf.set("fs.default.name","hdfs://192.168.1.8:9000");
				FileSystem fs = null;
				try {
					 fs = FileSystem.get(conf);
					if(fs != null)
					{
						readIndexFormat = new ParseIndexFormat(path,fs);
						readTableFormat  = new ParseTableFormat(tablePath,fs);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
		     	String sql = "select start_time_s  from gentest on gentest  where STRINGTOLONG(calling_number_str,21,4) >= 9800" ;
		     	System.out.println("sql is " + sql);
		     	String fullSQL = getSQL(sql,readIndexFormat);
		    	System.out.println();
		    	System.out.println();
		    	System.out.println(fullSQL);
		    
		   }
	}

