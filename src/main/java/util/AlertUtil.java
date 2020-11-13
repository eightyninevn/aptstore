package util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

@SuppressWarnings("exports")
public class AlertUtil {

	public static Alert empty() {
		return new Alert(AlertType.WARNING, "Please fill in all the required field(s).", ButtonType.CLOSE);
	}

	public static Alert existing() {
		return new Alert(AlertType.WARNING, "The item is existing!", ButtonType.CLOSE);
	}

	public static Alert confirmToAdd() {
		return new Alert(AlertType.CONFIRMATION, "Do you want to add a new item ?", ButtonType.YES, ButtonType.CANCEL);
	}

	public static Alert successfully() {
		return new Alert(AlertType.INFORMATION, "Successfully!", ButtonType.CLOSE);
	}

	public static Alert deleteNull() {
		return new Alert(AlertType.INFORMATION, "Please select an item you want to delete.", ButtonType.YES);
	}

	public static Alert confirmToDelete() {
		return new Alert(AlertType.CONFIRMATION, "Do you want to delete selected item ?", ButtonType.YES,
				ButtonType.CANCEL);
	}

	public static Alert editNull() {
		return new Alert(AlertType.INFORMATION, "Please select an item you want to edit.", ButtonType.YES);
	}

	public static Alert confirmToEdit() {
		return new Alert(AlertType.CONFIRMATION, "Do you want to edit selected item ?", ButtonType.YES,
				ButtonType.CANCEL);
	}

	public static Alert searchNull() {
		return new Alert(AlertType.INFORMATION, "Please enter the value you want to search.", ButtonType.YES);
	}

	public static Alert searchNoReSult() {
		return new Alert(AlertType.INFORMATION, "No result found.", ButtonType.YES);
	}

}