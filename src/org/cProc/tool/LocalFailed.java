package org.cProc.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class LocalFailed {

	
	
	public static void  match(String filePath,String msg)
	{
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			String line  =  null;
			while((line=br.readLine())!=null)
			{
				
				if(line.indexOf(msg)!=-1)
				{
					System.out.println("find=================================");
					Thread.sleep(3000);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(br!=null)
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		File [] files = new File("Q:\\failed").listFiles();
		for(File file:files)
		{
			System.out.println(file.getAbsolutePath());
			match(file.getAbsolutePath(), "1349995558-1349995759-bssap-1-sigserver134");
			
		}

	}

}
