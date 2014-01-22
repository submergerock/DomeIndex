package org.cProc.log.createIndexLog;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class TransLogMessage {
//
//	public  static final String subContentSeprater ="#";
//	private static final String taskNumberSeperater ="!-_-!";
//	
//	public  static final  int  startTaskInt = 1;
//	private static final String startTaskType = "01";
//	private  static final String startTask ="startTask :";
//	
//	public static final  int  endTaskInt = 2;
//	private static final String endTaskType = "02";
//	private static final String endTask ="endTask :";
//	
//
//	public static final  int  inforMessageInt = 3;
//	private static final String inforMessageType="03";
//	private static final String inforMessage="inforMessage :";
//	
//	public static final  int startLogFileTailInforInt  =4;
//	private static final String startLogFileTailInforType="04";
//	private static final String startLogFileTailInfor="startLogFileTailInfor :";
//	
//	public static final  int endLogFileTailInforInt  =5;
//	private static final String endLogFileTailInforType="05";
//	private static final String endLogFileTailInfor="endLogFileTailInfor :";
//	
//	
//	
//	private  static final  String  format ="yyyy-MM-dd HH:mm:ss";
//	private  static final	SimpleDateFormat df=new SimpleDateFormat(format);
//	private static final   String  WHITESPACE_STRING=" ";
//	
//	
//	//��ʽΪ�� ����+��  ��+������Ϣ+taskNumber(#subTaskNumber)+ʱ��
//	public static String startLogFileTail()
//	{
//		return  df.format(Calendar.getInstance().getTime())+WHITESPACE_STRING+ startLogFileTailInforType+"     "+startLogFileTailInfor ;
//	}
//	
//	public static String endLogFileTail()
//	{
//		return  df.format(Calendar.getInstance().getTime())+WHITESPACE_STRING+endLogFileTailInforType+"     "+endLogFileTailInfor;
//	}
//	
//	public  static String startTask(String taskNumber, String request)
//	{
//		return  df.format(Calendar.getInstance().getTime())+WHITESPACE_STRING+startTaskType+"     "+startTask +taskNumberSeperater+taskNumber+taskNumberSeperater+"    " + request;
//	}
//	
//	public static String endTask(String taskNumber)
//	{
//		return   df.format(Calendar.getInstance().getTime())+WHITESPACE_STRING+endTaskType+"    "+endTask +taskNumberSeperater+taskNumber;
//	}
//	
//	
//	public static String inforMeesageForTask(String taskNumber ,String message)
//	{
//		return   df.format(Calendar.getInstance().getTime())+WHITESPACE_STRING+inforMessageType+"   "+ inforMessage +taskNumberSeperater+taskNumber+taskNumberSeperater+"    " + message;
//	}
//	
//	
//	private static String getTaskNumberInfor(String message)
//	{
//		 int start_pos = message.indexOf(taskNumberSeperater);
//		 int end_pos = message.indexOf(taskNumberSeperater, start_pos+taskNumberSeperater.length());
//		 if(end_pos != -1)
//		 {
//		 String number =message.substring(start_pos+taskNumberSeperater.length(), end_pos);
//		 return number;
//		 }
//		 else {
//			 String number =message.substring(start_pos+taskNumberSeperater.length());
//			 return number;
//		}
//		
//	}
//	
//	private static String getMessageInfor(String line)
//	{
//		 int start_pos = line.indexOf(taskNumberSeperater);
//		 int end_pos = line.indexOf(taskNumberSeperater, start_pos+taskNumberSeperater.length());
//		 return line.substring(end_pos+taskNumberSeperater.length()).trim();
//	}
//	
//	public static boolean getMessageInfor(String line , MessageInfor messageInfor)
//	{
//		line = line.substring(format.length()+1);
//		String type = line.substring(0, 2);
//		try {
//			messageInfor.messageType =	Integer.parseInt(type);
//		} catch (Exception e) {
//			messageInfor.message="";
//			messageInfor.taskNumber =  -1  ;
//			return false;
//		}
//		switch(messageInfor.messageType)
//		{		
//			case  startTaskInt:
//				messageInfor.message=getMessageInfor(line);
//				messageInfor.taskNumber =  Integer.parseInt(getTaskNumberInfor(line))  ;
//				break;
//			case  endTaskInt:
//				messageInfor.message=endTask;
//				messageInfor.taskNumber =    Integer.parseInt(getTaskNumberInfor(line)) ;
//				break;
//			case startLogFileTailInforInt:
//				break;
//			case  endLogFileTailInforInt:
//				break;
//			case  inforMessageInt:
//				messageInfor.message=getMessageInfor(line);
//				messageInfor.taskNumber =  Integer.parseInt(getTaskNumberInfor(line))  ;
//				break;
////			case  inforSubMessageInt:
////				messageInfor.message=getMessageInfor(line);;
////				taskNumberString = getTaskNumberInfor(line);
////				numberArray = taskNumberString.split(subContentSeprater);
////				messageInfor.taskNumber =  Integer.parseInt(numberArray[0])  ;
////				messageInfor.subTaskNumber =  Integer.parseInt(numberArray[1]) ;
////				break;
////			case subTaskInForInt:
////				messageInfor.message=getMessageInfor(line);
////				taskNumberString = getTaskNumberInfor(line);
////				numberArray = taskNumberString.split(subContentSeprater);
////				messageInfor.taskNumber =  Integer.parseInt(numberArray[0])  ;
////				messageInfor.subTaskNumber =  Integer.parseInt(numberArray[1]) ;
////				break;
//			default:
//				messageInfor.message="";
//				messageInfor.taskNumber =  -1  ;
//				return false;
//		}
//		return true;
//	}
//	
//
//	
//	public static void main(String[] args)
//	{
//		MessageInfor messageInfor = new MessageInfor();
//		messageInfor.parseLogLine(endLogFileTail());
////		 String task = inforMeesageForTask(3333,"yxxxxxxxxxxxxxxxxxxxxxxxxxxxxxy");
////		 int start_pos = task.indexOf(taskNumberSeperater);
////		 int end_pos = task.indexOf(taskNumberSeperater, start_pos+taskNumberSeperater.length());
////		 String number =task.substring(start_pos+taskNumberSeperater.length(), end_pos);
//		System.out.println("message type  is "+messageInfor.messageType);
//		System.out.println("taskNumber is "+messageInfor.taskNumber);
//		System.out.println("message is "+messageInfor.message);
//		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//		System.out.println( df.format(Calendar.getInstance().getTime()));
//	}
//	
//
//	
//
//	
}
