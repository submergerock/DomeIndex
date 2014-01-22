package org.cProc.GernralRead.ReadData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLDao {
	
	private  Connection conn;
	boolean isConn = false;
	int 	      commitNumber;
	private PreparedStatement ps ;
	String tableName = "";

	
	public  MySQLDao()
	{

        String driver = "com.mysql.jdbc.Driver";

        String url = "jdbc:mysql://127.0.0.1/test?rewriteBatchedStatements=true";

        String user = "root"; 
        String password = "gyy";

        try { 
         Class.forName(driver);
         System.out.println("begin connecting to the Database!");
          conn = DriverManager.getConnection(url, user, password);
      
         if(!conn.isClosed()) 
         {
          System.out.println("Succeeded connecting to the Database!");
          isConn = true;
         }
  
        }
        catch (Exception e) {
			e.printStackTrace();
		}
}
	
	public PreparedStatement getPreparedStatement(String insetSQL)
	{
		if(ps == null)
		{
			try {
				   conn.setAutoCommit(false);
				   ps =  conn.prepareStatement( insetSQL);
			} catch (SQLException e) {
				e.printStackTrace();
			} 
		}
		return ps ;
	}
	
	public void  addDataIntoStoreTable( )
	{

		if(isConn)
		{
			commitNumber++;
			  try {  
			    ps.addBatch();
				if( commitNumber > 1000)
				{
					commitNumber= 0;
					ps.executeBatch();
					conn.commit();
					ps.clearBatch();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//close;
	public void close()
	{
		try {
			if(ps != null)
			{
				ps.executeBatch();
				ps.close();
				conn.commit();
			}
			if(conn != null && !conn.isClosed())
			{
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static  void main(String args[])
	{
		
		MySQLDao dao = new MySQLDao(); 
		String insetSQL ="insert into memoryTest values( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement ps = dao.getPreparedStatement(insetSQL);
		long startTime = System.currentTimeMillis();
		int count = 0 ;
		for(int i = 0 ; i < 50000; i++)
		{
			count++;
			System.out.println("count number is " + count);
			try {
				ps.setInt(1, i);
				ps.setInt(2, i);
				ps.setInt(3, i);
				ps.setInt(4, i);
				ps.setInt(5, i);
				ps.setInt(6, i);
				ps.setInt(7, i);
				ps.setInt(8, i);
				ps.setInt(9, i);
				ps.setInt(10, i);
				ps.setInt(11, i);
				ps.setInt(12, i);
				ps.setInt(13, i);
				ps.setInt(14, i);
				ps.setInt(15, i);
				ps.setInt(16, i);
				ps.setInt(17, i);
				ps.setInt(18, i);
				ps.setInt(19, i);
				ps.setInt(20, i);
				ps.setInt(21, i);
				ps.setInt(22, i);
				ps.setInt(23, i);
				ps.setInt(24, i);
				ps.setInt(25, i);
				ps.setInt(26, i);
				ps.setInt(27, i);
				ps.setInt(28, i);
				ps.setInt(29, i);
				ps.setInt(30, i);
				ps.setInt(31, i);
				ps.setInt(32, i);
				ps.setInt(33, i);
				ps.setInt(34, i);
				ps.setInt(35, i);
				ps.setInt(36, i);
				ps.setInt(37, i);
				ps.setInt(38, i);
				dao.addDataIntoStoreTable();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		dao.close();
		long endTime = System.currentTimeMillis();
		System.out.println("time is " + (endTime - startTime));
	}
}
