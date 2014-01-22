package org.cProc.tool;

import java.text.ParseException;

public class DateUtils {

	private static final java.text.DateFormat FORMATE_1 = new java.text.SimpleDateFormat(
			"yyyy/MM/dd hh:mm");
	private static final java.text.DateFormat FORMATE_2 = new java.text.SimpleDateFormat(
	"yyyyMMdd hh:mm:ss:sss");

	private DateUtils() {
	}

	// 2011/4/11 8:59
	public static long getDate2Long(String time) throws ParseException {
		return FORMATE_1.parse(time).getTime();
	}

	public static long getDate2Long(String yyyyMMdd,String hhmmsssss) throws ParseException {
		return FORMATE_2.parse(yyyyMMdd+" "+hhmmsssss).getTime();
	}
	public static void main(String[] args) throws ParseException {
		System.out.println(DateUtils.getDate2Long("20111211","10:06:00:000"));
		System.out.println(DateUtils.getDate2Long("2011/12/11 10:06"));
	}

}
