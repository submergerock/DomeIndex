package org.cProc.distributed.zookeeperDispatch.cloudComputingServer;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.cProc.tool.Bytes;
import org.cProc.tool.myXMLReader.MyXmlReader;


public class CloudComputingServerDoSomeThing extends CloudComputingServerBase{
	
	
	
	 private final static Log Log4jlogger = LogFactory.getLog(CloudComputingServerDoSomeThing.class); 
	 
	 
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
    
 
    //read the conf file
    private void initConf(String confFileName)
    {
    	
    	MyXmlReader reader = new MyXmlReader(confFileName);
    	connectString= reader.getName("connectString");
        sessionTimeout =Integer.parseInt(reader.getName("sessionTimeout"));
        root=reader.getName("app");
        serverPath =root+"/"+ reader.getName("serverPath");
        serverTmp =root+"/"+ reader.getName("serverTmp");
       serverCon = root+"/"+  reader.getName("serverCon");
        
        clientPath =root+"/"+  reader.getName("clientPath");
        clientTmp =root+"/"+  reader.getName("clientTmp");
        clientPei = root+"/"+  reader.getName("clientPei");
        
        serverBeforCon=serverCon+"before";
    }
    
    
    public CloudComputingServerDoSomeThing(String filename) {
    	initConf(filename);

    }
    
    public void initZookeeper()
    {
    	 attributes=""; 
         myZnode   ="";
         active = false;    	
         requestCount = 0;
         chooseNumber = 0;
        isBefore = true;
        request="";
        start = true;
        
        if(zk == null){
            try {
                zk = new ZooKeeper(connectString, sessionTimeout, this);
                mutex = new Integer(-1);
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
            } catch (KeeperException e) {
            	e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public boolean lifeOver()
    {
    	System.out.println("lifeIsOver  is " + lifeIsOver);
    	return lifeIsOver;
    }
    
    void getLock() throws KeeperException, InterruptedException{
    	  if( !initOK)
          {  
          	for(int i = 0 ; i < 5 ; i++)
          	{
          		Thread.sleep(1000);
          		if(initOK)
          		{
          			return;
          		}
          	}
          }
        
        List<String> list = zk.getChildren(serverTmp, false);
        String[] nodes = list.toArray(new String[list.size()]);
        Arrays.sort(nodes);
       
        if(myZnode.equals(serverTmp+"/"+nodes[0])){
        	active = true;
        	//第一个获得服务权的机器不用读属�?，�?虑到每次测试用；
        	if(!start)
        	{
        		getAttribute();
        	}
        	Log4jlogger.info("mynode node is " + myZnode);
 //       	getAttribute();
            doAction();
    		while(!initOK)
    		{
	    		while( zk == null)
	    		{
	    			zk = null;
	    			initZookeeper();
	    		}
	    		for(int i = 0 ; i < 3; i++)
	    		{
	    			try {
						Thread.sleep(1000*3*i);
						if(initOK)
						{
							break;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	    		}
    		}
    		if(zk == null)
    		{
    			initZookeeper();
    		}
            myZnode = zk.create(serverTmp + "/" , new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
            zk.setData(myZnode, Bytes.toBytes(IP.getIP()), -1);
    		lifeIsOver = false;
    		System.out.println("reconnect is over *************************************");
        }
        else{
            waitForLock(nodes[0]);
        }
    }
    
    
    public  void check() throws InterruptedException, KeeperException {
    	
    	initZookeeper();
        myZnode = zk.create(serverTmp + "/" , new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
        zk.setData(myZnode, Bytes.toBytes(IP.getIP()), -1);
        while(true)
        {
        	getLock();
        	Log4jlogger.info("get lock once");
        }
    }
    
    void waitForLock(String lower) throws InterruptedException, KeeperException {
        Stat stat = zk.exists(serverTmp + "/" + lower,true);
        if(stat != null){
        	Log4jlogger.info("I am a server ,and I am waiting");
        		waitEvent();
        		Log4jlogger.info("I am a wake");
        }
        getLock();
        
    }
    
    public boolean isActive()
    {
    	return active;
    }
    
    public void recover( String beforattirbute)
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

    }
    

    @Override
    public void process(WatchedEvent event) {    	
    	if(event.getState() == KeeperState.Disconnected)
    	{
    		zk = null;
    		initOK = false;
    		System.out.println("lifeIsOver = true");
    		lifeIsOver = true;    
    		
    	}
    	if(event.getState() == KeeperState.SyncConnected)
    	{
    		System.out.println("KeeperState.SyncConnected*************************************");
    		initOK = true;
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
    public void doAction(){

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
    
    public void getAttribute()
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
    	attributes = attributes+cutRequest + request;
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
    

    
    public static void main(String[] args) {
        String connectString = "192.168.1.8:"+2181;

        CloudComputingServerDoSomeThing lk = new CloudComputingServerDoSomeThing(connectString);
        try {
            lk.check();
        } catch (InterruptedException e) {
           e.printStackTrace();
        } catch (KeeperException e) {
        	e.printStackTrace();
        }
    }
	

	
}
