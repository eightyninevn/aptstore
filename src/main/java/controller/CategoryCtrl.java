package controller;

import java.net.URL;
import java.util.ResourceBundle;

import dao.CategoryDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
	private Button Add;
	@FXML
	private Button Delete;
	@FXML
	private Button Edit;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		CategoryList = getList();
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
		Category cate = Table.getSelectionModel().getSelectedItem();
		CategoryList.remove(cate);
		CategoryDao.instance.Delete(String.valueOf(cate.getId()));
		Refresh();
	}
	
	public void Refresh() {
		Table.getItems().clear();
		Table.setItems(getList());
	}
	
	public ObservableList<Category> getList() {
		ObservableList<Category> data = FXCollections.observableArrayList();
		data = (ObservableList<Category>) CategoryDao.instance.getAllCategory();
		return data;
		
	}
}
