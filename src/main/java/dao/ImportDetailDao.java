package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.ImportDetail;
import util.DBUtil;

public class ImportDetailDao {

	public static ImportDetailDao instance = new ImportDetailDao();

	private ImportDetailDao() {

	}

	public boolean Create(ImportDetail newImportDetail) {
		int kq = DBUtil.instance.Create("INSERT INTO ImportDetail VALUES (?,?,?,?,?,?)", new String[] {
				Integer.toString(newImportDetail.getImport_id()), Integer.toString(newImportDetail.getProduct_id()),
				Integer.toString(newImportDetail.getQuantity()), Integer.toString(newImportDetail.getPrice()),
				Integer.toString(newImportDetail.getTotal()), (newImportDetail.isActive() == true ? "1" : "0") });
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean Delete(String Selected) {
		String[] sl = { Selected };
		int kq = DBUtil.instance.Create("DELETE ImportDetail WHERE id = ?", sl);
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean Edit(String[] Selected) {
		int kq = DBUtil.instance.Create(
				"UPDATE ImportDetail SET import_id = ?, product_id = ?, quantity = ?, price = ?, total = ?, active = ? WHERE id = ?",
				Selected);
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public ObservableList<ImportDetail> getAllImportDetail(int t, int f) {
		ObservableList<ImportDetail> data = FXCollections.observableArrayList();
		ResultSet rs = util.DBUtil.instance
				.Query("SELECT * FROM ImportDetail WHERE active =" + t + " OR active =" + f + " ORDER BY id ASC");
		try {
			while (rs.next()) {
				ImportDetail dt = new ImportDetail();
				dt.setId(rs.getInt("id"));
				dt.setImport_id(rs.getInt("import_id"));
				dt.setProduct_id(rs.getInt("product_id"));
				dt.setQuantity(rs.getInt("quantity"));
				dt.setPrice(rs.getInt("price"));
				dt.setTotal(rs.getInt("total"));
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

	public ObservableList<ImportDetail> searchImportDetail(String item) {
		ObservableList<ImportDetail> data = FXCollections.observableArrayList();
		ResultSet rs = util.DBUtil.instance.Query("SELECT * FROM ImportDetail WHERE name LIKE N'%" + item + "%'");
		try {
			while (rs.next()) {
				ImportDetail dt = new ImportDetail();
				dt.setId(rs.getInt("id"));
				dt.setImport_id(rs.getInt("import_id"));
				dt.setProduct_id(rs.getInt("product_id"));
				dt.setQuantity(rs.getInt("quantity"));
				dt.setPrice(rs.getInt("price"));
				dt.setTotal(rs.getInt("total"));
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
		ResultSet rs = util.DBUtil.instance.Query("SELECT name FROM ImportDetail WHERE code = ?", item);
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
