
package org.cProc.sql;
import java.util.ArrayList;
import java.util.TreeSet;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.FileSystem;
import org.cProc.GernralRead.ReadData.ParseIndexFormat;
import org.cProc.index.BPlusTree.Data.Data;
import org.cProc.sql.Tree.IndexFilterTree;
import org.cProc.tool.Bytes;



/**
 * 类名: RowFilter
 * @Comments     该类作用： 1.获取数据（字节数组的形式），然后根据SQL的条件来判断是否满足。
 * @author gyy 
 * @date 2011.11.08
 * @Version: 版本1.0
 */
public class IndexFilter<T extends Data>   {
	
	
	
	
	public static final Log LOG = LogFactory.getLog(IndexFilter.class.getName());
	
	ParseIndexFormat indexFormat = null;
    CreateTestLexer lexer =  null;
    CommonTokenStream tokens = null;
    CreateTestParser parser = null;
    CommonTree tree = null;
    ArrayList<String> showColumn = new ArrayList<String>();
    private FileSystem fs = null;
    private boolean filter = false;
    private Data data  = null;
    
    private TreeSet<String> set = new  TreeSet<String>() ;
    
    private IndexFilterTree indexFilterTree = null;
    
    public static int number = 0;
    public static int numberCost = 0;
        
    //set  :  索引锁涉及的字段；
    public IndexFilter(String sql  , FileSystem fs  ,ParseIndexFormat indexFormat )
    {
//    	this.sql = sql;
 //   	System.out.println("IndexFilter SQL is "+sql);
    	//sql = "select start_time_s  from M  where start_time_s  > 1234 and  start_time_ns > 1232131 " ;
    	this.indexFormat = indexFormat;
    	ArrayList<String> list = indexFormat.getIndexColumnName();
    	for(int i = 0 ; i < list.size() ; i++)
    	{
    		set.add(list.get(i));
    	}
    	long startTime = System.currentTimeMillis();
        lexer = new CreateTestLexer(new ANTLRStringStream(sql));//"select start_time_s  from M where start_time_s  in (4, 6, 7) "));//4^2*2/4-(6+2)+4
        tokens = new CommonTokenStream(lexer);
        parser = new CreateTestParser(tokens);
        try {
			this.tree = parser.statement().tree;
			if(LOG.isDebugEnabled())
				LOG.debug("grammer tree is " + tree.toStringTree());
			String tableName = tree.getChild(0).getChild(0).getText();
			String appName  = tree.getChild(1).getChild(0).getText();
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
	    	 long endTime = System.currentTimeMillis();
	    	 //LOG.info("Init RowFilter  cost time is " + (endTime - startTime));
	    	 indexFilterTree = new IndexFilterTree((CommonTree)tree.getChild(3),indexFormat, set);

		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public void setFileSystem(FileSystem fs)
    {
    	this.fs = fs;
    }
    
    
     public void ShowCloumn()
    {
    	for(int i = 0 ; i  < showColumn.size(); i++)
    	{
    		LOG.info("showColumn is  " + showColumn.get(i));
    	}
    }
   

   public boolean isDataOK(Data data)
   {
	   try {
		  this.data = data;
		  indexFilterTree.setData(data);
		  return indexFilterTree.process();
		//  return process( (CommonTree)tree.getChild(3));
	} catch (Exception e) {
		e.printStackTrace();
		return false;
	}
   }
       	   
	    public static void main(String[] args) throws Exception  {
	    	long  startTime = System.currentTimeMillis();
	    	RowFilter rf = new RowFilter("",null);
	    	 rf.ShowCloumn();
	   }
}
