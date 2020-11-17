package controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import dao.ProductDao;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Product;
import model.Unit;
import model.Category;

public class ProductCtrl implements Initializable {
	@FXML
	private TableView<Product> Table;
	@FXML
	private TableColumn<Product, Integer> noColumn;
	@FXML
	private TableColumn<Product, String> nameColumn;
	@FXML
	private TableColumn<Product, String> unitColumn;
	@FXML
	private TableColumn<Product, Integer> priceColumn;
	@FXML
	private TableColumn<Product, String> categoryColumn;
	@FXML
	private TableColumn<Product, String> descriptionColumn;
	@FXML
	private TableColumn<Product, String> activeColumn;
	@FXML
	private TextField nameTf;
	@FXML
	private ComboBox<String> unitCbx;
	@FXML
	private ComboBox<String> categoryCbx;
	@FXML
	private TextField priceTf;
	@FXML
	private TextField descriptionTf;
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
	
	ObservableList<Unit> unitList = UnitCtrl.comboBoxUnit();
	ObservableList<Category> categoryList = CategoryCtrl.comboBoxCategory();
	ObservableList<Product> ProductList = allList();

	@Override
	public void initialize(URL url, ResourceBundle resources) {
		// preparing data
		for (Unit unit : unitList) {
			unitCbx.getItems().add(unit.getName());
		}
		for (Category category : categoryList) {
			categoryCbx.getItems().add(category.getName());
		}
		// show table
		noColumn.setCellValueFactory(
				cell -> new ReadOnlyObjectWrapper<Integer>(Table.getItems().indexOf(cell.getValue()) + 1));
		nameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));

		unitColumn.setCellValueFactory(cell -> {
			Integer unit_id = cell.getValue().getUnit_id();
			String unit_name = null;
			for (Unit unit : unitList) {
				if (unit.getId() == unit_id) {
					unit_name = unit.getName();
				}
			}
			return new ReadOnlyObjectWrapper<String>(unit_name);
		});

		priceColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("price_sell"));
		categoryColumn.setCellValueFactory(cell -> {
			Integer category_id = cell.getValue().getCategory_id();
			String category_name = null;
			for (Category category : categoryList) {
				if (category.getId() == category_id) {
					category_name = category.getName();
				}
			}
			return new ReadOnlyObjectWrapper<String>(category_name);
		});
		
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
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
		Table.setItems(ProductList);
	}

	public void addProduct() {
		if (checkEmpty(new String[] { categoryCbx.getValue(), unitCbx.getValue(), nameTf.getText(), priceTf.getText() })) {
			util.AlertUtil.empty().show();
		} else if (ProductDao.instance.checkDouble(new String[] { nameTf.getText() })) {
			util.AlertUtil.existing().show();
		} else if(checkPrice(priceTf.getText())) {
			util.AlertUtil.priceInvalid().show();
		} else {
			util.AlertUtil.confirmToAdd().showAndWait().ifPresent(type -> {
				if (type == ButtonType.CANCEL) {
					Refresh();
				} else {
					Product item = new Product();

					for (Category category : categoryList) {
						if (category.getName() == categoryCbx.getValue()) {
							item.setCategory_id(category.getId());
						}
					}

					for (Unit unit : unitList) {
						if (unit.getName() == unitCbx.getValue()) {
							item.setUnit_id(unit.getId());
						}
					}
					item.setName(nameTf.getText());
					item.setDescription(descriptionTf.getText());
					item.setPrice_sell(Integer.parseInt(priceTf.getText()));
					item.setActive(activeChkbox.isSelected());

					ProductDao.instance.Create(item);
					Refresh();
					util.AlertUtil.successfully().show();
				}
			});
		}
	}

	public void deleteProduct() {
		Product item = Table.getSelectionModel().getSelectedItem();
		if (item == null) {
			util.AlertUtil.deleteNull().show();
		} else {
			util.AlertUtil.confirmToDelete().showAndWait().ifPresent(type -> {
				if (type == ButtonType.CANCEL) {
					Refresh();
				} else {
					ProductDao.instance.Delete(String.valueOf(item.getId()));
					Refresh();
					util.AlertUtil.successfully().show();
				}
			});
		}
	}

	public void editProduct() {
		Product item = Table.getSelectionModel().getSelectedItem();
		if (item == null) {
			util.AlertUtil.editNull().show();
		} else {
			if (checkEmpty(new String[] { categoryCbx.getValue(), unitCbx.getValue(), nameTf.getText(), priceTf.getText() })) {
				util.AlertUtil.empty().show();
			} else if (ProductDao.instance.checkDouble(new String[] { nameTf.getText() }) && !item.getName().equals(nameTf.getText())) {
				util.AlertUtil.existing().show();
			} else if(checkPrice(priceTf.getText())) {
				util.AlertUtil.priceInvalid().show();
			} else {
				util.AlertUtil.confirmToEdit().showAndWait().ifPresent(type -> {
					if (type == ButtonType.CANCEL) {
						Refresh();
					} else {

						String categoryId = null;
						for (Category category : categoryList) {
							if (category.getName() == categoryCbx.getValue()) {
								categoryId = Integer.toString(category.getId());
							}
						}

						String unitId = null;
						for (Unit unit : unitList) {
							if (unit.getName() == unitCbx.getValue()) {
								unitId = Integer.toString(unit.getId());
							}
						}

						ProductDao.instance.Edit(new String[] { categoryId, unitId, nameTf.getText(), descriptionTf.getText(), priceTf.getText(),
								activeChkbox.isSelected() == true ? "1" : "0", String.valueOf(item.getId()) });
						Refresh();
						util.AlertUtil.successfully().show();
					}
				});
			}
		}
	}

	public void searchProduct() {
		ObservableList<Product> searchList = FXCollections.observableArrayList();
		String searchName = nameTf.getText();
		if (searchName == null || searchName.isEmpty()) {
			util.AlertUtil.searchNull().show();
		} else {
			searchList = (ObservableList<Product>) ProductDao.instance.searchProduct(searchName);
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
		Product selected = Table.getSelectionModel().getSelectedItem();
		if (selected != null) {
			
			for (Category category : categoryList) {
				if (category.getId() == selected.getCategory_id()) {
					categoryCbx.setValue(category.getName());
				}
			}

			for (Unit unit : unitList) {
				if (unit.getId() == selected.getUnit_id()) {
					unitCbx.setValue(unit.getName());
				}
			}

			nameTf.setText(selected.getName());
			priceTf.setText(String.valueOf(selected.getPrice_sell()));
			descriptionTf.setText(selected.getDescription());
			activeChkbox.setSelected(selected.isActive());
			
		}
	}

	public void Refresh() {
		Table.getItems().clear();
		Table.setItems(allList());
	}

	public ObservableList<Product> allList() {
		ObservableList<Product> data = FXCollections.observableArrayList();
		data = (ObservableList<Product>) ProductDao.instance.getAllProduct(0,1);
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

	public Boolean checkPrice(String price) {
		String PRICE_PATTERN = "[0-9]{0,15}";
		if (Pattern.matches(PRICE_PATTERN, price)) {
			return false;
		} else {
			return true;
		}
	}
}
