package org.cProc.index.BPlusTree.Data;

import java.nio.ByteBuffer;
import java.util.ArrayList;



public class MulitFiledData extends Data {
		
	  private ArrayList<byte[]> dataArrayList = null;
	  ReadWriteIndexDataProxy proxy = null;
	  private int nullInfor = 0 ;
	  	  
	  public  MulitFiledData( ArrayList<byte[]> data ,ReadWriteIndexDataProxy proxy )
	  {
		  dataArrayList = data;
		  this.proxy = proxy;
		 // this.proxy.check(data);
	  }
	  
	  public  MulitFiledData( ArrayList<byte[]> data ,ReadWriteIndexDataProxy proxy  ,boolean notCheck)
	  {
		  dataArrayList = data;
		  this.proxy = proxy;
	  }
	  
	  	
	  public void setNullInfor(int nullInfor)
	  {
		  this.nullInfor = nullInfor;
	  }
	  public   Data getInstance() 
	  {
		  return new MulitFiledData(new ArrayList<byte[]>(), this.proxy);
	  }
	  
	  public ArrayList<byte[]> getArrayListData()
	  {
		  return dataArrayList;
		  
	  }
	  
	  public ReadWriteIndexDataProxy getIndexProxy()
	  {
		  return  this.proxy;
		  
	  }
	  
		public  void read(ByteBuffer buffer)
		{
			proxy.read(this.dataArrayList, buffer);

		}
		public  void write(ByteBuffer buffer)
		{
			proxy.write(this.dataArrayList, buffer);

		}
	  public String toString()
	  {
		  
		  StringBuffer sb = new StringBuffer();
		  //Integer.toHexString(dataArrayList.get(0)[0])+"@"+
//		  sb.append(dataArrayList.get(0)[0]);
//		  sb.append(":");
//		  sb.append(Bytes.toInt(dataArrayList.get(1)));
////		  sb.append("  ");
////		  sb.append(Bytes.toLong(dataArrayList.get(1)));
////		  sb.append("  ");
////		  sb.append(Bytes.toLong(dataArrayList.get(2)));
////		  sb.append("  ");
////		  sb.append((dataArrayList.get(3))[0]);
////		  sb.append("  ");
////		  sb.append(dataArrayList.get(4)[0]);
////		  sb.append("  ");
////		  sb.append(Bytes.toInt(dataArrayList.get(5)));
////		  sb.append("  ");
////		  sb.append(Bytes.toLong(dataArrayList.get(6)));
////		  sb.append("  ");
////		  sb.append(Bytes.toLong(dataArrayList.get(7)));
////		  for(int i = 0 ; i < dataArrayList.size() ; i++ )
////		  {
////			  //sb.append(dataArrayList.get(i)+":");
////			  sb.append(Bytes.toInt(dataArrayList.get(i)));
////			  sb.append(Bytes.toInt(dataArrayList.get(i)));
////			  break;
////		  }
		 return  sb+"";
	  }
//	  
	  public byte[] getIndexFiled(int pos)
	  {
		  return dataArrayList.get(pos);
	  }
	  public  int getByteSize()
	  {
		  int count = 0;
		  for(int i = 0 ; i < dataArrayList.size() ; i++ )
		  {
			  count = count+dataArrayList.get(i).length;
		  }
		  return count;
	  }
	  
	  public int compareTo(Object k) 
	  {
		  if ( ! (k instanceof MulitFiledData))
		  {
			 return -100;
		  }
		  MulitFiledData o =(MulitFiledData)k;
		  if(o == null)
		  {
			  return -1;
		  }
		  return proxy.compare(this.dataArrayList, o.dataArrayList) ;

	  }
	  
	  public   byte[] getFileValue(int i)
	  {
		  return dataArrayList.get(i);
	  }
	

}
