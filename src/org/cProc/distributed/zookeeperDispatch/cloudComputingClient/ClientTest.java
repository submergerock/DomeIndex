package org.cProc.distributed.zookeeperDispatch.cloudComputingClient;

import org.cProc.tool.myXMLReader.MyXmlReader;

public class ClientTest  extends CloudComputingClient {

   
    long endTime = 0;

    long  hdfsFileNumber = 0;
    	
	public ClientTest(String fileName )
	{
		super(fileName);
		
		initConf(fileName);
		
	}
	
    private void initConf(String confFileName)
    {
    	MyXmlReader reader = new MyXmlReader(confFileName);	
    }
	
	protected  void  parseRequest()
	{
		
		 //format:path@ID@path@ID@path@ID     write for the client , save for sever change recover the file and ID;
		
	}
	
	
	//Client��Ҫ������
	  public void doAction(String RequestContent,String nodeName)
	{
		  
	}
	  
       public void requestRollBack(String message)
      {
    	  
      }
	   public  void clearAllTask()
	   {
		   System.out.println("@gyyClear: clear all task in the queue");
	   }
	   
	     public void exit()
	     {
	    	 
	     }
		     
	public static void main(String[] args)
	{
		ClientTest  test = new ClientTest(args[0]);
		//test.check();

	}

	
}
