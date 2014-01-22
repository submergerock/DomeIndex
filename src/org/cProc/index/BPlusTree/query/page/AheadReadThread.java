package org.cProc.index.BPlusTree.query.page;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.cProc.index.BPlusTree.BTreeType;



public class AheadReadThread  implements Runnable{
	
	PageLoadProxy proxy = null;
	private BlockingQueue<Long> queue = new  ArrayBlockingQueue<Long>(1024);
	
	public AheadReadThread(PageLoadProxy proxy)
	{
		this.proxy = proxy; 
	}
	
	public void run()
	{
		while(true)
		{
			try {
				long offset =queue.take();
				//System.out.println("ahead one page"+"offset is "+offset);
				if(offset == 0)
				{
					return ;
				}
				try {
					
					if(proxy == null )
					{
						return ;
					}
					proxy.getMergerDataPage(offset, BTreeType.MegerBTreeType,false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setNextMergeDataPage( long offset)
	{
		queue.add(offset);
	}

}
