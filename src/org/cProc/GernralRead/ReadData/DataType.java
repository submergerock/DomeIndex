package org.cProc.GernralRead.ReadData;

import java.nio.ByteBuffer;

import org.cProc.tool.Bytes;




public class DataType {

	public static final String IntType ="INT";
	public static final String LongType ="LONG";
	public static final String StringType ="STRING";
	public static final String  ByteType ="BYTE";
	public static final String   ShortType="SHORT";
	
	
	public static byte[] getBytes(Class type ,String content)
	{	
		if(type == Integer.class)
		{	
			long  reslut = Long.parseLong(content);
			return Bytes.toBytes( (int)reslut );
		}
		else if(type == Long.class)
		{
			long  reslut = Long.parseLong(content);
			return Bytes.toBytes( reslut );
		}
		else if(type == String.class)
		{
			return content.getBytes();
		}else if(type == Byte.class)
		{
			long  reslut = Long.parseLong(content);
			return Bytes.toBytes( (byte)reslut );
		}else if(type == Short.class)
		{
			long  reslut = Long.parseLong(content);
			return Bytes.toBytes( (short)reslut );
		}
		content = null;
		return null;
	}
		
	public static  byte[]  getBigOrderByte(byte[] data)
	{
		//如果是大�?
		if(false)
		{
			return data;
		}
		//如果不是大端
		int length = data.length;
		byte[] reslut = new byte[length];
		for( int i = 0 ; i < length ; i++)
		{
			reslut[i] = data[data.length-1-i];
		}
		data = null;
		return reslut;
	}
	

	public static  byte[] readBuffer(Class type, ByteBuffer buffer)
	{
		if(type == Long.class)
		{
			byte[] data = new byte[8];
			 buffer.get(data);
			 return data;
		}else if(type == Integer.class){
			byte[] data = new byte[4];
			 buffer.get(data);
			 return data;
		}
		else if(type == Short.class){
			byte[] data = new byte[2];
			 buffer.get(data);
			 return data;
				}
		else if(type == Byte.class){
			byte[] data = new byte[1] ;
			 buffer.get(data, 0, 1);
			 return data;
		}
		else if(type == String.class){
			int size = buffer.getInt();
			byte [] temp = new byte[size];
			buffer.get(temp);
			return temp;
		}
		return null;
	}
	
	public static void writeBuffer(Class type,byte[] object, ByteBuffer buffer)
	{
			if(type != String.class)
			{
			buffer.put(object);	
			}
			else {
				//写文本没有处理，�?��，索引字段里，无字符�?
			}
	}
	
//	public static int compare(Class type , Object A , Object B)
//	{
//		if(type == Long.class)
//		{
//			return  ((Long)A).compareTo((Long)B);
//		}else if(type == Integer.class){
//			return ((Integer)A).compareTo((Integer)B);
//		}
//		else if(type == Short.class){
//			return ((Short)A).compareTo((Short)B);
//				}
//		else if(type == Byte.class){
//			return ((Byte)A).compareTo((Byte)B);
//		}
//		else if(type == String.class){
//			String  tempA = (String)A;
//			String  tempB = (String)B;
//			return tempA.compareTo(tempB);
//		}
//		return 0;
//	}
	
	
//    public  int unsignByteCompare(byte a , byte b)
//    {
//		int longA = a & 0x0FF;
//		int longB = b & 0x0FF;
//		return (longA<longB ? -1 : (longA==longB ? 0 : 1));
//    }
//    
//    public  int unsignShortCompare(short a , short b)
//    {
//		int longA = a & 0x0FFFF;
//		int longB = b & 0x0FFFF;
//		return (longA<longB ? -1 : (longA==longB ? 0 : 1));
//    }
//    
//    public  int unsignIntCompare(int a , int b)
//    {
//		long longA = a & 0x0FFFFFFFF;
//		long longB = b & 0x0FFFFFFFF;
//		return (longA<longB ? -1 : (longA==longB ? 0 : 1));
//    }
	
	
    //除了long之外，均为无符号的比较；
	public static int compare(Class type , byte[] A ,  byte[] B)
	{
		if( A == null || B ==null)
		{
			System.out.println("null");
			System.out.println("type is " + type);
		}
//		System.out.println("DataType:type is " + type +" a size is " + A.length+"  b size is " + B.length);
//		System.out.println(" a size is " + A.length );
//		System.out.println("  b size is " + B.length);
		if(type == Long.class)
		{
			long  tempA =Bytes.toLong(A);
			long  tempB = Bytes.toLong(B);			
			return (tempA<tempB ? -1 : (tempA==tempB ? 0 : 1));
		}else if(type == Integer.class){
			int  tempA = Bytes.toInt(A);
			int  tempB =Bytes.toInt(B);
			if(tempA < 0 || tempB < 0)
			{
				long longA = tempA & 0x0FFFFFFFFl;
				long longB = tempB & 0x0FFFFFFFFl;
				return (longA<longB ? -1 : (longA==longB ? 0 : 1));
			}
			return (tempA<tempB ? -1 : (tempA==tempB ? 0 : 1));
		}
		else if(type == Short.class){
			short  tempA =Bytes.toShort(A);
			short  tempB = Bytes.toShort(B);			
			if(tempA < 0 || tempB < 0)
			{
				int intA = tempA & 0x0FFFF;
				int intB = tempB & 0x0FFFF;
				return (intA<intB ? -1 : (intA==intB ? 0 : 1));
			}
			return (tempA<tempB ? -1 : (tempA==tempB ? 0 : 1));
		}
		else if(type == Byte.class){
			byte  tempA = A[0];
			byte  tempB = B[0];
			if(tempA < 0 || tempB < 0)
			{
				int intA = tempA & 0x0FF;
				int intB = tempB & 0x0FF;
				return (intA<intB ? -1 : (intA==intB ? 0 : 1));
			}
			return (tempA<tempB ? -1 : (tempA==tempB ? 0 : 1));
		}
//		else if(type == String.class){
//			String  tempA = new String(A);
//			String  tempB = new String(B);
//			return tempA.compareTo(tempB);
//		}
		return 0;
	}
	
	
	public static byte[] getMinObject(Class type )
	{
		if(type == Long.class)
		{
			return  Bytes.toBytes(Long.MIN_VALUE);
		}else if(type == Integer.class){
			return  Bytes.toBytes(Integer.MIN_VALUE);
		}
		else if(type == Short.class){
			return  Bytes.toBytes(Short.MIN_VALUE);
				}
		else if(type == Byte.class){
			return  Bytes.toBytes(Byte.MIN_VALUE);
		}
//		else if(type == String.class){
//			return  new  String(" ").getBytes();
//		}
		return new byte[1];
	}
	
	public static  byte[] getMaxObject(Class type )
	{
		if(type == Long.class)
		{
			return  Bytes.toBytes(Long.MAX_VALUE);
		}else if(type == Integer.class){
			return  Bytes.toBytes(Integer.MAX_VALUE);
		}
		else if(type == Short.class){
			return  Bytes.toBytes(Short.MAX_VALUE);
				}
		else if(type == Byte.class){
			return  Bytes.toBytes(Byte.MAX_VALUE);
		}
//		else if(type == String.class){
//			return  new  String(" ").getBytes();
//		}
		return new byte[1];
	}
	
	
	
	public static  byte[] getUnsginedMaxBytes(Class type )
	{
		long reslut = 0;
		long one = 1l;
		long two = 2l;
		if(type == Long.class)
		{
			reslut = Long.MAX_VALUE;
			return  Bytes.toBytes(reslut);
		}else if(type == Integer.class){
			reslut =Integer.MAX_VALUE*two+one;
			return  Bytes.toBytes(reslut);
		}
		else if(type == Short.class){
			reslut = Short.MAX_VALUE*2+one;
			return  Bytes.toBytes(reslut);
				}
		else if(type == Byte.class){
			reslut = Byte.MAX_VALUE*2+one;
			return  Bytes.toBytes(reslut);
		}
//		else if(type == String.class){
//			return  new  String(" ").getBytes();
//		}
		return new byte[1];
	}
	
	
	public static byte[] getUnsginedMinBytesReturnNot8Byte(Class type)
	{
		long a = 0;
		if(type == Long.class)
		{	
			return  Bytes.toBytes(a);
		}else if(type == Integer.class){
			
			return  Bytes.toBytes((int)a);
		}
		else if(type == Short.class){	
			return  Bytes.toBytes((short)a);
		}
		else if(type == Byte.class){
		
			return  Bytes.toBytes((byte)a);
		}
//		else if(type == String.class){
//			return  new  String(" ").getBytes();
//		}
		return new byte[4];
	}
	
	//return 
	public static byte[] getUnsginedMinBytesReturn8Byte(Class type )
	{
		long a = 0;
		if(type == Long.class)
		{	
			return  Bytes.toBytes(a);
		}else if(type == Integer.class){
			
			return  Bytes.toBytes(a);
		}
		else if(type == Short.class){
			
			return  Bytes.toBytes(a);
				}
		else if(type == Byte.class){
		
			return  Bytes.toBytes(a);
		}
//		else if(type == String.class){
//			return  new  String(" ").getBytes();
//		}
		return new byte[4];
	}
	

	
	
	
	
	
	
	public static  void main (String args[])
	{	
		byte[] A  = new byte[2];
		byte[] B  = new byte[2];
		A[0]= (byte)0xff;
		A[1]= (byte)0x0E;
//		A[2] = (byte)0xff;
//		A[3]=(byte)0xff;
		B[0]= 0;
		B[1]=0;
//		B[2]=0;
//		B[3]=0;
		System.out.println(Bytes.toShort(A));
		System.out.println(compare(Short.class,A,B));


		
	}
	
}
