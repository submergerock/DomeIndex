package org.cProc.distributed.createIndex;

/**
 * 系统全部的参数，有程序启动时候初始化里面的数据
 * @author zxc
 *
 */
public class Config {
	
	
	/**
	 * 处理的文件格式是否为文本
	 */
	
	public static boolean isText  = true;
	
	/**
	 * 跳过文本的前多少行数据
	 */
	
	
	public static int skipLine  = 0;
	
	
	
	/**
	 * 轮询符合的后缀名的文件
	 */
	
	public static String suffix = ".+\\.cdr";
	
	/**
	 * 文本格式的分隔符
	 */
	
	public static String split = ",";
	/**
	 * 失败信息保存路径
	 */
	
	public static String failedPath = "/tmp/";
	
	
	
	/**
	 * ip网卡
	 */
	
	public static String IP = "eth0";
	
	
	
}
