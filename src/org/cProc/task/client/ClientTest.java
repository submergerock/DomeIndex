package org.cProc.task.client;

import java.util.Date;

public class ClientTest {

	public static int i = 0;
	public static void main(String[] args) {
		for (int i = 0 ; i < 5; i++) {
			TestThread testThread = new TestThread();
			Thread thread = new Thread(testThread);
			Date date = new Date();
			System.out.println("thread " + thread.getId() + " starts:" + date.toString());
			thread.start();
			Date date1 = new Date();
			System.out.println("thread " + thread.getId() + " ends:" + date1.toString() + ", costs " + (date1.getTime()-date.getTime()));
		}
	}
    
}
