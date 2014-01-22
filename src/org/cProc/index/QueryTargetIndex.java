package org.cProc.index;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.hdfs.protocol.ClientProtocol;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.hdfs.protocol.LocatedBlocks;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.net.NetUtils;
import org.cProc.index.BPlusTree.BTreeType;

public class QueryTargetIndex {
	public static final long WEEK_OF_SEC = 7 * 24 * 60 * 60L;
	public static final long DAY_OF_SEC = 24 * 60 * 60L;
	public final static PathFilter NEEDDIR = new PathFilter() {

		@Override
		public boolean accept(Path path) {
			if (path.getName().endsWith(BTreeType.orgName)
					|| path.getName().endsWith(BTreeType.mergeName)
					|| path.getName().endsWith(BTreeType.tempName))
				return false;
			return true;
		}
	};

	public final static PathFilter NEEDDATA = new PathFilter() {

		@Override
		public boolean accept(Path path) {
			if (path.getName().endsWith(BTreeType.orgName)
					|| path.getName().endsWith(BTreeType.mergeName))
				return true;
			return false;
		}
	};

	private long _startS;
	private long _endS;
	private String _indexingRoot;
	private FileSystem _fs;
	
	public static  String HDFS_URI =null;
	public static  int HDFS_URI_LEN =-1;
	
	public static String getStringSuffix(final String str)
	{
		return str.substring(HDFS_URI_LEN);
	}
	
	
	public QueryTargetIndex(long startS, long endS, String indexingRoot,
			FileSystem fs) {
		this._startS = startS;
		this._endS = endS;
		this._indexingRoot = indexingRoot;
		this._fs = fs;
		if(HDFS_URI==null)
		{
			URI uri = this._fs.getUri();
			HDFS_URI = uri.toString();
			HDFS_URI_LEN =HDFS_URI.length();
		}
	}

	public String[] getResult() throws IOException {
		return this.getTargetIndex(this._fs, this._indexingRoot, this._startS,
				this._endS);
	}

	/**
	 * 开始的秒和结束的秒数
	 * 
	 * @param start
	 * @param end
	 * @return
	 * @throws IOException
	 */
	private String[] getTargetIndex(FileSystem fs, String indexRoot,
			long startS, long endS) throws IOException {
		long start_week = startS / WEEK_OF_SEC;
		long start_day = (startS % WEEK_OF_SEC) / DAY_OF_SEC;

		long end_week = endS / WEEK_OF_SEC;
		long end_day = (endS % WEEK_OF_SEC) / DAY_OF_SEC;

		// step1检查最小目录是否存在
		// step2如果不存在，排序获得最下的时间
		// 开始目录
		List<Long> rootList = new ArrayList<Long>();

		if (fs.exists(new Path(indexRoot + "/" + start_week))) {
			if (fs.exists(new Path(indexRoot + "/" + start_week + "/"
					+ start_day))) {
				// 是否存在目录
				FileStatus[] arr = fs.listStatus((new Path(indexRoot + "/"
						+ start_week + "/" + start_day)), NEEDDIR);
				if (arr != null && arr.length != 0) {
					String[] tempArr = arr[0].getPath().getName().split("_");
					start_week = Long.parseLong(tempArr[0]);
					start_day = Long.parseLong(tempArr[1]);

				}

			} else {
				// 取这个目录中最小的
				List<Long> tempList = new ArrayList<Long>();
				FileStatus[] arr = fs.listStatus(new Path(indexRoot + "/"
						+ start_week), NEEDDIR);
				if (arr != null && arr.length != 0) {
					for (FileStatus status : arr) {
						tempList
								.add(Long.parseLong(status.getPath().getName()));
					}
					Collections.sort(tempList);
				}
				// tempList.get(0);
				// 到最小目录
				arr = fs.listStatus(new Path(indexRoot + "/" + start_week + "/"
						+ tempList.get(0)), NEEDDIR);
				if (arr != null && arr.length != 0) {
					String[] tempArr = arr[0].getPath().getName().split("_");
					start_week = Long.parseLong(tempArr[0]);
					start_day = Long.parseLong(tempArr[1]);

				} else {
					start_day = tempList.get(0);

				}

				tempList.clear();
				tempList = null;

			}

		} else {// 获取应该的最小值
			if (rootList.isEmpty()) {
				//
				FileStatus[] arr = fs.listStatus(new Path(indexRoot), NEEDDIR);
				if (arr != null && arr.length != 0) {
					for (FileStatus status : arr) {
						rootList
								.add(Long.parseLong(status.getPath().getName()));
					}
					Collections.sort(rootList);
				}
			}

			if (rootList.isEmpty())
				return null;
			if (start_week > rootList.get(rootList.size() - 1))// 开始时间大于最大的结束时间
				return null;

			if (start_week < rootList.get(0))// 比最小时间还小
			{
				start_week = rootList.get(0);
				start_day = 0L;
			}

		}

		// 结束目录

//		if (fs.exists(new Path(indexRoot + "/" + end_week))) {// ok
//
//			if (fs.exists(new Path(indexRoot + "/" + end_week + "/" + end_day))) {
//				// 获取其中的back文件夹
//				FileStatus[] arr = fs.listStatus(new Path(indexRoot + "/"
//						+ end_week + "/" + end_day), NEEDDIR);
//				if (arr != null && arr.length != 0) {
//					String[] strArr = arr[0].getPath().getName().split("_");
//					end_week = Long.parseLong(strArr[0]);
//					end_day = Long.parseLong(strArr[1]);
//				}
//
//			} else {
//				// 取其中最小
//				List<Long> tempList = new ArrayList<Long>();
//				FileStatus[] arr = fs.listStatus(new Path(indexRoot + "/"
//						+ end_week), NEEDDIR);
//				if (arr != null && arr.length != 0) {
//					for (FileStatus status : arr) {
//						tempList.add(Long.parseLong(status.getPath().getName()));
//					}
//					Collections.sort(tempList);
//				}
//				
//				//
//				// 到最小目录
//				arr = fs.listStatus(new Path(indexRoot + "/" + end_week + "/"
//						+ tempList.get(0)), NEEDDIR);
//				if (arr != null && arr.length != 0) {
//					String[] tempArr = arr[0].getPath().getName().split("_");
//					end_week = Long.parseLong(tempArr[0]);
//					end_day = Long.parseLong(tempArr[1]);
//
//				} else {
//					end_day = tempList.get(0);
//
//				}
//
//				tempList.clear();
//				tempList = null;
//
//			}
//
//		} else {
//			if (rootList.isEmpty()) {
//				//
//				FileStatus[] arr = fs.listStatus(new Path(indexRoot), NEEDDIR);
//				if (arr != null && arr.length != 0) {
//					for (FileStatus status : arr) {
//						rootList
//								.add(Long.parseLong(status.getPath().getName()));
//					}
//					Collections.sort(rootList);
//				}
//			}
//
//			if (rootList.isEmpty())
//				return null;
//			if (end_week < rootList.get(0))// 结束时间小于最小时间
//				return null;
//			if (end_week > rootList.get(rootList.size() - 1))// 开始时间大于最大的结束时间
//			{
//				end_week = rootList.get(rootList.size() - 1);
//				end_day = 6;
//			}
//
//		}

		return getIndexFiles(fs, indexRoot, start_week, start_day, end_week,
				end_day).toArray(new String[0]);
	}

	private List<String> getIndexFiles(FileSystem fs, String root,
			long start_week, long start_day, long end_week, long end_day)
			throws IOException {
		
		
		
		
		
		List<String> list = new ArrayList<String>();
		if (start_week == end_week) {// 同一周

			for (long i = start_day; i <= end_day; i++) {
				FileStatus[] arr = fs.listStatus(new Path(root + "/"
						+ start_week + "/" + i), NEEDDATA);
				if (arr != null && arr.length != 0) {
					for (int j = 0; j < arr.length; j++) {
						// 时间过滤
						String[] temp = arr[j].getPath().getName().split("_");

						long ts = Long.parseLong(temp[0]);
						long te = Long.parseLong(temp[1]);

						if (!(ts > this._endS || te < this._startS)) {
							list.add(getStringSuffix(arr[j]
									.getPath().toString()));
						}

						temp = null;

					}
				}
				arr = null;
			}

		} else// 跨天
		{
			for (long i = start_day; i <= 6; i++)// start_day
			{
				
				
				
				Path path = new Path(root + "/"+ start_week + "/" + i);
				if(!fs.exists(path))
				{
					continue;
				}
				FileStatus[] arr = fs.listStatus(path, NEEDDATA);
				if (arr != null && arr.length != 0) {
					for (int j = 0; j < arr.length; j++) {
						String[] temp = arr[j].getPath().getName().split("_");
						long ts = Long.parseLong(temp[0]);
						long te = Long.parseLong(temp[1]);
						if (!(ts > this._endS || te < this._startS)) {
							list.add(getStringSuffix(arr[j]
									.getPath().toString()));
						}
						temp = null;

					}
				}
				arr = null;
			}
			// 间隔
			for (long i = start_week + 1; i < end_week; i++) {
				for (long k = 0; k <= 6; k++)// start_day
				{
					
					Path path = new Path(root + "/" + i
							+ "/" + k);
					if(!fs.exists(path))
					{
						continue;
					}
					
					
					FileStatus[] arr = fs.listStatus(path, NEEDDATA);
					if (arr != null && arr.length != 0) {
						for (int j = 0; j < arr.length; j++) {
							String[] temp = arr[j].getPath().getName().split(
									"_");
							long ts = Long.parseLong(temp[0]);
							long te = Long.parseLong(temp[1]);
							if (!(ts > this._endS || te < this._startS)) {
								list.add(getStringSuffix(arr[j].getPath()
												.toString()));
							}
							temp = null;
							// list.add(arr[j].getPath().toString());
						}
					}
					arr = null;
				}
			}

			// end_day
			for (long i = 0; i <= end_day; i++)// start_day
			{
				
				Path path = new Path(root + "/" + end_week
						+ "/" + i);
				if(!fs.exists(path))
				{
					continue;
				}
				
				FileStatus[] arr = fs.listStatus(path, NEEDDATA);
				if (arr != null && arr.length != 0) {
					for (int j = 0; j < arr.length; j++) {
						String[] temp = arr[j].getPath().getName().split("_");
						long ts = Long.parseLong(temp[0]);
						long te = Long.parseLong(temp[1]);
						if (!(ts > this._endS || te < this._startS)) {
							list.add(getStringSuffix(arr[j]
									.getPath().toString()));
						}
						temp = null;
						// list.add(arr[j].getPath().toString());
					}
				}
				arr = null;
			}
		}

		return list;

	}
	
	

	/**
	 * 
	 * @param n
	 *            今天为0 昨天为-1 ，后天为-2，明天为 1 后天为 2 类推
	 * @return aa/bb 前面不带 /符号
	 */
	public static String getCurrentTimeNextNDay(final int n) {
		String result = "";
		long current = System.currentTimeMillis() / 1000L;
		// System.out.println(current);
		long start_week = current / WEEK_OF_SEC;
		long start_day = (current % WEEK_OF_SEC) / DAY_OF_SEC;
		long week = 0;
		long day = 0;
		if (n != 0) {
			week = n / 7;
			day = n % 7;
		}

		result = (start_week + week) + "/" + (start_day + day);

		return result;
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();

		conf.set("fs.default.name", "hdfs://172.16.4.100:9000/");

		FileSystem fs = FileSystem.get(conf);
//		URI uri = fs.getUri();
//		String nnConectionStr = uri.getHost() + ":" + uri.getPort();
//		InetSocketAddress nameNodeAddr = NetUtils
//				.createSocketAddr(nnConectionStr);// 
//		ClientProtocol nameNode = null;
//		try {
//			nameNode = (ClientProtocol) RPC.getProxy(ClientProtocol.class,
//					ClientProtocol.versionID, nameNodeAddr,
//					new Configuration(), NetUtils.getSocketFactory(
//							new Configuration(), ClientProtocol.class));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//

		// long startS,long endS,String indexingRoot,FileSystem fs)
		long l = System.currentTimeMillis();
		// index.getResult()
		// 4_
		// 1323432185_1323432191//
		// 1323847303，1323848249
		// 1323705600  1323792000
//		FileStatus[] arr = fs.listStatus(new Path("/cwtest/bssap/indexfile/callingIndex/data/2150/2"));
//		System.out.println(arr.length);
//			arr = fs.listStatus(new Path("/cwtest/bssap/indexfile/callingIndex/data/2150/2"), NEEDDATA);
//			System.out.println(arr.length);
//			arr = fs.listStatus(new Path("/cwtest/bssap/indexfile/callingIndex/data/2150/2"), NEEDDIR);
//			System.out.println(arr.length);
			
//			start time-->1355502600    end time-->1355535600
		QueryTargetIndex index = new QueryTargetIndex(1353621600L, 1353628800L,
				"/smp/cdr/bicc/indexfile/callingIndex/data", null);
		// FileStatus[] arr = fs.listStatus(new
		// Path("/cwtest/bssap/indexfile/callingIndex/data"), NEEDDIR);
		// System.out.println(arr.length);

		String[] files = index.getResult();
//		LocatedBlocks[] lbArr = nameNode.getBlockLocationsForMutilFile(files, 0, 388);
//		int count = 0;
//		for(int i=0;i<files.length;i++)
//		{
//			LocatedBlocks ll = lbArr[i];
//			DatanodeInfo []  info = ll.getLocatedBlocks().get(0).getLocations();
//			//System.out.println(info.length);
//			
//			System.out.println(files[i]+"  "+info[0].getHost());
//				
//			
//		}
		showArr(files);
		//System.out.println(count);

	}

	public static void showArr(String[] arr) {
		for (String str : arr) {
			System.out.println(str);
		}
	}

}
