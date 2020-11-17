package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Supplier;
import util.DBUtil;

public class SupplierDao {

	public static SupplierDao instance = new SupplierDao();

	private SupplierDao() {

	}

	public boolean Create(Supplier newSupplier) {
		int kq = DBUtil.instance.Create("INSERT INTO Supplier VALUES (?,?,?,?,?)",
				new String[] { newSupplier.getName(), newSupplier.getPhone(), newSupplier.getEmail(),newSupplier.getAddress(), (newSupplier.isActive() == true ? "1" : "0") });
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean Delete(String Selected) {
		String[] sl = { Selected };
		int kq = DBUtil.instance.Create("DELETE Supplier WHERE id = ?", sl);
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean Edit(String[] Selected) {
		int kq = DBUtil.instance.Create("UPDATE Supplier SET name = ?, phone = ?, email = ?, address = ?, active = ? WHERE id = ?", Selected);
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public ObservableList<Supplier> getAllSupplier(int t,int f) {
		ObservableList<Supplier> data = FXCollections.observableArrayList();
		ResultSet rs = util.DBUtil.instance.Query("SELECT * FROM Supplier WHERE active =" + t +" OR active ="+ f + " ORDER BY id ASC");
		try {
			while (rs.next()) {
				Supplier dt = new Supplier();
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

	public ObservableList<Supplier> searchSupplier(String item) {
		ObservableList<Supplier> data = FXCollections.observableArrayList();
		ResultSet rs = util.DBUtil.instance.Query("SELECT * FROM Supplier WHERE name LIKE N'%" + item + "%'");
		try {
			while (rs.next()) {
				Supplier dt = new Supplier();
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
		ResultSet rs = util.DBUtil.instance.Query("SELECT name FROM Supplier WHERE name = ?", item);
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
