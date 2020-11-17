package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Product;
import util.DBUtil;

public class ProductDao {

	public static ProductDao instance = new ProductDao();

	private ProductDao() {

	}

	public boolean Create(Product newProduct) {
		int kq = DBUtil.instance.Create("INSERT INTO Product VALUES (?,?,?,?,?,?)",
				new String[] { Integer.toString(newProduct.getCategory_id()), Integer.toString(newProduct.getUnit_id()),
						newProduct.getName(), newProduct.getDescription(), Integer.toString(newProduct.getPrice_sell()),
						(newProduct.isActive() == true ? "1" : "0") });
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean Delete(String Selected) {
		String[] sl = { Selected };
		int kq = DBUtil.instance.Create("DELETE Product WHERE id = ?", sl);
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean Edit(String[] Selected) {
		int kq = DBUtil.instance.Create(
				"UPDATE Product SET category_id = ?, unit_id = ?, name = ?, description = ?, price_sell = ?, active = ? WHERE id = ?",
				Selected);
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public ObservableList<Product> getAllProduct(int t, int f) {
		ObservableList<Product> data = FXCollections.observableArrayList();
		ResultSet rs = util.DBUtil.instance
				.Query("SELECT * FROM Product WHERE active =" + t + " OR active =" + f + " ORDER BY id ASC");
		try {
			while (rs.next()) {
				Product dt = new Product();
				dt.setId(rs.getInt("id"));
				dt.setCategory_id(rs.getInt("category_id"));
				dt.setUnit_id(rs.getInt("unit_id"));
				dt.setName(rs.getNString("name"));
				dt.setDescription(rs.getNString("description"));
				dt.setPrice_sell(rs.getInt("price_sell"));
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

	public ObservableList<Product> searchProduct(String item) {
		ObservableList<Product> data = FXCollections.observableArrayList();
		ResultSet rs = util.DBUtil.instance.Query("SELECT * FROM Product WHERE name LIKE N'%" + item + "%'");
		try {
			while (rs.next()) {
				Product dt = new Product();
				dt.setId(rs.getInt("id"));
				dt.setCategory_id(rs.getInt("category_id"));
				dt.setUnit_id(rs.getInt("unit_id"));
				dt.setName(rs.getNString("name"));
				dt.setDescription(rs.getNString("description"));
				dt.setPrice_sell(rs.getInt("price_sell"));
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
		ResultSet rs = util.DBUtil.instance.Query("SELECT name FROM Product WHERE name = ?", item);
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
