package org.cProc.index.BPlusTree.Data;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.cProc.GernralRead.ReadData.DataType;
import org.cProc.GernralRead.ReadData.ParseIndexFormat;
import org.cProc.sql.NullPosConst;
import org.cProc.tool.Bytes;

public class ReadWriteIndexDataProxy {
	private ParseIndexFormat format = null;
	
	public ParseIndexFormat getIndexFormat()
	{
		return format;
	}
	
	public  ReadWriteIndexDataProxy(ParseIndexFormat  format)
	{
		this.format = format; 
	}
	
	public  void write(ArrayList<byte[]> objects,ByteBuffer buffer)
	{
		if(  objects != null)
		{
			
			buffer.put(Bytes.toBytes(objects.size()));	
			List<Class> types =format.getIndexColumnType();
			for(int i = 0 ; i< objects.size() ; i++)
			{	
				 DataType.writeBuffer(types.get(i),objects.get(i),buffer);
				
			}
			
			
		}
		else 
		{
			buffer.put(Bytes.toBytes((int)0));
		}
	}
		
	public  void read(ArrayList<byte[]> objects,ByteBuffer buffer)
	{
		if(  objects != null)
		{
			objects.clear();
			int listSize   =   buffer.getInt();
			List<Class> types =format.getIndexColumnType();
			
				for(int i = 0 ; i< listSize ; i++)
				{	
					byte[] filed =  DataType.readBuffer(types.get(i),buffer);
			//		System.out.println("ReadWriteIndexDataProxy:read  types  is " + types.get(i) +" list size is " + filed.length);
					objects.add(filed);
				}
			
		}
	}
	


	
	public int compare(ArrayList<byte[]> objectsA , ArrayList<byte[]> objectsB)
	{
		List<Class> types =format.getIndexColumnType();
		int sizeA = objectsA.size();
		int sizeB = objectsB.size();
		int  min = sizeA < sizeB? sizeA:sizeB; //鍙栦袱鑰呬箣闂存渶灏忓�
		//min -1 : because of the nullInof column
		for(int i = 0 ; i< min-1 ; i++)
		{	
			
			int reslut = DataType.compare(types.get(i) , objectsA.get(i) , objectsB.get(i));
			if(reslut != 0)
			{
				return reslut;
			}
		}
		if(sizeA == sizeB)
		{
			return 0;
		}
		if(sizeA >  sizeB)
		{
			return 1;
		}
		return -1;
	}
	
}
