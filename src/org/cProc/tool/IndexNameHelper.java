package org.cProc.tool;

import javax.swing.filechooser.FileNameExtensionFilter;

public class IndexNameHelper {
	
	public static String FileNameSeperater="_"; 
	public static String getIndexName(long startTime ,long endTime, int fileNumber, String indexName, int indexVersion, String radom ,String extend)
	{
			return startTime+FileNameSeperater+endTime+FileNameSeperater+fileNumber
			+FileNameSeperater+indexName+FileNameSeperater+indexVersion+FileNameSeperater+radom+extend;
	}

}
