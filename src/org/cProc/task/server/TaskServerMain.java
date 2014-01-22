package org.cProc.task.server;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.apache.zookeeper.KeeperException;
import org.cProc.distributed.createIndex.Config;
import org.cProc.distributed.createIndex.ReadFromHDFSClient;
import org.cProc.tool.myXMLReader.MyXmlReader;

public class TaskServerMain {

	
	static
	{
		PropertyConfigurator.configure(TaskServerMain.class.getClassLoader().getResource("mylog4j.properties"));
		
	}
	
	
	private MyXmlReader xml;

	public TaskServerMain(String confPath) throws IOException, InterruptedException, KeeperException {
		xml = new MyXmlReader(confPath);
		init();
		new ZKThread();
		
		
		
	}

	private void init() {
		// 初始化参数,放入Const中

		/**
		 * 后缀
		 */
		Const.SUFFIX = xml.getName("suffix");

		/**
		 * 通知目录
		 */
		Const.NOTIFY_PATH = xml.getName("sourceRoot");

		/**
		 * 轮询时间间隔
		 */

		Const.WATCH_TIME = Long.parseLong(xml.getName("watchTime"));

		/**
		 * hdfs路径
		 */

		Const.HDFS_PATH = xml.getName("hdfsURL");

		/**
		 * 前缀
		 */
		Const.PRE = xml.getName("databasePrePath");

		/**
		 * 几个合在一起
		 * 
		 * 
		 */
		Const.FILE_NUMBER = Integer.parseInt(xml
				.getName("oneMachDoWithFileNumber"));
		
		Const.ZK_CONNSTR  =xml.getName("connectString");
		
		Const.APP_NAME  =xml.getName("app");
		
		Const.SESSION_TIMEOUT = Integer.parseInt(xml
				.getName("sessionTimeout"));
		
		Const.IP  =xml.getName("ip");
		System.out.println("ip port is:"+Const.IP);
		Config.IP = Const.IP;
		
		String temp  =xml.getName("port");
		if(temp!=null)
		{
			Const.JETTY_PORT = Integer.parseInt(temp);
		}
		
		
		String excludeTable = xml.getName("excludeTable");
		if(excludeTable!=null)
		{
			excludeTable = excludeTable.trim();
			String [] arr = StringUtils.split(excludeTable, ",");
			for(String str:arr)
			{
				Const.ExcludeTable.add(str);
			}
			
		}

	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
		// TODO Auto-generated method stub
		
        String confile = args[0];
		//String confile="E:\\datacube\\createIndex.xml";
		TaskServerMain main = new TaskServerMain(confile);
		Thread.sleep(Long.MAX_VALUE);
		
		
	}

}
