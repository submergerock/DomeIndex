package org.cProc.distributed.createIndex;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.fs.FileSystem;
import org.cProc.GernralRead.ReadData.ParseIndexFormat;


/**
 * 索引生成接口
 * 
 * @author zxc
 * 
 */
public interface IIndexCreater {

	public void setIndexPath(String indexPath)throws IOException;

	/**
	 * 
	 * @param data
	 */
	public void addIndexData(Field field);

	public void setFileSystem(FileSystem fs);

	/**
	 * 写入索引
	 */
	public void create()throws IOException;
	
	public void setSources(ArrayList<String>  files);
	/*
	 * 设置index的格�?
	 * 
	 */
	public void setIndexFormat(ParseIndexFormat format);
	
	public ParseIndexFormat getIndexFormat();
	
	/**
	 * 设置�?���?��字节
	 * */
	
    public void setMinValue(ArrayList<byte []> data);
    
    public void setMaxValue(ArrayList<byte []> data);
    
    
    public String getTempLocalFileName();
    
    public String getHDFSFileName();

	
	public void setUuid(String uuid);
	
	

}
