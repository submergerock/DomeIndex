package org.cProc.log.indexmapinfor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.cProc.log.createIndexLog.IndexThreadInfor;



public class IndexMapInfor {
	
	
	
	//message format: 
	//soruceFileCount:tableCount:fileCount:sourceFile:sourceFile:IndexNameCount:IndexName:Version:IndexFileName:IndexName:Version:IndexFileName;
	//tableCount:�ۼ��ж��ٸ���;  
	//fileCount:�ۼ��ж��ٸ��ļ�;
	//oneTableFile:һ������ļ�����
	//IndexNameCount:IndexName:Version   �������������ƣ�����汾
	public static String changeIndexFileInforToMessage(ArrayList<LocalTempIndexFileInfor> list)
	{
		int sourceFilecount = 0 ;
		int tableCount =0 ;
		StringBuilder sb = new  StringBuilder();
		for(int i = 0 ; i < list.size() ; i++)
		{
			LocalTempIndexFileInfor fileInfor = list.get(i);
			ArrayList<String> sourceFileArrayList = fileInfor.getSourceFileArray();
			HashMap<String, String> indexAndIndexMap = fileInfor.getIndexAndVersionMap();
			sourceFilecount = sourceFilecount + sourceFileArrayList.size();
			sb.append(sourceFileArrayList.size() +IndexThreadInfor.inforSeperater );
			for(int k = 0; k < sourceFileArrayList.size() ; k++)
			{
				sb.append(sourceFileArrayList.get(k) + IndexThreadInfor.inforSeperater);
			}
			int indexNumber = indexAndIndexMap.size();
			Iterator<Entry<String, String>> iterator = indexAndIndexMap.entrySet().iterator();
			sb.append(indexNumber+IndexThreadInfor.inforSeperater);
			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				sb.append(entry.getValue()+IndexThreadInfor.inforSeperater);
				sb.append(entry.getKey()+IndexThreadInfor.inforSeperater);
			}
		}
		return sourceFilecount+IndexThreadInfor.inforSeperater+tableCount+IndexThreadInfor.inforSeperater+sb.toString().substring(0, sb.length()-1);
	}
	
	
	public static String createIndexInfor(IndexMapInfor mapInfor)
	{
		
		//soruceFileCount:tableCount:fileCount:sourceFile:sourceFile:IndexNameCount:IndexName:Version:IndexFileName:IndexName:Version:IndexFileName;
		StringBuilder sb = new  StringBuilder();
		HashMap<String, ArrayList<String>> sourceAndIndexMap = mapInfor.getSourceAndIndexMap();
		 HashMap<String,String> indexVersionMap = mapInfor.getIndexVerionMap();
		Iterator<Entry<String, ArrayList<String>>> iterator = sourceAndIndexMap.entrySet().iterator();
		while( iterator.hasNext())
		{
			Entry<String, ArrayList<String>> entry = iterator.next();
			sb.append(entry.getKey().split(IndexThreadInfor.inforSeperater).length);
			sb.append(IndexThreadInfor.inforSeperater);
			sb.append(entry.getKey());
		//	sb.append(IndexThreadInfor.inforSeperater);
			sb.append(entry.getValue().size());
			sb.append(IndexThreadInfor.inforSeperater);
			for(int i = 0 ; i < entry.getValue().size() ; i++)
			{
				String version = indexVersionMap.get(entry.getValue().get(i));
				sb.append(version);
				sb.append(IndexThreadInfor.inforSeperater);
				sb.append(entry.getValue().get(i));
				sb.append(IndexThreadInfor.inforSeperater);
			}
			
		}
		return mapInfor.sourceFileCount+IndexThreadInfor.inforSeperater+mapInfor.tableCount
						+IndexThreadInfor.inforSeperater
						+sb.toString().substring(0, sb.length()-1);
	}
	
	public static  IndexMapInfor    getIndexNameFromMessage(String message )
	{
		 IndexMapInfor map = new IndexMapInfor();
		 map.parseMessageInfor(message);
		 return map;
	}
	
	private int tableCount = 0 ;

	private int sourceFileCount = 0 ;
	private HashMap<String, ArrayList<String>> sourceAndIndexMap =  new  HashMap<String, ArrayList<String>>();
	//indexfile:indexName_version
	private HashMap<String,String> indexVersionMap = new HashMap<String,String>();

	
	public IndexMapInfor()
	{
		
	}
	
	public HashMap<String, ArrayList<String>> getSourceAndIndexMap()
	{
		return sourceAndIndexMap;
	}
	
	public  HashMap<String,String> getIndexVerionMap()
	{
		return indexVersionMap;
	}
	
	public int getTableCount() {
		return tableCount;
	}

	public void setTableCount(int tableCount) {
		this.tableCount = tableCount;
	}
	
	public int getSourceFileCount() {
		return sourceFileCount;
	}

	public void setSourceFileCount(int sourceFileCount) {
		this.sourceFileCount = sourceFileCount;
	}
		
	//message format: 
	//sourceFileCount:tableCount:fileCount:oneTableFile:sourceFile:sourceFile:IndexNameCount:IndexName:Version:IndexFileName:IndexName:Version:IndexFileName;
	//tableCount:�ۼ��ж��ٸ���;  
	//fileCount:�ۼ��ж��ٸ��ļ�;
	//oneTableFile:һ������ļ�����
	//IndexNameCount:IndexName:Version   �������������ƣ�����汾
	public void parseMessageInfor(String line)
	{
		String[] inforStrings = line.split(IndexThreadInfor.inforSeperater);
		//sourceFileCount:tableCount:
		this.setSourceFileCount(Integer.parseInt(inforStrings[0]));
		this.setTableCount(Integer.parseInt(inforStrings[1]));
		int fileCount = 0 ;
		int pos = 2;
		while (fileCount < this.sourceFileCount) {
			//fileCount:oneTableFile:sourceFile:sourceFile:IndexNameCount:IndexName:Version:IndexFileName:IndexName:Version:IndexFileName;
			 int tempSourceFileCount = Integer.parseInt(inforStrings[pos]);
			 pos++;
			 fileCount = fileCount + tempSourceFileCount;
			 StringBuilder sourceFileKey = new StringBuilder();
			 for(int i = 0 ; i < tempSourceFileCount ; i ++)
			 {
				 sourceFileKey.append(inforStrings[pos]);
				 sourceFileKey.append(IndexThreadInfor.inforSeperater);
				 pos++;
			 }
			 sourceAndIndexMap.put(sourceFileKey.toString(), new ArrayList<String>());
			 int tempIndexFileCount = Integer.parseInt(inforStrings[pos]);
			 pos++;
			 String key = sourceFileKey.toString();
			 for(int k = 0 ;  k < tempIndexFileCount; k++  )
			 {
				 //indexName:index
				 String indexName = inforStrings[pos];
				 pos++;
				 String indexVersion = inforStrings[pos];
				 pos++;
				 String indexFileName =  inforStrings[pos];
				 pos++;
				 indexVersionMap.put(indexFileName, indexName+IndexThreadInfor.inforSeperater+indexVersion);
				 sourceAndIndexMap.get(key).add(indexFileName);
			 }
		}
	}

	public void printlnMapInfor()
	{
		System.out.println("sourceFileCount  is "+this.sourceFileCount);
		System.out.println("tableCount  is "+this.tableCount);
		Iterator<Entry<String, ArrayList<String>>> sourceAndIndexIterator  = sourceAndIndexMap.entrySet().iterator();
		while (sourceAndIndexIterator.hasNext()) {
			Entry<String, ArrayList<String>> entry = sourceAndIndexIterator.next();
			String key = entry.getKey();
			ArrayList<String> list = entry.getValue();
			for(int i = 0 ; i < list.size() ; i ++)
			{
				System.out.println("fileName is  : " + key  +"  ------>    " +list.get(i) );
			}
		
		}
		Iterator<Entry<String, String>> indexVersionIterator  = indexVersionMap.entrySet().iterator();
		while (indexVersionIterator.hasNext()) {
			Entry<String, String> entry = indexVersionIterator.next();
			String key = entry.getKey();
			String value = entry.getValue();
			System.out.println("indexFileName is " + key +"  version is    " +  value);
		}
	}
	
	
	public static void  main(String[] args)
	{
		//sourceFileCount:tableCount:fileCount:oneTableFile:sourceFile:sourceFile:IndexNameCount:IndexName:Version:IndexFileName:IndexName:Version:IndexFileName;
	     
		ArrayList<LocalTempIndexFileInfor> list = new  ArrayList<LocalTempIndexFileInfor> ();
		LocalTempIndexFileInfor a = new LocalTempIndexFileInfor();
		ArrayList<String>  sourceFilesA = new ArrayList<String>();
		for(int i = 0 ; i < 2 ; i++)
		{
			sourceFilesA.add("a"+i);
		}
		a.addSourceFile(sourceFilesA);
		a.addIndexAndVersionMap("ahdfs1", "aa", 1);
		a.addIndexAndVersionMap("ahdfs2", "ab", 2);
		list.add(a);
		
		LocalTempIndexFileInfor b = new LocalTempIndexFileInfor();
		ArrayList<String>  sourceFilesB = new ArrayList<String>();
		for(int i = 0 ; i < 2 ; i++)
		{
			sourceFilesB.add("b"+i);
		}
		b.addSourceFile(sourceFilesB);
		b.addIndexAndVersionMap("bhdfs1", "ba", 1);
		b.addIndexAndVersionMap("bhdfs2", "bb", 2);
		list.add(b);
		
		
		String message = changeIndexFileInforToMessage(list);
		System.out.println(message);
		IndexMapInfor mapInfor = getIndexNameFromMessage(message);
		mapInfor.printlnMapInfor();
		System.out.println(createIndexInfor(mapInfor));
		IndexMapInfor mapInfor2 = getIndexNameFromMessage(createIndexInfor(mapInfor));
		mapInfor2.printlnMapInfor();
	}
	
}
