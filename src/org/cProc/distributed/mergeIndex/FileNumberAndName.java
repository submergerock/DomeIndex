package org.cProc.distributed.mergeIndex;

import org.apache.hadoop.fs.Path;

public class FileNumberAndName implements java.lang.Comparable<FileNumberAndName>{
	
	int number = 0;
	Path path = null;
	public FileNumberAndName(int number ,Path path)
	{
		this.number = number;
		this.path = path;
		
	}
	
	@Override
	public int compareTo(FileNumberAndName o) {
		 if(this.number > o.number)
		 {
			 return 1;
		 }
		 else if(this.number < o.number){
			return -1;
		}
		 return  0;

	}
	

	
}
