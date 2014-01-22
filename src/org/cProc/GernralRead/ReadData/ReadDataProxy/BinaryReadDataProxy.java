package org.cProc.GernralRead.ReadData.ReadDataProxy;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.cProc.GernralRead.ReadData.DataType;

public class BinaryReadDataProxy implements ReadDataProxy {
	private String fileName = "";
	private long offset = 0;
	private FileSystem fs = null;
	private int dataSize = 0;
	private final int ONCEREADSIZE = 65 * 1024 * 1024;
	BufferedInputStream bufferIn = null;
	private byte[] currentData = null;
	private ArrayList<Integer> startPosOfFileds = new ArrayList<Integer>();
	private ArrayList<Integer> lengthOfFileds = new ArrayList<Integer>();

	public BinaryReadDataProxy(FileSystem fs) {
		this.fs = fs;

	}

	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
		currentData = new byte[dataSize];
	}

	public void setStartPosOfFileds(ArrayList<Integer> startPosOfFileds) {
		this.startPosOfFileds = startPosOfFileds;
	}

	public void setLengthOfFileds(ArrayList<Integer> lengthOfFileds) {
		this.lengthOfFileds = lengthOfFileds;
	}

	// 获取文件系统
	public void setFileName(String fileName) {
		this.fileName = fileName;
		this.offset = 0;
	}

	public void startReadFile() throws IOException {
		offset = 0;
		FSDataInputStream in = fs.open(new Path(fileName));
		bufferIn = new BufferedInputStream(in, ONCEREADSIZE);

	}

	public void closeReadFile() {
		try {
			if (bufferIn != null) {
				bufferIn.close();
			}
			bufferIn = null;
		} catch (IOException e) {
			e.printStackTrace();
			bufferIn = null;
		}
	}

	public boolean readNextData() throws IOException {
		if (bufferIn == null) {
			startReadFile();
		}
		try {
			int readSize = bufferIn.read(currentData);
			offset = offset + readSize;
			if (readSize != dataSize) {
				return false;
			} else {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public byte[] getColumnData(int pos) {
		byte[] data = new byte[lengthOfFileds.get(pos)];
		System.arraycopy(currentData, startPosOfFileds.get(pos), data, 0,
				lengthOfFileds.get(pos));
		return DataType.getBigOrderByte(data);
	}

	public long getOffset() {
		return offset;
	}
}
