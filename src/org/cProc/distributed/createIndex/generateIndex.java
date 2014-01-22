package org.cProc.distributed.createIndex;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.io.IOUtils;
import org.cProc.GernralRead.ReadData.DataType;
import org.cProc.GernralRead.ReadData.IndexColumnInfor;
import org.cProc.GernralRead.ReadData.ParseIndexFormat;
import org.cProc.GernralRead.ReadData.ReadData;
import org.cProc.GernralRead.TableAndIndexTool.TableAndIndexTool;
import org.cProc.distributed.createIndexData.CreateIndexData;
import org.cProc.log.indexmapinfor.LocalTempIndexFileInfor;
import org.cProc.tool.myXMLReader.MyXmlReader;

public class generateIndex implements CreateIndexData {

	// public TreeMap<String, StringBuffer> treeMap = new TreeMap<String,
	// StringBuffer>();
	// public StringBuffer strBuf = new StringBuffer();
	// // private final static int VERSION = 1;
	// private static final String SPLIT = "\t";
	private static Log Log4jlogger = LogFactory.getLog(generateIndex.class);
	public String recordCounterPath = "";
	public String tmpName = "";// "tmp_x_index";
	public static final String FILE_SPLIT = "#";
	public static final String FILE_SPLIT_REPLACE = "/";
	public String sourceRootPath = ""; // /CDRDIR/

	public long miniStartTime = Long.MAX_VALUE;
	public long maxStartTime = Long.MIN_VALUE;
	public Configuration conf;
	public DistributedFileSystem fs;
	public long totalCount = 0;
	public String conFileName = "";
	private String hdfsURL = "";
	public long recordNum = 0;

	public StringBuffer errorMessage = new StringBuffer();
	private String tableName = "";
	private String appName = "";
	private String currentIndexPath = "";
	private IIndexCreater indexCreater = null;
	private ReadData readData = null;
	private List<String> maxAndMinPathList = new ArrayList<String>();
	private boolean hasMaxMin = false;
	private LocalTempIndexFileInfor indexFileInfor = null;
	private ArrayList<String> tempFileList = new ArrayList<String>();

	private String uuid;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getErrorMessage() {
		return errorMessage.toString();
	}

	public void addErrorMessage(String error) {
		this.errorMessage.append("\n").append(error);
	}

	public void setLocalTempIndexFileInfor(
			LocalTempIndexFileInfor indexFileInfor) {
		this.indexFileInfor = indexFileInfor;
	}

	public boolean isHasMaxMin() {
		return hasMaxMin;
	}

	public void setHasMaxMin(boolean hasMaxMin) {
		this.hasMaxMin = hasMaxMin;
	}

	private ArrayList<String> sourceFilePaths = new ArrayList<String>();

	/**
	 * 鎬ц兘鍙傛暟
	 * 
	 * @return
	 */
	private boolean needSleep = false;// 鏄惁瑕佺潯锟�
	private long countSleep = 0;// 澶氬皯鏉＄潯鐪犱竴锟�
	private long sleepSec = 0;// 姣忎竴娆＄潯鐪犲灏戞锟�

	public ArrayList<String> getSourceFilePaths() {
		return this.sourceFilePaths;
	}

	private IIndexCreater createIIndexCreater() {
		return new BTreeIndexCreater();
	}

	public void addInputFileName(String lName) {
		sourceFilePaths.add(lName);
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public void setGenerateConfFile(String filename) {
		conFileName = filename;
	}

	public void generateIndexFile(String tableName, ArrayList<String> pathes,
			String indexDataName, ArrayList<IndexColumnInfor> indexsPos)
			throws IOException {
		// tag1
		String time = String.valueOf(new Date().getTime());
		MyXmlReader reader = new MyXmlReader(conFileName);
		hdfsURL = reader.getName("hdfsURL");
		sourceRootPath = reader.getName("sourceRoot");
		// TODO:
		recordCounterPath = reader.getName("recordCounterPath");// "/indexingCDR/CDRTotalNum/";
		Log4jlogger.info("sourceRootPath    is  "
				+ reader.getName("sourceRoot"));

		tmpName = "tmp_" + time + "_index";
		// indexPath = indexName + tmpName + "/";
		currentIndexPath = indexDataName;
		// 貌似从tag1开始的代码都无用

		// 依次扫描原文件
		for (int i = 0; i < pathes.size(); i++) {
			scanFileInfo(pathes.get(i), indexsPos);
		}
		this.indexCreater.setFileSystem(this.fs);
		// 设置索引文件路径
		this.indexCreater.setIndexPath(indexDataName);
		writeToFile();

	}

	private void scanFileInfo(String path, ArrayList<IndexColumnInfor> indexsPos)
			throws IOException {
		Log4jlogger.info("@gyy:fileName is " + path);
		// 设置原文件路径
		this.readData.setFileName(path);
		this.readData.startReadFile();
		// Map<Integer, Object> data = new HashMap<Integer, Object>();
		int len = indexsPos.size(); //
		long offset = this.readData.getNextDataPos();

		// 如果需要跳过前n行，先跳过
		for (int skip = 0; skip < Config.skipLine; skip++) {
			this.readData.readNextData();
			offset = this.readData.getNextDataPos();
		}

		long tempCount = 0;
		if (this.needSleep) {// 需要睡眠时每读取countSleep条后睡眠sleepSec秒
			while (this.readData.readNextData()) {
				if (tempCount >= this.countSleep) {
					tempCount = 0;
					try {
						Thread.sleep(this.sleepSec);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				tempCount++;

				Field field = new Field();
				// 设置该记录原文件名
				field.setPath(path);
				// 设置该记录所有索引字段的值
				for (int i = 0; i < len; i++) {
					field.addData(i + 1,
							this.readData.readColumn(indexsPos.get(i)));
				}

				field.setOffset(offset);// 设置该记录在索引文件中的超始位置
				indexCreater.addIndexData(field);
				offset = this.readData.getNextDataPos();
				field = null;
			}

		} else {
			int count = 0;
			while (this.readData.readNextData()) {
				Field field = new Field();
				field.setPath(path);
				for (int i = 0; i < len; i++) {
					field.addData(i + 1,
							this.readData.readColumn(indexsPos.get(i)));
				}
				if (count % 10000 == 0) {
					System.out.println("scanFileInfo::offset is " + offset);
				}
				field.setOffset(offset);// 设置该记录在索引文件中的超始位置
				indexCreater.addIndexData(field);
				offset = this.readData.getNextDataPos();
				field = null;
				count++;
			}
		}
		// field = null;
		this.readData.closeReadFile();
	}

	public byte[] getMax(Class type, byte[] o1, byte[] o2) {
		int reslut = DataType.compare(type, o1, o2);
		if (reslut > 0) {
			return o1;
		} else if (reslut < 0) {
			return o2;
		}
		return o1;

		// if (type == Byte.class) {
		// Byte b1 = o1[0];
		// Byte b2 = o2[0];
		// if (b1.compareTo(b2) > 0) {
		// b1 = null;
		// b2 = null;
		// return o1;
		// }
		// b1 = null;
		// b2 = null;
		// return o2;
		// } else if (type == Short.class) {
		// Short b1 = Bytes.toShort(o1);
		// Short b2 = Bytes.toShort( o2);
		// if (b1.compareTo(b2) > 0) {
		// b1 = null;
		// b2 = null;
		// return o1;
		// }
		// b1 = null;
		// b2 = null;
		// return o2;
		//
		// } else if (type == Integer.class) {
		// Integer b1 = Bytes.toInt(o1);
		// Integer b2 = Bytes.toInt(o2);
		// if (b1.compareTo(b2) > 0) {
		// b1 = null;
		// b2 = null;
		// return o1;
		// }
		// b1 = null;
		// b2 = null;
		// return o2;
		//
		// } else if (type == Long.class) {
		// Long b1 =Bytes.toLong( o1);
		// Long b2 = Bytes.toLong(o2);
		// if (b1.compareTo(b2) > 0) {
		// b1 = null;
		// b2 = null;
		// return o1;
		// }
		// b1 = null;
		// b2 = null;
		// return o2;
		//
		// } else if (type == String.class) {
		// String b1 = Bytes.toString(o1);
		// String b2 = Bytes.toString(o2);
		// if (b1.compareTo(b2) > 0) {
		// b1 = null;
		// b2 = null;
		// return o1;
		// }
		// b1 = null;
		// b2 = null;
		// return o2;
		//
		// }
		// return null;
	}

	public byte[] getMin(Class type, byte[] o1, byte[] o2) {

		int reslut = DataType.compare(type, o1, o2);
		if (reslut < 0) {
			return o1;
		} else if (reslut > 0) {
			return o2;
		}
		return o1;

		// if (type == Byte.class) {
		// Byte b1 = o1[0];
		// Byte b2 = o2[0];
		// if (b1.compareTo(b2) > 0) {
		// b1 = null;
		// b2 = null;
		// return o2;
		// }
		// b1 = null;
		// b2 = null;
		// return o1;
		// } else if (type == Short.class) {
		// Short b1 = Bytes.toShort(o1);
		// Short b2 = Bytes.toShort( o2);
		// if (b1.compareTo(b2) > 0) {
		// b1 = null;
		// b2 = null;
		// return o2;
		// }
		// b1 = null;
		// b2 = null;
		// return o1;
		//
		// } else if (type == Integer.class) {
		// Integer b1 = Bytes.toInt(o1);
		// Integer b2 = Bytes.toInt(o2);
		// if (b1.compareTo(b2) > 0) {
		// b1 = null;
		// b2 = null;
		// return o2;
		// }
		// b1 = null;
		// b2 = null;
		// return o1;
		//
		// } else if (type == Long.class) {
		// Long b1 =Bytes.toLong( o1);
		// Long b2 = Bytes.toLong(o2);
		// if (b1.compareTo(b2) > 0) {
		// b1 = null;
		// b2 = null;
		// return o2;
		// }
		// b1 = null;
		// b2 = null;
		// return o1;
		//
		// } else if (type == String.class) {
		// String b1 = Bytes.toString(o1);
		// String b2 = Bytes.toString(o2);
		// if (b1.compareTo(b2) > 0) {
		// b1 = null;
		// b2 = null;
		// return o2;
		// }
		// b1 = null;
		// b2 = null;
		// return o1;
		//
		// }
		// return null;
	}

	// public byte[] object2Byte(Object content) {
	// Class type = content.getClass();
	// if (type == Integer.class) {
	// return Bytes.toBytes((Integer) content);
	// } else if (type == Long.class) {
	// return Bytes.toBytes((Long) content);
	// } else if (type == String.class) {
	// return content.toString().getBytes();
	// } else if (type == Byte.class) {
	// return Bytes.toBytes((Byte) content);
	// } else if (type == Short.class) {
	// return Bytes.toBytes((Short) content);
	// }
	// return null;
	// }

	public boolean startCreateIndex() {
		try {
			Log4jlogger.debug("create index starting...");

			MyXmlReader reader = new MyXmlReader(conFileName);
			hdfsURL = reader.getName("hdfsURL");
			this.needSleep = Boolean.valueOf(reader.getName("needSleep"));
			this.countSleep = Long.parseLong(reader.getName("countSleep"));
			this.sleepSec = Long.parseLong(reader.getName("sleepSec"));

			Date time1 = new Date();
			conf = new Configuration();
			fs = new DistributedFileSystem();
			fs.initialize(new URI(hdfsURL), conf);

			// 判断当前的数据格式

			if (Config.isText) {
				readData = ReadData.getTextReadData(tableName, appName, fs);
			} else {
				readData = ReadData.getBinaryReadData(tableName, appName, fs);
			}

			// 取出表的所有索引名
			ArrayList<String> indexList = TableAndIndexTool
					.getAllIndexsInTable(tableName, appName, fs);
			// DebugPrint.DebugPrint("indexList---->"+indexList.size(), this);
			// 依次对每个索引处理

			// TODO

			// 校验是否能打开
			for (String temp_path : sourceFilePaths) {
				InputStream is = null;
				try {

					is = fs.open(new Path(temp_path));
					
					

				} catch (Exception e) {

					e.printStackTrace();

					//
					//
					// 1文件存在 ，2大小 、、3 tmp,4时间

					StringBuffer sb = new StringBuffer();
					sb.append(e.getMessage()).append("\n");
					sb.append(temp_path).append("\n");
					if (!fs.exists(new Path(temp_path))) {
						sb.append("cdr file not exist").append("\n");
						if (fs.exists(new Path(temp_path + ".tmp"))) {

							sb.append("just   exist temp file").append("\n");

						} else {

							sb.append("not   exist temp file").append("\n");
						}

					}
					if (!fs.exists(new Path("/smp/fail_tmp"))) {
						fs.mkdirs(new Path("/smp/fail_tmp"));
					}

					FSDataOutputStream fos = null;

					try {
						fos = fs.create(new Path("/smp/fail_tmp/" + temp_path.replaceAll("/", "#")));
						fos.write(sb.toString().getBytes());
					} catch (Exception e2) {
						// TODO: handle exception
						e2.printStackTrace();
					} finally {
						if(fos!=null)
							fos.close();
					}
					

				
					fos.close();

					sourceFilePaths.remove(temp_path);

					Log4jlogger.error("open file:" + temp_path + "-------"
							+ e.getMessage());

				} finally {
					IOUtils.closeStream(is);
				}

			}

			for (int indexNum = (indexList.size() - 1); indexNum >= 0; indexNum--) {
				// LocalTempIndexFileInfor indexFileInfor = new
				// LocalTempIndexFileInfor();
				ArrayList<IndexColumnInfor> indexColumnIndexPos = ReadData
						.getIndexColumnPos(indexList.get(indexNum), tableName,
								appName, readData);
				// 索引文件存放路径
				// ex. /smp/cdr/bssap/indexfile/calledIndex/data
				String indexDataPath = TableAndIndexTool.getIndexDataPath(
						tableName, indexList.get(indexNum), appName);

				// 取出索引对应的格式，每个索引都有一个格式，内含索引相关字段的信息
				ParseIndexFormat indexFormat = ReadData.getIndexFormat(
						indexList.get(indexNum), tableName, appName, readData);
				indexCreater = createIIndexCreater();
				indexCreater.setIndexFormat(indexFormat);
				indexCreater.setUuid(uuid);
				generateIndexFile(tableName, sourceFilePaths, indexDataPath,
						indexColumnIndexPos);
				indexCreater = null;
				Log4jlogger.debug("*****");

				Date time2 = new Date();
				Log4jlogger.debug("scan costs "
						+ (time2.getTime() - time1.getTime()) / 1000
						+ " seconds");
				Log4jlogger.debug("scan costs "
						+ (time2.getTime() - time1.getTime()) + " ms");

				Date time3 = new Date();
				Log4jlogger.debug("write costs "
						+ (time3.getTime() - time2.getTime()) / 1000
						+ " seconds");
				Log4jlogger.debug("write costs "
						+ (time3.getTime() - time2.getTime()) + " ms");

				Log4jlogger.info("Total costs "
						+ (time3.getTime() - time1.getTime()) / 1000
						+ " seconds");
				Log4jlogger.debug("ToTAL costs "
						+ (time3.getTime() - time1.getTime()) + " ms");
				Log4jlogger.info("Scaned " + totalCount + " in total");
				Log4jlogger.debug("MiniStartTime:" + miniStartTime);
				Log4jlogger.debug("MaxStartTime:" + maxStartTime);
				indexCreater = null;
			} // end for
				// 涓嶅垹闄ゅ暒锛屽ザ濂剁殑瑕佸锟�
				// this.deleteMaxMin();
		} // end try
		catch (Exception e) {
			this.addErrorMessage(e.getMessage());
			e.printStackTrace();
			Log4jlogger.info("@cw: " + "create index failed");
			Log4jlogger.info("@cw: " + e.getMessage());
			for (int i = 0; i < tempFileList.size(); i++) {
				indexFileInfor.removeLocalFileInfor(tempFileList.get(i));
			}
			tempFileList.clear();
			return false;
		}
		tempFileList.clear();
		return true;
	}

	public void updateStartTime(String startTime) {
		long tmp = Long.valueOf(startTime);
		if (tmp < miniStartTime)
			miniStartTime = tmp;
		if (tmp > maxStartTime)
			maxStartTime = tmp;
	}

	private void calcMaxMin() throws IOException {

		ArrayList<byte[]> max = new ArrayList<byte[]>();// Max
		ArrayList<byte[]> min = new ArrayList<byte[]>();// Min

		String temp = null;
		int len = this.readData.getColumnCount();
		int k = 1;
		for (String _path : maxAndMinPathList) {

			temp = _path;
			// temp = temp.replaceAll(FILE_SPLIT, FILE_SPLIT_REPLACE);
			this.readData.setFileName(temp);
			Log4jlogger.debug("@cwMaxAndMin:fileName: " + temp);
			while (this.readData.readNextData()) {
				if (k == 1) {// 锟�锟斤拷
					for (int i = 0; i < len; i++) {
						byte[] oTemp = this.readData.readColumn(i);
						// System.out.print("@cwMin:column: "+this.readData.getColumnName(i)+"    ");
						// System.out.print("  data is :  " );
						// for(int otgyy = 0 ; otgyy < oTemp.length ; otgyy++)
						// {
						// System.out.print( oTemp[otgyy] +" " );
						// }

						if (min.size() < len) {
							min.add(oTemp);
						} else {
							min.set(i, this.getMin(
									this.readData.getColumnType(i), min.get(i),
									oTemp));
						}
						oTemp = null;
					}
					// Log4jlogger.info();
					k++;
				} else if (k == 2) {// 锟�锟斤拷
					for (int i = 0; i < len; i++) {
						byte[] oTemp = this.readData.readColumn(i);
						// System.out.print("@cwMax:column: "+this.readData.getColumnName(i)+"    ");
						// System.out.print("  data is :  " );
						// for(int otgyy = 0 ; otgyy < oTemp.length ; otgyy++)
						// {
						// System.out.print( oTemp[otgyy]+" " );
						// }

						if (max.size() < len) {
							max.add(oTemp);
						} else {
							max.set(i, this.getMax(
									this.readData.getColumnType(i), max.get(i),
									oTemp));
						}
						oTemp = null;
					}
					// Log4jlogger.info();
					k = 1;
				}
			}
			this.readData.closeReadFile();
		}

		// temp = null;
		if (!max.isEmpty())
			this.indexCreater.setMaxValue(max);
		if (!min.isEmpty())
			this.indexCreater.setMinValue(min);

	}

	public void writeToFile() throws IOException {
		// 璁剧疆锟�锟斤拷锟�锟斤拷锟�
		// DebugPrint.InforPrint("maxAndMinPathList is-->"+this.maxAndMinPathList.size());
		// DebugPrint.InforPrint("maxAndMinPathList is-->"+this.maxAndMinPathList);

		// ArrayList<Object> maxObject = new ArrayList<Object>();// Max
		// ArrayList<Object> minObject = new ArrayList<Object>();// Min

		if (this.hasMaxMin)// 计算最大最小值
		{
			this.calcMaxMin();
		}

		try {
			this.indexCreater.setSources(sourceFilePaths);
			// 写入索引文件
			this.indexCreater.create();
			indexFileInfor.addLocalToHDFSInFor(
					this.indexCreater.getTempLocalFileName(),
					this.indexCreater.getHDFSFileName());
			indexFileInfor.addIndexAndVersionMap(this.indexCreater
					.getHDFSFileName(), indexCreater.getIndexFormat()
					.getIndexName(), indexCreater.getIndexFormat()
					.getIndexVersion());
			tempFileList.add(this.indexCreater.getTempLocalFileName());
		} catch (Exception e) {
			// DebugPrint.DebugPrint("exception:" + e.getMessage(), this);
			e.printStackTrace();

		}
	}

	/**
	 * 鍒犻櫎锟�锟斤拷锟�锟斤拷鍊兼枃锟�
	 */
	public void deleteMaxMin() {
		if (maxAndMinPathList.isEmpty()) {
			maxAndMinPathList = null;
			return;
		}

		for (String _path : maxAndMinPathList) {
			if (_path == null || _path.trim().length() == 0)
				continue;
			try {
				this.fs.delete(new Path(_path), true);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		maxAndMinPathList = null;
	}

	/***
	 * 绉诲姩鏂囦欢
	 * 
	 * @return
	 */
	public void mvFile(String src, String dest) {
		try {
			this.fs.rename(new Path(src), new Path(dest));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public IIndexCreater getIndexCreater() {
		return indexCreater;
	}

	public void addMaxAndMinPath(String maxAndMinPath) {
		maxAndMinPathList.add(maxAndMinPath);
	}

	public void setIndexCreater(IIndexCreater indexCreater) {
		this.indexCreater = indexCreater;
	}

	// 娴嬭瘯
	// private void test() {
	// Object int1 = 45;
	// Object String1 = "112";
	//
	// Object int2 = 4115;
	// Object String2 = "234";
	// /////
	// Object int3 = 1;
	// Object String3 = "934";
	//
	// Object int4 = 11;
	// Object String4 = "411111";
	//
	// // 璁剧疆锟�锟斤拷锟�锟斤拷锟�
	// ArrayList<Object> maxObject = new ArrayList<Object>();// Max
	// ArrayList<Object> minObject = new ArrayList<Object>();// Min
	//
	// ArrayList<byte[]> max = new ArrayList<byte[]>();// Max
	// ArrayList<byte[]> min = new ArrayList<byte[]>();// Min
	//
	// String temp = null;
	//
	//
	// if (minObject.isEmpty()) {
	// minObject.add(int1);
	// minObject.add(String1);
	// }
	//
	// if (maxObject.isEmpty()) {
	// maxObject.add(int2);
	// maxObject.add(String2);
	// }
	//
	// minObject.set(0, this.getMin(minObject.get(0), int3));
	// minObject.set(1, this.getMin(minObject.get(1), String3));
	//
	//
	// maxObject.set(0, this.getMax(maxObject.get(0), int4));
	// maxObject.set(1, this.getMax(maxObject.get(1), String4));
	//
	// Log4jlogger.info(maxObject);
	// Log4jlogger.info(minObject);
	// // 杞负byte
	// for (int i = 0; i < 2; i++) {
	// max.add(this.object2Byte(maxObject.get(i)));
	// min.add(this.object2Byte(minObject.get(i)));
	//
	// }
	// //
	// maxObject.clear();
	// minObject.clear();
	// maxObject = null;
	// minObject = null;
	// maxAndMinPathList.clear();
	// maxAndMinPathList = null;
	//
	// }

	// public void newFolder(String folderPath) {
	// try {
	//
	// String filePath = folderPath;
	// filePath = filePath.toString();
	// File myFilePath = new File(filePath);
	// if (!myFilePath.exists()) {
	// myFilePath.mkdir();
	// }
	// } catch (Exception e) {
	// Log4jlogger.info("");
	// e.printStackTrace();
	// }
	// }

	public static void main(String[] args) {
		generateIndex index = new generateIndex();

		// index.test();
	}
}
