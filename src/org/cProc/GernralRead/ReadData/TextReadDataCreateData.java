package org.cProc.GernralRead.ReadData;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;

public class TextReadDataCreateData {

	private final  static String pemission = "-rw-rw-rw-";

	public static void main(String args[])
	{		
		String  dataDir ="D:\\test\\testData.text";
		String content ="1,2,3,4,'hello','world'";
		Configuration conf = new Configuration();
	    conf.set("fs.default.name","hdfs://192.168.1.8:9000");
		try {
			FileSystem fs = FileSystem.get(conf);
			String fileName = ""+System.currentTimeMillis();
			fs.copyFromLocalFile(new Path(dataDir),  new Path("/GernalDataTest/testTable/datafile"+"/"+fileName));
			fs.setPermission(new Path("/GernalDataTest/testTable/datafile"+"/"+fileName), FsPermission.valueOf(pemission));	
			
/*			if(fs != null)
			{
				 //read the files in the local dir
				File localFile = new File(dataDir);
				if(!localFile.exists())
				{
					localFile.createNewFile();
				}
					FileWriter fw = new FileWriter(localFile);
					fw.write(content);
					fw.flush();
					fw.close();
			}
			*/
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}



