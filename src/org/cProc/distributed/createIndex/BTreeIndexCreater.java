package org.cProc.distributed.createIndex;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hdfs.protocol.ClientProtocol;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.hdfs.protocol.LocatedBlock;
import org.apache.hadoop.hdfs.protocol.LocatedBlocks;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.net.NetUtils;
import org.cProc.GernralRead.ReadData.DataType;
import org.cProc.GernralRead.ReadData.ParseIndexFormat;
import org.cProc.index.QueryTargetIndex;
import org.cProc.index.BPlusTree.BPlusTree;
import org.cProc.index.BPlusTree.BTreeType;
import org.cProc.index.BPlusTree.Data.MulitFiledData;
import org.cProc.index.BPlusTree.Data.ReadWriteIndexDataProxy;
import org.cProc.tool.Bytes;
import org.cProc.tool.IndexNameHelper;




public class BTreeIndexCreater implements IIndexCreater {
	 private static Log logger = LogFactory.getLog(BTreeIndexCreater.class); 
	
	
	public static Integer lock = new Integer(0);
	private static final long WEEK_OF_SEC = 7 * 24 * 60 * 60L;
	private static final long DAY_OF_SEC = 24 * 60 * 60L;
	// private static final FsPermission OPEN = new FsPermission((short) 777);
	private static final String pemission = "drwxrwxrwx";
	private static final FsPermission PERMISSION = FsPermission
			.valueOf(pemission);

	public final static PathFilter PATHFILTER = QueryTargetIndex.NEEDDIR;

	// private FSDataOutputStream fos = null;
	private FileSystem fs = null;
	// private ParseIndexFormat format = null;
	// private TreeMap<Object, StringBuffer> treeMap = new TreeMap<Object,
	// StringBuffer>();
	private BPlusTree<MulitFiledData> btree = null;
	// private ArrayList<Class> typeList = null;
	private ReadWriteIndexDataProxy proxy = null;
	private HashMap<String, Integer> fileMap = new HashMap<String, Integer>();
	private int fileCount = 1;
	private String indexPath = null;
	private long minTime = Long.MAX_VALUE;
	private long maxTime = Long.MIN_VALUE;
	private long tempTime;
	public final static int STEP = 30;// 步调，分钟为单位
	private String[] files = null;// 索引的文�?
	private ClientProtocol nameNode = null;
	private String nnConectionStr = "";
	private int fileNum = 0;
	private int timeIndex = 0;
	// private ParseIndexFormat format = null;
	private ArrayList<Class> indexTypes = null;
	//private Set<IndexValue> treeSet = new TreeSet<IndexValue>();

	private List<IndexValue> list = new java.util.LinkedList<IndexValue>();
	private long test= 0;
	
	private ParseIndexFormat indexFormat  = null;
	
	
	private String tempLocalIndexFile ="";
	private String hdfsIndexFile="";
	private String uuid;
	
	public void setIndexFormat(ParseIndexFormat format) {
		this.indexFormat = format;
		timeIndex = format.getPrimryTimeColumnPosInIndex() + 1;
		proxy = new ReadWriteIndexDataProxy(format);
		ArrayList<byte[]> dataList = new ArrayList<byte[]>();
		ArrayList<Class> types = format.getIndexColumnType();
		indexTypes = types;
		for (int i = 0; i < types.size()-1; i++) {
			dataList.add(					DataType.getBytes(types.get(i), 
			         Bytes.toLong(DataType.getUnsginedMinBytesReturn8Byte(types.get(i)))+"")
			);
		}
		btree = new BPlusTree<MulitFiledData>(501, new MulitFiledData(dataList,
				proxy));

	}
	
	 	public ParseIndexFormat getIndexFormat()
	 	{
	 		return indexFormat;
	 	}
	

	@Override
	public void addIndexData(Field field) {
		// TODO Auto-generated method stub
		// DebugPrint.DebugPrint("addIndexData");
		ArrayList<byte[]> dataList = new ArrayList<byte[]>();
		int size = field.getMapSize();

		for (int i = 1; i <= size; i++) {
			dataList.add(field.getData(i));

		}
		
		/**
		 * 时间
		 */
		//add by chenwei
		/**
		 * 时间的适配器,待定
		 */
		
		
		tempTime = Bytes.toInt(field.getData(this.timeIndex));
		maxTime = Math.max(maxTime, tempTime);
		minTime = Math.min(tempTime, minTime);

		MulitFiledData data = new MulitFiledData(dataList, proxy);

		String fileNameString = field.getPath();
		Integer fileNumber = fileMap.get(fileNameString);
		if (fileNumber == null) {

			fileMap.put(fileNameString, fileCount);
			fileCount++;
			fileNumber = fileMap.get(fileNameString);
		}
		// DebugPrint.DebugPrint("offset--->"+field.getOffset());

		// add
		// if (treeMap.get(field.getData(1)) != null) {
		// IndexValue value = new IndexValue(data, fileNum, field.getOffset());
		//
		// treeMap.get(field.getData(1)).add(value);
		// value = null;
		//
		// } else {
		// Set<IndexValue> set = new TreeSet<IndexValue>();
		// IndexValue value = new IndexValue(data, fileNum, field.getOffset());
		// set.add(value);
		// treeMap.put(field.getData(1), set);
		//			
		// value = null;
		// set = null;
		// }
		
		IndexValue value = new IndexValue(data, fileNumber, field.getOffset());
		//treeSet.add(value);
		list.add(value);
		test++;
		//value = null;

		// end

		// btree.insert(data, fileNumber.intValue(), field.getOffset());
		//field = null;
//		dataList.clear();
		//dataList = null;
		data = null;

	}

	private void insert2Btree() {

		

	long l1 = System.currentTimeMillis();
		
	
		java.util.Collections.sort(this.list);
	//	logger.info(System.currentTimeMillis()-l1);
		
		Iterator<IndexValue> it = list.iterator();
		IndexValue value = null;
	//	logger.info(System.currentTimeMillis()+"   "+this.list.size());
		long i = 1;
		while (it.hasNext()) {
			value = it.next();
			i++;
			btree.insert(value.getData(), value.getFileNumber(), value.getOffset());
			//value.release();
			it.remove();
			value = null;
		}
		it = null;
		logger.info(System.currentTimeMillis());
		//this.list.iterator();
		

	}

	@Override
	public void create() throws IOException {
		// add
		long l1 = System.currentTimeMillis();
		
		this.insert2Btree();
		long l2 = System.currentTimeMillis();
		// 在插入的过程中已经将树建立完成；
		// 这里将树写入硬盘
		btree.writeFiles(fileMap);
		// 写到本地磁盘的临时目�?
		String name = this.getLocalFileName();

		btree.writeToFile(name);
//		long l3 = System.currentTimeMillis();
		//
		this.fileNum = this.btree.getFileNumberCount();
		btree = null;

		
		File file = new File(name);
		if (copyToHdfs(file.getAbsolutePath(), file.length())) {
		//	file.delete();
		}
		file = null;
		this.list.clear();
		list = null;
	}

	
    public String getTempLocalFileName()
    {
    	return this.tempLocalIndexFile;
    }
    
    public String getHDFSFileName()
    {
    	return this.hdfsIndexFile;
    }
	
	public boolean copyToHdfs(String localFile, long size) {
		try {
			// 将本地磁盘的临时目录下的Bplus索引写到HDFS�?暂时不支�?
			/**
			List<String> dns = this.getLocaltionHost(this.files);
			if (this.fs.getClass() == DistributedFileSystem.class) {
				DistributedFileSystem temp = (DistributedFileSystem) this.fs;
				temp.setDNIPs(dns.toArray(new String[dns.size()]));// �?��化优�?
				temp = null;
			}
            **/
			int i=0;
		    logger.debug("create --->"+(i++));
		    logger.debug(this.indexPath+"  "+this.minTime+"   "+this.maxTime);
			createBackFloder(this.indexPath, this.minTime, this.maxTime);// need
			logger.debug("create --->"+(i++));
			//test
			resetIndexPath();
			logger.debug("create --->"+(i++));
			String fileNameString = this.indexPath + "/" + 
			IndexNameHelper.getIndexName(	this.minTime, this.maxTime, this.fileNum, 
					this.indexFormat.getIndexName(), this.indexFormat.getIndexVersion(), 
					this.uuid ,  BTreeType.orgName);
			 this.tempLocalIndexFile = localFile;
			 this.hdfsIndexFile = fileNameString;

			 logger.debug("************************************************************temp file is " + localFile);
//			Path fileName = new Path(fileNameString);
//			logger.info("create --->"+(i++));
//			logger.info("path--->"+fileName.toString());
//			long baseBlock = 64 * 1024 * 1024;
//			long blockSize = ((size / baseBlock) + 1) * baseBlock;
			
			//Configuration conf = new Configuration();
			//conf.set("fs.default.name", fs.getConf().get("fs.default.name"));
//			try {
				//conf.set("dfs.block.size", blockSize + "");
				//conf.set("dfs.replication", fs.getConf().get("dfs.replication"));
				// String hdfsMasterIP = "192.168.1.8";
				// String hdfsMasterPort ="9000";
				//logger.info("create --->"+(i++));
				//DistributedFileSystem newfs = new DistributedFileSystem();
				//newfs.initialize(fs.getUri(), conf);

				//logger.info("create --->"+(i++));
//				this.printFileMap(this.files, fileNameString);
//				fs.copyFromLocalFile(new Path(localFile), fileName);
				//newfs.copyFromLocalFile(new Path(localFile), fileName);
				//logger.info("create --->"+(i++));
				//newfs.close();
				//newfs = null;
//				this.fs.setPermission(fileName, PERMISSION);
//			} catch (IOException e) {
//				DebugPrint.InforPrint("printStackTrace----->" + e.getMessage());
//				e.printStackTrace();
//			}

			// this.fs.copyFromLocalFile(new Path(localFile), fileName);
			// this.fs.setPermission(fileName, PERMISSION);
//			fileName = null;
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
			return false;
		}
	}
	
	void printFileMap(String[] files , String hdfsFile)
	{
		synchronized (lock) {
			for(int i = 0 ; i < files.length ; i++)
			{
				logger.info(System.currentTimeMillis()+":"+"@gyyIndexMap:  " + files[i] + " : " +  hdfsFile);
			}
		}
	}

	void resetIndexPath() {
		long start_week = this.minTime / WEEK_OF_SEC;
		long start_day = (this.minTime % WEEK_OF_SEC) / DAY_OF_SEC;
		this.indexPath = this.indexPath + "/" + start_week + "/" + start_day;
	}

	
	public void createBackFloder(String root, long startS, long endS)
			throws IOException {
		//int kk = 100;
		//logger.info("kkkkkk---------->"+(kk++));
		long start_week = startS / WEEK_OF_SEC;
		long start_day = (startS % WEEK_OF_SEC) / DAY_OF_SEC;

		long end_week = endS / WEEK_OF_SEC;
		long end_day = (endS % WEEK_OF_SEC) / DAY_OF_SEC;
		
		// 先处理本身的文件存放目录
		String temp = root + "/" + start_week + "/" + start_day;
		//logger.info("kkkkkk---------->"+(kk++));
		Path path = new Path(temp);
		if (!fs.exists(path)) {
			fs.mkdirs(path.getParent(), PERMISSION);
			fs.setPermission(path.getParent(), PERMISSION);
			fs.mkdirs(path, PERMISSION);
			fs.setPermission(path, PERMISSION);
		}
		if(start_week==end_week)
		{
			for (long i = (start_day+1); i <= end_day; i++) {
				createFloder(root + "/" + end_week + "/" + i, start_week,start_day);
			}
			return;
		}
		//logger.info("kkkkkk---------->"+(kk++));
		// 处理多余的周
		//如果为同�?��
		//
		logger.info(start_week+"  ************ "+start_day+"   "+end_week+"    "+end_day);
		//
		if(start_week==end_week)
		{
			for(long i=(start_day+1);i<=end_day;i++)
			{
				logger.info(root + "/" + start_week + "/" + i+"      "+ start_week+"    "+start_day);
				this.createFloder(root + "/" + start_week + "/" + i, start_week,start_day);
			}
			
			
			return;
		}
		for (long i = start_week; i <= end_week; i++) {
			//logger.info("start---------->"+(kk++));
			// 周中的每�?��
			if (i == start_week) {// 只处理后面的�?
				// TODO
				if (start_day == end_day) {
					continue;
				}
				for (long j = start_day + 1; j <= 6; j++) {
					createFloder(root + "/" + start_week + "/" + j, start_week,
							start_day);
				}
				continue;
			}
			if (i == end_week) {// 只处理前面的天数(包含)
				for (long j = 0; j <= end_day; j++) {
					createFloder(root + "/" + end_week + "/" + j, start_week,
							start_day);
				}

				continue;
			}
			for (long j = 0; j <= 6; j++) {// 其中间隔的周,全部处理
				createFloder(root + "/" + i + "/" + j, start_week, start_day);
			}

		}

	}

	public void createFloder(String dir, long start_week, long start_day)
			throws IOException {

		Path path = new Path(dir);
		if (fs.exists(path)) {
			// 比较
			FileStatus[] arr = fs.listStatus(path, PATHFILTER);
			if (arr != null && arr.length != 0) {
				String[] strArr = arr[0].getPath().getName().split("_");
				if (strArr.length == 2) {
					long l1 = Long.parseLong(strArr[0]);
					if (start_week < l1) {
						fs.rename(arr[0].getPath(), new Path(arr[0].getPath()
								.getParent(), start_week + "_" + start_day));

					} else if (start_week == l1) {
						// 在比较天
						l1 = Long.parseLong(strArr[1]);
						if (l1 > start_day) {
							fs.rename(arr[0].getPath(), new Path(arr[0]
									.getPath().getParent(), start_week + "_"
									+ start_day));
						}
					}
				}
			} else {

				// 直接mkdir

				fs.mkdirs(path, PERMISSION);
				fs.setPermission(path, PERMISSION);
				fs.mkdirs(new Path(dir + "/" + (start_week + "_" + start_day)),
						PERMISSION);
				fs.setPermission(new Path(dir + "/"
						+ (start_week + "_" + start_day)), PERMISSION);
			}

		} else {
			fs.mkdirs(path, PERMISSION);
			fs.setPermission(path, PERMISSION);
			fs.mkdirs(new Path(dir + "/" + (start_week + "_" + start_day)),
					PERMISSION);
			fs.setPermission(new Path(dir + "/"
					+ (start_week + "_" + start_day)), PERMISSION);
			// 设置
		}

	}

	// private void createFloder() throws IOException {
	// //
	// String temp = this.indexPath;
	//
	// long start_day = minTime / DAY_OF_SEC;
	//
	// long start_hour = minTime % DAY_OF_SEC;
	//
	// start_hour = start_hour / HOUR_OF_SEC;
	//
	// this.indexPath = this.indexPath + start_day + File.separator
	// + start_hour;
	// Path path = new Path(this.indexPath);
	// if (!fs.exists(path)) {
	// fs.mkdirs(path, OPEN);
	// }
	// path = null;
	//
	// // 设置backn
	//
	// long end_day = maxTime / DAY_OF_SEC;
	//
	// long end_hour = maxTime % DAY_OF_SEC;
	//
	// logger.info(end_day + " " + end_hour);
	//
	// end_hour = end_hour / HOUR_OF_SEC;
	// logger.info(end_day + " " + end_hour);
	//
	// // 天数不同
	// if (start_day != end_day) {
	//
	// createFloderFlag(temp, start_day, end_day, start_hour, end_hour);
	//
	// } else {// 同一�?
	// if (start_hour != end_hour) {// 小时不同
	// createFloderFlag(temp, end_day, start_hour, end_hour);
	// }
	//
	// }
	//
	// }
	//
	// // 天数不同
	// private void createFloderFlag(String root, long startDay, long endDay,
	// long startHour, long endHour) {
	//
	// // 循环天数
	// for (long i = startDay; i <= endDay; i++) {
	//			
	// }
	//
	// }
	//
	// // 天数相同�?
	// private void createFloderFlag(String root, long day, long startHour,
	// long endHour) throws IOException {
	// StringBuffer sb = new StringBuffer();
	// long step = endHour - startHour;
	// for (long i = startHour; i <= endHour; i++) {
	// sb.delete(0, sb.length());
	//
	// String temp = sb.append(root).append(File.separator).append(day)
	// .append(File.separator).append(i).append(File.separator)
	// .toString();
	// Path path = new Path(temp);
	// if (fs.exists(path)) {
	// // 查看其中的数值，如果大于他，就rename
	// FileStatus[] arr = fs.listStatus(path, new PathFilter() {
	//
	// @Override
	// public boolean accept(Path p) {
	// if (!p.getName().endsWith(".data")) {
	// return false;
	// }
	// return true;
	// }
	// });
	//
	// if (arr == null || arr.length == 0) {
	// // 直接mkdir
	// fs.mkdirs(new Path(path, step + ""), OPEN);
	//
	// } else {
	// FileStatus status = arr[0];
	// int temp_i = Integer.parseInt(status.getPath().getName());
	// if (temp_i < step) {
	// fs.rename(status.getPath(), new Path(status.getPath()
	// .getParent(), step + ""));
	// }
	//
	// status = null;
	// }
	//
	// arr = null;
	//
	// } else {
	// // 直接mkdir,//放入flag
	// path = new Path(path, step + "");
	// fs.mkdirs(path);
	// }
	//
	// path = null;
	// temp = null;
	// }
	//
	// sb = null;
	// }

	/**
	 * 文件�?
	 */
	public String getLocalFileName() {
		// File file = new File();
		return java.util.UUID.randomUUID().toString();
	}

	@Override
	public void setFileSystem(FileSystem fs) {
		// TODO Auto-generated method stub
		this.fs = fs;
	}

	@Override
	public void setIndexPath(String indexPath) throws IOException {
		// TODO Auto-generated method stub
		this.indexPath = indexPath;

	}

	@Override
	public void setMaxValue(ArrayList<byte[]> data) {
		// TODO Auto-generated method stub
		this.btree.setMaxValue(data);
	}

	@Override
	public void setMinValue(ArrayList<byte[]> data) {
		// TODO Auto-generated method stub
		this.btree.setMinValue(data);
	}
	
	@Override
	public void setSources(ArrayList<String> files) {
		this.files = (String[]) files.toArray(new String[0]);

	}

	public List<String> getLocaltionHost(String[] files) throws IOException {
		//
		 URI uri = fs.getUri();
		nnConectionStr = uri.getHost()+":"+uri.getPort();
		InetSocketAddress nameNodeAddr = NetUtils
				.createSocketAddr(nnConectionStr);// 
		try {
			nameNode = (ClientProtocol) RPC.getProxy(ClientProtocol.class,
					ClientProtocol.versionID, nameNodeAddr,
					new Configuration(), NetUtils.getSocketFactory(
							new Configuration(), ClientProtocol.class));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//

		LocatedBlocks[] lbArr = nameNode.getBlockLocationsForMutilFile(files,
				0, 777);
		Map<String, Integer> tempMap = new HashMap<String, Integer>();
		int replication = -1;
		// List<FileInfo> fileInfoList = new ArrayList<FileInfo>(files.length);
		java.util.Random random = new Random(System.currentTimeMillis());
		for (int i = 0; i < files.length; i++) {
			LocatedBlocks bb = lbArr[i];
			LocatedBlock lb = bb.getLocatedBlocks().get(0);
			DatanodeInfo[] dn = lb.getLocations();

			replication = dn.length;
			logger.info("cpoy len:" + dn.length);
			for (int k = 0; k < dn.length; k++) {
				String temp = dn[k].getHost();
				logger.info("hosts-->" + temp);
				if (tempMap.containsKey(temp)) {
					tempMap.put(temp, tempMap.get(temp) + 1);
				} else {
					tempMap.put(temp, 1);
				}
				temp = null;
			}
			bb = null;
			lb = null;
			dn = null;
		}
		// 对key-value排序
		// List<Integer> valueList = map.values();

		// 按照value分组
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		int max = -1;
		for (Map.Entry<String, Integer> entry : tempMap.entrySet()) {
			if (map.containsKey(entry.getValue())) {
				map.get(entry.getValue()).add(entry.getKey());

			} else {
				map.put(entry.getValue(), new ArrayList<String>());
				map.get(entry.getValue()).add(entry.getKey());
			}
		}
		logger.info(tempMap);
		tempMap.clear();
		tempMap = null;

		// 按照分组筛�?
		for (Map.Entry<Integer, List<String>> entry : map.entrySet()) {
			logger.info(entry.getKey() + "  " + entry.getValue());
		}
		// 按照备份的数�?
		List<String> returnStr = new ArrayList<String>(replication);
		int count = 0;
		for (int i = files.length; i >= 1; i--) {
			if (count >= replication)
				break;
			if (map.get(i) != null) {
				// 查看有几台，还需要几�?
				int all = map.get(i).size();
				int need = replication - count;
				// 如果相同
				if (all == need) {
					returnStr.addAll(map.get(i));
					count += map.get(i).size();

				} else if (all > need) {// 随机的�?need�?
					List<String> temp = map.get(i);
					for (int kk = 1; kk <= need; kk++) {
						int temp_i = random.nextInt(temp.size());
						returnStr.add(temp.get(temp_i));
						temp.remove(temp_i);
						count++;
					}

				} else if (all < need) {//
					// 都要
					returnStr.addAll(map.get(i));
					count += map.get(i).size();
				}

			}

		}
		map.clear();
		map = null;
		return returnStr;

	}

	@Override
	public void setUuid(String uuid) {
		this.uuid=uuid;
		
	}

}
