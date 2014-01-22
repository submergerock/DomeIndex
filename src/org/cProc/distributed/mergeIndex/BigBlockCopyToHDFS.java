package org.cProc.distributed.mergeIndex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;

public class BigBlockCopyToHDFS {
	
	public static  void main(String[] args)
	{
		
	try{
		File file = new File(args[0]+"");
		if(!file.exists())
		{
			System.out.println(" failed merge path is " + args[0]);
			return ;
		}
		Path path = new Path(args[0]);
		
		Configuration conf = new Configuration();
		conf.set("fs.default.name", "hdfs://172.3.2.205:9000" );
		FileSystem fs = FileSystem.get(conf);
		try {
			conf.set("dfs.block.size", 268435456+"");
			conf.set("dfs.replication", 3+"");
			
			DistributedFileSystem newfs = new DistributedFileSystem();
			newfs.initialize(fs.getUri(), conf);
			newfs.copyFromLocalFile(path, new Path("/just_test/"+path.getName()));
			newfs.close();
			newfs = null;
			conf = null;
	} catch (IOException e) {
		e.printStackTrace();
	}
	}catch (Exception e) {
	
	}
	}

}
