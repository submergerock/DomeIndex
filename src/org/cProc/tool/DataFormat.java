package org.cProc.tool;

public class DataFormat {


	public static String FormatePhoneNum(String phoneNum) {
		if (phoneNum.length() == 16)
			return phoneNum;
		else if (phoneNum.length() > 16)
			return phoneNum.substring(phoneNum.length() - 16);
		else
			return String.format("%016d", Long.valueOf(phoneNum));
	}
	
	public static boolean isLegalNum(String str) {
		boolean flag = true;
		try {
			Long.valueOf(str);
		} catch (Exception e) {
			flag = false;
			System.out.println(str);
		}
		return flag;
	}
}
