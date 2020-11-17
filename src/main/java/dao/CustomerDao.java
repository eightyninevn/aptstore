package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import util.DBUtil;

public class CustomerDao {

	public static CustomerDao instance = new CustomerDao();

	private CustomerDao() {

	}

	public boolean Create(Customer newCustomer) {
		int kq = DBUtil.instance.Create("INSERT INTO Customer VALUES (?,?,?,?,?)",
				new String[] { newCustomer.getName(), newCustomer.getPhone(), newCustomer.getEmail(),newCustomer.getAddress(), (newCustomer.isActive() == true ? "1" : "0") });
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean Delete(String Selected) {
		String[] sl = { Selected };
		int kq = DBUtil.instance.Create("DELETE Customer WHERE id = ?", sl);
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean Edit(String[] Selected) {
		int kq = DBUtil.instance.Create("UPDATE Customer SET name = ?, phone = ?, email = ?, address = ?, active = ? WHERE id = ?", Selected);
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public ObservableList<Customer> getAllCustomer(int t, int f) {
		ObservableList<Customer> data = FXCollections.observableArrayList();
		ResultSet rs = util.DBUtil.instance.Query("SELECT * FROM Customer WHERE active =" + t +" OR active ="+ f + " ORDER BY id ASC");
		try {
			while (rs.next()) {
				Customer dt = new Customer();
				dt.setId(rs.getInt("id"));
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

	public ObservableList<Customer> searchCustomer(String item) {
		ObservableList<Customer> data = FXCollections.observableArrayList();
		ResultSet rs = util.DBUtil.instance.Query("SELECT * FROM Customer WHERE name LIKE N'%" + item + "%'");
		try {
			while (rs.next()) {
				Customer dt = new Customer();
				dt.setId(rs.getInt(1));
				dt.setName(rs.getNString(2));
				dt.setPhone(rs.getString(3));
				dt.setEmail(rs.getString(4));
				dt.setAddress(rs.getString(5));
				dt.setActive(rs.getBoolean(6));
				data.add(dt);
			}
			rs.close();

		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return data;

	}

	public Boolean checkDouble(String[] item) {
		ResultSet rs = util.DBUtil.instance.Query("SELECT name FROM Customer WHERE name = ?", item);
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
