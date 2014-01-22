package org.cProc.tool;

public class CompareTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String str1 = "1339295236";
		String str2 = "1339295237";
		
		
		long l = System.currentTimeMillis();
		for(int i=1;i<=100000;i++)
		{
			boolean c  = str1.equals(str2);
		}
		long l1 = System.currentTimeMillis();
		System.out.println(l1-l);
		for(int i=1;i<=100000;i++)
		{
			int a = Integer.parseInt(str1);
			int b = Integer.parseInt(str2);
			boolean c = a==b;
		}
		System.out.println( System.currentTimeMillis()-l1);
		
		

	}

}
