package src.ordersystem;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;

public class HelloController {
    @FXML
    public TextField idField;
    @FXML
    public TextField passwdField;
    @FXML
    public Button loginButton;
    @FXML
    protected void handleLogin() {
        SQLLoader sqlLoader = new SQLLoader();
        boolean loginSuccess;
        try {
            sqlLoader.connect();
            loginSuccess=sqlLoader.search(Integer.parseInt(idField.getText()),passwdField.getText());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (loginSuccess){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("log in success!");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("log in failed!");
            alert.showAndWait();
        }
    }

}