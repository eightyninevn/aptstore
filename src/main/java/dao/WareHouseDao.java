package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.WareHouse;
import util.DBUtil;

public class WareHouseDao {

	public static WareHouseDao instance = new WareHouseDao();

	private WareHouseDao() {

	}

	public boolean Create(WareHouse newWareHouse) {
		int kq = DBUtil.instance.Create("INSERT INTO WareHouse VALUES (?,?,?,?)",
				new String[] { newWareHouse.getName(), newWareHouse.getLocation(), newWareHouse.getStyle(), (newWareHouse.isActive() == true ? "1" : "0") });
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean Delete(String Selected) {
		String[] sl = { Selected };
		int kq = DBUtil.instance.Create("DELETE WareHouse WHERE id = ?", sl);
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean Edit(String[] Selected) {
		int kq = DBUtil.instance.Create("UPDATE WareHouse SET name = ?, location = ?, style = ?, active = ? WHERE id = ?", Selected);
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public ObservableList<WareHouse> getAllWareHouse(int t, int f) {
		ObservableList<WareHouse> data = FXCollections.observableArrayList();
		ResultSet rs = util.DBUtil.instance.Query("SELECT * FROM WareHouse WHERE active =" + t +" OR active ="+ f + " ORDER BY id ASC");
		try {
			while (rs.next()) {
				WareHouse dt = new WareHouse();
				dt.setId(rs.getInt("id"));
				dt.setName(rs.getNString("name"));
				dt.setLocation(rs.getNString("location"));
				dt.setStyle(rs.getNString("style"));
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

	public ObservableList<WareHouse> searchWareHouse(String item) {
		ObservableList<WareHouse> data = FXCollections.observableArrayList();
		ResultSet rs = util.DBUtil.instance.Query("SELECT * FROM WareHouse WHERE name LIKE N'%" + item + "%'");
		try {
			while (rs.next()) {
				WareHouse dt = new WareHouse();
				dt.setId(rs.getInt(1));
				dt.setName(rs.getNString(2));
				dt.setLocation(rs.getNString(3));
				dt.setStyle(rs.getNString(4));
				dt.setActive(rs.getBoolean(5));
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
		ResultSet rs = util.DBUtil.instance.Query("SELECT name FROM WareHouse WHERE name = ?", item);
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
