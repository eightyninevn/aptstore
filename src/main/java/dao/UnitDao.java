package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Unit;
import util.DBUtil;

public class UnitDao {

	public static UnitDao instance = new UnitDao();

	private UnitDao() {

	}

	public boolean Create(Unit newUnit) {
		int kq = DBUtil.instance.Create("INSERT INTO Unit VALUES (?,?)",
				new String[] { newUnit.getName(), (newUnit.isActive() == true ? "1" : "0") });
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean Delete(String Selected) {
		String[] sl = { Selected };
		int kq = DBUtil.instance.Create("DELETE Unit WHERE id = ?", sl);
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean Edit(String[] Selected) {
		int kq = DBUtil.instance.Create("UPDATE Unit SET name = ?, active = ? WHERE id = ?", Selected);
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public ObservableList<Unit> getAllUnit() {
		ObservableList<Unit> data = FXCollections.observableArrayList();
		ResultSet rs = util.DBUtil.instance.Query("SELECT * FROM Unit ORDER BY id ASC");
		try {
			while (rs.next()) {
				Unit dt = new Unit();
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

	public ObservableList<Unit> searchUnit(String item) {
		ObservableList<Unit> data = FXCollections.observableArrayList();
		ResultSet rs = util.DBUtil.instance.Query("SELECT * FROM Unit WHERE name LIKE N'%" + item + "%'");
		try {
			while (rs.next()) {
				Unit dt = new Unit();
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
		ResultSet rs = util.DBUtil.instance.Query("SELECT name FROM Unit WHERE name = ?", item);
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
