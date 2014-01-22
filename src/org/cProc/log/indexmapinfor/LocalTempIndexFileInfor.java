package org.cProc.log.indexmapinfor;

import java.util.ArrayList;
import java.util.HashMap;

import org.cProc.log.createIndexLog.IndexThreadInfor;


public class LocalTempIndexFileInfor {
		
	//sourceFile:soruceFile:IndexName:Version:IndexName
	private ArrayList<String> sourceFileArray = new   ArrayList<String>();
	//from ----- to; 
	private  HashMap<String, String> fromLocalToHDFS = new HashMap<String, String>();
	//indexVersion;
	private HashMap<String ,String> indexAndVersionMap = new HashMap<String, String>();
	
	public void addSourceFile(ArrayList<String> sourceFiles)
	{
		for(int i = 0 ; i < sourceFiles.size() ; i ++)
		{
			sourceFileArray.add(sourceFiles.get(i) );
		}
	}
	
	public void addLocalToHDFSInFor(String localFile ,String hdfsFile)
	{
		fromLocalToHDFS.put(localFile, hdfsFile);
	}
	
	public void addIndexAndVersionMap(String hdfsFile , String indexName , int indexVersion)
	{
		indexAndVersionMap.put(hdfsFile, indexName+IndexThreadInfor.inforSeperater+indexVersion);
	}
	
	
	public void removeLocalFileInfor(String localFile)
	{
		indexAndVersionMap.remove(fromLocalToHDFS.get(localFile));
		fromLocalToHDFS.remove(localFile);
	}
	
	public ArrayList<String> getSourceFileArray()
	{
		return  sourceFileArray;
	}
	public   HashMap<String, String> getFromLocalToHDFSMap()
	{
		return  fromLocalToHDFS;
	}
	
	public  HashMap<String ,String> getIndexAndVersionMap()
	{
		return  indexAndVersionMap;
	}
	

}
