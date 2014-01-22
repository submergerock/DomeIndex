package org.cProc.log.loganalyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.cProc.log.createIndexLog.IndexThreadInfor;
import org.cProc.log.createIndexLog.MessageInfor;
import org.cProc.log.createIndexLog.TransLogMessage;
import org.cProc.log.indexmapinfor.IndexMapInfor;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;




public class logContent {
//	private Set<Long> taskSet = new TreeSet<Long>(); 
//	private HashMap<Long, String> taskMap = new HashMap<Long, String>();
//	private HashMap<Long,IndexMapInfor> taskInfor = new  HashMap<Long, IndexMapInfor>();
//	private ArrayList<Long> whenLogEndTaskIsRunning = new ArrayList<Long>();
//	private boolean startTailLog = false;
//	private IndexThreadInfor indexInfor = null;
//	private UpdateIndexMapInfor update = null;
//	
//	public logContent(IndexThreadInfor indexInfor)
//	{
//		this.indexInfor = indexInfor;
//	}
//	
//	public void resetContent()
//	{
//		taskSet.clear();
//		taskMap.clear();
//		startTailLog = false;
//	}
//	
//	public void setUpdateIndexMapInfor(UpdateIndexMapInfor update)
//	{
//			this.update = update;
//	}
//	
//	
//	public void showUnDoInfor()
//	{
//		//check task which is not do;
//		Iterator<Entry<Long, String>> iteratorUndoTask = taskMap.entrySet().iterator();
//		while(iteratorUndoTask.hasNext())
//		{
//			Entry<Long, String> entry = iteratorUndoTask.next();
//			//if the taks is uncomplete in unexcpetion;
//			if(whenLogEndTaskIsRunning.indexOf(entry.getKey()) == -1 )
//			{
//				//delete the files this task upload;
//				IndexMapInfor infor = taskInfor.get(entry.getKey());
//				ArrayList<String> indexInfor = new ArrayList<String>();
//			   Iterator<Entry<String, String>> iterator=	infor.getIndexVerionMap().entrySet().iterator();
//			   while (iterator.hasNext()) {
//				   indexInfor.add( iterator.next().getKey());
//				}
//			   update.undoIndexMap(indexInfor);
//			}
//		}
//
//	}
//	
//	
//	public void addContent(MessageInfor infor)
//	{
//		switch(infor.messageType)
//		{		
//			case   TransLogMessage.startLogFileTailInforInt:
//				startTailLog = true;
//				break;
//			case   TransLogMessage.endLogFileTailInforInt:
//				startTailLog = false;
//				showUnDoInfor();
//				break;
//			case  TransLogMessage.startTaskInt:
//				taskSet.add(infor.taskNumber);
//				taskMap.put(infor.taskNumber, infor.message);
//				break;
//			case  TransLogMessage.endTaskInt:
//				//update all the indexInfor to ZK;
//				IndexMapInfor mapInfor = taskInfor.get(infor.taskNumber);
//				String message = "";
//				if( mapInfor != null )
//				{
//					 message = IndexMapInfor.createIndexInfor(taskInfor.get(infor.taskNumber));
//					 update.addNewIndexMap( message  );
//				}			
//				taskSet.remove(infor.taskNumber);
//				taskMap.remove(infor.taskNumber);
//				taskInfor.remove(infor.taskNumber);
//				break;
//				//for startLogFileHeadInforInt , startLogFileTailInforInt
//			case  TransLogMessage.inforMessageInt:   
//				//tail infor: get which tasks is in the threads, 
//				//check now  tasks which threads are dealing with 
//				 if(startTailLog) {
//					 whenLogEndTaskIsRunning.add(Long.parseLong(infor.message));
//				}
//				//this infor is for pepele to read ,or for the database use ;
//				else {
//					IndexMapInfor indexMap =  IndexMapInfor.getIndexNameFromMessage(infor.message);
//					if(indexMap != null )
//					{
//						taskInfor.put(infor.taskNumber, indexMap);
//					}
//				}
//				break;
//			default:	
//		}
//	}
//
}
