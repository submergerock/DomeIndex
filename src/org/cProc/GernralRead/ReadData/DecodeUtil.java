package org.cProc.GernralRead.ReadData;


/**
 * 通用解码工具�?
 * 
 * @author jiafeng.zhang
 * 
 */
public class DecodeUtil {
	
	public static byte getByte(byte[] bytes) {		
		return getByte(bytes,0);
	}
	
	public static byte getByte(byte[] bytes,int p) {		
		return bytes[p];
	}
	
	public static short getShort(byte[] bytes) {		
		return getShort(bytes,0);
	}
	
	public static short getShort(byte[] bytes , int p) {		
		return makeShort(bytes[p + 1], bytes[p + 0]);
	}
	
	public static int getUShort(byte[] bytes) {		
		return getUShort(bytes,0);
	}
	
	public static int getUShort(byte[] bytes , int p) {
		byte zero= 0;
		return makeInt(zero,zero,bytes[p + 1], bytes[p + 0]);
	}
	
	public static int getInt(byte[] bytes) {
		return getInt(bytes,0);
	}
	
	public static int getInt(byte[] bytes, int p) {
		return makeInt(bytes[p + 3], bytes[p + 2], bytes[p + 1], bytes[p + 0]);
	}
	
	public static long getUInt(byte[] bytes) {		
		return getUInt(bytes,0);
	}

	public static long getUInt(byte[] bytes, int p) {
		byte zero= 0;
		return makeLong(zero,zero,zero,zero,bytes[p + 3], bytes[p + 2], bytes[p + 1], bytes[p + 0]);
	}
	
	public static long getLong(byte[] bytes) {
		return  getLong(bytes,0);
	}

	public static long getLong(byte[] bytes, int p) {
		return makeLong(bytes[p + 7],bytes[p + 6],bytes[p + 5],bytes[p + 4],bytes[p + 3], bytes[p + 2], bytes[p + 1], bytes[p + 0]);
	}
	
	public static String getString(byte[] bytes) {
		return new String(bytes);
	}

	public static String getString(byte[] bytes,int p ,int len) {
		return new String(bytes,p,len);
	}

	private static short makeShort(byte b1, byte b0) {		
		return (short)((b1 << 8) | (b0 & 0xff));
	}
	
	private static int makeInt(byte b3, byte b2, byte b1, byte b0) {
		return (int) ((((b3 & 0xff) << 24) | ((b2 & 0xff) << 16) | ((b1 & 0xff) << 8) | ((b0 & 0xff) << 0)));
	}
		
	private static long makeLong(byte b7, byte b6, byte b5, byte b4, byte b3, byte b2, byte b1, byte b0) {
		return ((((long) b7 & 0xff) << 56) | (((long) b6 & 0xff) << 48) | (((long) b5 & 0xff) << 40) | (((long) b4 & 0xff) << 32) 
				| (((long) b3 & 0xff) << 24) | (((long) b2 & 0xff) << 16) | (((long) b1 & 0xff) << 8) | (((long) b0 & 0xff) << 0));
	}
}
