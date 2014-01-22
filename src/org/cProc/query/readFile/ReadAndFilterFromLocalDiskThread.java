package org.cProc.query.readFile;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.cProc.sql.RowFilter;




public class ReadAndFilterFromLocalDiskThread  extends ReadFromLocalDiskThreadBase<DataPosOffset> {
	public static final Log LOG = LogFactory.getLog(ReadAndFilterFromLocalDiskThread.class.getName());
	private int dataSize = 0;
	private String startPath = "";
	private String fileName = "";
	private String offsets = "";
	private boolean isFilter =false;
	public static  String fileSeperatorString ="/";
	private RowFilter  rf = null;
	
	//conPath  HDFS的配置文件，主要关系到有多少个硬盘的问题
	//dataSize  数据的大小
	//condition  条件变量，主要用来同步的
	//isFilter    读数据的时候，是否需要过滤
	//RowFilter  过滤器
	public static ArrayList<ReadAndFilterFromLocalDiskThread> InitReadThread(
			String conPath, int dataSize, Integer condition ,boolean isFilter ,RowFilter rf) {
		ArrayList<ReadAndFilterFromLocalDiskThread> list = new ArrayList<ReadAndFilterFromLocalDiskThread>();
		if(conPath == null)
		{
			ReadAndFilterFromLocalDiskThread thread = new ReadAndFilterFromLocalDiskThread(
					condition);
			thread.setDiskPath("D:");
			thread.setDataSize(dataSize);
			thread.setIsFilter(isFilter);
			RowFilter rfOne = new RowFilter(rf.getSQL(), rf.getFS());
			thread.setRowFilter(rfOne);
			list.add(thread);
			return list;
		}
		Configuration conf = new Configuration();
	   conf.addResource(new Path(conPath));
		String disks = conf.get("dfs.data.dir");
		if (disks.length() == 0) {
			return null;
		}
		String[] offsets = disks.split(",");
		for (int i = 0; i < offsets.length; i++) {
			ReadAndFilterFromLocalDiskThread thread = new ReadAndFilterFromLocalDiskThread(
					condition);
			thread.setDiskPath(offsets[i].trim()+fileSeperatorString);
			thread.setDataSize(dataSize);
			thread.setIsFilter(isFilter);
			RowFilter rfOne = new RowFilter(rf.getSQL(), rf.getFS());
			thread.setRowFilter(rfOne);
			list.add(thread);
		}
		return list;
	}
	
	public void setIsFilter(boolean isFilter)
	{
		this.isFilter = isFilter;
	}
	
	public void setRowFilter(RowFilter rowFilter)
	{
		this.rf = rowFilter;
	}
	
	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
	}

	public void setDiskPath(String startPath) {
		this.startPath = startPath;
	}
	
	public ReadAndFilterFromLocalDiskThread(Integer condition) {
		super(condition);
	}

	public void addReadIndex(String fileName, String offsets) {
		if (fileName.startsWith(startPath)) {
			this.fileName = fileName;
			this.offsets = offsets;
			addReadIndex();
		}
	}
	
	//@override ;
	protected DataPosOffset  currentReadPos()
	{
		return new DataPosOffset(fileName,offsets);
	}

	//@override ;
	protected  void readData( DataPosOffset data)
	{
		
		try
		{
//		System.out.println("@gyy: fileName is "+data.getFileName()+"  offsets is " + data.getOffsets());
		File f = new File(data.getFileName());
		String[] offsets = data.getOffsets().split(",");
		long offset = 0;
		long lastOffset = 0;
		byte[] rawInfo = new byte[dataSize];
		//long length = dataSize;
		if (f.exists()) {
			FileInputStream input = new FileInputStream(f);
			for (int i = 0; i < offsets.length; i++) {
				lastOffset = offset;
				offset = Long.valueOf(offsets[i]);
					if (i == 0) {
						 input.skip(offset);
					} else {
						 input.skip(offset - lastOffset-dataSize);
					}
//				System.out.println("@gyy:file is " + fileName +" the offset is " +offset);
				 input.read(rawInfo, 0, dataSize);
				 if(isFilter)
				 {
					 if(rf.isDataOK(rawInfo))
					 {
					
						 writeOneData(rawInfo, dataSize);
					 }
					 else {
						 //LOG.warn("data is not satify");
					}
				 }
				 else {
						writeOneData(rawInfo,dataSize);
				}
			
			}
			input.close();
			//LOG.info("file name is:"+data.getHdfsPath()+"  "+count);
		}
	}catch(Exception e)
	{
		e.printStackTrace();
	}
}
	
}


