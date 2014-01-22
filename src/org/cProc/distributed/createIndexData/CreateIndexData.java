package org.cProc.distributed.createIndexData;

import org.cProc.log.indexmapinfor.LocalTempIndexFileInfor;



public interface CreateIndexData  {

	public void addInputFileName(String Name);
	
	public void setTableName( String ltype);
	
	public boolean startCreateIndex();
	
	public void setGenerateConfFile(String filename);
	
	public void setAppName(String appName);
	
	public void setLocalTempIndexFileInfor(LocalTempIndexFileInfor  indexFileInfor);
	


}
