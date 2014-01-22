package org.cProc.log.createIndexLog;

import java.util.ArrayList;

public interface IndexThreadInfor {
	
	//must can create meesage format:
	//soruceFileCount:tableCount:fileCount:oneTableFile:sourceFile:sourceFile:IndexNameCount:IndexName:Version:IndexFileName:IndexName:Version:IndexFileName;
	//tableCount:�ۼ��ж��ٸ���;  
	//fileCount:�ۼ��ж��ٸ��ļ�;
	//oneTableFile:һ������ļ�����
	//IndexNameCount:IndexName:Version   �������������ƣ�����汾
	
	
	public String inforSeperater =":";
	//change the getIndexThreadsInfor reslut to the format :  task_number
	public ArrayList<String> getIndexThreadsInfor();
	public String parseIndexThreadInfor(String infor);

	
	
}
