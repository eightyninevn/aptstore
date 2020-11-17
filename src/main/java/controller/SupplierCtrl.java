package controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import dao.SupplierDao;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Supplier;

public class SupplierCtrl implements Initializable {
	@FXML
	private TableView<Supplier> Table;
	@FXML
	private TableColumn<Supplier, Integer> noColumn;
	@FXML
	private TableColumn<Supplier, String> nameColumn;
	@FXML
	private TableColumn<Supplier, String> phoneColumn;
	@FXML
	private TableColumn<Supplier, String> emailColumn;
	@FXML
	private TableColumn<Supplier, String> addressColumn;
	@FXML
	private TableColumn<Supplier, String> activeColumn;
	@FXML
	private TextField nameTf;
	@FXML
	private TextField phoneTf;
	@FXML
	private TextField emailTf;
	@FXML
	private TextField addressTf;
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
		ObservableList<Supplier> SupplierList = allList();
		noColumn.setCellValueFactory(
				cell -> new ReadOnlyObjectWrapper<Integer>(Table.getItems().indexOf(cell.getValue()) + 1));
		nameColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("name"));
		phoneColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("phone"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("email"));
		addressColumn.setCellValueFactory(new PropertyValueFactory<Supplier, String>("address"));
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
		Table.setItems(SupplierList);
	}

	public void addSupplier() {
		Supplier item = new Supplier();
		item.setName(nameTf.getText());
		item.setPhone(phoneTf.getText());
		item.setEmail(emailTf.getText());
		item.setAddress(addressTf.getText());
		item.setActive(activeChkbox.isSelected());
		if (checkEmpty(new String[] { nameTf.getText(), phoneTf.getText(), emailTf.getText(), addressTf.getText() })) {
			util.AlertUtil.empty().show();
		} else if (SupplierDao.instance.checkDouble(new String[] { nameTf.getText() })) {
			util.AlertUtil.existing().show();
		} else if (checkPhone(phoneTf.getText())) {
			util.AlertUtil.phone().show();
		}else if (checkEmail(emailTf.getText())) {
			util.AlertUtil.email().show();
		} else {
			util.AlertUtil.confirmToAdd().showAndWait().ifPresent(type -> {
				if (type == ButtonType.CANCEL) {
					Refresh();
				} else {
					SupplierDao.instance.Create(item);
					Refresh();
					util.AlertUtil.successfully().show();
				}
			});
		}
	}

	public void deleteSupplier() {
		Supplier item = Table.getSelectionModel().getSelectedItem();
		if (item == null) {
			util.AlertUtil.deleteNull().show();
		} else {
			util.AlertUtil.confirmToDelete().showAndWait().ifPresent(type -> {
				if (type == ButtonType.CANCEL) {
					Refresh();
				} else {
					SupplierDao.instance.Delete(String.valueOf(item.getId()));
					Refresh();
					util.AlertUtil.successfully().show();
				}
			});
		}
	}

	public void editSupplier() {
		Supplier item = Table.getSelectionModel().getSelectedItem();
		if (item == null) {
			util.AlertUtil.editNull().show();
		} else {
			if (checkEmpty(new String[] { nameTf.getText(), phoneTf.getText(), emailTf.getText(), addressTf.getText() })) {
				util.AlertUtil.empty().show();
			} else if (SupplierDao.instance.checkDouble(new String[] { nameTf.getText() })
					&& !item.getName().equals(nameTf.getText())) {
				util.AlertUtil.existing().show();
			} else if (checkPhone(phoneTf.getText())) {
				util.AlertUtil.phone().show();
			}else if (checkEmail(emailTf.getText())) {
				util.AlertUtil.email().show();
			} else {
				util.AlertUtil.confirmToEdit().showAndWait().ifPresent(type -> {
					if (type == ButtonType.CANCEL) {
						Refresh();
					} else {
						SupplierDao.instance.Edit(new String[] { nameTf.getText(), phoneTf.getText(), emailTf.getText(),
								addressTf.getText(), activeChkbox.isSelected() == true ? "1" : "0",
								String.valueOf(item.getId()) });
						Refresh();
						util.AlertUtil.successfully().show();
					}
				});
			}
		}
	}

	public void searchSupplier() {
		ObservableList<Supplier> searchList = FXCollections.observableArrayList();
		String searchName = nameTf.getText();
		if (searchName == null || searchName.isEmpty()) {
			util.AlertUtil.searchNull().show();
		} else {
			searchList = (ObservableList<Supplier>) SupplierDao.instance.searchSupplier(searchName);
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
		Supplier selected = Table.getSelectionModel().getSelectedItem();
		if (selected != null) {
			nameTf.setText(selected.getName());
			phoneTf.setText(selected.getPhone());
			emailTf.setText(selected.getEmail());
			addressTf.setText(selected.getAddress());
			activeChkbox.setSelected(selected.isActive());
		}
	}

	public void Refresh() {
		Table.getItems().clear();
		Table.setItems(allList());
	}

	public static ObservableList<Supplier> allList() {
		ObservableList<Supplier> data = FXCollections.observableArrayList();
		data = (ObservableList<Supplier>) SupplierDao.instance.getAllSupplier(0,1);
		return data;
	}
	
	public static ObservableList<Supplier> comboBoxSuplier() {
		ObservableList<Supplier> data = FXCollections.observableArrayList();
		data = (ObservableList<Supplier>) SupplierDao.instance.getAllSupplier(1,1);
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

	public Boolean checkEmail(String email) {
		String EMAIL_PATTERN = "^[a-zA-Z][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$";
		if (Pattern.matches(EMAIL_PATTERN, email)) {
			return false;
		} else {
			return true;
		}
	}
	public Boolean checkPhone(String phone) {
		String PHONE_PATTERN = "[0-9]{10,15}";
		if (Pattern.matches(PHONE_PATTERN, phone)) {
			return false;
		} else {
			return true;
		}

	}

}
