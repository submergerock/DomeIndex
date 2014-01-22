package org.cProc.distributed.createIndex;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;


//this class is for create data for testing. 
public class CreateDataForTest {
	
	public static void main(String args[])
	{
		String  dataDir ="";
		String  notfiDir ="/TelecomNotify";
		String  localDir ="D:\\telecomTestData";
		Configuration conf = new Configuration();
		conf.set("fs.default.name","hdfs://192.168.1.8:9000");
		try {
			FileSystem fs = FileSystem.get(conf);
			if(fs != null)
			{
				 //read the files in the local dir
				File localFile = new File(localDir);
				if(localFile.exists())
				{
//					DebugPrint.DebugPrint("in the "+localDir , this);
					File[] files =  localFile.listFiles();
//					DebugPrint.DebugPrint("sub file number is " + files.length);
					for(int i = 0 ; i < files.length ; i++)
					{
//						DebugPrint.DebugPrint("file name is " + files[i].getAbsolutePath());
						 
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
