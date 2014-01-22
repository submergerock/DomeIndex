package org.cProc.tool;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HdfsTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Configuration conf  = new Configuration();
		conf.set("fs.default.name", "hdfs://192.168.1.12:8000");
		FileSystem fs = FileSystem.get(conf);
		fs.listStatus(new Path("/hadoop"));
		
		
	}

}
