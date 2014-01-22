package org.cProc.index.BPlusTree.Data;

import java.nio.ByteBuffer;

public interface  CProWritable {

		public  void read(ByteBuffer buffer);
		public  void write(ByteBuffer buffer);
}
