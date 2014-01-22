package org.cProc.distributed.zookeeperDispatch.cloudComputingServer;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
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
import org.cProc.distributed.createIndex.ReadFromHDFSSever;
import org.cProc.distributed.zookeeperDispatch.help.MonitorExitThread;
import org.cProc.distributed.zookeeperDispatch.help.ThreadExit;
import org.cProc.distributed.zookeeperDispatch.help.ZKHelper;
import org.cProc.tool.Bytes;

import org.cProc.tool.myXMLReader.MyXmlReader;


public abstract class CloudComputingServer extends CloudComputingServerBase implements RequestStateOperater , ThreadExit{
	
	 private final static Log Log4jlogger = LogFactory.getLog(CloudComputingServer.class); 
	public static final  String createIndexLogString ="createIndexLog";
	public  static final  String createIndexFalid ="createIndexFalid";
	public static final  String serverExit ="serverExit";
	public static final  String clientExit ="clientExit";
	private String cutRequest="$";
	String cut="@";
	String attributes=""; 
	
	
    String myZnode   ="";
    
    private boolean active = false;    	
    private  String connectString = "";
    private int sessionTimeout = 0;
    private String root="";
    private String serverPath ="";
    private String serverTmp ="";
    private String serverCon ="";
    private String serverBeforCon="";
    
    private String clientPath ="";
    private String clientTmp ="";
    private String clientPei ="";
    
    long  requestCount = 0;

    int   chooseNumber = 0;
    
    private boolean isBefore = true;
    private String request="";
    
    private boolean start = true;
    
    private  boolean lifeIsOver = false;
    private  boolean initOK = false;
    private final int closeCountMax = 10;
    private int closeCount = 0;
    private String zklogRequestLogPathString="";
    private String zkRequestFailedString="";
    private String zkServerExitPathString ="";
    private String zkClientExitPathString ="";
    private volatile boolean exit = false;
    
//    private CountDownLatch exitLatch = new  CountDownLatch(1);
    
    private MonitorExitThread monitorExitThread =null;
 
    //read the conf file
    private void initConf(String confFileName)
    {
    	MyXmlReader reader = new MyXmlReader(confFileName);
    	//zk连接 路径
    	connectString= reader.getName("connectString");
        sessionTimeout =Integer.parseInt(reader.getName("sessionTimeout"));
        //应用名，ex. /TestCreateIndex
        root=reader.getName("app");
        //日志节点,ex. /TestCreateIndex/createIndexLog
        zklogRequestLogPathString = root+"/"+createIndexLogString;
        //异常节点，ex./TestCreateIndex/createIndexFalid
        zkRequestFailedString = root+"/"+createIndexFalid;
        //ex./TestCreateIndex/serverExit
        zkServerExitPathString = root+"/"+serverExit;
        //ex./TestCreateIndex/clientExit
        zkClientExitPathString = root +"/"+clientExit;
        //ex. /TestCreateIndex/Server
        serverPath =root+"/"+ reader.getName("serverPath");
        //ex. /TestCreateIndex/Server/tmp
        serverTmp =root+"/"+ reader.getName("serverTmp");
        //ex. /TestCreateIndex/Server/conf
       serverCon = root+"/"+  reader.getName("serverCon");
       //ex. /TestCreateIndex/Client
        clientPath =root+"/"+  reader.getName("clientPath");
        //临时节点，每个client都会在下面注册一个节点，ex. /TestCreateIndex/Client/Tmp
        clientTmp =root+"/"+  reader.getName("clientTmp");
        //持久化节点，ex. /TestCreateIndex/Client/Pei
        clientPei = root+"/"+  reader.getName("clientPei");
        //ex. /TestCreateIndex/Server/confbefore
        serverBeforCon=serverCon+"before";
    }
    
    
    public CloudComputingServer(String filename) {
    	initConf(filename);

    	Thread thead = new Thread( new MonitorTheClientThread(this, connectString  ,sessionTimeout ,clientTmp));
        thead.start();
        initExitThread();
    }
    
    private void initExitThread()
    {
    	monitorExitThread  = new MonitorExitThread(this, connectString, sessionTimeout, zkServerExitPathString) ;
    	Thread thread  = new Thread(monitorExitThread);
    	thread.start();
    }
    
    public void initZookeeper()
    {
    	if(zk != null && initOK)
    	{
    		return ;
    	}
    	 attributes=""; 
         myZnode   ="";
         active = false;    	
         requestCount = 0;
         chooseNumber = 0;
        isBefore = true;
        request="";
        start = true;
        while(zk == null || !initOK ){
            try {
            	mutex = new Integer(-1);
                zk = new ZooKeeper(connectString, sessionTimeout, this);
                synchronized (mutex) {
                	mutex.wait(5000);
				}
                if( !initOK )
                {
                	zk.close();
                	zk = null;
                }
            } catch (Exception e) {
                zk = null;
                e.printStackTrace();
            }
        }
        //init the zookeeper
        if (zk != null) {
            try {
                Stat s = zk.exists(root, false);
                if (s == null) {
                    zk.create(root, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
                s = zk.exists(zklogRequestLogPathString, false);
                if( s == null){
             	   zk.create(zklogRequestLogPathString, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
                s = zk.exists(zkRequestFailedString, false);
                if( s == null){
             	   zk.create(zkRequestFailedString, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
                
                s = zk.exists(zkServerExitPathString, false);
                if( s == null){
             	   zk.create(zkServerExitPathString, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
                
                s = zk.exists(zkClientExitPathString, false);
                if( s == null){
             	   zk.create(zkClientExitPathString, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
                
               s = zk.exists(serverPath, false);
               if( s == null){
            	   zk.create(serverPath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
               }

               s = zk.exists(serverTmp, false);
               if( s == null){
            	   zk.create(serverTmp, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
               }
               s = zk.exists(serverCon, false);
               if( s == null){
            	   zk.create(serverCon, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
               }
               
               s = zk.exists(serverBeforCon, false);
               if( s == null){
            	   zk.create(serverBeforCon, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
               }
               
               s = zk.exists(clientPath, false);
               if( s == null){
            	   zk.create(clientPath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
               }
               
               s = zk.exists(clientTmp, false);
               if( s == null){
            	   zk.create(clientTmp, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
               }
               
               s = zk.exists(clientPei, false);
               if( s == null){
            	   zk.create(clientPei, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
               }
               myZnode = zk.create(serverTmp + "/" , new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
               zk.setData(myZnode, Bytes.toBytes(IP.getIP()), -1);
            } catch (KeeperException e) {
            	e.printStackTrace();
            	System.out.println("@gyyException: init zookerException: KeeperException" );
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("@gyyException: init zookerException: InterruptedException" );
            }
        }
    }
    
    public boolean lifeOver()
    {
    	return lifeIsOver;
    }
    
    void getLock() {

    	 List<String> list = null;
    	 boolean getChild = false;
    	 while(!getChild)
    	 {
    		 try {
        		 list = zk.getChildren(serverTmp, false);
    		} catch (Exception e) {
    			zkCloseCountDre();  
    			initZookeeper();
    			System.out.println("@gyyException: getLock zookerException: KeeperException" );
    			continue;
    		}
    		getChild = true;
    	 }
    	 
        String[] nodes = list.toArray(new String[list.size()]);
        Arrays.sort(nodes);
        //只有第一个节点的server做，其它节点的server等
        if(myZnode.equals(serverTmp+"/"+nodes[0])){
        	active = true;
        	//第一个获得服务权的机器不用读属�?，�?虑到每次测试用；
        	if(!start)
        	{
        		try {
        			getAttribute();
				} catch (KeeperException e) {
						e.printStackTrace();
						System.out.println("@gyyException: getLock:getAttribute zookerException: KeeperException" );
					} catch (InterruptedException e) {
						e.printStackTrace();
						System.out.println("@gyyException: getLock:getAttribute zookerException: KeeperException" );
					}	
        	}
        
        	doAction();
        	zkCloseCountDre();  
        	initZookeeper();	
        }
        else{
            waitForLock(nodes[0]);
        }
    }
    
    
    public  void check()  {
    	initZookeeper();
        while(!isExit())
        {
        	getLock();
        }
	  boolean reslut = 	ZKHelper.createPesiSeqChildNode(zk, zkClientExitPathString, "over".getBytes());
		System.out.println("create new node in " + zkClientExitPathString +"    " + zk  +"   " + reslut);
        System.out.println("**********************main thread is over*************************");
    }
    
    void waitForLock(String lower) {
    	try {
    		 Stat stat = zk.exists(serverTmp + "/" + lower,true);
    	        if(stat != null){
    	        		System.out.println("I am a server ,and I am waiting");
    	        		waitEvent();
    	        		System.out.println("I am a wake");
    	        }
		} catch (Exception e) {
			System.out.println( "wait for lock is exception");
			System.out.println("@gyyException: waitForLock zookerException: KeeperException" );
			initZookeeper();
		}
       // getLock();      
    }
    
    public boolean isActive()
    {
    	return active;
    }
    
    public void recover( String beforattirbute)  throws KeeperException, InterruptedException
    {
    		//��ȡ �ɷ��Ľڵ� ��,�Լ� �ɷ������ݣ�
    		StringTokenizer as = new StringTokenizer(beforattirbute, cutRequest );
    		as.nextToken();
    		String node					  = as.nextToken();
    		String lastRequest        = as.nextToken();
    		long number = 0 - requestCount;
    		Log4jlogger.info("start recover");
    		try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		sendData(lastRequest,node ,  number);
    }
    
    public void beforeSendData()
    {
    	
    }
    
    
    public void zkCloseSetOrg()
    {
    	closeCount = 0;
    }
    
    public void zkCloseCountDre()
    {
    	closeCount ++ ;
    	System.out.println("@gyy:zkCloseCountDre" + closeCount);
    	if(closeCount > closeCountMax)
    	{
    		closeCount = 0;
    		try {
    			if(zk !=null )
    			{
    				zk.close();
    			}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				System.out.println("@gyy"+e1.getMessage()+" checkRequestComing: zkCloseException"  );
			}
			zk = null;
    		initOK= false;	
    		lifeIsOver = true; 
    	}
		  
    }
    
    @Override
    public void process(WatchedEvent event) {    	
    	if(event.getState() == KeeperState.Disconnected)
    	{
    			zkCloseCountDre();      		
    	}
    	if(event.getState() == KeeperState.SyncConnected)
    	{
    		System.out.println("KeeperState.SyncConnected*************************************");
    		initOK = true;
    		lifeIsOver = false;
    		zkCloseSetOrg();
    	}
    			
        if(event.getType() == Event.EventType.NodeDeleted){
        	Log4jlogger.info("I am a server ,and I am wake");
            long tmp = 0;
            try
            {
            	start = false;
            }catch( Exception e)
            {
            	e.printStackTrace();
            }
            super.process(event);
        }
    }
    /**
     * ִ����������
     */
    public void doAction() {

    }
    
    public int getClientNumber()
    {
    	   List<String> orglist = null;
    	   try {
			orglist = zk.getChildren(clientTmp,false);
    	   } catch (KeeperException e) {
   			e.printStackTrace();
   		} catch (InterruptedException e) {
   			e.printStackTrace();
   		}
   		return orglist.size();
    }

    //�ɷ�����ʱ��ѡ��ڵ�?
    public String GetPersistentNode() throws KeeperException, InterruptedException
    {
    	   List<String> orglist = null;
		   orglist = zk.getChildren(clientTmp,false);
			while(orglist.size() == 0)
			{
	        	  try {
	  				Thread.sleep(1000);
	  			} catch (InterruptedException e) {
	  				e.printStackTrace();
	  			}
				orglist = zk.getChildren(clientTmp,false);
			}			

        String[] nodes = orglist.toArray(new String[orglist.size()]);
        Arrays.sort(nodes);
        
        byte[] reslut  = null;
        while(reslut  == null)
        {
			reslut = zk.getData( clientTmp+"/"+nodes[chooseNumber%nodes.length], false,null);
			chooseNumber++;
        	  if( reslut != null)
        	  {
        		  return  Bytes.toString(reslut);
        	  }
        	  try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        
        return  Bytes.toString(reslut);
    }
    
    public long GetRequestNumber( )
    {
    	if(requestCount < 0)
    	{
    		requestCount = 0;
    	}
		return requestCount;
    }

    public void IncRequestNumber()
    {
    	requestCount++;
    }
    
    
    public void addSaveAttribute(String attribute)
    {
    	attributes=attributes+attribute+cut;
    }
    
    
    public void clearAttribute()
    {
    	attributes ="";
    }
    
    public void PrintAttribute()
    {
    	System.out.println("requestCount  is " + requestCount);
    }
    public void parseAttribute()
    {
    	String content = getNextAttibute();
    	StringTokenizer as = new StringTokenizer(content,cutRequest);
    	requestCount = Long.parseLong(as.nextToken());
    }
    
    public String getNextAttibute()
    {
    	StringTokenizer as = new StringTokenizer(attributes,cut);
    	if( !as.hasMoreTokens())
    	{
    		return attributes;
    	}
    	String reslut =  as.nextToken();
    	attributes = attributes.substring(reslut.length(), attributes.length());
    	return reslut;
    }
    
    public void getAttribute()  throws KeeperException, InterruptedException
    {
    	byte[]  byteAttribute = null;
    	byte[] beforebyteAttribute = null;
		  try {
		byteAttribute =zk.getData(serverCon, false,null);
		beforebyteAttribute = zk.getData(serverBeforCon, false, null);
			if(beforebyteAttribute ==null  || beforebyteAttribute.length == 0 )
			{
						return ;
			}
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		attributes = Bytes.toString(byteAttribute);
		String beforeAttributes = Bytes.toString(beforebyteAttribute);

		if(attributes.length() > 0   &&  beforeAttributes.length() > 0 && beforeAttributes  != attributes  )
		{
			attributes =  beforeAttributes;
			parseAttribute();
			recover(beforeAttributes);
		}
		else
		{
			if(attributes.length() > 0)
			{
				parseAttribute();
			}
		}
    }
    

    public  void saveAttribute ()
    {
    	attributes = attributes+GetRequestNumber()+cutRequest + request;
		try {
//			DebugPrint.DebugPrint("save   attributes  is " + attributes ,this);
			
			if(isBefore)
			{
				zk.setData(serverBeforCon,  attributes.getBytes(),-1);
			}else
			{
			zk.setData(serverCon,  attributes.getBytes(),-1);
			}
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    public void sendData(String requestContent  )  throws KeeperException, InterruptedException
	{
		if(requestContent.length() > 0)
		{
	    	String parentNode = GetPersistentNode();
	    	 if(parentNode == null)
	    	 {	    		 
	    		 return;
	    	 }

			IncRequestNumber();
			sendData(requestContent , parentNode,GetRequestNumber() );
		};
	}
    
	//�ɷ�����  fileContentΪ����parentNodeΪѡ�еĵĽڵ�
    public  void sendData(String requestContent , String parentNode , long requestNumber  )   throws KeeperException, InterruptedException
    {
    	
    	beforeSendData();
		if(requestContent.length() > 0)
		{	
			request = parentNode + cutRequest + requestContent;
			isBefore = true;
			saveAttribute();
			clearAttribute();
			if(zk.exists(parentNode+"/"+requestNumber, false) == null)
			{
				zk.create(parentNode+"/"+requestNumber , requestContent.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
			}
			isBefore = false;
			saveAttribute();
			clearAttribute();
		}
    	
    }
    
    
    public String EncapsulationRequest()
    {
    	return "";
    }
    abstract public void requestRollBack(String message);
    
    //other thread call;
    public void threadExit()
    {
    	exit = true;
//    	this.notifyMainThread();
//    	waitAcitonOver();
//		exit();
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
    
    public boolean isExit()
    {
    	return exit;
    }
    
    
//    public void waitAcitonOver()
//    {
//    	try {
//			exitLatch.await();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//    }
//    
//    public void notifyAcitonOver()
//    {
//    	exitLatch.countDown();
//    }
//    
    
	   abstract   public  void exit();
    

	
}
