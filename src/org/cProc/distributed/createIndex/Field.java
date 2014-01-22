package org.cProc.distributed.createIndex;

import java.util.HashMap;
import java.util.Map;

import org.cProc.tool.Bytes;

/**
 * 
 * @author zxc
 *
 */
public class Field {
	
	/**
	 * key为索引关键字的优先级，越小优先级越大，最小为1
	 */
	private Map<Integer, byte[]> data = new HashMap<Integer, byte[]>();
	private String path;
	private long offset;
	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public int getMapSize() {
		return this.data.size();
	}

	public void addData(Integer key, byte[] value) {
		data.put(key, value);
	}

	public byte[] getData(Integer key) {
		return data.get(key);
	}

	public Map<Integer, byte[]> getData() {
		return data;
	}

	public void setData(Map<Integer, byte[]> data) {
		this.data = data;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
