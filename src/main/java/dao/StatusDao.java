package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Status;
import util.DBUtil;

public class StatusDao {

	public static StatusDao instance = new StatusDao();

	private StatusDao() {

	}

	public boolean Create(Status newStatus) {
		int kq = DBUtil.instance.Create("INSERT INTO Status VALUES (?,?)",
				new String[] { newStatus.getName(), (newStatus.isActive() == true ? "1" : "0") });
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean Delete(String Selected) {
		String[] sl = { Selected };
		int kq = DBUtil.instance.Create("DELETE Status WHERE id = ?", sl);
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean Edit(String[] Selected) {
		int kq = DBUtil.instance.Create("UPDATE Status SET name = ?, active = ? WHERE id = ?", Selected);
		if (kq > 0) {
			return true;
		} else {
			return false;
		}
	}

	public ObservableList<Status> getAllStatus(int t, int f) {
		ObservableList<Status> data = FXCollections.observableArrayList();
		ResultSet rs = util.DBUtil.instance.Query("SELECT * FROM Status WHERE active =" + t +" OR active ="+ f + " ORDER BY id ASC");
		try {
			while (rs.next()) {
				Status dt = new Status();
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

	public ObservableList<Status> searchStatus(String item) {
		ObservableList<Status> data = FXCollections.observableArrayList();
		ResultSet rs = util.DBUtil.instance.Query("SELECT * FROM Status WHERE name LIKE N'%" + item + "%'");
		try {
			while (rs.next()) {
				Status dt = new Status();
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
		ResultSet rs = util.DBUtil.instance.Query("SELECT name FROM Status WHERE name = ?", item);
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
