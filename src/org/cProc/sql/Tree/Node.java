package org.cProc.sql.Tree;

import java.util.ArrayList;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class Node {
//	1.nodeType : 类型， int型的  : 决定是and ,or ,atom
//	2.filedType: 类型， Class;   : 决定是啥类型
//	3.reslut  : 类型， boolean; : 决定值是啥：主要因为非索引字段的存在
//	4.name	   : 类型， String;  : 字符串名
//	5.operater: 类型， int ;    : 操作符名
//	6.value    : 类型， long;    : 条件值；
	public int nodeType = 0;
	public Class filedType = null;
	public boolean reslut = false;
	public String columnName = "";
	public String funtionName ="";
	public ArrayList<String> funtionParaNameArrayList =  new ArrayList<String>();
	public boolean isFunction = false;
	public int operater = -1;
	public long value =0;
	public Node parent = null; 
	public int filedPos = -1;
	public String  strValue = "";
	
	public ArrayList<Node> childNode = new ArrayList<Node>();
	//in
	public boolean hasNot = false;
	public ArrayList<Long> inValues = new ArrayList<Long>();
	public ArrayList<String> inStringValues = new ArrayList<String>();
	
	//is not null
	public boolean isExist = false;
	public boolean isNotExist = false;
	public int nullInforPos = 0;
	public static final byte NULLStringData = 0;
	
}
