package org.cProc.query.readFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.cProc.sql.RowFilter;
import org.cProc.sql.TextRowFilter;
import org.cProc.tool.Bytes;


public class TextReadAndFilterFromLocalDiskThread  extends ReadFromLocalDiskThreadBase<DataPosOffset> {
	public static final Log LOG = LogFactory.getLog(ReadAndFilterFromLocalDiskThread.class.getName());
	private int dataSize = 0;
	private String startPath = "";
	private String fileName = "";
	private String offsets = "";
	private boolean isFilter =false;
	public static  String fileSeperatorString ="/";
	private TextRowFilter  rf = null;
	
	//conPath  HDFS的配置文件，主要关系到有多少个硬盘的问题
	//dataSize  数据的大小
	//condition  条件变量，主要用来同步的
	//isFilter    读数据的时候，是否需要过滤
	//RowFilter  过滤器
	public static ArrayList<TextReadAndFilterFromLocalDiskThread> InitReadThread(
			String conPath, int dataSize, Integer condition ,boolean isFilter ,TextRowFilter rf) {
		ArrayList<TextReadAndFilterFromLocalDiskThread> list = new ArrayList<TextReadAndFilterFromLocalDiskThread>();
		if(conPath == null)
		{
			TextReadAndFilterFromLocalDiskThread thread = new TextReadAndFilterFromLocalDiskThread(
					condition);
			thread.setDiskPath("D:");
			thread.setDataSize(dataSize);
			thread.setIsFilter(isFilter);
			TextRowFilter rfOne = new TextRowFilter(rf.getSQL(), rf.getFS());
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
			TextReadAndFilterFromLocalDiskThread thread = new TextReadAndFilterFromLocalDiskThread(
					condition);
			thread.setDiskPath(offsets[i].trim()+fileSeperatorString);
			//LOG.info("disk is:"+offsets[i].trim());
			thread.setDataSize(dataSize);
			thread.setIsFilter(isFilter);
			TextRowFilter rfOne = new TextRowFilter(rf.getSQL(), rf.getFS());
			thread.setRowFilter(rfOne);
			list.add(thread);
		}
		return list;
	}
	
	public void setIsFilter(boolean isFilter)
	{
		this.isFilter = isFilter;
	}
	
	public void setRowFilter(TextRowFilter rowFilter)
	{
		this.rf = rowFilter;
	}
	
	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
	}

	public void setDiskPath(String startPath) {
		this.startPath = startPath;
	}
	
	public TextReadAndFilterFromLocalDiskThread(Integer condition) {
		super(condition);
	}

	public void addReadIndex(String fileName, String offsets) {
		if (fileName.startsWith(startPath)) {
			//System.out.println("@gyy:startPath is " + startPath);
			this.fileName = fileName;
			this.offsets = offsets;
			addReadIndex();
		}
	}
	
	//@override ;
	protected DataPosOffset  currentReadPos()
	{
		return null;
		//return new DataPosOffset(fileName,offsets);
	}

	//@override ;
	protected  void readData( DataPosOffset data)
	{
		try {
			
			File f = new File(data.getFileName());
			String[] offsets = data.getOffsets().split(",");
			long offset = 0;
			long lastOffset = 0;
			int length = 0;
			int Strlength = 0;
			//System.out.println(offsets.toString());
			//LOG.info("f.exists()  --->"+f.exists());
			if (f.exists()) {
				BufferedReader reader =  new BufferedReader(new FileReader(f));
				for (int i = 0; i < offsets.length; i++) {
					lastOffset = offset;
					offset = Integer.valueOf(offsets[i]);
					if (i == 0) {
						 reader.skip(offset);
					} else {
						reader.skip(offset - lastOffset - Strlength - 2 );
					}
					//LOG.info("@yzy:file is " + fileName +" the offset is " +offset);
					
					 String dataStr = reader.readLine();
					 
					//.info("@yzy:readLine is"+dataStr);
					
					 byte[] rawInfo =Bytes.toBytes(dataStr);
					 if(isFilter)
					 {
						if(length <= 0)
						{
							length = rawInfo.length;
						}
					//	byte [] data1 = new byte[Strlength];
					//	System.arraycopy(rawInfo, 0, data1, 0, length);
						if(rf.isDataOK(rawInfo)){
							 writeOneData(rawInfo, rawInfo.length);
						}
						
					//	System.out.println(data1.toString());
					 }
					 else {
//						if(length <= 0)
//						{
//							length = rawInfo.length;
//						}
						 writeOneData(rawInfo, rawInfo.length);
						//System.out.println(data1.toString());
					}
				
				}
				reader.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
//		File file = new File("Q:/cProc.txt");
//		RandomAccessFile ra = new RandomAccessFile(file,"rw");
//		String bbbb = ra.readLine();
//		System.out.println(bbbb);
//		int bbbbL = bbbb.getBytes().length;
//		int bbbbE = 107;
//		ra.skipBytes(bbbbE - bbbbL);
//		System.out.println(ra.readLine());
//		ra.close();
//		
//		BufferedReader reader =  new BufferedReader(new FileReader(file));
//		String aaaa = reader.readLine();
//		System.out.println(aaaa);
//		long aaaaL = aaaa.getBytes().length;
//		System.out.println(aaaaL);
//		
//		reader.skip(66);
//		System.out.println(reader.readLine());
//		reader.close();
		
		DataPosOffset data = null;
		//new DataPosOffset("Q:/test.cvs", "479133,835696,1044391,1258152,1711632,1866347,2823201,2867814,3320983,3517527,3836730,4245819,4250448,4646852,5413435,6102801,6246261,6330084,6588340,6636561,6972361,6974841,6999495,7558762,7654777,7837210,8194356,8447115,8447951,8461032,8904266,9833638,10561384,10688266,10785936,11158118,11197070,11272601,12844862,12931273,14009439,14343470,14646845,14770549,15100023,15305404,15623605,15825102,16161367,16176050,16295416,16581653,16591932,16764489,16855228,16878022,17309485,17983639,18009206,18619829,18893768,19573780,19845540,19899651,21513296,21817326,21847553,22515771,22621724,22998888,22999308,23003553,23013165,23375484,23492411,23770535");
		try {
			File f = new File(data.getFileName());
			String[] offsets = data.getOffsets().split(",");
			long offset = 0;
			long lastOffset = 0;
			int length = 0;
			int Strlength = 0;
			boolean filter = false;
			if (f.exists()) {
				BufferedReader reader =  new BufferedReader(new FileReader(f));
				for (int i = 0; i < offsets.length; i++) {
					lastOffset = offset;
					offset = Integer.valueOf(offsets[i]);
					if (i == 0) {
						long number = reader.skip(offset);
					} else {
						long number = reader.skip(offset - lastOffset - Strlength - 2 );
					}
	//				System.out.println("@gyy:file is " + fileName +" the offset is " +offset);
					 String dataStr = reader.readLine();
					 System.out.println(dataStr);
					 if(dataStr!=null){
						 Strlength = dataStr.getBytes().length;
					 }
					 byte[] rawInfo = new byte[Strlength];
					 if(filter)
					 {
						if(length <= 0)
						{
							length = rawInfo.length;
						}
						byte [] data1 = new byte[Strlength];
						System.arraycopy(rawInfo, 0, data1, 0, length);
//						System.out.println(data1.toString());
					 }
					 else {
//						if(length <= 0)
//						{
//							length = rawInfo.length;
//						}
						byte [] data1 = new byte[Strlength];
						System.arraycopy(rawInfo, 0, data1, 0, length);
//						System.out.println(data1.toString());
					}
				
				}
				reader.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}


