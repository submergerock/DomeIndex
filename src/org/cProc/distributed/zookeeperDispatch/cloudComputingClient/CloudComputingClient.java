package org.cProc.distributed.zookeeperDispatch.cloudComputingClient;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.cProc.distributed.createIndex.Config;
import org.cProc.distributed.zookeeperDispatch.cloudComputingServer.CloudComputingServer;
import org.cProc.distributed.zookeeperDispatch.cloudComputingServer.IP;
import org.cProc.distributed.zookeeperDispatch.cloudComputingServer.RequestStateOperater;
import org.cProc.distributed.zookeeperDispatch.help.MonitorExitThread;
import org.cProc.distributed.zookeeperDispatch.help.ThreadExit;
import org.cProc.distributed.zookeeperDispatch.help.ZKConnectionState;
import org.cProc.distributed.zookeeperDispatch.help.ZKHelper;
import org.cProc.tool.Bytes;
import org.cProc.tool.myXMLReader.MyXmlReader;

public abstract class CloudComputingClient extends CloudComputingClientBase  {

	 private static Log Log4jlogger = LogFactory.getLog(CloudComputingClient.class); 
	 static{
		org.apache.log4j.PropertyConfigurator.configure("mylog4j.properties");
	 }
	
//	    private final String zkNodeInforSeperater ="!!-_-!!";
	    String myZnode;
	    String myContentNodeName;
	    int      sessionTimeout = 0;
	    String connectFileName ="";
	    String root="";
	    String clientPath="";
	    String clientTmpPath="";
	    String clientPeiPath ="";
	    int    lastEnd  =-1;
	    String cut="@";
	    private  volatile   boolean  initOK = false;
	    private String zklogRequestLogPathString="";
	    private String zkRequestFailedString="";
//	    private String zkClientExitPathString ="";
	    
	    private volatile boolean exit = false;
	
	    private CountDownLatch latch = new CountDownLatch(1);  // for when exit ,read all the task;
	    
//	    private MonitorExitThread monitorExitThread =  null;
	    
	   
//	    private ArrayList<Integer>  failFileName = new ArrayList<Integer>();
	    
	    //read the conf file
	    private void initConf(String confFileName)
	    {
	    	MyXmlReader reader = new MyXmlReader(confFileName);
	    	connectFileName = reader.getName("connectString");
	    	root = reader.getName("app");
//	        zklogRequestLogPathString = root+"/"+CloudComputingServer.createIndexLogString;
//	        zkRequestFailedString = root+"/"+CloudComputingServer.createIndexFalid;
//		    zkClientExitPathString = root+"/"+CloudComputingServer.clientExit;
//	    	clientPath= 		root +"/" + reader.getName("clientPath");
//	    	clientTmpPath =  root +"/" + reader.getName("clientTmp");
//	    	clientPeiPath =  root +"/" + reader.getName("clientPei");
	    	sessionTimeout = Integer.parseInt(reader.getName("sessionTimeout"));
	    	Log4jlogger.info("clientPeiPath  is " + clientPeiPath);
	    	String ip = reader.getName("ip");
			if(ip!=null)
			{
				Config.IP = ip.trim();
			}
			
	    }
	    
//	    synchronized  public  void  initZookeeper()
//	   {
//	    	if(zk != null &&initOK)
//	    	{
//	    		return ;
//	    	}
//		 try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e1) {
//			e1.printStackTrace();
//		}
//			if (zk != null) {
//				try {
//					zk.close();
//					zk = null;
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		   Log4jlogger.info("init zookeeper");
//	        while(zk == null || !initOK ){
//	            try {
//	            	mutex = new Integer(-1);
//	                zk = new ZooKeeper(connectFileName, sessionTimeout, this);
//	                synchronized (mutex) {
//	                	mutex.wait(5000);
//					}
//	                if( !initOK && zk != null )
//	                {
//	                	zk.close();
//	                	zk = null;
//	                }
//	            } catch (Exception e) {
//	                zk = null;
//	                e.printStackTrace();
//	            }
//	        }
//	        Log4jlogger.info("init  zk  is OK ");
//	        if (zk != null  && initOK) {
//	            try {
//	                Stat s = zk.exists(root, false);
//	                if (s == null) {
//	                    zk.create(root, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//	                }
//	                s = zk.exists(zklogRequestLogPathString, false);
//	                if( s == null){
//	             	   zk.create(zklogRequestLogPathString, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//	                }
//	                s = zk.exists(zkRequestFailedString, false);
//	                if( s == null){
//	             	   zk.create(zkRequestFailedString, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//	                }
//	           	                
//	                s = zk.exists(zkClientExitPathString, false);
//	                if( s == null){
//	             	   zk.create(zkClientExitPathString, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//	                }
//	                
//	                
//	                 s = zk.exists(clientPath, false);
//	                if (s == null) {
//	                    zk.create(clientPath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//	                }
//	                 s = zk.exists(clientTmpPath, false);
//	                if (s == null) {
//	                    zk.create(clientTmpPath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//	                }
//	                 s = zk.exists(clientPeiPath, false);
//		            if (s == null) {
//		                    zk.create(clientPeiPath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//		             }
//		    	    	Log4jlogger.info("create the node");  
//		    	    	myContentNodeName = zk.create(clientPeiPath+"/" ,IP.getIP().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT_SEQUENTIAL);
//		    		    myZnode = zk.create(clientTmpPath + "/" , myContentNodeName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
//		    	        Log4jlogger.info("myContentNode is " + myContentNodeName);
//		    	        Log4jlogger.info("myZnode is "+ myZnode);
//	            } catch (KeeperException e) {
//	            	e.printStackTrace();
//	            	return ;
//	            } catch (InterruptedException e) {
//	                e.printStackTrace();
//	                return ;
//	            }
//	        }
//	        return ;
//	   }
	    
	    
	    public CloudComputingClient(String filePath) {
	    	initConf(filePath);
	    	//initZookeeper();
	    	//initExitThread();
	    }
	    
//	    private void initExitThread()
//	    {
//	    	monitorExitThread  = new MonitorExitThread(this, connectFileName, sessionTimeout, zkClientExitPathString) ;
//	    	Thread thread  = new Thread(monitorExitThread);
//	    	thread.start();
//	    }
	    
	    public synchronized void RequestIsFailed(String nodeName)
	    {
	    	  try {
	    		  Log4jlogger.info("zk  : " + nodeName +"  is   failed");
	    		  // when is not expierd ,then this move ;
	    		  //if expierd, then the server move;
			    	if(this.initOK) 
			    	{
				   		 byte[]  data = ZKHelper.getZKNodeData(zk, nodeName);
						 //if no request, then 
						if (data == null) {
							return;
						}
					 String message = new String(data);
					 int state =  RequestStateChangeHelper.getMessageType(message);
					 if (state == RequestStateChangeHelper.requetStartUpLoadState) {
						// chech the indexfile ,if exits ,then delete;
						this.requestRollBack(message);
						ZKHelper.createPesiNode(zk,zkRequestFailedString+ "/"
										+ Integer.parseInt(nodeName.substring(nodeName.lastIndexOf("/") + 1)),
								ZKHelper.getZKNodeData(zk, nodeName));
						ZKHelper.deleteNodeData(zk, nodeName);
						zk.getChildren(myContentNodeName, true); // register new watch
						Log4jlogger.info("zk delete : " + nodeName);
					}
					 else {
						 RequestIsOver(nodeName);
					}
			    	}
		    	  }catch (Exception e) {
		    		  Log4jlogger.info("zk delete : " + nodeName +"   failed");
				}
	    }
	    
	    public synchronized boolean RequestIsOver(String nodeName)
	    {
	    	  try {
		    	if(this.initOK)
		    	{
		    	  ZKHelper.deleteNodeData(zk,nodeName);
		    	  zk.getChildren(myContentNodeName, true); //register new watch
		    	  Log4jlogger.info("zk delete : " + nodeName );
		    	  return true;
		    	}
		    	else {
					return false;
				}
	    	  }catch (Exception e) {
	    		  Log4jlogger.info("zk delete : " + nodeName +"   failed");
	    		  return false;
			}
	    }
	    
	    public synchronized void changeNodeInfor(String nodeName,String newMessage)
	    {
	    	  try {
	    		  byte[] a = zk.getData(nodeName, false, null);
	    		  String newData =RequestStateChangeHelper.zkNodeRequestContentChange(  new String(a), newMessage) ;
		    	  zk.setData(nodeName, newData.getBytes(), -1);
		    	  zk.getChildren(myContentNodeName, true); //register new watch
		    	  Log4jlogger.info("zk node change : " + nodeName  +"  message is " + newData);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (KeeperException e) {
					e.printStackTrace();
				}
	    }
	    
	    public synchronized void addLogIndex(String newMessage)
	    {
	    	  ZKHelper.createPesiSeqChildNode(zk, zklogRequestLogPathString, newMessage.getBytes())  ;
	    }
	    
//	    public void check()  {	        
//	        checkRequestComing(myContentNodeName);
//	    }
	     
	    boolean checkIsContain( ArrayList<Integer> list ,int number)
	    {
	    	for(int i = 0 ; i < list.size() ; i ++)
	    	{
	    		if(list.get(i).intValue() == number)
	    		{
	    			return true;
	    		}
	    	}
	    	return false;
	    }
	    
//	    //这个方法里有些问题，当出现意外情况时，没有可靠性保证；
//	    void checkRequestComing(String myContentNode)
//	    {
//	    	int maxNumber = -1;
//	        while(!exit)
//	        {
//	        	maxNumber =findNewTask(myContentNode ,  maxNumber , true );
//	        }
//	        //chech the last time
//	        String message =  "check the  last time :scan  : "+myContentNode;
//	        Log4jlogger.info( message);
//	        findNewTask(myContentNode, maxNumber, false);
//	        latch.countDown();
//			
//	    }
	    
//	    public int findNewTask( String myContentNode , int maxNumber ,  boolean wait)
//	    {
//	    	int orgMaxNumber = maxNumber;
//        	while( !initOK || zk == null) //if zookeeper disconneted , init zookeeper
//        	{
//        		initZookeeper();    
//        		myContentNode = this.myContentNodeName; 
//        	}
//        	Log4jlogger.info("@gyy:start scan contentNodeName: "+myContentNode);
//        	try {
//            List<String> orglist = null;
//			orglist = ZKHelper.getPathChildNode(zk, myContentNode,true);
//			if(orglist == null)
//			{
//				Log4jlogger.info("@gyy:checkRequestComing*************");
//				return orgMaxNumber;
//			} 
//             List<String> list = new ArrayList<String>();
//               boolean clearFailFileTag = true;
//               for(int i = 0 ; i < orglist.size() ; i++)
//               {
//            	   int number = Integer.parseInt(orglist.get(i));
//            	   // ������ 1. ����ڵ�ʧ�ܺ�ת�Ƶ����ڵ������  
//            	   //               2.��ͬ��һ��������Ϊserver�л���������������2�Σ��ڶ����Ը������ʽ���ͣ�ֱ�����ӣ�?
//            	   if(number < 0 )
//            	   {
//            		   clearFailFileTag = false;
//            	   }
//            	   if(number < 0 && ! checkIsContain(failFileName,number))
//            	   {
//	            		   if(Math.abs(number)  == lastEnd)
//	            		   {
//	            			   //��Ҫ���node ,����Ҫ�����ѯĿ¼������?*********
//	            			   RequestIsOver(myContentNode+"/"+number);
//	            		   }
//	            		   else
//	            		   {
//	            		   Log4jlogger.info(" number is " + number);
//	            		   failFileName.add(number);
//	            		   list.add(orglist.get(i));
//	            		   }
//            	   }
//            	   
//            	   if( number > lastEnd && number > 0)
//            	   {
//            		   list.add(orglist.get(i));
//            	   }
//            	   
//            	   if(maxNumber < number )
//            	   {
//            		   maxNumber = number;
//            	   }
//               }
//             
//               lastEnd	= maxNumber;
//               
//               if(clearFailFileTag)
//               {
//            	   failFileName.clear();
//               }
//               
//               String[] nodes = list.toArray(new String[list.size()]);
//               for(int i = 0 ; i < nodes.length ; i++)
//               {
//            	   int count = 0 ; // try ten time , "find zk.getData will return Null"�?
//            	   byte[] data = null;
//            	   data = ZKHelper.getZKNodeData(zk, myContentNode+"/"+nodes[i]);
//            	   if(data!=null)
//            	   {
//            		   doAction(Bytes.toString(data),myContentNode+"/"+nodes[i]);
//            	   }	            	
//               }     
//               if(wait)
//               {
//            	   this.waitEvent(); 	
//               }
//	       	   Log4jlogger.debug("@gyy:oneEvent is happened");
//        	} catch (Exception e) {
//				Log4jlogger.info("@gyy"+e.getMessage()+" checkRequestComing: Exception"  );
//				e.printStackTrace();
//				return orgMaxNumber;
//			}
//        	return maxNumber;
//        
//	    }
	    
	   	    	    	    
//	    @Override
//	    public void process(WatchedEvent event) {
//	    	
//	    	//this zk connection is faild ;
//	    	if(event.getState() == KeeperState.Expired)
//	    	{
//	    		Log4jlogger.info("Expired*************************************");
//	    		initOK= false;	   
//	    		try {
//	    			if(zk != null)
//	    			{
//	    				zk.close();
//	    				zk = null;
//	    				clearAllTask();
//	    			}
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//	    	}
//	    	if(event.getState() == KeeperState.Disconnected)
//	    	{
//	    		Log4jlogger.info("Disconnected*************************************");
//	    		super.process(event);
//	    	}
//	    	if(event.getState() == KeeperState.SyncConnected)
//	    	{
//	    		Log4jlogger.info("KeeperState.SyncConnected*************************************");
//	    		initOK = true;
//	    	}
//	    	if(event.getType() == Event.EventType.NodeChildrenChanged)
//	    	{	
//	    		super.process(event);
//	    	}	        
//	    }
	    /**
	     * ִ����������
	     */
	    	abstract public void doAction(String fileName,String nodeName);
	       
	        abstract public  void clearAllTask();
	        
	        abstract public void requestRollBack(String message);
	        public boolean checkZKConnectionIsExpire()
	        {
	        	return initOK;
	        }
	    
	    
       	   abstract   public  void exit();
	         public void threadExit()
	         {
		        	exit = true;
		        	notifyMainThread();
					waitAllTask();
					exit();

	         }
	       
	        public void waitAllTask()
	        {
	        	try {
	        		System.out.println("wait all task**************************");
					latch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        }
	        public void close()
	        {
	        	if(zk != null)
	        	{
	        		try {
						zk.close();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	        	}
	        }

}

	
	

