package org.cProc.task.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ByteConvert {

	public static List<String> byte2List(final byte[] bytes) {
		List<String> obj = null;
		try {
			// bytearray to object
			ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
			ObjectInputStream oi = new ObjectInputStream(bi);

			obj = (List<String>) oi.readObject();

			bi.close();
			oi.close();
		} catch (Exception e) {
			System.out.println("translation" + e.getMessage());
			e.printStackTrace();
		}
		return obj;
	}

	public static byte[] list2byte(final List<String> list) {

		byte[] bytes = null;
		try {
			// object to bytearray
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(list);

			bytes = bo.toByteArray();

			bo.close();
			oo.close();
		} catch (Exception e) {
			System.out.println("translation" + e.getMessage());
			e.printStackTrace();
		}
		return bytes;

	}

	public static void main(String[] args) {

		List<String> arr = new ArrayList<String>();

		arr.add("dsdsds4444444444444dsd");
		arr.add("dsd44444444sdsdsd");
		arr.add("dsdsd55555sdsd");
		
		byte [] bytes = list2byte(arr);
		System.out.println(bytes.length);
		
		List<String> ss = byte2List(bytes);
		
		for(String ff:ss)
		{
			System.out.println(ff);
		}

	}
}
