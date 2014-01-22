package org.cProc.distributed.zookeeperDispatch.cloudComputingServer;

public interface RequestStateOperater {

	//message: request and index infor . 
	//delete all the index
    public void requestRollBack(String message);	


}
