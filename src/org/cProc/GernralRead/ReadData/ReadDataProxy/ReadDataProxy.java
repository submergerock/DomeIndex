package org.cProc.GernralRead.ReadData.ReadDataProxy;

import java.io.IOException;

public interface ReadDataProxy {

	public void startReadFile()throws IOException;
	public void closeReadFile()throws IOException;
	public boolean readNextData()throws IOException;
	public byte[] getColumnData(int pos);
	public long  getOffset();
	public void  setFileName(String fileName );

	
}
