package org.cProc.del;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.cProc.index.QueryTargetIndex;
import org.cProc.tool.Bytes;

public class Main {

	private static final String PRE = "/smp/cdr/";
	private static FileSystem fs;

	private static String hdfs;
	private static String tableName;
	private static String startTime;
	private static String endTime;
	
	private static  boolean delete = false;
	
	private static long start;
	
	private static long end;

	public static void validateArgs(String[] args) {

		if (args == null || args.length != 5) {
			alert("args must assign three parameter，like   hdfs  tablename   starttime  endtime   true|false");
			alert("time format like yyyy-hh-dd");
			return;
		}
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void main(String[] args) throws IOException, ParseException {
		// 1.表名
		// 2 开始时间
		// 3 结束时间 2012-xx-xx

		validateArgs(args);

		hdfs = args[0];
		tableName = args[1];
		startTime = args[2];
		endTime = args[3];
		delete = Boolean.parseBoolean(args[4]);
		
		// 转化成long类型,秒数
		 start = getSecond(startTime);
		 end = getSecond(endTime);

		// 1.获取下面的索引个数
		Configuration conf = new Configuration();

		conf.set("fs.default.name", hdfs);

		fs = FileSystem.get(conf);

		FileStatus[] arrs = fs.listStatus(new Path(PRE + tableName
				+ "/indexfile"));

		if (arrs != null && arrs.length != 0) {
			// // 循环处理

			for (FileStatus status : arrs) {
				// 2.时间转化，调用索引查询获取可能的索引

				delIndex(status, start, end);
				// QueryTargetIndex target = new QueryTargetIndex();

			}

		}

	}

	private static void delIndex(FileStatus status, long start, long end)
			throws IOException {

		QueryTargetIndex index = new QueryTargetIndex(start, end, PRE
				+ tableName + "/indexfile/" + status.getPath().getName()
				+ "/data", fs);

		String[] result = index.getResult();
		
		if(result==null)
			return;
		
		for (String path : result) {// 3 精确查询索引格式，是否包含不在之内的时间,如果包含，不删除

			if(!fs.exists(new Path(path)))
			{
				continue;
			}
			
			// 获取索引文件中的文件路径
			String[] contents = getIndexFileContent(path);
			boolean bool = false;
			if(contents!=null && contents.length!=0)
			{
				bool = validate(contents);
			}else
			{
				bool = true;
			}
			 
			
			if(bool)
			{//删除
				
				System.out.println("delete:------->"+path);
				if(delete)
				{
						fs.delete(new Path(path), true);
				}
			}else
			{//不删除
				System.out.println("not delete:------->"+path);	
			}
			
			

		}

		
	}

	private static String[] getIndexFileContent(String path) throws IOException {

		FSDataInputStream fis = fs.open(new Path(path));

		DataInputStream dis = new DataInputStream(fis);
		String[] val = getIndexFileContentInter(dis);


		dis.close();

		return val;

	}

	public static boolean validate(String[] val) {
		// TODO Auto-generated method stub
		
		for(String str:val)
		{
			Path path = new Path(str);
			String fileName = path.getName();
			//1349979930-1349980146
			String [] temp = fileName.split("-");
			
			long a  = Long.parseLong(temp[0]);
			long b = Long.parseLong(temp[1]);
			
			if((a>=start && a<=end)&& (b>=start && b<=end))
			{
				
			}else
			{
				return false;
			}
			
			
			
		}
		return true;
	}

	public static String[] getIndexFileContentInter(DataInputStream dis)
		 {

		List<String> list = new ArrayList<String>(5);

		// // int long int int int(文件个数) 80 {长度(int)，string，编号(int)}

		try {
			
			dis.skipBytes(4 + 8 + 4 + 4 + 80);
			int fileNumber = dis.readInt();

			for (int i = 1; i <= fileNumber; i++) {

				int len = dis.readInt();
				byte[] bytes = new byte[len];

				dis.read(bytes);
				String str = Bytes.toString(bytes);
				list.add(str);
				dis.skipBytes(4);
			}

			
		}  catch (EOFException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return null;
			
			
			
		}catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return null;
		}

		
		return list.toArray(new String[list.size()]);

	}

	public static long getSecond(String time) throws ParseException {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		return format.parse(time).getTime() / 1000;

	}

	public static void alert(String msg) {
		System.out.println(msg);
	}

}
