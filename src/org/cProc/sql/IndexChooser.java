package org.cProc.sql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.cProc.GernralRead.ReadData.ParseIndexFormat;
import org.cProc.GernralRead.TableAndIndexTool.TableAndIndexTool;



/**
 * 类名: IndexChooser
 * 
 * @Comments     该类有两个作用： 1.选择索引（该功能尚未开发） 2.根据SQL和索引字段判断，查完索引后，在读数据时，是否还要过滤数据。
 * @author gyy 
 * @date 2011.11.08
 * @Version: 版本1.0
 */
public class IndexChooser {
	
	
	public static final Log LOG = LogFactory.getLog(IndexChooser.class.getName());
	
	
	
    CreateTestLexer lexer =  null;
    CommonTokenStream tokens = null;
    CreateTestParser parser = null;
    CommonTree tree = null;
    ArrayList<String> showColumn = new ArrayList<String>();
    SortedSet<String>  columSet  = new TreeSet<String>();
    String tableName = "";
    String appName ="";
    String newSql = null;
    boolean ok = true;
    public  String getTableName()
    {
    	return tableName;
    }
    
    public  String getAppName()
    {
    	return appName;
    }
    
    public  String getNewSql()
    {
    	return newSql;
    }
    
	public IndexChooser(String sql) 
	{
		newSql =LikeMatchOperator.repacleLike(sql);
	    lexer = new CreateTestLexer(new ANTLRStringStream(newSql));//"select start_time_s  from M where start_time_s  in (4, 6, 7) "));//4^2*2/4-(6+2)+4
	    tokens = new CommonTokenStream(lexer);
	    parser = new CreateTestParser(tokens);
	  
	    try{
			this.tree = parser.statement().tree;
			if(LOG.isDebugEnabled())
				LOG.debug("grammer tree is " + tree.toStringTree());
			  tableName = tree.getChild(0).getChild(0).getText();
			  appName = tree.getChild(1).getChild(0).getText();
	    	 for(int i = 0 ; i < tree.getChild(2).getChildCount() ; i++)
	    	 {
	    		 showColumn.add(tree.getChild(2).getChild(i).getText());
	    	 }
	    	 if(tree.getChildCount() > 3)
	    	 {
	    		 if(tree.getChild(3).getType() == CreateTestParser.TOK_ORCONDITION)
	    		 {
	    			 this.process((CommonTree)tree.getChild(3));
	    		 }
	    	 }
	    	 
	    }catch(Exception e)
	    {
	    	e.printStackTrace();
	    	ok = false;
	    }
	    
	    
		
	}
	
	public boolean isOk()
	{
		return ok;
	}
	
	private  void process(CommonTree tree) throws Exception
	   {
		   boolean reslut = false;
		   boolean tempReslut = false;
		   switch(tree.getToken().getType())
		   {
		   case CreateTestParser.TOK_ORCONDITION:
			   	for(int i = 0 ; i <  tree.getChildCount(); i++)
			   	{
			   		process((CommonTree)tree.getChild(i));
			   		
			   	}
			   	return ;
		   case CreateTestParser.TOK_ANDCONDITION:
			   	for(int i = 0 ; i <  tree.getChildCount(); i++)
			   	{
			   		process((CommonTree)tree.getChild(i));

			   	}
			   	return ;
		   case CreateTestParser.TOK_ATOMCONDITION:
			   	  atomProcess( tree);
			   	return ;
		    default:
		    	throw new Exception("AND or OR is error " + tree.getText());
		   }
	   }
	      
	 private void  atomProcess(CommonTree tree) throws Exception
	   {
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
			columSet.add(column.toString());
		 }
		 else {
			  String columName = tree.getChild(0).getText();
			  columSet.add(columName);
		}
		
	   }
	 
	 public SortedSet<String> getConditonColumn()
	 {
		 try {
			this.process((CommonTree)tree.getChild(3));
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return columSet;
	 }
	 
	 public boolean isNeedFilterData(ParseIndexFormat indexFormat)
	 {
		 ArrayList<String> indexColumnNameList = indexFormat.getIndexColumnName();
		 for(int i = 0  ; i< indexColumnNameList.size() ;i++)
		 {
			 columSet.remove(indexColumnNameList.get(i));
		 }
		 if(columSet.size() == 0)
		 {
			 return false;
		 }
		 return true;
	 }
	 
	 
	 public void printColumName()
	 {
		 System.out.print(columSet);
	 }
	 
	 public static String getIndexName(int number)
	 {
		 if( 0 == number)
		 {
			 return "callingIndex";
		 }
		 else if( 1 == number)
		 {
			 return "calledIndex";
		 }
		 else {
			return  "callingIndex";
		}
	 }
	 
		public static void main(String args[]) throws Exception
		{
			
			
			long startTime = System.currentTimeMillis();
			String sql ="select all from bssap on gentest where  toNumber(calling_number,0,10) == 100  start_time_s>=1300723200 and start_time_s<=1300748400 " ;
			
			
			IndexChooser a = new IndexChooser(sql);
//	    	String indexNameString =IndexChooser.getIndexName(0);
//			String indexPath = TableAndIndexTool.getIndexFormat(a.getTableName(), indexNameString, a.getAppName());
//			String indexDataPath = TableAndIndexTool.getIndexDataPath(a.getTableName(), indexNameString, a.getAppName());
//	    	ParseIndexFormat readIndexFormat = null;
//			Configuration conf = new Configuration();
////			conf.set("fs.default.name","hdfs://192.168.1.8:9000");
//			 indexPath = "D:\\dingli\\dataFile\\tableInfo\\bssap\\indexfile\\callingIndex\\index.frm";
//			try {
//				FileSystem fs = FileSystem.get(conf);
//				if(fs != null)
//				{
//					readIndexFormat = new ParseIndexFormat(indexPath,fs);
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			//true
//			//索引文件
//			//数据文件目录
//			long endTime = System.currentTimeMillis();
////			System.out.println(" parse SQL cost time is "+ (endTime-startTime));
//			 startTime = System.currentTimeMillis();
//			System.out.println(" is need filter data :" + a.isNeedFilterData(readIndexFormat));
		}
}
