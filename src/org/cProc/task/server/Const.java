package org.cProc.task.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Const {

	/**
	 * 控制server是否运行
	 */
	public static final AtomicBoolean SERVER_RUNNING = new AtomicBoolean(false);

	
	public static  int JETTY_PORT = 55555;

	/**
	 * 后缀
	 */
	public static String SUFFIX = null;

	/**
	 * 通知目录
	 */
	public static String NOTIFY_PATH = null;

	/**
	 * 轮询时间间隔
	 */

	public static long WATCH_TIME = 0;

	/**
	 * hdfs路径
	 */

	public static String HDFS_PATH = "";

	/**
	 * 前缀
	 */
	public static String PRE = "";

	/**
	 * 几个合在一起
	 * 
	 * 
	 */
	public static int FILE_NUMBER = 3;
	
	
	/**
	 * zk路径
	 */
	
	public static String ZK_CONNSTR="";
	
	/**
	 * zk 根目录
	 */
	
	public static String APP_NAME = "";
	
	public static int  SESSION_TIMEOUT = 10000;
	
	/**
	 * 网口
	 */
	public static String IP = "eth0";
	
	
	public static final List<String> ExcludeTable = new ArrayList<String>();

}
