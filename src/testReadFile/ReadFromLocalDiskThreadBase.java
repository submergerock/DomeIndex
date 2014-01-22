package testReadFile;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public  abstract class ReadFromLocalDiskThreadBase<T> implements Runnable {
	public static final Log LOG = LogFactory.getLog(ReadFromLocalDiskThreadBase.class.getName());

	
	private List<T> list = new ArrayList<T>();
	private volatile boolean isOver = false;
	
	private List<byte[]> tmpReslutData = new ArrayList<byte[]>(); // 锟结供锟斤拷锟斤拷幕锟斤拷锟�为锟剿匡拷锟斤拷l锟斤拷停锟侥秜锟斤拷锟�
	
	private List<byte[]> reslutData = new ArrayList<byte[]>(); //
	
	private Integer condition = null;
	
	public ReadFromLocalDiskThreadBase(Integer condition) {
		this.condition = condition;
	}

	//check the threads is over ? if one thread is over  and the data of the thread  is get over ,then remove it ;
	public static boolean ThreadsIsOver(ArrayList<? extends ReadFromLocalDiskThreadBase> list) {
		for (int i = 0; i < list.size(); i++) {
				if (list.get(i).isOver && !list.get(i).isHaveData()) {
						list.remove(i);
			}
		}
		if (list.size() == 0) {
			return true;
		}
		return false;
	}

	public boolean isOver() {
		return isOver;
	}
	
	public void getResultData(List<byte[]> reslutDataFromMainThread) {
		
		synchronized (tmpReslutData) {
//			System.out.println("@gyy: once getList is " + tmpReslutData.size());
			reslutDataFromMainThread.addAll(tmpReslutData);
			tmpReslutData.clear();
		}
	}
 
	
	public boolean isHaveData() {
		boolean haveData = true;
		haveData = !tmpReslutData.isEmpty();
	//	LOG.info("tmpReslutData  size is " +tmpReslutData.size()+"  haveData is   " + haveData +"  " + Thread.currentThread().toString());
		return haveData;
	}

	public void writeOneData(byte[] rawInfo,int length)
	{
//		System.out.println("@gyy:write one data***************************");
		if(length <= 0)
		{
			length = rawInfo.length;
		}
		byte [] data = new byte[length];
		System.arraycopy(rawInfo, 0, data, 0, length);
		 reslutData.add(data);
		if (reslutData.size() > 1000) {
			synchronized (tmpReslutData) {
				tmpReslutData.addAll(reslutData);
			}
			if(!reslutData.isEmpty()) {
				reslutData.clear();
			}
			synchronized (condition) {
				condition.notifyAll();
			}
		}
	}
	
	public void writeDataOver()
	{				
//		System.out.println("@gyy:writeDataOver***************************");
		synchronized (tmpReslutData) {
			tmpReslutData.addAll(reslutData);
		}
		reslutData.clear();
		synchronized (condition) {
			isOver = true;
			condition.notifyAll();
		}
	}
	
	public void run() {
		try {
			for (int j = 0; j < list.size(); j++) {
				System.out.println("lise size is  " + list.size()+" i  is " + j);
				readData(list.get(j));
			}
			writeDataOver();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private  T getReadPos()
	{
		return currentReadPos();
	}
	

	//@override ;
	protected abstract T  currentReadPos();
	//@override
	protected abstract void readData( T data);
	
	public void addReadIndex() {
			list.add(getReadPos());
	}
		
}
