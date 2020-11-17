package controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import dao.CustomerDao;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;

public class CustomerCtrl implements Initializable {
	@FXML
	private TableView<Customer> Table;
	@FXML
	private TableColumn<Customer, Integer> noColumn;
	@FXML
	private TableColumn<Customer, String> nameColumn;
	@FXML
	private TableColumn<Customer, String> phoneColumn;
	@FXML
	private TableColumn<Customer, String> emailColumn;
	@FXML
	private TableColumn<Customer, String> addressColumn;
	@FXML
	private TableColumn<Customer, String> activeColumn;
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
		ObservableList<Customer> CustomerList = allList();
		noColumn.setCellValueFactory(
				cell -> new ReadOnlyObjectWrapper<Integer>(Table.getItems().indexOf(cell.getValue()) + 1));
		nameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
		phoneColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("email"));
		addressColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
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
		Table.setItems(CustomerList);
	}

	public void addCustomer() {
		Customer item = new Customer();
		item.setName(nameTf.getText());
		item.setPhone(phoneTf.getText());
		item.setEmail(emailTf.getText());
		item.setAddress(addressTf.getText());
		item.setActive(activeChkbox.isSelected());
		if (checkEmpty(new String[] { nameTf.getText(), phoneTf.getText(), emailTf.getText(), addressTf.getText() })) {
			util.AlertUtil.empty().show();
		} else if (CustomerDao.instance.checkDouble(new String[] { nameTf.getText() })) {
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
					CustomerDao.instance.Create(item);
					Refresh();
					util.AlertUtil.successfully().show();
				}
			});
		}
	}

	public void deleteCustomer() {
		Customer item = Table.getSelectionModel().getSelectedItem();
		if (item == null) {
			util.AlertUtil.deleteNull().show();
		} else {
			util.AlertUtil.confirmToDelete().showAndWait().ifPresent(type -> {
				if (type == ButtonType.CANCEL) {
					Refresh();
				} else {
					CustomerDao.instance.Delete(String.valueOf(item.getId()));
					Refresh();
					util.AlertUtil.successfully().show();
				}
			});
		}
	}

	public void editCustomer() {
		Customer item = Table.getSelectionModel().getSelectedItem();
		if (item == null) {
			util.AlertUtil.editNull().show();
		} else {
			if (checkEmpty(new String[] { nameTf.getText(), phoneTf.getText(), emailTf.getText(), addressTf.getText() })) {
				util.AlertUtil.empty().show();
			} else if (CustomerDao.instance.checkDouble(new String[] { nameTf.getText() })
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
						CustomerDao.instance.Edit(new String[] { nameTf.getText(), phoneTf.getText(), emailTf.getText(),
								addressTf.getText(), activeChkbox.isSelected() == true ? "1" : "0",
								String.valueOf(item.getId()) });
						Refresh();
						util.AlertUtil.successfully().show();
					}
				});
			}
		}
	}

	public void searchCustomer() {
		ObservableList<Customer> searchList = FXCollections.observableArrayList();
		String searchName = nameTf.getText();
		if (searchName == null || searchName.isEmpty()) {
			util.AlertUtil.searchNull().show();
		} else {
			searchList = (ObservableList<Customer>) CustomerDao.instance.searchCustomer(searchName);
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
		Customer selected = Table.getSelectionModel().getSelectedItem();
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

	public ObservableList<Customer> allList() {
		ObservableList<Customer> data = FXCollections.observableArrayList();
		data = (ObservableList<Customer>) CustomerDao.instance.getAllCustomer(0,1);
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
