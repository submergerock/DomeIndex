package org.cProc.sql.Tree;

import org.antlr.runtime.tree.CommonTree;
import org.cProc.GernralRead.ReadData.ReadData;
import org.cProc.function.SupportFunction;
import org.cProc.sql.CreateTestLexer;
import org.cProc.sql.CreateTestParser;
import org.cProc.sql.StringLike;
import org.cProc.tool.Bytes;

public class TextRowFilterTree {
	
	private ReadData rp = null;
//    private TreeSet<String> set = new  TreeSet<String>() ;
//    private Data data  = null;	
	private Node root = new Node();

	private String [] columns = null;
	private static final String split = ",";
	
	private StringLike stringLike = new StringLike();
	
	
		
	public TextRowFilterTree(CommonTree tree, ReadData  rp)
	{
		try {
			this.rp= rp;
			generateIndex( tree , root);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private  boolean generateIndex(CommonTree tree , Node node) throws Exception
	{
		   boolean tempReslut = false;
		   node.nodeType = tree.getToken().getType();
		   switch(tree.getToken().getType())
		   {
		   case CreateTestParser.TOK_ORCONDITION:
			   //or的子节点中存在一个真值，则这个值为真.
			   //这个真值的产生是因为：条件里包含一个非索引字段的列；
			   	for(int i = 0 ; i <  tree.getChildCount(); i++)
			   	{	
			   		Node childNode = new Node();
			   		childNode.parent = node;

			   		node.childNode.add(childNode);
			   		tempReslut = generateIndex((CommonTree)tree.getChild(i),childNode);
			   		if( tempReslut )
			   		{
			   			childNode.reslut = true;
			   			return true;
			   		}
			   	}
		   		return false;
		   case CreateTestParser.TOK_ANDCONDITION:
			   	for(int i = 0 ; i <  tree.getChildCount(); i++)
			   	{
			   		Node childNode = new Node();
			   		childNode.parent = node;

			   		node.childNode.add(childNode);
			   		tempReslut = generateIndex((CommonTree)tree.getChild(i),childNode);
			   	}
		   		return false;
		   case CreateTestParser.TOK_ATOMCONDITION:
			   	  return generateAtomProcess( tree , node);
		    default:
		    	throw new Exception("AND or OR is error " + tree.getText());
		   }
	 }
	
	 private boolean  generateAtomProcess(CommonTree tree ,Node node) throws Exception
	   {
		 String columName ="";
		  Class type  =  null;
		 if(tree.getChild(0).getType() == CreateTestParser.TOK_FUN)
			 {
			 	node.isFunction = true;
				int childCount =  tree.getChild(0).getChildCount();
				StringBuffer column = new StringBuffer();
				column.append(tree.getChild(0).getChild(0).getText());
				column.append("(");
				for(int i = 1 ; i < childCount  ; i++)
				{
					column.append(tree.getChild(0).getChild(i).getText());
					column.append(",");
					node.funtionParaNameArrayList.add(tree.getChild(0).getChild(i).getText() );
				}
				column.deleteCharAt(column.length()-1);
				column.append(")");
				columName = column.toString();
				  type =  SupportFunction.functionReturnValueMap.get(tree.getChild(0).getChild(1).getText());
				  node.filedType = type;
			 }
		 else {
			     columName = tree.getChild(0).getText();
				 node.columnName = columName;
				   type =   rp.getColumnType(columName);
				  node.filedType = type;
		}
		 		  
		  node.operater = tree.getChild(1).getType();
		  if(node.operater == CreateTestLexer.IN )
		  {
			  int count = 2;
			  if(tree.getChild(2).getType() == CreateTestLexer.NOT)
			  {
				  node.hasNot = true;
				  count++;
			  }
			    if(type == String.class)
			    {
			    	for(int i = count ; i < tree.getChildCount() ; i++)
				  	{				  		
			    		String temp = tree.getChild(i).getText().trim();
				  		node.inStringValues.add(temp);
				  	}
			    }
			    else {
			    	for(int i = count ; i < tree.getChildCount() ; i++)
				  	{
				  		long temp = Long.parseLong(tree.getChild(i).getText());
				  		node.inValues.add(temp);
				  	}
				}
			  	
			  	return false;
		  }
		  
		  //处理String：is not null
		  if(node.operater == CreateTestLexer.IS )
		  {
			  node.isExist = true;
			  if(tree.getChildCount() == 2 )
			  {
				  return false;
			  }
			  if(tree.getChild(2).getType() == CreateTestLexer.NOT)
			  { 
				  node.isNotExist = true;
			  }
			  return false;
		  }
		  
		  columName = null;
		  if( type == String.class)
		  {
			  	
			  	node.value = 0;
			  	node.strValue  = tree.getChild(2).getText();
			  	return false;
		  }
		  else if(type == Integer.class)
		  {
			   node.value = Long.parseLong(tree.getChild(2).getText()) ;
			   return false;
		  }
		  else if(type == Long.class)
		  {
			  node.value = Long.parseLong(tree.getChild(2).getText()) ;
			  return false;
		  }
		  else if(type == Byte.class)
		  {
			  node.value = Long.parseLong(tree.getChild(2).getText()) ;
			  return false;
		  }
		  else if(type == Short.class)
		  {
			  node.value = Long.parseLong(tree.getChild(2).getText()) ;
			  return false;
		  }
	    	 throw  new Exception("atomProcess is error " + tree.getText());
	   }
	 
	 public void setData(byte[] data)
	 {
		  
		   String str = Bytes.toString(data);
		   this.columns = str.split(split);
	 }
	 
	 public boolean process( ) throws Exception
	 {
		 return this.process(root);
	 }
	 
	    private   boolean process(Node node) throws Exception
		   {
			   boolean tempReslut = false;
			   switch(node.nodeType)
			   {
			   case CreateTestParser.TOK_ORCONDITION:
				   int count = node.childNode.size();
				   	for(int i = 0 ; i <  count; i++)
				   	{
				   		tempReslut = process(node.childNode.get(i));
				   		if( tempReslut )
				   		{
				   			return true;
				   		}
				   	}
			   		return false;
			   case CreateTestParser.TOK_ANDCONDITION:
				   int countAnd = node.childNode.size();
				   	for(int i = 0 ; i <  countAnd; i++)
				   	{
				   		tempReslut = process(node.childNode.get(i));
				   		if( !tempReslut )
				   		{
				   			return false;
				   		}
				   	}
			   		return true;
			   case CreateTestParser.TOK_ATOMCONDITION:
				   	return  atomProcess( node);
			    default:
			    	throw new Exception("AND or OR is error " + node.columnName);
			   }
		   }
		      
		 private boolean  atomProcess(Node node) throws Exception
		 {
			  Class type =  node.filedType;
			  if( type == String.class)
			  {
				  type = null;
				  return atomStringProcess(node);
			  }
			  else if(type == Integer.class)
			  {
				  type = null;
				  return atomIntProcess(node);
			  }
			  else if(type == Long.class)
			  {
				  type = null;
				  return atomLongProcess(node);
			  }
			  else if(type == Byte.class)
			  {
				  type = null;
				  return atomByteProcess(node);
			  }
			  else if(type == Short.class)
			  {
				  type = null;
				  return atomShortProcess(node);
			  }
		    	 throw  new Exception("atomProcess is error " + node.columnName);
		   }
		 private boolean  atomByteProcess(Node node) throws Exception
		 {
			  int sign   			  =   node.operater;//tree.getChild(1).getType();
			  int pos = 	rp.getColumnPos(node.columnName);
			  byte tempValue = Bytes.toBytes(this.columns[pos])[0];
			  int value = 0;
			  if(tempValue < 0)
			  {
				  value = tempValue & 0x0FF;
			  }
			  else {
				  value = tempValue;
			  }
			  switch(sign)
			  {
			  case CreateTestLexer.EQUAL :
//				  System.out.println("RowFilter:value is " + value +"node.vlaue is  " + node.value);
				      if(value == node.value )
				      {				    	
				    	  return true;
				      }
				  return false;
			  case CreateTestLexer.NOTEQUAL :
			      if(value  != node.value )
			      {
			    	  return true;
			      }
	     		  return false;

			  case CreateTestLexer.LESS :
			      if(value <= node.value)
			      {
			    	  return true;
			      }
			      return false;
			  case CreateTestLexer.MORE :
			      if( value >= node.value )
			      {
			    	  return true;
			      }
			      return false;
			  case CreateTestLexer.IN :
				  if(node.hasNot)
				  {
					   int tempCount = node.inValues.size();
					  	for(int i = 0 ; i < tempCount ; i++)
					  	{
						      if( node.inValues.get(i) == value)
						      {
						    	  return false;
						      }
					  	}
					  	return true;
				  }
				  else
				  {
					  int tempCount = node.inValues.size();
					  	for(int i = 0 ; i < tempCount ; i++)
					  	{
//					  		System.out.println("Indexfilter is " + value +"node.inValues.get(i) is  " + node.inValues.get(i)+"********************************");
						      if( node.inValues.get(i) == value)
						      {
						    	  return true;
						      }
					  	}
					  	return false;
					  
				  }
			  case CreateTestLexer.IS :
				  return false;
			    default:
			    	 throw  new Exception("atom is error " + node.columnName);
			  }
		 }
		 
		 private boolean  atomShortProcess(Node node) throws Exception
		 {
			  int sign   			  =   node.operater;//tree.getChild(1).getType();
			  int pos = 	rp.getColumnPos(node.columnName);
			  int value = Integer.parseInt(this.columns[pos]);
			 
			
			  
			  switch(sign)
			  {
			  case CreateTestLexer.EQUAL :
				      if(value == node.value )
				      {
				    	  return true;
				      }
				  return false;
			  case CreateTestLexer.NOTEQUAL :
			      if(value  != node.value )
			      {
			    	  return true;
			      }
	     		  return false;

			  case CreateTestLexer.LESS :
			      if(value <= node.value)
			      {
			    	  return true;
			      }
			      return false;
			  case CreateTestLexer.MORE :
			      if( value >= node.value )
			      {
			    	  return true;
			      }
			      return false;
			  case CreateTestLexer.IN :
				  if(node.hasNot)
				  {
					   int tempCount = node.inValues.size();
					  	for(int i = 0 ; i < tempCount ; i++)
					  	{
						      if( node.inValues.get(i) == value)
						      {
						    	  return false;
						      }
					  	}
					  	return true;
				  }
				  else
				  {
					  int tempCount = node.inValues.size();
					  	for(int i = 0 ; i < tempCount ; i++)
					  	{
						      if( node.inValues.get(i) == value)
						      {
						    	  return true;
						      }
					  	}
					  	return false;
					  
				  }
			  case CreateTestLexer.IS :
				  return false;
			    default:
			    	 throw  new Exception("atom is error " + node.columnName);
			  }
		 }
		 
		 private boolean  atomIntProcess(Node node) throws Exception
		 {
			  int sign   			  =   node.operater;//tree.getChild(1).getType();
			  int pos = 	rp.getColumnPos(node.columnName);
			  long value = Long.parseLong(this.columns[pos]);
			 
			
			  switch(sign)
			  {
			  case CreateTestLexer.EQUAL :
				      if(value == node.value )
				      {
				    	  return true;
				      }
				  return false;
			  case CreateTestLexer.NOTEQUAL :
				   
			      if(value  != node.value )
			      {
			    	  return true;
			      }
	     		  return false;

			  case CreateTestLexer.LESS :
				 
			      if(value <= node.value)
			      {
			    	  return true;
			      }
			      return false;
			  case CreateTestLexer.MORE :
				  
			      if( value >= node.value )
			      {
			    	  return true;
			      }
			      return false;
			  case CreateTestLexer.IN :
				  if(node.hasNot)
				  {
					  
					   int tempCount = node.inValues.size();
					  	for(int i = 0 ; i < tempCount ; i++)
					  	{
						      if( node.inValues.get(i) == value)
						      {
						    	  return false;
						      }
					  	}
					  	return true;
				  }
				  else
				  {
					  int tempCount = node.inValues.size();
					  	for(int i = 0 ; i < tempCount ; i++)
					  	{
//							  System.out.println("RowFilter: in  value is " + value +"**** is  " +node.inValues.get(i));
						      if( node.inValues.get(i) == value)
						      {
						    	  return true;
						      }
					  	}
					  	return false;
					  
				  }
			  case CreateTestLexer.IS :
				  return false;
			    default:
			    	 throw  new Exception("atom is error " + node.columnName);
			  }
		   }
		   
		   private boolean  atomLongProcess(Node node) throws Exception
		   {
				  int sign   			  =   node.operater;//tree.getChild(1).getType();
				   long value  = 0;
				   if(node.isFunction)
				   {
					   	//TODO
					   //value = Bytes.toLong(SupportFunction.functionCall(node.funtionName,node.funtionParaNameArrayList,rp.getColumnData(node.columnName,rowData)));
				   }
				   else {
					   int pos = 	rp.getColumnPos(node.columnName);
						value = Long.parseLong(this.columns[pos]);
				   }
				  switch(sign)
				  {
				  case CreateTestLexer.EQUAL :
					      if(value == node.value )
					      {
					    	  return true;
					      }
					  return false;
				  case CreateTestLexer.NOTEQUAL :
				      if(value  != node.value )
				      {
				    	  return true;
				      }
		     		  return false;

				  case CreateTestLexer.LESS :	 
				      if(value <= node.value)
				      {
				    	  return true;
				      }
				      return false;
				  case CreateTestLexer.MORE :
				      if( value >= node.value )
				      {
				    	  return true;
				      }
				      return false;
				  case CreateTestLexer.IN :
					  if(node.hasNot)
					  {
						   int tempCount = node.inValues.size();
						  	for(int i = 0 ; i < tempCount ; i++)
						  	{
							      if( node.inValues.get(i) == value)
							      {
							    	  return false;
							      }
						  	}
						  	return true;
					  }
					  else
					  {
						  int tempCount = node.inValues.size();

						  	for(int i = 0 ; i < tempCount ; i++)
						  	{
							      if( node.inValues.get(i) == value)
							      {
							    	  return true;
							      }
						  	}
						  	return false;	  
					  }
				  case CreateTestLexer.IS :
					  return false;
				    default:
				    	 throw  new Exception("atom is error " + node.columnName);
				  }
		   }
		   
		   private boolean  atomStringProcess(Node node) throws Exception
		   {
			   	//专门处理is null 和 is not null;
			   int pos = 	rp.getColumnPos(node.columnName);
			   
			   
			   
			   byte[] data = Bytes.toBytes(this.columns[pos]);
			   //equals
			  // System.out.p
			   
			   if(node.operater==CreateTestLexer.LIKE)
			   {
				   
				   return stringLike.getValue(this.columns[pos],node.strValue);
				   
			   }
			   if(node.operater==CreateTestLexer.EQUAL)
			   {
				   
				   //node.strValue = node.strValue.substring(1, node.strValue.length()-1);
				   
				   if(node.strValue.equals("'"+this.columns[pos]+"'"))
				   {
					   return true;
				   }
				   return false;
				   
			   }
			   
			   
			   if(node.isExist)
			   {	
				   if(node.isNotExist)
				   {
					   for(int i = 0 ; i < data.length ; i++)
					   {
						   if(data[i] != Node.NULLStringData)
						   { 
							   return true;   
						   }
					   }
					   return false;
				   }
				   else {
					   for(int i = 0 ; i < data.length ; i++)
					   {
						   if(data[i] != Node.NULLStringData)
						   { 
							   return false;   
						   }
					   }
					   return true;
				   }
			   }
			      //处理 in, not in
			   	  String stringValue =this.columns[pos];;
			   	  System.out.println("StringValue:" + stringValue);
			   	  if(node.operater == CreateTestLexer.IN )
			   	  {
					  if(node.hasNot)
					  {					  
						    int tempCount = node.inStringValues.size();
						  	for(int i = 0 ; i < tempCount ; i++)
						  	{	
							      if( node.inStringValues.get(i).equals(stringValue))
							      {
							    	  return false;
							      }
						  	}
						  	return true;
					  }
					  else
					  {
						  int tempCount = node.inStringValues.size();
						  	for(int i = 0 ; i < tempCount ; i++)
						  	{
							      if( node.inStringValues.get(i).equals(stringValue))
							      {
							    	  return true;
							      }
						  	}
						  	return false;						  
					  }
			   	  }				   
			   return false;
		   }
	 public static void main(String[] args)	
	 {
		 
	 }
	
}
