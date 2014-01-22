package org.cProc.del;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.hadoop.fs.Path;

public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		DataInputStream dis = new DataInputStream(new FileInputStream("c://dingli//debug.org"));
		
		System.out.println(Main.getIndexFileContentInter(dis)[0]);
		
		
		
		dis.close();
		
		Path path = new Path("/smp/cdr/bssap/datafile/20121012/02/1349979930-1349980146-bssap-1-sigserver146.cdr");
		
		System.out.println(path.getName());
		
	}

}
