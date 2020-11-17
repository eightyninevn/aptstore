package controller;

import java.net.URL;
import java.util.ResourceBundle;

import dao.WareHouseDao;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.WareHouse;

public class WareHouseCtrl implements Initializable {
	@FXML
	private TableView<WareHouse> Table;
	@FXML
	private TableColumn<WareHouse, Integer> noColumn;
	@FXML
	private TableColumn<WareHouse, String> nameColumn;
	@FXML
	private TableColumn<WareHouse, String> locationColumn;
	@FXML
	private TableColumn<WareHouse, String> styleColumn;
	@FXML
	private TableColumn<WareHouse, String> activeColumn;
	@FXML
	private TextField nameTf;
	@FXML
	private TextField locationTf;
	@FXML
	private TextField styleTf;
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
		ObservableList<WareHouse> WareHouseList = allList();
		noColumn.setCellValueFactory(
				cell -> new ReadOnlyObjectWrapper<Integer>(Table.getItems().indexOf(cell.getValue()) + 1));
		nameColumn.setCellValueFactory(new PropertyValueFactory<WareHouse, String>("name"));
		locationColumn.setCellValueFactory(new PropertyValueFactory<WareHouse, String>("location"));
		styleColumn.setCellValueFactory(new PropertyValueFactory<WareHouse, String>("style"));
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
		Table.setItems(WareHouseList);
	}

	public void addWareHouse() {
		WareHouse item = new WareHouse();
		item.setName(nameTf.getText());
		item.setLocation(locationTf.getText());
		item.setStyle(styleTf.getText());
		item.setActive(activeChkbox.isSelected());
		if (checkEmpty(new String[] { nameTf.getText(), locationTf.getText(), styleTf.getText()  })) {
			util.AlertUtil.empty().show();
		} else if (WareHouseDao.instance.checkDouble(new String[] { nameTf.getText() })) {
			util.AlertUtil.existing().show();
		} else {
			util.AlertUtil.confirmToAdd().showAndWait().ifPresent(type -> {
				if (type == ButtonType.CANCEL) {
					Refresh();
				} else {
					WareHouseDao.instance.Create(item);
					Refresh();
					util.AlertUtil.successfully().show();
				}
			});
		}
	}

	public void deleteWareHouse() {
		WareHouse item = Table.getSelectionModel().getSelectedItem();
		if (item == null) {
			util.AlertUtil.deleteNull().show();
		} else {
			util.AlertUtil.confirmToDelete().showAndWait().ifPresent(type -> {
				if (type == ButtonType.CANCEL) {
					Refresh();
				} else {
					WareHouseDao.instance.Delete(String.valueOf(item.getId()));
					Refresh();
					util.AlertUtil.successfully().show();
				}
			});
		}
	}

	public void editWareHouse() {
		WareHouse item = Table.getSelectionModel().getSelectedItem();
		if (item == null) {
			util.AlertUtil.editNull().show();
		} else {
			if (checkEmpty(new String[] { nameTf.getText(), locationTf.getText(), styleTf.getText() })) {
				util.AlertUtil.empty().show();
			} else if (WareHouseDao.instance.checkDouble(new String[] { nameTf.getText() })
					&& !item.getName().equals(nameTf.getText())) {
				util.AlertUtil.existing().show();
			} else {
				util.AlertUtil.confirmToEdit().showAndWait().ifPresent(type -> {
					if (type == ButtonType.CANCEL) {
						Refresh();
					} else {
						WareHouseDao.instance.Edit(new String[] { nameTf.getText(), locationTf.getText(), styleTf.getText(),
								activeChkbox.isSelected() == true ? "1" : "0", String.valueOf(item.getId()) });
						Refresh();
						util.AlertUtil.successfully().show();
					}
				});
			}
		}
	}

	public void searchWareHouse() {
		ObservableList<WareHouse> searchList = FXCollections.observableArrayList();
		String searchName = nameTf.getText();
		if (searchName == null || searchName.isEmpty()) {
			util.AlertUtil.searchNull().show();
		} else {
			searchList = (ObservableList<WareHouse>) WareHouseDao.instance.searchWareHouse(searchName);
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
		WareHouse selected = Table.getSelectionModel().getSelectedItem();
		if (selected != null) {
			nameTf.setText(selected.getName());
			locationTf.setText(selected.getLocation());
			styleTf.setText(selected.getStyle());
			activeChkbox.setSelected(selected.isActive());
		}
	}

	public void Refresh() {
		Table.getItems().clear();
		Table.setItems(allList());
	}

	public ObservableList<WareHouse> allList() {
		ObservableList<WareHouse> data = FXCollections.observableArrayList();
		data = (ObservableList<WareHouse>) WareHouseDao.instance.getAllWareHouse(0,1);
		return data;
	}
	
	public static ObservableList<WareHouse> comboBoxWareHouse() {
		ObservableList<WareHouse> data = FXCollections.observableArrayList();
		data = (ObservableList<WareHouse>) WareHouseDao.instance.getAllWareHouse(1,1);
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
