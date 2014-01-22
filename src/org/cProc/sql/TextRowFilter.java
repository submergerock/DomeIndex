package org.cProc.sql;
import java.util.ArrayList;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.FileSystem;
import org.cProc.GernralRead.ReadData.ReadData;
import org.cProc.sql.Tree.RowFilterTree;
import org.cProc.sql.Tree.TextRowFilterTree;


/**
 *  RowFilter
 * @Comments   
 * @author gyy 
 * @date 2011.11.08
 * @Version: 鐗堟湰1.0
 */
public class TextRowFilter   {
	
	
	public static final Log LOG = LogFactory.getLog(TextRowFilter.class.getName());
	
	
	
	private  ReadData  rp= null;

    CreateTestLexer lexer =  null;
    CommonTokenStream tokens = null;
    CreateTestParser parser = null;
    CommonTree tree = null;
    ArrayList<String> showColumn = new ArrayList<String>();
    private FileSystem fs = null;

    private boolean filter = false;
    private TextRowFilterTree rowFilter=  null;
    
//    private HashMap<String, String> maxValue = new HashMap<String, String>();
//    private  HashMap<String, String> minValue = new HashMap<String, String>();
    private String sql = null;
    
//    private IndexFilterTree rowFilter = null;
    
    public FileSystem getFS()
    {
    	return fs;
    }
    public String getSQL()
    {
    	return sql;
    }
    
    public TextRowFilter(String sql  , FileSystem fs)
    {
    	this.sql = sql;
    	this.fs = fs;
    	//sql = "select start_time_s  from M  where start_time_s  > 1234 and  start_time_ns > 1232131 " ;
    	long startTime = System.currentTimeMillis();
        lexer = new CreateTestLexer(new ANTLRStringStream(sql));//"select start_time_s  from M where start_time_s  in (4, 6, 7) "));//4^2*2/4-(6+2)+4
        tokens = new CommonTokenStream(lexer);
        parser = new CreateTestParser(tokens);
        try {
			this.tree = parser.statement().tree;
			//DebugPrint.InforPrint("grammer tree is " + tree.toStringTree());
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
	    	 
	    	 // TODO
	    	 rp = ReadData.getTextReadData(tableName, appName, fs);
	    	 
	    	 
	    	 rowFilter = new TextRowFilterTree((CommonTree)tree.getChild(3), rp);
	    	 long endTime = System.currentTimeMillis();
	    	 LOG.debug("Init RowFilter  cost time is " + (endTime - startTime));
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
    		 LOG.debug("showColumn is  " + showColumn.get(i));
    	}
    }
   

   public boolean isDataOK(byte[] data)
   {
	   try {
		  if(!filter) //没有where子句
		  {
			  return false;
		  }
		  rowFilter.setData(data);
		  return rowFilter.process();
	} catch (Exception e) {
		e.printStackTrace();
		return false;
	}
   }
       	   
	   public int getDataSize()
	   {
		   return rp.getDataSize();
	   }

	   
	    public static void main(String[] args) throws Exception  {
	    	
	    	long  startTime = System.currentTimeMillis();
	    	TextRowFilter rf = new TextRowFilter("",null);
	    	long  endTime = System.currentTimeMillis();
	    	System.out.println("time is "+ (endTime - startTime));
	    	  startTime = System.currentTimeMillis();
	    	  //data type: A,B,fileName,offsets;
	    	  endTime = System.currentTimeMillis();
	    	  System.out.println("time is "+ (endTime - startTime));
	    	  startTime = System.currentTimeMillis();

	    	 endTime = System.currentTimeMillis();
	    	 rf.ShowCloumn();
	   }
}
