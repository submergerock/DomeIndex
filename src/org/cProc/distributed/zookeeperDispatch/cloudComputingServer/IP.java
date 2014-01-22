package org.cProc.distributed.zookeeperDispatch.cloudComputingServer;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

import org.cProc.distributed.createIndex.Config;

public class IP {

	
private static String ip = null;
public static String getIP()
{
	
	if(ip==null){
		ip =  getLocalIP();
		if(ip.equals("0.0.0.0")){
			ip = UUID.randomUUID().toString();
		}
		System.out.println(" my ip is :"+ip);
	}
	return ip;
}




private static String getLocalIP() {
	String ip = "";
	try {
		Enumeration e1 = NetworkInterface.getNetworkInterfaces();
		while (e1.hasMoreElements()) {
			NetworkInterface ni = (NetworkInterface) e1.nextElement();

			if (!(ni.getName().equals(Config.IP)))
				continue;
			Enumeration e2 = ni.getInetAddresses();
			while (e2.hasMoreElements()) {
				InetAddress ia = (InetAddress) e2.nextElement();
				if (ia instanceof Inet6Address)
					continue;
				ip = ia.getHostAddress();
			}
		}
	} catch (SocketException e) {
		e.printStackTrace();
		//System.exit(-1);
	}
	if("".equals(ip))
		return "0.0.0.0";
	return ip;
}
static public void main(String[] args) {
	for(int i=0;i<=100;i++)
		System.out.println(getIP());
}

}

