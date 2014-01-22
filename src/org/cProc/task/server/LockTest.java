package org.cProc.task.server;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		ReentrantLock lock = new ReentrantLock();
		
		Condition con = lock.newCondition();
		
		
		
		System.out.println(1);
		//con.
		//System.out.println(1);
		
		
		
		lock.lock();
		
		System.out.println(lock.getHoldCount());
		
		
		
		Thread.sleep(100000);
	}

}
