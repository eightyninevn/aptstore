package controller;

import java.net.URL;
import java.util.ResourceBundle;

import dao.StatusDao;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Status;

public class StatusCtrl implements Initializable {
	@FXML
	private TableView<Status> Table;
	@FXML
	private TableColumn<Status, Integer> noColumn;
	@FXML
	private TableColumn<Status, String> nameColumn;
	@FXML
	private TableColumn<Status, String> activeColumn;
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
		ObservableList<Status> StatusList = allList();
		noColumn.setCellValueFactory(
				cell -> new ReadOnlyObjectWrapper<Integer>(Table.getItems().indexOf(cell.getValue()) + 1));
		nameColumn.setCellValueFactory(new PropertyValueFactory<Status, String>("name"));
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
		Table.setItems(StatusList);
	}

	public void addStatus() {
		Status item = new Status();
		item.setName(nameTf.getText());
		item.setActive(activeChkbox.isSelected());
		if (checkEmpty(new String[] { nameTf.getText() })) {
			util.AlertUtil.empty().show();
		} else if (StatusDao.instance.checkDouble(new String[] { nameTf.getText() })) {
			util.AlertUtil.existing().show();
		} else {
			util.AlertUtil.confirmToAdd().showAndWait().ifPresent(type -> {
				if (type == ButtonType.CANCEL) {
					Refresh();
				} else {
					StatusDao.instance.Create(item);
					Refresh();
					util.AlertUtil.successfully().show();
				}
			});
		}
	}

	public void deleteStatus() {
		Status item = Table.getSelectionModel().getSelectedItem();
		if (item == null) {
			util.AlertUtil.deleteNull().show();
		} else {
			util.AlertUtil.confirmToDelete().showAndWait().ifPresent(type -> {
				if (type == ButtonType.CANCEL) {
					Refresh();
				} else {
					StatusDao.instance.Delete(String.valueOf(item.getId()));
					Refresh();
					util.AlertUtil.successfully().show();
				}
			});
		}
	}

	public void editStatus() {
		Status item = Table.getSelectionModel().getSelectedItem();
		if (item == null) {
			util.AlertUtil.editNull().show();
		} else {
			if (checkEmpty(new String[] { nameTf.getText() })) {
				util.AlertUtil.empty().show();
			} else if (StatusDao.instance.checkDouble(new String[] { nameTf.getText() })
					&& !item.getName().equals(nameTf.getText())) {
				util.AlertUtil.existing().show();
			} else {
				util.AlertUtil.confirmToEdit().showAndWait().ifPresent(type -> {
					if (type == ButtonType.CANCEL) {
						Refresh();
					} else {
						StatusDao.instance.Edit(new String[] { nameTf.getText(),
								activeChkbox.isSelected() == true ? "1" : "0", String.valueOf(item.getId()) });
						Refresh();
						util.AlertUtil.successfully().show();
					}
				});
			}
		}
	}

	public void searchStatus() {
		ObservableList<Status> searchList = FXCollections.observableArrayList();
		String searchName = nameTf.getText();
		if (searchName == null || searchName.isEmpty()) {
			util.AlertUtil.searchNull().show();
		} else {
			searchList = (ObservableList<Status>) StatusDao.instance.searchStatus(searchName);
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
		Status selected = Table.getSelectionModel().getSelectedItem();
		if (selected != null) {
			nameTf.setText(selected.getName());
			activeChkbox.setSelected(selected.isActive());
		}
	}

	public void Refresh() {
		Table.getItems().clear();
		Table.setItems(allList());
	}

	public ObservableList<Status> allList() {
		ObservableList<Status> data = FXCollections.observableArrayList();
		data = (ObservableList<Status>) StatusDao.instance.getAllStatus(0,1);
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
