package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @SuppressWarnings("exports")
	@Override
    public void start(Stage stage) throws IOException {
    		
        scene = new Scene(loadFXML("Product"), 900, 700);
        scene.getStylesheets().add(getClass().getResource("style/style.css").toExternalForm());
        stage.setTitle("Phầm Mềm Quản Lý Cửa Hàng Vật Liệu Xây Dựng APT ngày 09-11-2020");
        stage.setScene(scene);
        stage.show();
    }
//========================================================================================\\
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("ui/"+fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}