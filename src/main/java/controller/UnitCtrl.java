package controller;

import java.net.URL;
import java.util.ResourceBundle;

import dao.UnitDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Unit;

public class UnitCtrl implements Initializable {
	@FXML
	private TableView<Unit> Table;
	@FXML
	private TableColumn<Unit, Integer> idColumn;
	@FXML
	private TableColumn<Unit, String> nameColumn;
	@FXML
	private TableColumn<Unit, Boolean> activeColumn;
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
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		ObservableList<Unit> UnitList = allList();
		idColumn.setCellValueFactory(new PropertyValueFactory<Unit, Integer>("id"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<Unit, String>("name"));
		activeColumn.setCellValueFactory(new PropertyValueFactory<Unit, Boolean>("active"));
		Table.setItems(UnitList);
	}

	public void addUnit() {
		Unit item = new Unit();
		item.setName(nameTf.getText());
		item.setActive(activeChkbox.isSelected());

		if (checkEmpty(new String[] {nameTf.getText()})) {
			Alert alert1 = new Alert(AlertType.WARNING, "Please fill in all the required field(s)", ButtonType.CLOSE);
			alert1.show();
		} else if (UnitDao.instance.checkDouble(new String[] {nameTf.getText()})) {
			Alert alert2 = new Alert(AlertType.WARNING, "The Unit is existing!", ButtonType.CLOSE);
			alert2.show();
		} else {
			Alert alert3 = new Alert(AlertType.CONFIRMATION, "Please confirm to add a new Unit", ButtonType.YES,
					ButtonType.CANCEL);
			alert3.showAndWait().ifPresent(type -> {
				if (type == ButtonType.CANCEL) {
					Refresh();
				} else {
					UnitDao.instance.Create(item);
					Refresh();
					Alert alert4 = new Alert(AlertType.INFORMATION, "Successfully", ButtonType.CLOSE);
					alert4.show();
				}
			});
		}
	}

	public void deleteUnit() {
		Unit item = Table.getSelectionModel().getSelectedItem();
		if (item == null) {
			Alert Alert1 = new Alert(AlertType.INFORMATION, "Please select Unit you want to delete", ButtonType.YES);
			Alert1.show();
		} else {
			Alert alert2 = new Alert(AlertType.CONFIRMATION, "Please confirm to delete selected Unit", ButtonType.YES,
					ButtonType.CANCEL);
			alert2.showAndWait().ifPresent(type -> {
				if (type == ButtonType.CANCEL) {
					Refresh();
				} else {
					UnitDao.instance.Delete(String.valueOf(item.getId()));
					Refresh();
					Alert alert3 = new Alert(AlertType.INFORMATION, "Successfully", ButtonType.CLOSE);
					alert3.show();
				}
			});
		}
	}

	public void editUnit() {
		Unit item = Table.getSelectionModel().getSelectedItem();
		if (item == null) {
			Alert Alert1 = new Alert(AlertType.INFORMATION, "Please select Unit you want to edit", ButtonType.YES);
			Alert1.show();
		} else {
			if (checkEmpty(new String[] {nameTf.getText()})) {
				Alert alert1 = new Alert(AlertType.WARNING, "Please fill in all the required field(s)",
						ButtonType.CLOSE);
				alert1.show();
			} else if (UnitDao.instance.checkDouble(new String[] {nameTf.getText()})
					&& !item.getName().equals(nameTf.getText())) {
				Alert alert2 = new Alert(AlertType.WARNING, "The Unit is existing!", ButtonType.CLOSE);
				alert2.show();
			} else {
				Alert alert3 = new Alert(AlertType.CONFIRMATION, "Please confirm to edit selected Unit", ButtonType.YES,
						ButtonType.CANCEL);
				alert3.showAndWait().ifPresent(type -> {
					if (type == ButtonType.CANCEL) {
						Refresh();
					} else {
						UnitDao.instance.Edit(new String[] {nameTf.getText(),
								activeChkbox.isSelected() == true ? "1" : "0", String.valueOf(item.getId())});
						Refresh();
						Alert alert4 = new Alert(AlertType.INFORMATION, "Successfully", ButtonType.CLOSE);
						alert4.show();
					}
				});
			}
		}
	}

	public void searchUnit() {
		ObservableList<Unit> searchList = FXCollections.observableArrayList();
		String searchName = nameTf.getText();
		if (searchName == null || searchName.isEmpty()) {
			Alert Alert1 = new Alert(AlertType.INFORMATION, "Please type Unit's name you want to search",
					ButtonType.YES);
			Alert1.show();
		} else {
			searchList = (ObservableList<Unit>) UnitDao.instance.searchUnit(searchName);
			if (searchList.isEmpty()) {
				Table.getItems().clear();
				Alert Alert2 = new Alert(AlertType.INFORMATION, "No result found");
				Alert2.show();
			} else {
				Table.getItems().clear();
				Table.setItems(searchList);
			}
		}
	}

	public void SelectedItem() {
		Unit selected = Table.getSelectionModel().getSelectedItem();
		if(selected != null) {
			nameTf.setText(selected.getName());
			activeChkbox.setSelected(selected.isActive());
		}
	}

	public void Refresh() {
		Table.getItems().clear();
		Table.setItems(allList());
	}

	public ObservableList<Unit> allList() {
		ObservableList<Unit> data = FXCollections.observableArrayList();
		data = (ObservableList<Unit>) UnitDao.instance.getAllUnit();
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
