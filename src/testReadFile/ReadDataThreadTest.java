package testReadFile;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.cProc.GernralRead.TableAndIndexTool.TableAndIndexTool;
import org.cProc.sql.RowFilter;

public class ReadDataThreadTest {
	
	public static void main(String[] args)
	{
		
		String hdfsString = "hdfs://172.16.4.100:9000";
		ArrayList<byte[]> reslutData = new ArrayList<byte[]> ();
		Integer condition = new Integer(0);
	   TableAndIndexTool.setPrePath("/smp");
		//sqlFormat:  SQL-_-!indexPath
		FileSystem fs = null;
		Configuration conf = new Configuration();
		conf.set("fs.default.name",hdfsString);
		try { 
			 fs = FileSystem.get(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String sql = 
			"select all from bssap on cdr where start_time_s>=0 ";
		RowFilter rf = new RowFilter(sql,fs);
		ArrayList<ReadAndFilterFromLocalDiskThread> threadList = ReadAndFilterFromLocalDiskThread.InitReadThread(null, rf.getDataSize(),condition,false,rf);
		String fileName ="C:\\debug.cdr";
		String offsets="7671664,14578928,22067136,25322388,31247944,33976852,39610480";
		for (int _i = 0; _i < threadList.size(); _i++) {
				threadList.get(_i).addReadIndex(fileName,offsets);
			}

		// LOG.info("------------------DoINDEX1----------------222-------");
		for (int _i = 0; _i < threadList.size(); _i++) {
			Thread thread = new Thread(threadList.get(_i));
			thread.start();
		}
		// LOG.info("------------------DoINDEX1----------------333-------");
		while (!ReadAndFilterFromLocalDiskThread.ThreadsIsOver(threadList)) {
			synchronized (condition) {
				try {
					if (!ReadAndFilterFromLocalDiskThread.ThreadsIsOver(threadList)) {
						condition.wait(6000);
						for (int _i = 0; _i < threadList.size(); _i++) {
							threadList.get(_i).getResultData(reslutData);
						}
						
			
						for(int i=0;i<reslutData.size();i++)
						{
							
							
						}
						
						reslutData.clear();
						
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println("data size is " +reslutData.size());
		
		
		
		
	
		
		System.out.println("main thread is over");
	}
}
