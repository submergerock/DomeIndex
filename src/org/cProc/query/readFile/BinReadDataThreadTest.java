package org.cProc.query.readFile;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.cProc.GernralRead.TableAndIndexTool.TableAndIndexTool;
import org.cProc.sql.RowFilter;

public class BinReadDataThreadTest {
	
	public static void main(String[] args)
	{
		
		TableAndIndexTool.setPrePath("/smp");
		String hdfsString = "hdfs://192.168.1.12:8000";
		ArrayList<byte[]> reslutData = new ArrayList<byte[]> ();
		Integer condition = new Integer(0);
	   
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
			"select all from bssap on cdr where calling_number_str=='8613427537314'";
		RowFilter rf = new RowFilter(sql,fs);
		ArrayList<ReadAndFilterFromLocalDiskThread> threadList = ReadAndFilterFromLocalDiskThread.InitReadThread(null, rf.getDataSize(),condition,true,rf);
		String fileName ="D:\\indexTest\\data.cdr";
		String offsets ="52073476,55060460";
			for (int _i = 0; _i < threadList.size(); _i++) {
				//threadList.get(_i).addReadIndex(fileName,offsets);
			}

		// LOG.info("------------------DoINDEX1----------------222-------");
		for (int _i = 0; _i < threadList.size(); _i++) {
			Thread thread = new Thread(threadList.get(_i));
			thread.start();
		}
		
		
		System.out.println("----------------------------");
		while (!ReadAndFilterFromLocalDiskThread.ThreadsIsOver(threadList)) {

			synchronized (condition) {
				try {
					if (!ReadAndFilterFromLocalDiskThread.ThreadsIsOver(threadList)) {
						condition.wait(6000);
						for (int _i = 0; _i < threadList.size(); _i++) {
							threadList.get(_i).getResultData(reslutData);
						}

						}
					System.out.println("size is " + reslutData.size());
//					for(int i = 0 ; i < reslutData.get(0).length ; i++)
//					{
//						System.out.print(reslutData.get(0)[i]+" ");
//					}
		//			System.out.println(Bytes.toString(reslutData.get(0)));
					reslutData.clear();	
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// LOG.info("reslutDataFromMainThread size is " +
			// reslutDataFromMainThread.size());

		}
		
		

	}
}
