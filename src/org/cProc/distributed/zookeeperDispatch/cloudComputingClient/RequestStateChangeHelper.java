package org.cProc.distributed.zookeeperDispatch.cloudComputingClient;

public class RequestStateChangeHelper {
	
	
	//zkNodeInfor state:
	//requet--------> request!!-_-!!indexInfor-------------> request!!-_-!!indexInfor!!-_-!!end
	  public static  final String zkNodeInforSeperater ="!!-_-!!";
	  public static int requetOrgState = 1 ;
	  public static int requetStartUpLoadState = 2 ;
	  public static int requetEndUpLoadState = 3 ;
	  
	  public static int indexInforPos = 1;
	
	  
	  public static int getMessageType(String message)
	  {
		  if(message != null)
		  {
			   String[] splitsStrings = message.split(zkNodeInforSeperater);
			   return splitsStrings.length;
		  }
		  return 1;
	  }
	  
	public  static void changeZKNodeStat( CloudComputingClient client ,String node ,String newMessage)
	{
		client.changeNodeInfor(node, newMessage);
	}
	
	public static void changeZKNodeRequestToStart(CloudComputingClient client ,String node ,String newMessage)
	{
		changeZKNodeStat(client, node ,  newMessage);
	}
	
	public static void changeZKNodeRequestToEnd(CloudComputingClient client ,String node )
	{
		changeZKNodeStat(client, node ,  "end");
	}
	
	public static String zkNodeRequestContentChange(String oldMessage , String newMessage)
	{
		return  oldMessage+zkNodeInforSeperater+newMessage;
	}
	

}
