package org.cProc.sql.Tree;

import java.util.TreeSet;

import org.antlr.runtime.tree.CommonTree;
import org.cProc.GernralRead.ReadData.ParseIndexFormat;
import org.cProc.function.SupportFunction;
import org.cProc.index.BPlusTree.Data.Data;
import org.cProc.sql.CreateTestLexer;
import org.cProc.sql.CreateTestParser;

import org.cProc.tool.Bytes;


public class IndexFilterTree {
	
	private ParseIndexFormat indexFormat = null;
    private TreeSet<String> set = new  TreeSet<String>() ;
    private Data data  = null;
	
	private Node root = new Node();
	
		
	public IndexFilterTree(CommonTree tree, ParseIndexFormat  indexFormat , TreeSet<String> set)
	{
		try {
			this.set = set;
			this.indexFormat = indexFormat;
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
				node.funtionName = tree.getChild(0).getChild(0).getText();
				  if(!set.contains(columName))
				  {	
					  node.reslut = true;
					  return true;
				  }
				  node.filedPos = indexFormat.getColunmPos(columName);
				   type =  SupportFunction.functionReturnValueMap.get(tree.getChild(0).getChild(0).getText());
				  node.filedType = type;
			 }
		 else {
			     columName = tree.getChild(0).getText();
				 node.columnName = columName;
				  if(!set.contains(columName))
				  {	
					  node.reslut = true;
					  return true;
				  }
				  node.filedPos = indexFormat.getColunmPos(columName);
				   type =  indexFormat.getColunmType(columName);
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
			  
			  	for(int i = count ; i < tree.getChildCount() ; i++)
			  	{
			  		long temp = Long.parseLong(tree.getChild(i).getText());
			  		node.inValues.add(temp);
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
	 
	 public void setData(Data data)
	 {
		   this.data = data;
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
			 //如果该节点的列为不在索引的列；
			 if(node.reslut)  
			 {
				 return true;
			 }
			  Class type =  node.filedType;
			 // System.out.println("columnname is " + node.columnName +"  function name is " + node.funtionName );
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
			  //byte value =   data.getIndexFiled(indexFormat.getColunmPos(columName))[0];
			  byte tempValue =   data.getIndexFiled(node.filedPos)[0];
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
				   
				  //if nullSet is 1 in the pos , the value of the pos in the data is null; 
				   if(node.isExist)
				   {	
					   if(node.isNotExist)
					   {
						   //check this  is null ; 
						   //if is null then return false;
						   //if is not null then return true;
						 
						   return true;
					   }
					   else {
						 //check this  is null ; 
						   //if is null  then return true;
						   //if is not null then return false;
						  
						   return false;
					   }
				   }
			    default:
			    	 throw  new Exception("atom is error " + node.columnName);
			  }
		 }
		 
		 private boolean  atomShortProcess(Node node) throws Exception
		 {
			  int sign   			  =   node.operater;//tree.getChild(1).getType();
			  short tempValue =   Bytes.toShort(data.getIndexFiled(node.filedPos));
			  int value = 0;
			  if(tempValue < 0)
			  {
				  value = tempValue & 0x0FFFF;
			  }
			  else {
				  value = tempValue;
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
				  
				  //if nullSet is 1 in the pos , the value of the pos in the data is null; 
				   if(node.isExist)
				   {	
					   if(node.isNotExist)
					   {
						   //check this  is null ; 
						   //if is null then return false;
						   //if is not null then return true;
						   
						   return true;
					   }
					   else {
						 //check this  is null ; 
						   //if is null  then return true;
						   //if is not null then return false;
						   
						   return false;
					   }
				   }
			    default:
			    	 throw  new Exception("atom is error " + node.columnName);
			  }
		 }
		 
		 private boolean  atomIntProcess(Node node) throws Exception
		 {
			  int sign   			  =   node.operater;//tree.getChild(1).getType();
			  int tempValue =   Bytes.toInt(data.getIndexFiled(node.filedPos));

			  long value = 0;
			  if(tempValue < 0)
			  {
				  value = tempValue & 0x0FFFFFFFF;
			  }
			  else
			  {
			       value = tempValue;
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
				  
				  //if nullSet is 1 in the pos , the value of the pos in the data is null; 
				   if(node.isExist)
				   {	
					   if(node.isNotExist)
					   {
						   //check this  is null ; 
						   //if is null then return false;
						   //if is not null then return true;
						   
						   return true;
					   }
					   else {
						 //check this  is null ; 
						   //if is null  then return true;
						   //if is not null then return false;
						   
						   return false;
					   }
				   }
			    default:
			    	 throw  new Exception("atom is error " + node.columnName);
			  }
		   }
		   
		   private boolean  atomLongProcess(Node node) throws Exception
		   {
			   long value  = 0;
//			   if(node.isFunction)
//			   {
//				  byte[] reslut = SupportFunction.functionCall(node.funtionName,node.funtionParaNameArrayList,data.getIndexFiled(node.filedPos)); 
//				   value = Bytes.toLong(SupportFunction.functionCall(node.funtionName,node.funtionParaNameArrayList,data.getIndexFiled(node.filedPos)));
//			   }
//			   else {
				    value =    Bytes.toLong(data.getIndexFiled(node.filedPos));
			//   }
			   int sign   			  =   node.operater;//tree.getChild(1).getType();
			   
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
					  
					  //if nullSet is 1 in the pos , the value of the pos in the data is null; 
					   if(node.isExist)
					   {	
						   if(node.isNotExist)
						   {
							   //check this  is null ; 
							   //if is null then return false;
							   //if is not null then return true;
							   
							   return true;
						   }
						   else {
							 //check this  is null ; 
							   //if is null  then return true;
							   //if is not null then return false;
							  
							   return false;
						   }
					   }
				    default:
				    	 throw  new Exception("atom is error " + node.columnName);
				  }
		   }
		   
		   private boolean  atomStringProcess(Node node) throws Exception
		   {
			   return false;
		   }

	 
	
	 public static void main(String[] args)	
	 {
		 
	 }
	
}
