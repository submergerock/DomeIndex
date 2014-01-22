package org.cProc.sql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class LikeMatchOperator {
	
	//�� A  like 1? ���  A in ��10,11,12,13,14,15,16,17,18,19)
	public static final char point ='?';
	public static final char comma  =',';
	public static final char star  ='*';
	public static final String OROPERATOR=" or ";
	public static final String ANDOPERATOR=" and ";
	public static final String LeftParent=" ( ";
	public static final String RightParent=" ) ";
	public static final String LessAndEqual=" <= ";
	public static final String MoreAndEqual=" >= ";

	public static final String CONFPATH="/home/cw/likeStar.txt";
	
	public static String repacleLike(String input)
	{
		// String test =" select * from A where ( B ==1  and  D like 23* ) or  K == 5";
		 if(input.matches(".+ like .*") )
		 {	
			 int likeStart = 0;
			 int likeEnd = 0;
			 int likePos = input.indexOf(" like ");

			 String likeBefore = input.substring(0, likePos).trim();
			 String likeAfter =input.substring(likePos+" like ".length(), input.length()).trim();
			 StringBuffer sb = new StringBuffer(likeBefore);
			 int likeColumnPos = sb.lastIndexOf(" ");		 
			 likeStart = likeColumnPos;
			 StringBuffer sb2 = new StringBuffer(likeAfter);
			 int likeConditonPos = sb2.indexOf(" ");
			 if( likeConditonPos == -1)
			 {
				 likeEnd  =likeStart +sb2.length()+" like ".length();
			 }
			 else
			 {
				 likeEnd  = input.indexOf(sb2.substring(0, likeConditonPos))+sb2.substring(0, likeConditonPos).length();
			 }
			 StringBuffer reslut =  new StringBuffer(input);
		
			 if( input.matches(".+\\?.*") )
			 {
				 String temp = reslut.substring(likeStart,likeEnd);
				 reslut.delete(likeStart,likeEnd) ;
				 reslut.insert(likeStart, " "+ LikeMatchOperator.repalcePoint(temp)+" ");
			 }
			 else if(input.matches(".+\\*.*") )
			 {
				 String temp = reslut.substring(likeStart,likeEnd);
				 reslut.delete(likeStart,likeEnd) ;
				 reslut.insert(likeStart, " "+LikeMatchOperator.repalceStar(temp)+" ");
			 }
			 return reslut.toString();
		 }
		   
		 
		 return input;
	}
	
	public static String  repalcePoint(String input)
	{
		 String operator = " like ";
		 String operatormathc = ".+ like .*";
		 String pointoperatormathc = ".+\\?.*";
		 if(input.matches(operatormathc) && input.matches(pointoperatormathc))
		 {
			 StringBuffer reslut = new StringBuffer(input.subSequence(0,  input.indexOf(operator)));
			 reslut.append(" in ( ");
		
			 StringBuffer after = new StringBuffer( (input.substring( input.indexOf(operator)+operator.length(),input.length()).trim() ));

			 ArrayList<String> allList = repalceAllPoint(after);
			for(int i= 0 ; i < allList.size() ; i++)
			{
				 reslut.append(allList.get(i)+" "+ comma);
			}
			reslut.deleteCharAt(reslut.length()-1);
			 reslut .append(" ) ");
			 return reslut.toString();
		 }
		 else {
			return input;
		}
	}
	
	public static ArrayList<String> repalceAllPoint(StringBuffer condtion)
	{
		if(condtion.length() == 0)
		{
			return null;
		}
		if(condtion.length() == 1)
		{
			 ArrayList<String> reslutList  = new ArrayList<String>();
			if(condtion.charAt(0) == point)
			{
				 for( int i = 0 ; i < 10 ; i++)
				 {
					 reslutList.add(i+"");
				 }
				 return  reslutList;
			}
			reslutList.add(condtion.toString());
			return reslutList;
		}
		
		StringBuffer preBuffer = new StringBuffer();
		for( int i = 0 ; i < condtion.length() ; i++)
		{
			if( condtion.charAt(i) ==  point)
			{
				 StringBuffer reslut = new StringBuffer(condtion.substring(i+1, condtion.length()));
				 ArrayList<String> tempreslutList = repalceAllPoint( reslut);
				 ArrayList<String> reslutList  = new ArrayList<String>();
				 if( null == tempreslutList )
				 {
					 for(int k = 0 ; k < 10 ; k++)
					 {
							 reslutList.add(preBuffer.toString()+k);
					 }
					 return reslutList;
				 }
				 for(int k = 0 ; k < 10 ; k++)
				 {
					 for( int m = 0 ; m < tempreslutList.size() ; m ++)
					 {
						 reslutList.add(preBuffer.toString()+k+tempreslutList.get(m));
					 }
				 }
				 return reslutList;
			}
			preBuffer.append(condtion.charAt(i));
		}
		return null;
	}
	
	
	public static String repalceStar(String input)
	{
		 String operator = " like ";
		 String operatormathc = ".+ like .*";
		 String staroperatormathc = ".+\\*.*";
	
		 if(input.matches(operatormathc) && input.matches(staroperatormathc) )
		 {
			 File file =new File(CONFPATH);
			 String maxLengthString = "";
			 int maxLeng = 0;
			if(file.exists())
			{
				BufferedReader   in = null;
				 try {
					 in=new   BufferedReader(new FileReader(file));
					String oneLine =  in.readLine();
					while( oneLine  != null)
					{
						StringBuffer confString = new StringBuffer(oneLine);
						StringBuffer reslut = new StringBuffer(LeftParent);
						StringBuffer columnName = new StringBuffer(
								input.subSequence(0, input.indexOf(operator)));

						String after = (input.substring(input.indexOf(operator)
								+ operator.length(), input.length())).trim();
						String preString = confString.substring(0,
								confString.indexOf("*")).trim();
						//匹配上了
						if (after.startsWith(preString)) {
							int starPos = after.indexOf('*');
							String beforeStarString = after.substring(0,
									starPos);
							String lengthString = confString.substring(
									confString.indexOf(":") + 1,
									confString.length());
							String[] lengthList = lengthString.split(",");
							boolean  replace = false;
							for (int i = 0; i < lengthList.length; i++) {
								int length = Integer.parseInt(lengthList[i])
										- beforeStarString.length();
								if (length >= 0) {
									replace = true;
									StringBuffer startBuffer = new StringBuffer(
											beforeStarString);
									StringBuffer endBuffer = new StringBuffer(
											beforeStarString);
									for (int m = 0; m < length; m++) {
										startBuffer.append('0');
										endBuffer.append('9');
									}
									reslut.append(LeftParent);
									reslut.append(columnName + MoreAndEqual 
											+ startBuffer);
									reslut.append(ANDOPERATOR);
									reslut.append(columnName + LessAndEqual
											+ endBuffer);
									reslut.append(RightParent);
									reslut.append(OROPERATOR);
								}
							}
							if(replace)
							{
							reslut.delete(
									reslut.length() - OROPERATOR.length(),
									reslut.length());
							reslut.append(RightParent);
							}
							else {
								reslut.append(LeftParent);
								reslut.append(columnName + LessAndEqual
										+ after.substring(0 , after.length() -1));
								reslut.append(ANDOPERATOR);
								reslut.append(columnName + MoreAndEqual
										+  after.substring(0 , after.length() -1));
								reslut.append(RightParent);
								reslut.append(RightParent);
							}
							return reslut.toString();
						}
						oneLine = in.readLine();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				 finally {
									if (null != in) {
										try {
											in.close();
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								}
			}
			return input;
		 }
		 else {
			return input;
		}
	}
	public static void  main(String[] args)
	{
		
	}
	

}
