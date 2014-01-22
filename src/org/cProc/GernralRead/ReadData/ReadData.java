package org.cProc.GernralRead.ReadData;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.fs.FileSystem;
import org.cProc.GernralRead.ReadData.ReadDataProxy.BinaryReadDataProxy;
import org.cProc.GernralRead.ReadData.ReadDataProxy.ReadDataProxy;
import org.cProc.GernralRead.ReadData.ReadDataProxy.TextReadDataProxy;
import org.cProc.GernralRead.TableAndIndexTool.TableAndIndexTool;
import org.cProc.function.SupportFunction;
import org.cProc.tool.Bytes;



public  class ReadData {

	private ParseTableFormat  tableFormatRead = null;
	private FileSystem fs = null;

	private ReadDataProxy  readProxy = null;

	private final String ColumnSeperate =",";
	
	//获取文本读取的方�?
	public static ReadData getTextReadData(String tableName , String appName,FileSystem fs )
	{
		String tableFormatFile = TableAndIndexTool.getTableFormat(tableName, appName);

		ReadData reader =  new ReadData(tableFormatFile, fs );
		ParseTableFormat  tableFormat = reader.getParseTableFormat() ;
		TextReadDataProxy proxy = new TextReadDataProxy(fs);
		
		proxy.setParseTableFormat(tableFormat);
//		proxy.setDataSize(tableFormat.getDataSize());
//		proxy.setLengthOfFileds(tableFormat.getLengthOfFileds());
//		proxy.setStartPosOfFileds(tableFormat.getStartPosOfFileds());
		reader.setReadDataProxy(proxy);		
		return reader;
	}
	
	public static ReadData getTextReadData(String tableName , String appName,FileSystem fs ,String tableFormatFile)
	{
		ReadData reader =  new ReadData(tableFormatFile, fs );
		ParseTableFormat  tableFormat = reader.getParseTableFormat() ;
		TextReadDataProxy proxy = new TextReadDataProxy(fs);
		
		proxy.setParseTableFormat(tableFormat);
//		proxy.setDataSize(tableFormat.getDataSize());
//		proxy.setLengthOfFileds(tableFormat.getLengthOfFileds());
//		proxy.setStartPosOfFileds(tableFormat.getStartPosOfFileds());
		reader.setReadDataProxy(proxy);		
		return reader;
	}
	
	//获取二进制读取的方式
	public static ReadData getBinaryReadData(String tableName ,String appName, FileSystem fs )
	{
		String tableFormatFile = TableAndIndexTool.getTableFormat(tableName, appName);

		ReadData reader =  new ReadData(tableFormatFile, fs );
		ParseTableFormat  tableFormat = reader.getParseTableFormat() ;
		BinaryReadDataProxy proxy = new BinaryReadDataProxy(fs);
		proxy.setDataSize(tableFormat.getDataSize());
		proxy.setLengthOfFileds(tableFormat.getLengthOfFileds());
		proxy.setStartPosOfFileds(tableFormat.getStartPosOfFileds());
		reader.setReadDataProxy(proxy);		
		return reader;
	}
	
	public static  ReadData getBinaryReadData(String tableName ,String appName, FileSystem fs  ,String tableFormateFile)
	{

		ReadData reader =  new ReadData(tableFormateFile, fs );
		ParseTableFormat  tableFormat = reader.getParseTableFormat() ;
		BinaryReadDataProxy proxy = new BinaryReadDataProxy(fs);
		proxy.setDataSize(tableFormat.getDataSize());
		proxy.setLengthOfFileds(tableFormat.getLengthOfFileds());
		proxy.setStartPosOfFileds(tableFormat.getStartPosOfFileds());
		reader.setReadDataProxy(proxy);		
		return reader;
	}
	
	//获取索引在列中的位置
	public static ArrayList<IndexColumnInfor>  getIndexColumnPos( String indexName, String tableName,String appName,ReadData readData)
	{

		//索引定义文件名，ex. /smp/cdr/tableInfo/bssap/indexfile/calledIndex/index.frm
		String indexFileName = TableAndIndexTool.getIndexFormat(tableName, indexName, appName);

		
		ParseIndexFormat  indexFormat= ParseIndexFormat.getParseIndexFormat(indexFileName, readData.getFileSystem());
		
		ArrayList<IndexColumnInfor> result = indexFormat.getColumnPosInTable(readData.getParseTableFormat());
		return result;
	}
	
	//获取索引格式
	public static ParseIndexFormat  getIndexFormat( String indexName, String tableName,String appName,ReadData readData)
	{
		//根据索引名取出索引定义文件名，ex. /smp/cdr/tableInfo/bssap/indexfile/calledIndex/index.frm
		String indexFileName = TableAndIndexTool.getIndexFormat(tableName, indexName, appName);
		ParseIndexFormat  indexFormat= ParseIndexFormat.getParseIndexFormat(indexFileName, readData.getFileSystem());
		return indexFormat;
	}
	
	//获取读取的方�?
	public void setReadDataProxy(ReadDataProxy readProxy )
	{
		this.readProxy = readProxy;
	}
	
	//获取表格�?
	private ParseTableFormat  getParseTableFormat()
	{
		return tableFormatRead;
	}
	
	public ReadData(String tableFormatFile , FileSystem fs )
	{
		tableFormatRead = new ParseTableFormat(tableFormatFile, fs) ;
		setFileSystem(fs);
	}
	
	
	public int getColumnCount()
	{
		return tableFormatRead.getColumnName().size();
	}
	//设置文件�?
	public void setFileName(String fileName)
	{
		readProxy.setFileName(fileName);
	}
	//获取文件系统
	private void setFileSystem(FileSystem fs )
	{
		this.fs  = fs ;
	}
	//获取文件系统
	private FileSystem getFileSystem()
	{
		return fs;
	}
	
	//�?��读取文件
	public   void startReadFile() throws IOException
	{
		readProxy.startReadFile();
	}
	//关闭文件
	public   void closeReadFile() throws IOException
	{
		readProxy.closeReadFile();
	}
	//获取下一个数据的偏移
	public   long getNextDataPos()
	{
		return readProxy.getOffset();
	}
	//读取下一个数�?
	public   boolean readNextData() throws IOException
	{
		return readProxy.readNextData();
	}
	
	//读取指定列的值，返回字节
	protected     byte[]  readColumnBytes(String columnName)
	{
		return readProxy.getColumnData(tableFormatRead.getColunmPos(columnName));
	}
	//返回指定列的类型
	public  Class getColumnType(String columnName)
	{
		Class type = tableFormatRead.getColumnType(columnName);
		return type;
	}
	/**
	 * 返回指定列的类型
	 * @param columnPos
	 * @return
	 */
	public  Class getColumnType(int index)
	{
		Class type = tableFormatRead.getColumnType(this.getColumnName(index));
		return type;
	}
	//返回指定位置的列的名�?
	public String getColumnName(int columnPos)
	{
		return tableFormatRead.getColumnName(columnPos);
	}
	
	//返回指定位置的列的数�?
	public  byte[]  readColumn(int columnPos)
	{
		String columnName = getColumnName(columnPos);
		return readColumn(columnName);
	}
	
	//返回指定位置的列的数�?
	public  byte[]  readColumn(IndexColumnInfor columnInfor)
	{
		if(columnInfor.isFunction())
		{
			
			String functionName = columnInfor.getFunctionName();
			String columnName = columnInfor.getColumnName();
			return SupportFunction.supportFunction(functionName, columnInfor.getFunctionPara(), readColumn(columnInfor.getColumnName()), 
							this.getColumnType(columnInfor.getColumnName()), 
							 columnInfor.getFunctionReturnType());
		}
		else
		{
			return readColumn(columnInfor.getColumnName());
		}
	}
	
//	//返回指定列的数据
//	public   Object readColumnfForObject(String columnName)
//	{
//		byte[] data = readColumnBytes(columnName);
//		Class  type = getColumnType(columnName);
//		return DataType.getObject(type, data);
//	}
	public   byte[] readColumn(String columnName)
	{
		byte[] data = readColumnBytes(columnName);
		return data;
	}
	
	public  byte[] getColumnData(String columnName ,byte[] data)
	{
			int columnPos = tableFormatRead.getColunmPos(columnName);
			int startPos  = tableFormatRead.getStartPosOfFileds().get(columnPos);
			int length   =  tableFormatRead.getLengthOfFileds().get(columnPos);
			byte [] reslutData = new byte[length];
			
//			System.arraycopy(data, startPos, reslutData, 0, length);

			
				for(int i = 0 ;i < length  ; i++)
				{
					reslutData[i] = data[startPos+length-1-i];
				}
			
			
			return reslutData;			
	}
	
	
	public  int getColumnPos(String columnName)
	{
			return tableFormatRead.getColunmPos(columnName);
	
	}
	
	
	public  byte[] getColumnDataNoRever(String columnName ,byte[] data)
	{
			int columnPos = tableFormatRead.getColunmPos(columnName);
			int startPos  = tableFormatRead.getStartPosOfFileds().get(columnPos);
			int length   =  tableFormatRead.getLengthOfFileds().get(columnPos);
			byte [] reslutData = new byte[length];
			
			System.arraycopy(data, startPos, reslutData, 0, length);
			
			return reslutData;			
	}
	
	public int getDataSize()
	{
		return tableFormatRead.getDataSize();
	}
	
		
	public static void main(String args[])
	{
		
	}
//	{
//		boolean isMySQL = true;
//		Configuration conf = new Configuration();
////	    conf.set("fs.default.name","hdfs://192.168.1.111:9000");
//	    TableAndIndexTool.setPrePath("/smp");
//		try {
//			FileSystem fs = FileSystem.get(conf); 
//
//			String fileName =  "C:\\dingli\\test\\data\\testbssap\\1329712649-1329712719-bssap-1-server1110.cdr";
//			
//			String indexName = "C:\\dingli\\CDR_direct\\tableInfo\\bssap\\indexfile\\drillIndex\\index.frm";
//			ReadData reader = ReadData.getBinaryReadData("bssap", "cdr", fs);
//			System.out.println("dataSize is " + reader.getDataSize());
//			int count = 0 ;
//			MySQLDao dao = new MySQLDao(); 
//			ParseIndexFormat  readIndexFormat = null;
//			if(fs != null)
//				{
//					 readIndexFormat = new ParseIndexFormat(indexName,fs);
//				}
//
//			ReadWriteIndexDataProxy proxy = new ReadWriteIndexDataProxy(readIndexFormat);
//			ArrayList<byte[]> dataList = new ArrayList<byte[]>();
//			dataList.add(					DataType.getBytes(Long.class, 
//			         Bytes.toLong(DataType.getUnsginedMinBytes(Long.class))+"")
//						);
//			
//			dataList.add(					DataType.getBytes(Integer.class, 
//			         Bytes.toLong(DataType.getUnsginedMinBytes(Integer.class))+"")
//						);
//			dataList.add(					DataType.getBytes(Long.class, 
//			         Bytes.toLong(DataType.getUnsginedMinBytes(Long.class))+"")
//						);
//			dataList.add(					DataType.getBytes(Byte.class, 
//			         Bytes.toLong(DataType.getUnsginedMinBytes(Byte.class))+"")
//						);
//			dataList.add(					DataType.getBytes(Byte.class, 
//			         Bytes.toLong(DataType.getUnsginedMinBytes(Byte.class))+"")
//						);
//			dataList.add(					DataType.getBytes(Long.class, 
//			         Bytes.toLong(DataType.getUnsginedMinBytes(Long.class))+"")
//						);
//			dataList.add(					DataType.getBytes(Short.class, 
//			         Bytes.toLong(DataType.getUnsginedMinBytes(Short.class))+"")
//						);
//
//	        BPlusTree<MulitFiledData> b=new BPlusTree<MulitFiledData>(131 , new MulitFiledData(dataList,proxy));   
//	    	FileStatus fileList[] = fs.listStatus(new Path("/smp/cdr/bicc/datafile"));
//	       //  System.out.println(fileList[0].getPath().getName());
//	    	 int countNumber= 0 ; 
//		      int reslutCount = 0;
//	//    	 System.out.println("file number is " + fileList.length);
////			for( int i = 0 ; i < fileList.length ; i++)
//////
//	    	for( int i = 0 ; i < 1 ; i++)
//			{                                                                          
//					System.out.println(" i  is " + i);
///*				    System.out.println("/gentest/bssap/datafile/"+fileList[i].getPath().getName());
//					reader.setFileName("/gentest/bssap/datafile/"+fileList[i].getPath().getName());*/
//	    			reader.setFileName(fileName);
//					reader.startReadFile();
//			       long offset = 0;
//			       String insertSQL=" insert into new_gyybssap values ( ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?,?)";
//			       PreparedStatement ps = dao.getPreparedStatement(insertSQL);
//
//			       long start_time = System.currentTimeMillis();
//			       int numberCount = 0;
//					while(reader.readNextData())
//					{
//						numberCount++;
//						try {
//							if(isMySQL)
//							{
////								System.out.println("start_time_s  is " +Bytes.toInt(reader.readColumn("start_time_s")) );
//								   ps.setInt(1, Bytes.toInt(reader.readColumn("start_time_s")));
//								   ps.setInt(2, Bytes.toInt(reader.readColumn("start_time_ns")));
//								   ps.setInt(3, Bytes.toInt(reader.readColumn("end_time_s")));
//								   ps.setInt(4, Bytes.toInt(reader.readColumn("end_time_ns")));
//								   ps.setInt(5, Bytes.toInt(reader.readColumn("cdr_index")));
//								   ps.setInt(6, (int)(reader.readColumn("cdr_type")[0]));
//								   ps.setInt(7, (int)(reader.readColumn("cdr_result")[0]));
//								   ps.setInt(8, Bytes.toInt(reader.readColumn("base_cdr_index")));
//								   ps.setInt(9, (int)(reader.readColumn("base_cdr_type")[0]));
//								   ps.setInt(10, Bytes.toInt(reader.readColumn("tmsi")));
//								   ps.setInt(11, Bytes.toInt(reader.readColumn("new_tmsi")));
//								  ps.setLong  (12,Bytes.toLong(reader.readColumn("imsi")));
//								  ps.setLong (13,Bytes.toLong(reader.readColumn("imei")));
//								  ps.setLong (14,Bytes.toLong(reader.readColumn("calling_number")));
//								  ps.setLong (15,Bytes.toLong(reader.readColumn("called_number")));
//								ps.setString(16,Bytes.toString(reader.readColumn("calling_number_str")));
//								 ps.setString(17,Bytes.toString(reader.readColumn("called_number_str")));
//								  ps.setLong  (18,Bytes.toLong(reader.readColumn("mgw_ip")));
//								  ps.setLong (19,Bytes.toLong(reader.readColumn("msc_server_ip")));
//								   ps.setInt(20, Bytes.toInt(reader.readColumn("bsc_spc")));
//								   ps.setInt(21, Bytes.toInt(reader.readColumn("msc_spc")));
//								   ps.setInt(22, Bytes.toShort(reader.readColumn("lac")));
//								   ps.setInt(23, Bytes.toShort(reader.readColumn("ci")));
//								   ps.setInt(24, Bytes.toShort(reader.readColumn("last_lac")));
//								   ps.setInt(25, Bytes.toShort(reader.readColumn("last_ci")));
//								   ps.setInt(26, (int)(reader.readColumn("cref_cause")[0]));
//								   ps.setInt(27, (int)(reader.readColumn("cm_rej_cause")[0]));
//								   ps.setInt(28, (int)(reader.readColumn("lu_rej_cause")[0]));
//								   ps.setInt(29, (int)(reader.readColumn("assign_failure_cause")[0]));
//								   ps.setInt(30, (int)(reader.readColumn("rr_cause")[0]));
//								   ps.setInt(31, (int)(reader.readColumn("cip_rej_cause")[0]));
//								   ps.setInt(32, (int)(reader.readColumn("disconnect_cause")[0]));
//								   ps.setInt(33, (int)(reader.readColumn("cc_rel_cause")[0]));
//								   ps.setInt(34, (int)(reader.readColumn("clear_cause")[0]));
//								   ps.setInt(35, (int)(reader.readColumn("cp_cause")[0]));
//								   ps.setInt(36, (int)(reader.readColumn("rp_cause")[0]));
//								   ps.setInt(37, Bytes.toInt(reader.readColumn("first_paging_time")));
//								   ps.setInt(38, Bytes.toInt(reader.readColumn("second_paging_time")));
//								   ps.setInt(39, Bytes.toInt(reader.readColumn("third_paging_time")));
//								   ps.setInt(40, Bytes.toInt(reader.readColumn("fourth_paging_time")));
//								   ps.setInt(41, Bytes.toInt(reader.readColumn("cc_time")));
//								   ps.setInt(42, Bytes.toInt(reader.readColumn("assignment_time")));
//								   ps.setInt(43, Bytes.toInt(reader.readColumn("assignment_cmp_time")));
//								   ps.setInt(44, Bytes.toInt(reader.readColumn("setup_time")));
//								   ps.setInt(45, Bytes.toInt(reader.readColumn("alert_time")));
//								   ps.setInt(46, Bytes.toInt(reader.readColumn("connect_time")));
//								   ps.setInt(47, Bytes.toInt(reader.readColumn("disconnect_time")));
//								   ps.setInt(48, Bytes.toInt(reader.readColumn("clear_request_time")));
//								   ps.setInt(49, Bytes.toInt(reader.readColumn("clear_command_time")));
//								   ps.setInt(50, Bytes.toInt(reader.readColumn("rp_data_time")));
//								   ps.setInt(51, Bytes.toInt(reader.readColumn("rp_ack_time")));
//								   ps.setInt(52, Bytes.toInt(reader.readColumn("auth_request_time")));
//								   ps.setInt(53, Bytes.toInt(reader.readColumn("auth_response_time")));
//								   ps.setInt(54, Bytes.toInt(reader.readColumn("cm_service_accept_time")));
//								   ps.setInt(55, Bytes.toInt(reader.readColumn("call_confirm_preceding_time")));
//								   ps.setInt(56, Bytes.toInt(reader.readColumn("connect_ack_time")));
//								 ps.setString(57,Bytes.toString(reader.readColumn("smsc")));
//								   ps.setInt(58, (int)(reader.readColumn("sm_type")[0]));
//								   ps.setInt(59, (int)(reader.readColumn("sm_data_coding_scheme")[0]));
//								   ps.setInt(60, Bytes.toShort(reader.readColumn("sm_length")));
//								   ps.setInt(61, (int)(reader.readColumn("rp_data_count")[0]));
//								   ps.setInt(62, (int)(reader.readColumn("handover_count")[0]));
//								   ps.setInt(63,  (int)(reader.readColumn("info_trans_capability")[0]));
//								   ps.setInt(64,  (int)(reader.readColumn("speech_version")[0]));
//								   ps.setInt(65,  (int)(reader.readColumn("failed_handover_count")[0]));
//								   ps.setInt(66,  (int)(reader.readColumn("call_stop")[0]));
//								   ps.setInt(67,  (int)(reader.readColumn("interbsc_ho_count")[0]));
//								   ps.setInt(68, Bytes.toShort(reader.readColumn("pcmts")));
//								  ps.setLong  (69,Bytes.toLong(reader.readColumn("bscid")));
//								   ps.setInt(70,  (int)(reader.readColumn("call_stop_msg")[0]));
//								   ps.setInt(71,  (int)(reader.readColumn("call_stop_cause")[0]));
//								   ps.setInt(72, Bytes.toInt(reader.readColumn("mscid")));
//								  ps.setLong  (73,Bytes.toLong(reader.readColumn("last_bscid")));
//								   ps.setInt(74, Bytes.toInt(reader.readColumn("last_mscid")));
//								   ps.setInt(75, (int)(reader.readColumn("dtmf")[0]));
//								   ps.setInt(76, Bytes.toInt(reader.readColumn("cid")));
//								   ps.setInt(77, Bytes.toInt(reader.readColumn("last_cid")));
//								   ps.setInt(78, Bytes.toInt(reader.readColumn("tac")));
//								   ps.setInt(79,  (int)(reader.readColumn("cdr_rel_type")[0]));
//								   ps.setInt(80, Bytes.toShort(reader.readColumn("last_pcm")));
//								   ps.setInt(81, Bytes.toInt(reader.readColumn("identity_request_time")));
//								   ps.setInt(82, Bytes.toInt(reader.readColumn("identity_response_time")));
//								   ps.setInt(83, Bytes.toInt(reader.readColumn("ciph_mode_cmd_time")));
//								   ps.setInt(84, Bytes.toInt(reader.readColumn("ciph_mode_cmp_time")));
//								   ps.setInt(85, Bytes.toInt(reader.readColumn("tmsi_realloc_cmd_time")));
//								   ps.setInt(86, Bytes.toInt(reader.readColumn("tmsi_realloc_cmp_time")));
//								   ps.setInt(87, Bytes.toInt(reader.readColumn("cc_release_time")));
//								   ps.setInt(88, Bytes.toInt(reader.readColumn("cc_release_cmp_time")));
//								   ps.setInt(89, Bytes.toInt(reader.readColumn("clear_cmp_time")));
//								   ps.setInt(90, Bytes.toInt(reader.readColumn("sccp_release_time")));
//								   ps.setInt(91, Bytes.toInt(reader.readColumn("sccp_release_cmp_time")));
//								   dao.addDataIntoStoreTable();
//						}
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						if(!isMySQL)
//						{
//						
////						System.out.println("111start_time_s  is " +Bytes.toLong(reader.readColumn("calling_number")) );
//						//System.out.println("start_time_s  is " +Bytes.toInt(reader.readColumn("start_time_s")) );
////						System.out.println("cdr_type  is " + (reader.readColumn("cdr_type")[0]) );
////						System.out.println("cdr_result  is " + (reader.readColumn("cdr_result")[0]) );
//						//System.out.println("city_code  is " +Bytes.toShort(reader.readColumn("city_code")) );						
//						ArrayList<byte[]> tempDataList = new ArrayList<byte[]>();
//						tempDataList.add( reader.readColumn("calling_number"));
//						tempDataList.add(reader.readColumn("start_time_s") );
//						tempDataList.add( reader.readColumn("called_number"));
//						tempDataList.add( reader.readColumn("cdr_type"));
//						tempDataList.add( reader.readColumn("cdr_result"));
//						tempDataList.add( reader.readColumn("imsi"));
//						tempDataList.add( reader.readColumn("city_code"));
////						if( Bytes.toLong( reader.readColumn("calling_number")) ==2585608064l 
////								&&  Bytes.toInt( reader.readColumn("start_time_s")) >= 1300532460  
////								&&  Bytes.toInt( reader.readColumn("start_time_s")) <= 1300539600 
////							)
////						{
////							reslutCount++;
////						}
//						b.insert(new MulitFiledData(tempDataList,proxy), 1, offset);
//						offset = offset +364;
//						tempDataList = null;
//						}
//						countNumber++;
//					}
//					reader.closeReadFile();
//			}
//			if(!isMySQL)
//			{
//			HashMap<String, Integer> map = new HashMap<String, Integer>();
//			map.put("/cdr/bssap/datafile/1300536000-1300536051-bssap-1-node27.cdr", 1);
//			b.writeFiles(map);
//			String reslutPathString ="C:\\dingli\\test\\index\\newcity"+System.currentTimeMillis()+BTreeType.orgName;
//			b.writeToFile(reslutPathString);		
//			b.print();
//			}
//		     long end_time = System.currentTimeMillis();
//			dao.close();
//			System.out.println("dataSize is " + reader.getDataSize());
//			System.out.println("count is " +countNumber);
//			System.out.println("reslutCount  count is " +reslutCount);
//		
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
