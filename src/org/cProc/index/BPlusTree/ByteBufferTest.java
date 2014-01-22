package org.cProc.index.BPlusTree;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ByteBufferTest {

	
		public static void main(String[] args)
		{
			String name ="q:\\1350402271204.xls";
	    	try {
	    		File file = new File(name);
	        	if(!file.exists())
	        	{
	        		file.createNewFile();
	        	}	        	
	            FileChannel fc = new RandomAccessFile(name, "r").getChannel();  
	            	            
	              MappedByteBuffer temp = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
	              System.out.println(temp.getClass());
	          //  MappedByteBuffer ib = fc.map(FileChannel.MapMode.READ_WRITE, 0, fc.size());  
	              int i = 0 ;
		       	while(temp.hasRemaining() && i < 5)
		        {
		        	System.out.print(temp.get()+" ");
		        	i++;
		        }
		    	System.out.println();

		    	System.out.println("read is over" );
	            fc.close();

			} catch ( Exception e) {
				e.printStackTrace();
			}
	    	
		}
}
