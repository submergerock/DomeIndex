package org.cProc.tool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.cProc.task.common.IRemote;
import org.cProc.tool.myXMLReader.MyXmlReader;

import com.caucho.hessian.client.HessianProxyFactory;

public class FailedTask implements Watcher,Runnable{
	
	


	private ZooKeeper zk = null;
	private CountDownLatch signal = new CountDownLatch(1);

	String zkStr;
	private FileSystem fs=null;

	public FailedTask(String confFile) throws IOException,
			InterruptedException, KeeperException {

		init(confFile);
		zk = new ZooKeeper(zkStr, 10000, this);
		signal.await();

	
	}

	private void init(String confFile) throws IOException {

		MyXmlReader reader = new MyXmlReader(confFile);
		zkStr = reader.getName("connectString");
		String hdfsURL = reader.getName("hdfsURL");
		
		
		Configuration conf=  new Configuration();
		conf.set("fs.default.name", hdfsURL);
		
		
		fs = FileSystem.get(conf);
		
	}

	private void displayInfo() throws KeeperException, InterruptedException,
			MalformedURLException {
	

	

		byte [] bytes = zk.getData("/newCproc/URL", false, null);
		String url = new String(bytes, 0, bytes.length);
		// client
		HessianProxyFactory factory = new HessianProxyFactory();

		IRemote remote = (IRemote) factory.create(IRemote.class, url);

		
		
		
		//1.先重命名/smp/failed
		//2.遍历下面的文件
		//读取内容
		//3.添加到failed
		//4.
		//5.
		String newPath = "/smp/failed_"+System.currentTimeMillis();
		
		try {
			fs.rename(new Path("/smp/failed"), new Path(newPath));
			
			
			FileStatus [] fileStatus = fs.listStatus(new Path(newPath));
			for(int i=0;i<fileStatus.length;i++)
			{
				
				List<String> list = new ArrayList<String>(3);
				
				String uuid = fileStatus[i].getPath().getName();
				uuid = uuid.split("_")[1];
				
				
				FSDataInputStream fis = null;
				try {
					fis = fs.open(fileStatus[i].getPath());
					String line  =  null;
					while((line=fis.readLine())!=null)
					{
						
						if(line.startsWith("/smp/cdr"))
						{
							line = line.replaceAll("/", "#");
							list.add(line);
						}
					}
					
					
					System.out.println("task id:"+uuid+",content is:"+list);
					
					remote.addFailedTask(uuid, list);
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					if(fis!=null)
						try {
							fis.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
				}
			
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			fs.delete(new Path(newPath),true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * @param args
	 * @throws KeeperException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException,
			InterruptedException, KeeperException {

		String confFile = args[0];
		//String confFile = "E:\\datacube\\createIndex.xml";
		new Thread(new FailedTask(confFile)).start();

	}

	@Override
	public void process(WatchedEvent event) {
		// TODO Auto-generated method stub
		// EventType type = event.getType();
		if (event.getState() == KeeperState.SyncConnected) {
			signal.countDown();
		}

	}

	@Override
	public void run() {
		while(true)
		{
			
			try {
				displayInfo();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (KeeperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(60*60*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}


}
