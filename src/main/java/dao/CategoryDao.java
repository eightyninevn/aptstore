package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Category;
import util.DBUtil;

public class CategoryDao {

	public static CategoryDao instance = new CategoryDao();

	private CategoryDao() {

	}

	public boolean Create(Category newCategory) {
		int kq = DBUtil.instance.Create("INSERT INTO Category VALUES (?,?)",
				new String[] { newCategory.getName(), (newCategory.isActive() == true ? "1" : "0") });
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean Delete(String Selected) {
		String[] sl = { Selected };
		int kq = DBUtil.instance.Create("DELETE Category WHERE id = ?", sl);
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean Edit(String[] Selected) {
		int kq = DBUtil.instance.Create("UPDATE Category SET name = ?, active = ? WHERE id = ?", Selected);
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public ObservableList<Category> getAllCategory(int t, int f) {
		ObservableList<Category> data = FXCollections.observableArrayList();
		ResultSet rs = util.DBUtil.instance.Query("SELECT * FROM Category WHERE active =" + t +" OR active ="+ f + " ORDER BY id ASC");
		try {
			while (rs.next()) {
				Category dt = new Category();
				dt.setId(rs.getInt("id"));
				dt.setName(rs.getNString("name"));
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

	public ObservableList<Category> searchCategory(String item) {
		ObservableList<Category> data = FXCollections.observableArrayList();
		ResultSet rs = util.DBUtil.instance.Query("SELECT * FROM Category WHERE name LIKE N'%" + item + "%'");
		try {
			while (rs.next()) {
				Category dt = new Category();
				dt.setId(rs.getInt(1));
				dt.setName(rs.getNString(2));
				dt.setActive(rs.getBoolean(3));
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
		ResultSet rs = util.DBUtil.instance.Query("SELECT name FROM Category WHERE name = ?", item);
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
