package controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import dao.EmployeeDao;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Employee;
import model.Position;

public class EmployeeCtrl implements Initializable {
	@FXML
	private TableView<Employee> Table;
	@FXML
	private TableColumn<Employee, Integer> noColumn;
	@FXML
	private TableColumn<Employee, String> nameColumn;
	@FXML
	private TableColumn<Employee, String> genderColumn;
	@FXML
	private TableColumn<Employee, String> birthdayColumn;
	@FXML
	private TableColumn<Employee, String> positionColumn;
	@FXML
	private TableColumn<Employee, String> phoneColumn;
	@FXML
	private TableColumn<Employee, String> emailColumn;
	@FXML
	private TableColumn<Employee, String> addressColumn;
	@FXML
	private TableColumn<Employee, String> activeColumn;
	@FXML
	private TextField nameTf;
	@FXML
	private ComboBox<String> genderCbx;
	@FXML
	private DatePicker birthdayDpkr;
	@FXML
	private ComboBox<String> positionCbx;
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

	ObservableList<Position> positionList = PositionCtrl.comboxBoxPosition();
	ObservableList<String> genderList = FXCollections.observableArrayList("Nam", "Nữ");
	ObservableList<Employee> EmployeeList = allList();

	@Override
	public void initialize(URL url, ResourceBundle resources) {
		// preparing data
		birthdayDpkr.setConverter(util.DateUtil.formatDatePicker());
		genderCbx.getItems().addAll(genderList);
		for (Position position : positionList) {
			positionCbx.getItems().add(position.getName());
		}
		// show table
		noColumn.setCellValueFactory(
				cell -> new ReadOnlyObjectWrapper<Integer>(Table.getItems().indexOf(cell.getValue()) + 1));
		nameColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("name"));
		genderColumn.setCellValueFactory(cell -> {
			Boolean gender = cell.getValue().isGender();
			String genderString;
			if (gender == true) {
				genderString = "Nam";
			} else {
				genderString = "Nữ";
			}
			return new ReadOnlyObjectWrapper<String>(genderString);
		});
		birthdayColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("birthday"));
		positionColumn.setCellValueFactory(cell -> {
			Integer position_id = cell.getValue().getPosition_id();
			String position_name = null;
			for (Position position : positionList) {
				if (position.getId() == position_id) {
					position_name = position.getName();
				}
			}
			return new ReadOnlyObjectWrapper<String>(position_name);
		});
		phoneColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("phone"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("email"));
		addressColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("address"));
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
		Table.setItems(EmployeeList);
	}

	public void addEmployee() {		
		if (checkEmpty(new String[] { nameTf.getText(), genderCbx.getValue(), birthdayDpkr.getEditor().getText(),
				positionCbx.getValue(), phoneTf.getText(), emailTf.getText(), addressTf.getText() })) {
			util.AlertUtil.empty().show();
		} else if (util.DateUtil.isValidDate(birthdayDpkr.getEditor().getText())) {
			util.AlertUtil.dateInvalid().show();
		} else if (EmployeeDao.instance.checkDouble(new String[] { nameTf.getText() })) {
			util.AlertUtil.existing().show();
		} else if (checkPhone(phoneTf.getText())) {
			util.AlertUtil.phone().show();
		} else if (checkEmail(emailTf.getText())) {
			util.AlertUtil.email().show();
		} else {
			util.AlertUtil.confirmToAdd().showAndWait().ifPresent(type -> {
				if (type == ButtonType.CANCEL) {
					Refresh();
				} else {
					Employee item = new Employee();

					item.setName(nameTf.getText());
					item.setGender(genderCbx.getValue() == "Nam" ? true : false);
					item.setBirthday(birthdayDpkr.getEditor().getText());
					for (Position position : positionList) {
						if (position.getName() == positionCbx.getValue()) {
							item.setPosition_id(position.getId());
						}
					}
					item.setPhone(phoneTf.getText());
					item.setEmail(emailTf.getText());
					item.setAddress(addressTf.getText());
					item.setActive(activeChkbox.isSelected());

					EmployeeDao.instance.Create(item);
					Refresh();
					util.AlertUtil.successfully().show();
				}
			});
		}
	}

	public void deleteEmployee() {
		Employee item = Table.getSelectionModel().getSelectedItem();
		if (item == null) {
			util.AlertUtil.deleteNull().show();
		} else {
			util.AlertUtil.confirmToDelete().showAndWait().ifPresent(type -> {
				if (type == ButtonType.CANCEL) {
					Refresh();
				} else {
					EmployeeDao.instance.Delete(String.valueOf(item.getId()));
					Refresh();
					util.AlertUtil.successfully().show();
				}
			});
		}
	}

	public void editEmployee() {
		Employee item = Table.getSelectionModel().getSelectedItem();
		if (item == null) {
			util.AlertUtil.editNull().show();
		} else {
			if (checkEmpty(new String[] { nameTf.getText(), genderCbx.getValue().toString(),
					birthdayDpkr.getEditor().getText(), positionCbx.getValue().toString(), phoneTf.getText(),
					emailTf.getText(), addressTf.getText() })) {
				util.AlertUtil.empty().show();
			} else if (util.DateUtil.isValidDate(birthdayDpkr.getEditor().getText())) {
				util.AlertUtil.dateInvalid().show();
			} else if (EmployeeDao.instance.checkDouble(new String[] { nameTf.getText() })
					&& !item.getName().equals(nameTf.getText())) {
				util.AlertUtil.existing().show();
			} else if (checkPhone(phoneTf.getText())) {
				util.AlertUtil.phone().show();
			} else if (checkEmail(emailTf.getText())) {
				util.AlertUtil.email().show();
			} else {
				util.AlertUtil.confirmToEdit().showAndWait().ifPresent(type -> {
					if (type == ButtonType.CANCEL) {
						Refresh();
					} else {
						String positionName = null;
						for (Position position : positionList) {
							if (position.getName() == positionCbx.getValue()) {
								positionName = Integer.toString(position.getId());
							}
						}
						EmployeeDao.instance.Edit(new String[] { nameTf.getText(),
								genderCbx.getValue() == "Nam" ? "1" : "0", birthdayDpkr.getEditor().getText(),
								positionName, phoneTf.getText(), emailTf.getText(), addressTf.getText(),
								activeChkbox.isSelected() == true ? "1" : "0", String.valueOf(item.getId()) });
						Refresh();
						util.AlertUtil.successfully().show();
					}
				});
			}
		}
	}

	public void searchEmployee() {
		ObservableList<Employee> searchList = FXCollections.observableArrayList();
		String searchName = nameTf.getText();
		if (searchName == null || searchName.isEmpty()) {
			util.AlertUtil.searchNull().show();
		} else {
			searchList = (ObservableList<Employee>) EmployeeDao.instance.searchEmployee(searchName);
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
		Employee selected = Table.getSelectionModel().getSelectedItem();
		if (selected != null) {
			nameTf.setText(selected.getName());
			genderCbx.setValue(selected.isGender() == true ? "Nam" : "Nữ");
			birthdayDpkr.getEditor().setText(selected.getBirthday());
			for (Position position : positionList) {
				if (position.getId() == selected.getPosition_id()) {
					positionCbx.setValue(position.getName());
				}
			}
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

	public ObservableList<Employee> allList() {
		ObservableList<Employee> data = FXCollections.observableArrayList();
		data = (ObservableList<Employee>) EmployeeDao.instance.getAllEmployee(0,1);
		return data;
	}
	public static ObservableList<Employee> comboBoxEmployee() {
		ObservableList<Employee> data = FXCollections.observableArrayList();
		data = (ObservableList<Employee>) EmployeeDao.instance.getAllEmployee(1,1);
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
