package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Import;
import util.DBUtil;

public class ImportDao {

	public static ImportDao instance = new ImportDao();

	private ImportDao() {

	}

	public boolean Create(Import newImport) {
		int kq = DBUtil.instance.Create("INSERT INTO Import VALUES (?,?,?,?,?,?)",
				new String[] { 
						Integer.toString(newImport.getSupplier_id()),
						Integer.toString(newImport.getWarehouse_id()),
						Integer.toString(newImport.getEmployee_id()), Integer.toString(newImport.getAmount()),
						newImport.getCreate_at(), (newImport.isActive() == true ? "1" : "0") });
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean Delete(String Selected) {
		String[] sl = { Selected };
		int kq = DBUtil.instance.Create("DELETE Import WHERE id = ?", sl);
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean Edit(String[] Selected) {
		int kq = DBUtil.instance.Create(
				"UPDATE Import SET supplier_id = ?, warehouse_id = ?, employee_id = ?, amount = ?, create_at = ?, active = ? WHERE id = ?",
				Selected);
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public ObservableList<Import> getAllImport(int t, int f) {
		ObservableList<Import> data = FXCollections.observableArrayList();
		ResultSet rs = util.DBUtil.instance
				.Query("SELECT * FROM Import WHERE active =" + t + " OR active =" + f + " ORDER BY id ASC");
		try {
			while (rs.next()) {
				Import dt = new Import();
				dt.setId(rs.getInt("id"));
				dt.setSupplier_id(rs.getInt("supplier_id"));
				dt.setWarehouse_id(rs.getInt("warehouse_id"));
				dt.setEmployee_id(rs.getInt("employee_id"));
				dt.setAmount(rs.getInt("amount"));
				dt.setCreate_at(rs.getString("create_at"));
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

	public ObservableList<Import> searchImport(String item) {
		ObservableList<Import> data = FXCollections.observableArrayList();
		ResultSet rs = util.DBUtil.instance.Query("SELECT * FROM Import WHERE id LIKE N'%" + item + "%'");
		try {
			while (rs.next()) {
				Import dt = new Import();
				dt.setId(rs.getInt("id"));
				dt.setSupplier_id(rs.getInt("supplier_id"));
				dt.setWarehouse_id(rs.getInt("warehouse_id"));
				dt.setEmployee_id(rs.getInt("employee_id"));
				dt.setAmount(rs.getInt("amount"));
				dt.setCreate_at(rs.getString("create_at"));
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

	public Boolean checkDouble(String[] item) {
		ResultSet rs = util.DBUtil.instance.Query("SELECT name FROM Import WHERE name = ?", item);
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
