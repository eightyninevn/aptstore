package controller;

import java.net.URL;
import java.util.ResourceBundle;

import dao.CategoryDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Category;


public class CategoryCtrl implements Initializable {
	
	@FXML
	private TableView<Category> Table;
	@FXML
	private TableColumn<Category, Integer> idColumn;
	@FXML
	private TableColumn<Category, String> nameColumn;
	@FXML
	private TableColumn<Category, Boolean> activeColumn;
	ObservableList<Category> CategoryList = FXCollections.observableArrayList();
	
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
		CategoryList = allList();
		idColumn.setCellValueFactory(new PropertyValueFactory<Category, Integer>("id"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<Category, String>("name"));
		activeColumn.setCellValueFactory(new PropertyValueFactory<Category, Boolean>("active"));
		Table.setItems(CategoryList);
	}
	
	@FXML
	public void addCategory() {
		Category cate = new Category();
		cate.setName(nameTf.getText());
		cate.setActive(activeChkbox.isSelected());
		CategoryDao.instance.Create(cate);
		Refresh();
	}
	
	public void deleteCategory() {
		Category item = Table.getSelectionModel().getSelectedItem();
//		CategoryList.remove(item);
		CategoryDao.instance.Delete(String.valueOf(item.getId()));
		Refresh();
	}
	
	public void editCategory() {
		Category item = Table.getSelectionModel().getSelectedItem();
		CategoryDao.instance.Edit( new String[] {nameTf.getText(), activeChkbox.isSelected()==true?"1":"0", String.valueOf(item.getId())});
		Refresh();
	}
	
	public void searchCategory() {
		ObservableList<Category> searchList = FXCollections.observableArrayList();
		String searchName = nameTf.getText();
		if (searchName == null || searchName.isEmpty()) {
			Alert Alert1 = new Alert(AlertType.WARNING, "Please type Category's name you want to search", ButtonType.YES, ButtonType.NO);
			Alert1.show();
		}
		else {
			searchList = (ObservableList<Category>) CategoryDao.instance.searchCategory(searchName);
			if(searchList.isEmpty()) {
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
		Category selected = Table.getSelectionModel().getSelectedItem();
		nameTf.setText(selected.getName());
		activeChkbox.setSelected(selected.isActive());
	}
	
	public void Refresh() {
		Table.getItems().clear();
		Table.setItems(allList());
	}
	
	public ObservableList<Category> allList() {
		ObservableList<Category> data = FXCollections.observableArrayList();
		data = (ObservableList<Category>) CategoryDao.instance.getAllCategory();
		return data;
		
	}
}
