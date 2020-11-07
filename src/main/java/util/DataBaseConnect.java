package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnect {
	@SuppressWarnings("exports")
	public static Connection openConnection() {
		String sqlserver = "jdbc:sqlserver://localhost;", databasename = "databasename=APTStore;",
				user = "user=sa;", password = "password=12345";
		String url = sqlserver + databasename + user + password;
		
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
			if (conn != null) {
				System.out.println("Successful");
				return conn;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("NOsuccessful");
		}
		return conn;
	}

	
	

	
//end class	
}
