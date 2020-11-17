package controller;

import java.net.URL;
import java.util.ResourceBundle;

import dao.UnitDao;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Unit;

public class UnitCtrl implements Initializable {
	@FXML
	private TableView<Unit> Table;
	@FXML
	private TableColumn<Unit, Integer> noColumn;
	@FXML
	private TableColumn<Unit, String> nameColumn;
	@FXML
	private TableColumn<Unit, String> activeColumn;
	@FXML
	private TextField nameTf;
	@FXML
	private CheckBox activeChkbox;
	@FXML
	private Button addBtn;
	@FXML
	private Button deleteBtn;
	@FXML
	private Button editBtn;
	@FXML
	private Button searchBtn;
	@FXML
	private Button showAllBtn;

	@Override
	public void initialize(URL url, ResourceBundle resources) {
		ObservableList<Unit> UnitList = allList();
		noColumn.setCellValueFactory(
				cell -> new ReadOnlyObjectWrapper<Integer>(Table.getItems().indexOf(cell.getValue()) + 1));
		nameColumn.setCellValueFactory(new PropertyValueFactory<Unit, String>("name"));
		activeColumn.setCellValueFactory(cell -> {
			Boolean active = cell.getValue().isActive();
			String activeString;
			if (active == true) {
				activeString = "Yes";
			} else {
				activeString = "No";
			}
			return new ReadOnlyObjectWrapper<String>(activeString);
		});
		Table.setItems(UnitList);
	}

	public void addUnit() {
		Unit item = new Unit();
		item.setName(nameTf.getText());
		item.setActive(activeChkbox.isSelected());
		if (checkEmpty(new String[] { nameTf.getText() })) {
			util.AlertUtil.empty().show();
		} else if (UnitDao.instance.checkDouble(new String[] { nameTf.getText() })) {
			util.AlertUtil.existing().show();
		} else {
			util.AlertUtil.confirmToAdd().showAndWait().ifPresent(type -> {
				if (type == ButtonType.CANCEL) {
					Refresh();
				} else {
					UnitDao.instance.Create(item);
					Refresh();
					util.AlertUtil.successfully().show();
				}
			});
		}
	}

	public void deleteUnit() {
		Unit item = Table.getSelectionModel().getSelectedItem();
		if (item == null) {
			util.AlertUtil.deleteNull().show();
		} else {
			util.AlertUtil.confirmToDelete().showAndWait().ifPresent(type -> {
				if (type == ButtonType.CANCEL) {
					Refresh();
				} else {
					UnitDao.instance.Delete(String.valueOf(item.getId()));
					Refresh();
					util.AlertUtil.successfully().show();
				}
			});
		}
	}

	public void editUnit() {
		Unit item = Table.getSelectionModel().getSelectedItem();
		if (item == null) {
			util.AlertUtil.editNull().show();
		} else {
			if (checkEmpty(new String[] { nameTf.getText() })) {
				util.AlertUtil.empty().show();
			} else if (UnitDao.instance.checkDouble(new String[] { nameTf.getText() })
					&& !item.getName().equals(nameTf.getText())) {
				util.AlertUtil.existing().show();
			} else {
				util.AlertUtil.confirmToEdit().showAndWait().ifPresent(type -> {
					if (type == ButtonType.CANCEL) {
						Refresh();
					} else {
						UnitDao.instance.Edit(new String[] { nameTf.getText(),
								activeChkbox.isSelected() == true ? "1" : "0", String.valueOf(item.getId()) });
						Refresh();
						util.AlertUtil.successfully().show();
					}
				});
			}
		}
	}

	public void searchUnit() {
		ObservableList<Unit> searchList = FXCollections.observableArrayList();
		String searchName = nameTf.getText();
		if (searchName == null || searchName.isEmpty()) {
			util.AlertUtil.searchNull().show();
		} else {
			searchList = (ObservableList<Unit>) UnitDao.instance.searchUnit(searchName);
			if (searchList.isEmpty()) {
				Table.getItems().clear();
				util.AlertUtil.searchNoReSult().show();
			} else {
				Table.getItems().clear();
				Table.setItems(searchList);
			}
		}
	}

	public void SelectedItem() {
		Unit selected = Table.getSelectionModel().getSelectedItem();
		if (selected != null) {
			nameTf.setText(selected.getName());
			activeChkbox.setSelected(selected.isActive());
		}
	}

	public void Refresh() {
		Table.getItems().clear();
		Table.setItems(allList());
	}

	public static ObservableList<Unit> allList() {
		ObservableList<Unit> data = FXCollections.observableArrayList();
		data = (ObservableList<Unit>) UnitDao.instance.getAllUnit(0,1);
		return data;
	}
	
	public static ObservableList<Unit> comboBoxUnit() {
		ObservableList<Unit> data = FXCollections.observableArrayList();
		data = (ObservableList<Unit>) UnitDao.instance.getAllUnit(1,1);
		return data;
	}

	public Boolean checkEmpty(String[] fields) {
		for (int i = 0; i < fields.length; i++) {
			if (fields[i] == null || fields[i].isEmpty()) {
				return true;
			}
		}
		return false;
	}
}
