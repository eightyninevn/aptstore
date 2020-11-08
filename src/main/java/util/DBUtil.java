package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import javax.swing.JComboBox;

import model.Account;
import model.Area;
import model.Department;
import model.Position;
import model.Specialize;
import model.Status;
import model.WorkShift;
import modelView.EmployeeView;
import modelView.RoomView;

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
	
	public void Rendercbox_Area(LinkedHashSet<Area> data, JComboBox cbox) {
		for (Area area : data) {
			cbox.addItem(area.getName());
		}
	}
	
	public void Rendercbox_Department (LinkedHashSet<Department> data, JComboBox cbox) {
		for (Department dt : data) {
			cbox.addItem(dt.getName());
		}
	}
	
	
	public void Rendercbox_Position (LinkedHashSet<Position> data, JComboBox cbPosition) {
		for (Position dt : data) {
			cbPosition.addItem(dt.getName());
		}
	}
	
	public void Rendercbox_Specialize (LinkedHashSet<Specialize> data, JComboBox cbSpecialize) {
		for (Specialize dt : data) {
			cbSpecialize.addItem(dt.getName());
		}
	}
	
	public void Rendercbox_Account (LinkedHashSet<Account> data, JComboBox cbAccount) {
		for (Account dt : data) {
			cbAccount.addItem(dt.getName());
		}
	}
	
	public void Rendercbox_Status (LinkedHashSet<Status> data, JComboBox cbStatus) {
		for (Status dt : data) {
			cbStatus.addItem(dt.getName());
		}
	}
	
	public void Rendercbox_EmployeeView (LinkedHashSet<EmployeeView> data, JComboBox cbEmployeeView) {
		for (EmployeeView dt : data) {
			cbEmployeeView.addItem(dt.getName()+"  -  "+dt.getNamePosition()+ "  -  "+dt.getNameDepartment());
		}
	}
	
	public void Rendercbox_RoomView (LinkedHashSet<RoomView> data, JComboBox cbRoomView) {
		for (RoomView dt : data) {
			cbRoomView.addItem(dt.getName());
		}
	}
	
	public void Rendercbox_WorkShift (LinkedHashSet<WorkShift> data, JComboBox cbWorkShift) {
		for (WorkShift dt : data) {
			cbWorkShift.addItem(dt.getName()+":  "+dt.getTime());
		}
	}

	
//	public <T> void Rendercbox (LinkedHashSet<T> data, JComboBox cbox) {
//		for (T dt : data) {
//			cbox.addItem(dt.getName());
//		}
//	}
	
}
