package org.cProc.GernralRead.TableAndIndexTool;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;


/*	
 	表结构和索引结构�?
          应用目录/tableInfo/tableName/table.frm
 	应用目录/tableInfo/tableName/indexfile/indexName/index.frm
	数据�?
 *    /应用目录
 *    -------------/表名
 *    -------------------/datafile    目录
 *    -------------------/indexfile  目录
 *    -----------------------------/indexName  目录		  
 * --------------------------------------------/data  目录
 */

public class TableAndIndexTool {
    
	
	private static   String prePath = "";
	private  static   int tableNamePosInFileName = 2;
	private static    int  appNamePosInFileName = 1;
	private   static final String FILE_SEPERATOR ="/"; 
	private static final char FILE_SEPERATOR_CHAR ='/';
	public static final String pemission = "-rwxrwxrwx";

	//get tableName    
	//input: @fileName : /appName/tableName/dtafile/xxxx
	
	//设置前缀
	public static void setPrePath(String localPrePath)
	{
		prePath = localPrePath;
	}
	//获取文件中的表名
	public   static   String  getTableName(String fileName)
	{
		fileName = fileName.substring(prePath.length());
		int pos =tableNamePosInFileName;
		String reslut  =fileName;
		for(int i = 1 ; i < pos ; i++)
		{
			reslut = reslut.substring(reslut.indexOf(FILE_SEPERATOR)+1);
		}
		int first = reslut.indexOf(FILE_SEPERATOR);
		int second = reslut.substring(first+1).indexOf(FILE_SEPERATOR) ;
		if(second == -1)
		{
			return reslut.substring(first+1); 
		}
		return reslut.substring(first+1,first+1+second);
	}
	

	//input: @fileName : /appName/tableName/dtafile/xxxx
	//get appName
	public   static   String  getAppName(String fileName)
	{
		fileName = fileName.substring(prePath.length());
		int pos =appNamePosInFileName;
		String reslut  =fileName;
		for(int i = 1 ; i < pos ; i++)
		{
			reslut = reslut.substring(reslut.indexOf(FILE_SEPERATOR)+1);
		}
		int first = reslut.indexOf(FILE_SEPERATOR);
		int second = reslut.substring(first+1).indexOf(FILE_SEPERATOR) ;
		if(second == -1)
		{
			return reslut.substring(first+1); 
		}
		return reslut.substring(first+1,first+1+second);
	}
	
	
	//获取表的数据文件区域
	public static String getTableDataPath(String tableName, String appName)
	{
		return  prePath+FILE_SEPERATOR+appName+FILE_SEPERATOR+tableName+FILE_SEPERATOR+"datafile";
	}
	
	//获取表的索引定义文件路径
	public static String getIndexPath(String tableName, String appName)
	{
				return  prePath+FILE_SEPERATOR+appName+FILE_SEPERATOR+"tableInfo"+
				FILE_SEPERATOR+tableName+FILE_SEPERATOR+"indexfile";
	}
	

	//获取表的结构
	public static String getTableFormat(String tableName, String appName)
	{
		return  prePath+FILE_SEPERATOR+appName+FILE_SEPERATOR+"tableInfo"+
		FILE_SEPERATOR+tableName+FILE_SEPERATOR+"table.frm";
	}
	
	//获取该表的所有索引
	public static ArrayList<String>getAllIndexsInTable(String tableName, String appPath,FileSystem fs)
	{
		

		ArrayList<String> list = new ArrayList<String>();
		//ex. /smp/cdr/tableInfo/bssap/indexfile
		String indexPath = getIndexPath(tableName, appPath);

		 try {
			 
			FileStatus[]  fsts = fs.listStatus(new Path(indexPath));
			for(int i = 0 ; i < fsts.length ; i ++)
			{
				list.add(fsts[i].getPath().getName());
			}
		} catch (IOException e) {
			e.printStackTrace();
			return list;
		}
		return list;
	}

	//获取索引的数据目�?
	public static String getIndexDataPath(String tableName,String indexName, String appName)
	{
		System.out.println("indexPath is: " + prePath+FILE_SEPERATOR+appName+FILE_SEPERATOR+tableName+FILE_SEPERATOR+"indexfile"+FILE_SEPERATOR+indexName+FILE_SEPERATOR+"data");
		return  prePath+FILE_SEPERATOR+appName+FILE_SEPERATOR+tableName+FILE_SEPERATOR+"indexfile"+FILE_SEPERATOR+indexName+FILE_SEPERATOR+"data";
	}
	
	//获取索引的索引定义文件名
	public static String getIndexFormat(String tableName,String indexName, String appName)
	{
		return  prePath+FILE_SEPERATOR+appName+FILE_SEPERATOR+"tableInfo"+FILE_SEPERATOR+tableName+
		FILE_SEPERATOR+"indexfile"+FILE_SEPERATOR+indexName+FILE_SEPERATOR+"index.frm";
	}
	
//	public static int createTable(FileSystem fs ,String appName ,String tableName, String filePath)
//	{
//		try
//		{
//				if(fs.exists(new Path(prePath+FILE_SEPERATOR + appName)))
//				{
//					if( !fs.exists(new Path(prePath+FILE_SEPERATOR + appName+FILE_SEPERATOR+tableName)))
//					{
//						//   /应用�?表名
//						FileSystem.mkdirs(fs,new Path(prePath+FILE_SEPERATOR + appName+FILE_SEPERATOR+tableName),FsPermission.valueOf(pemission));
//						//1.数据文件放在�?应用目录/表名/datafile/数据文件�?
//						FileSystem.mkdirs(fs,new Path(prePath+FILE_SEPERATOR + appName+FILE_SEPERATOR+tableName+FILE_SEPERATOR+"datafile"),FsPermission.valueOf(pemission));
//						//3.索引文件放在�?应用目录/表名/indexfile
//						FileSystem.mkdirs(fs,new Path(prePath+FILE_SEPERATOR + appName+FILE_SEPERATOR+tableName+FILE_SEPERATOR+"indexfile"),FsPermission.valueOf(pemission));
//						//2.表格式文件放在：/应用目录/表名/table.frm;
//						fs.copyFromLocalFile(new Path(filePath), new Path(prePath+FILE_SEPERATOR + appName+FILE_SEPERATOR+tableName+FILE_SEPERATOR+"table.frm"));
//						fs.setPermission(new Path(prePath+FILE_SEPERATOR + appName+FILE_SEPERATOR+tableName+FILE_SEPERATOR+"table.frm"), FsPermission.valueOf(pemission));
//					}
//					else
//					{
//						return -1;
//					}
//					return 0;
//				}
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//			return -1;
//		}
//		return -1;
//	}
//	
//	   public static  int  createTable(String hdfs,String appName ,String tableName, String filePath)
//	   {
//		   Configuration conf = new Configuration();
//		   conf.set("fs.default.name", hdfs);
//			FileSystem fs;
//			try {
//				fs = FileSystem.get(conf);
//				int reslut = createTable( fs , appName , tableName,  filePath);
//				fs = null;
//				return reslut;
//			} catch (IOException e) {
//				e.printStackTrace();
//				return -1;
//			}
//	   }
//	
//	   public static int createIndex(FileSystem fs,String appName,String tableName, String indexname,String filePath)
//	   {
//			try
//			{
//					if(fs.exists(new Path(prePath+FILE_SEPERATOR + appName+FILE_SEPERATOR+tableName+FILE_SEPERATOR+"indexfile")))
//					{
//						if( !fs.exists(new Path(prePath+FILE_SEPERATOR + appName+FILE_SEPERATOR+tableName+FILE_SEPERATOR+"indexfile"+FILE_SEPERATOR+indexname)))
//						{
//							//1.索引放在�?应用目录/表名/indexfile/indexName
//							FileSystem.mkdirs(fs,
//									new Path(prePath+FILE_SEPERATOR + appName+FILE_SEPERATOR+tableName+FILE_SEPERATOR+"indexfile"
//													+FILE_SEPERATOR+indexname),	FsPermission.valueOf(pemission));
//							//2.索引文件放在�?应用目录/表名/indexfile/indexName/data
//							FileSystem.mkdirs(fs,
//															new Path(prePath+FILE_SEPERATOR + appName+FILE_SEPERATOR+tableName+FILE_SEPERATOR+"indexfile"
//																			+FILE_SEPERATOR+indexname+FILE_SEPERATOR+"data"),
//															FsPermission.valueOf(pemission));
//							//3.表格式文件放在：/应用目录/表名/indexfile/indexName/index.frm;
//							fs.copyFromLocalFile(new Path(filePath), 
//										new Path(prePath+FILE_SEPERATOR + appName+FILE_SEPERATOR+tableName+FILE_SEPERATOR+"indexfile"
//												+FILE_SEPERATOR+indexname+FILE_SEPERATOR+"index.frm"));
//							fs.setPermission(new Path(prePath+FILE_SEPERATOR + appName+FILE_SEPERATOR+tableName+FILE_SEPERATOR+"indexfile"
//									+FILE_SEPERATOR+indexname+FILE_SEPERATOR+"index.frm"), FsPermission.valueOf(pemission));
//					}
//						else
//						{
//							return -1;
//						}
//						return 0;
//					}
//			}
//			catch (Exception e) {
//				e.printStackTrace();
//				return -1;
//			}
//			return -1;
//	   }
//	   
//	   public static int createIndex(String hdfs,String appName,String tablename, String indexname,String filePath)
//	   {
//		   Configuration conf = new Configuration();
//		   conf.set("fs.default.name", hdfs);
//			FileSystem fs;
//			try {
//				fs = FileSystem.get(conf);
//				int reslut = createIndex( fs , appName , tablename, indexname , filePath);
//				fs = null;
//				return reslut;
//			} catch (IOException e) {
//				e.printStackTrace();
//				return -1;
//			}
//	   }
//	   
//	   
//	   public static int  loadDataIntoHDFS(FileSystem fs , String appName, String tableName, String filePath , String notifyPath)
//	   {
//			try
//			{
//					if(fs.exists(new Path(prePath+FILE_SEPERATOR + appName+FILE_SEPERATOR+tableName+FILE_SEPERATOR+"datafile")))
//					{
//							Path  path = new Path(filePath);
//							fs.copyFromLocalFile(path, 
//										new Path(prePath+FILE_SEPERATOR + appName+FILE_SEPERATOR+tableName+FILE_SEPERATOR+"datafile"
//												+FILE_SEPERATOR+path.getName()));
//							fs.setPermission(new Path(prePath+FILE_SEPERATOR + appName+FILE_SEPERATOR+tableName+FILE_SEPERATOR+"datafile"
//									+FILE_SEPERATOR+path.getName()), FsPermission.valueOf(pemission));
//							FileSystem.mkdirs(fs,
//									new Path(notifyPath+ FILE_SEPERATOR+( prePath+FILE_SEPERATOR+appName+FILE_SEPERATOR+tableName+FILE_SEPERATOR+"datafile"
//											+FILE_SEPERATOR+path.getName()).replace(FILE_SEPERATOR, "#")
//													),
//									FsPermission.valueOf(pemission));		
//							System.out.println(notifyPath+ FILE_SEPERATOR+"#"+(prePath+FILE_SEPERATOR+appName+FILE_SEPERATOR+tableName+FILE_SEPERATOR+"datafile"
//									+FILE_SEPERATOR+path.getName()).replace(FILE_SEPERATOR, "#")
//							);
//					}
//						else
//						{
//							return -1;
//						}
//						return 0;
//			}
//			catch (Exception e) {
//				e.printStackTrace();
//				return -1;
//			}
//	   }
//	   
//	   public static int  loadDataIntoHDFS(String hdfs , String appName, String tableName, String filePath , String notifyPath)
//	   {
//		   Configuration conf = new Configuration();
//		   conf.set("fs.default.name", hdfs);
//			FileSystem fs;
//			try {
//				fs = FileSystem.get(conf);
//				int reslut = loadDataIntoHDFS( fs , appName , tableName, filePath , notifyPath);
//				fs = null;
//				return reslut;
//			} catch (IOException e) {
//				e.printStackTrace();
//				return -1;
//			}
//	   }
     public static void main(String[] args)
	{
		//String testFile ="D:\\tableAndIndex\\TestTable\\datafile\\1.txt";
		String testFile ="/tableAndIndex/TestTable/datafile/1.txt";
		String tableString = getTableFormat("bssap", "cdr");
		System.out.println(getAppName("/cdr/bssap/datafile/1.data"));
		System.out.println(getTableName("/cdr/bssap/datafile/1.data"));
		System.out.println(getIndexDataPath("bssap", "callingNumber", "cdr"));
		System.out.println(getIndexFormat("bssap", "callingNumber", "cdr"));
		System.out.println(getTableFormat("bssap", "cdr"));
		System.out.println(getTableDataPath("bssap", "cdr"));
	}
}
