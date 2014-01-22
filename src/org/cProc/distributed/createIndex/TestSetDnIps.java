package org.cProc.distributed.createIndex;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hdfs.DistributedFileSystem;

public class TestSetDnIps {
	private static final String pemission = "drwxrwxrwx";
	private static final FsPermission PERMISSION = FsPermission.valueOf(pemission);
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		
		conf.set("fs.default.name", "hdfs://192.168.1.8:9000/");
		
		FileSystem fs = FileSystem.get(conf);
		fs.mkdirs(new Path("/kk/kk/kkddds11k"), PERMISSION);
		fs.setPermission(new Path("/kk/kk/kkddds11k"), PERMISSION);
		System.out.print(fs.listStatus(new Path("/olk"))==null);
		//FileSystem.create(fs, new Path("/yyyy/fff/llll2"), PERMISSION).close();
		fs.close();
//		DistributedFileSystem temp = (DistributedFileSystem) fs;
//		
//		temp.setDNIPs(new String[]{"192.168.1.14","192.168.1.16","192.168.1.18"});
//		
//		
//		temp = null;
//		
//		fs.copyFromLocalFile(new Path("d://Bin.rar"), new Path("/tmp1/bin.rar"));
//		
//		fs.close();

	}

}
