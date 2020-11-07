module application {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.sql;

    opens application to javafx.fxml; exports application;
    opens controller to javafx.fxml; exports controller;
    opens dao to javafx.fxml; exports dao;
    opens model to javafx.fxml; exports model;
    opens util to javafx.fxml; exports util;
    
    
}