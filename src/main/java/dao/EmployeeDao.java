package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Employee;
import util.DBUtil;

public class EmployeeDao {

	public static EmployeeDao instance = new EmployeeDao();

	private EmployeeDao() {

	}

	public boolean Create(Employee newEmployee) {
		int kq = DBUtil.instance.Create("INSERT INTO Employee VALUES (?,?,?,?,?,?,?,?)",
				new String[] { newEmployee.getName(), (newEmployee.isGender() == true ? "1" : "0"),
						newEmployee.getBirthday().toString(), Integer.toString(newEmployee.getPosition_id()),
						newEmployee.getPhone(), newEmployee.getEmail(), newEmployee.getAddress(),
						(newEmployee.isActive() == true ? "1" : "0") });
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean Delete(String Selected) {
		String[] sl = { Selected };
		int kq = DBUtil.instance.Create("DELETE Employee WHERE id = ?", sl);
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean Edit(String[] Selected) {
		int kq = DBUtil.instance.Create(
				"UPDATE Employee SET name = ?, gender = ?, birthday = ?, position_id = ?, phone = ?, email = ?, address = ?, active = ? WHERE id = ?", Selected);
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public ObservableList<Employee> getAllEmployee(int t, int f) {
		ObservableList<Employee> data = FXCollections.observableArrayList();
		ResultSet rs = util.DBUtil.instance.Query("SELECT * FROM Employee WHERE active =" + t +" OR active ="+ f + " ORDER BY id ASC");
		try {
			while (rs.next()) {
				Employee dt = new Employee();
				dt.setId(rs.getInt("id"));
				dt.setName(rs.getNString("name"));
				dt.setGender(rs.getBoolean("gender"));
				dt.setBirthday(rs.getString("birthday"));
				dt.setPosition_id(rs.getInt("position_id"));
				dt.setName(rs.getNString("name"));
				dt.setPhone(rs.getString("phone"));
				dt.setEmail(rs.getString("email"));
				dt.setAddress(rs.getNString("address"));
				dt.setActive(rs.getBoolean("active"));
				data.add(dt);
			}

			rs.close();

		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return data;
	}

	public ObservableList<Employee> searchEmployee(String item) {
		ObservableList<Employee> data = FXCollections.observableArrayList();
		ResultSet rs = util.DBUtil.instance.Query("SELECT * FROM Employee WHERE name LIKE N'%" + item + "%'");
		try {
			while (rs.next()) {
				Employee dt = new Employee();
				dt.setId(rs.getInt("id"));
				dt.setName(rs.getNString("name"));
				dt.setGender(rs.getBoolean("gender"));
				dt.setBirthday(rs.getString("birthday"));
				dt.setPosition_id(rs.getInt("position_id"));
				dt.setName(rs.getNString("name"));
				dt.setPhone(rs.getString("phone"));
				dt.setEmail(rs.getString("email"));
				dt.setAddress(rs.getNString("address"));
				dt.setActive(rs.getBoolean("active"));
				data.add(dt);;
			}
			rs.close();

		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return data;

	}

	public Boolean checkDouble(String[] item) {
		ResultSet rs = util.DBUtil.instance.Query("SELECT name FROM Employee WHERE name = ?", item);
		try {
			if (rs.next() == false) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
