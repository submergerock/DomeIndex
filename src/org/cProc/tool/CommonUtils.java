package org.cProc.tool;

import org.apache.commons.lang.StringUtils;
import org.cProc.task.server.Const;

public class CommonUtils {

	private static final String NOTIFY_SPLIT_CHAR = "#";
	private static final String PATH_SPLIT_CHAR = "/";

	public static synchronized String getUUID() {
		return java.util.UUID.randomUUID().toString();
	}

	/**
	 * 类似 #pre#appname#tablename 
	 * appname#tablename
	 *  #appname#tablename
	 * 
	 * @param path
	 * @return
	 */
	public static synchronized String getNotifyPathTableName(String path) {

		int i = 0;

		
		
		int len = Const.PRE.length();
		
		String temp = path.substring(len);
		
		if (temp.startsWith(NOTIFY_SPLIT_CHAR)) {
			//有前缀
			temp = temp.substring(1);
		} 
		
		//#appname#tablename#
		
			
		
		int start  = StringUtils.indexOf(temp, NOTIFY_SPLIT_CHAR);
		
		int end  = StringUtils.indexOf(temp, NOTIFY_SPLIT_CHAR,start+1);
		
		System.out.println(start);
		System.out.println(end);
		
		return StringUtils.substring(temp, start+1, end);
		

	}
	
	public static void main(String[] args) {
		     Const.PRE="/pre";
			getNotifyPathTableName("pre#appname#tablename#datafile#");
	}

}
