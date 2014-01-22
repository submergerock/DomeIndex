package org.cProc.check;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

public class CheckOmit {

	private static FileSystem FS;

	private static final Logger LOG = Logger.getLogger(CheckOmit.class);
	
	private static final Path NOTIFY = new Path("/smp/cdr_notify");
	private static final Path OMIT = new Path("/smp/cdr_omit");

	private static final long MAX = 5 * 60 * 60 * 1000;
	
	
	
	private static final long SLEEP = 1 * 60 * 60 * 1000;

	static {
		Configuration conf = new Configuration();
		conf.set("fs.default.name", "hdfs://172.16.4.100:9000");
		try {
			FS = FileSystem.get(conf);
			if(!FS.exists(OMIT))
			{
				FS.mkdirs(OMIT);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws IOException,
			InterruptedException {

		while (true) {
			
			boolean aa = false;
			LOG.info("begin new compare");
			FileStatus[] files = FS.listStatus(NOTIFY);
			Arrays.sort(files, new TimeComparator());

			// 两两比较
			if (files == null || files.length == 0) {
				LOG.info("begin sleep1");
				Thread.sleep(SLEEP);
				LOG.info("end sleep1");
				continue;
			}

			int j = files.length - 1;
			for (int i = 0; i < j; i++) {
				long distince = files[i + 1].getModificationTime()
						- files[i].getModificationTime();
				if (distince >= MAX) {
					
					LOG.info(files[i + 1].getPath().toString());
					
					LOG.info(files[i].getPath().toString());
					LOG.info("-------------------------------------");
					//
					//将时间小的移除到异常目录
					FileStatus temp = files[i];
					FS.mkdirs(new Path(OMIT,temp.getPath().getName()));
					//删除
					FS.delete(temp.getPath(), true);
					aa = true;
					break;
				}

			}
			
			if(aa)
				continue;
			//没有 
			LOG.info("begin sleep2");
			Thread.sleep(SLEEP);
			LOG.info("end sleep2");

		}

	}
}

class TimeComparator implements Comparator<FileStatus> {

	public int compare(FileStatus o1, FileStatus o2) {

		long time1 = o1.getModificationTime();

		long time2 = o2.getModificationTime();

		if (time1 > time2) {
			return 1;
		} else if (time2 > time1) {
			return -1;
		}

		return 0;
	}
}
