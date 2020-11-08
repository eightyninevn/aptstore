package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class DBUtil {

	public static DBUtil instance = new DBUtil();
	private String connString = "jdbc:sqlserver://localhost;databaseName=DoctorSchedule;user=sa;password=duc12345";
	
	private DBUtil() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public ResultSet Query(String StrQuery) {
		Statement stmt;
//		try (Connection conn = DriverManager.getConnection(connString);)
		try {
			Connection conn = DriverManager.getConnection(connString);
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(StrQuery);
			return rs;
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}	
	}
	
	public ResultSet Query (String StrQuery, String[] params) {
		PreparedStatement stmt;
//		try (Connection conn = DriverManager.getConnection(connString);)
		try {
			Connection conn = DriverManager.getConnection(connString);
			stmt = conn.prepareStatement(StrQuery);
			for (int i = 0; i < params.length; i++) {
				stmt.setString(i+1, params[i]);
			}
			ResultSet rs = stmt.executeQuery();
			return rs;
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	public int Create(String Query, String[] params) {
		try (Connection conn = DriverManager.getConnection(connString); PreparedStatement stmt = conn.prepareStatement(Query)) {
			for (int i = 0; i < params.length; i++) {
				stmt.setString(i+1, params[i]);				
			}
			int rs = stmt.executeUpdate();
			return rs;	
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return -1;
	}
	
}
