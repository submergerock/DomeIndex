package org.cProc.tool;

import java.io.File;

public class TextFormateUtils {


	public static String FormatePhoneNum(String phoneNum) {
		if (phoneNum.length() == 16)
			return phoneNum;
		else if (phoneNum.length() > 16)
			return phoneNum.substring(phoneNum.length() - 16);
		else
			return String.format("%016d", Long.valueOf(phoneNum));
	}
	

}
