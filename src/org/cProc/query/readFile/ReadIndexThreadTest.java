package org.cProc.query.readFile;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.cProc.GernralRead.ReadData.ParseIndexFormat;
import org.cProc.GernralRead.TableAndIndexTool.TableAndIndexTool;
import org.cProc.sql.IndexChooser;
import org.cProc.sql.SupplementNewFiled;
import org.cProc.tool.Bytes;


public class ReadIndexThreadTest {
	
	public static void main(String[] args) throws IOException
	{
//		Properties p = new Properties();
//		p.load(	ReadIndexThreadTest.class.getClassLoader().getResourceAsStream("config.properties"));
//		System.out.print(p.get("xml"));
		String path = "E:\\datacube\\tableInfo\\bssap\\indexfile\\calledIndex\\index.frm";
		String hdfsPath ="hdfs://192.168.1.12:8000";
		String sql =
		"select all from bssap on cdr where called_number==10086";
		TableAndIndexTool.setPrePath("/smp");
//		String sql =" select start_time_s  from bssap on gentest where  cdr_type >= -128 and cdr_type <= 127 and cdr_result >= -128 and cdr_result <= 127 and new_tmsi >= -2147483648 and new_tmsi <= 2147483647 and imsi >= -9223372036854775808 and imsi <= 9223372036854775807 and imei >= -9223372036854775808 and imei <= 9223372036854775807 and  (    calling_number == 90248 and called_number ==0  and  start_time_s >= 1321524192  ) ";;
//		String sql ="select all from bssap on cwtest where  called_number >= -9223372036854775808 and called_number <= 9223372036854775807 and cdr_result >= -128 and cdr_result <= 127 and new_tmsi >= -2147483648 and new_tmsi <= 2147483647 and imsi >= -9223372036854775808 and imsi <= 9223372036854775807 and imei >= -9223372036854775808 and imei <= 9223372036854775807 and  (   calling_number == 0 and   cdr_type in (1,2,3,4,5,6,7,8,12,13,14,18,19) and  start_time_s>=1323847303 and start_time_s<=1323848249  ) ";
		ArrayList<byte[]> reslutData = new ArrayList<byte[]> ();
		Integer condition = new Integer(0);
		
		
		
		
		ArrayList<ReadBPlusIndexThread> threadList = ReadBPlusIndexThread.InitReadThread(null, condition);
		
		//sqlFormat:  SQL-_-!indexPath
    	ParseIndexFormat readIndexFormat = null;
    	IndexChooser ic = new IndexChooser(sql);
     	System.out.println("sql is " + sql);
		Configuration conf = new Configuration();
//		conf.set("fs.default.name",hdfsPath);
		FileSystem fs = null;
		try {
			 fs = FileSystem.get(conf);
			if(fs != null)
			{
				readIndexFormat = new ParseIndexFormat(path,fs);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//start_time_s == 1321340888 and
    	String fullSQL = SupplementNewFiled.getSQL(sql,readIndexFormat);
    	System.out.println("fullSQL is " + fullSQL);
    	sql = fullSQL+"-_-!"+path;

			for (int _i = 0; _i < threadList.size(); _i++) {
				threadList.get(_i).addReadIndex("D:\\indexTest\\1340927984_1340937726_3_5ba4f66a-9127-4aff-863f-d97f13cb10dc.org"
								,sql , hdfsPath);
			}
			
			

		// LOG.info("------------------DoINDEX1----------------222-------");
		for (int _i = 0; _i < threadList.size(); _i++) {
			Thread thread = new Thread(threadList.get(_i));
			thread.start();
		}
	
		// LOG.info("------------------DoINDEX1----------------333-------");
			while (!ReadBPlusIndexThread.ThreadsIsOver(threadList)) {
				synchronized (condition) {
					try {
						if (!ReadBPlusIndexThread.ThreadsIsOver(threadList)) {
							condition.wait(6000);
							for (int _i = 0; _i < threadList.size(); _i++) {
								threadList.get(_i).getResultData(reslutData);
							}
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			// 如果还有剩余的结果的话，将剩余的结果读出来；
			for (int _i = 0; _i < threadList.size(); _i++) {
				threadList.get(_i).getResultData(reslutData);
			}

		
		
		
//		// LOG.info("------------------DoINDEX1----------------333-------");
//		while (!ReadBPlusIndexThread.ThreadsIsOver(threadList)) {
//			// LOG.info("------------------DoINDEX1----------------444-------");
//			if (!ReadBPlusIndexThread.listIsHaveData(threadList)) {
//				// LOG.info("------------------DoINDEX1----------------555-------");
//				if (!ReadBPlusIndexThread.ThreadsIsOver(threadList)) {
//					// LOG.info("------------------DoINDEX1----------------666-------");
//					synchronized (condition) {
//						try {
//							condition.wait(1000);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//			for (int _i = 0; _i < threadList.size(); _i++) {
//				threadList.get(_i).getResultData(reslutData);
//			}
//			//file Name is /gentest/bssap/datafile/3.dat
//			//file Name is /gentest/bssap/datafile/2.dat
//		}
    	System.out.println("file  is  " + reslutData.size());
    	for( int i = 0 ; i < reslutData.size() ; i++)
    	{
    		System.out.println(Bytes.toString(reslutData.get(i)));
    	}
    	//.substring(0,1000)
		System.out.println("Data filter is "+ic.isNeedFilterData(readIndexFormat));
	}

}
