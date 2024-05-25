package src.ordersystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import src.ordersystem.MainApplication;
import src.ordersystem.SQLLoader;
import src.ordersystem.entity.Administrator;
import src.ordersystem.entity.Purchaser;
import src.ordersystem.entity.Seller;
import src.ordersystem.entity.User;

import java.io.IOException;
import java.sql.SQLException;

public class LoginStageController {
    @FXML
    public TextField idField;
    @FXML
    public TextField passwdField;
    @FXML
    public Button loginButton;

    @FXML
    protected void handleLogin() throws IOException, SQLException {
        SQLLoader sqlLoader = new SQLLoader();
        boolean loginSuccess;
        try {
            sqlLoader.connect();
            loginSuccess=sqlLoader.login(Integer.parseInt(idField.getText()),passwdField.getText());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (loginSuccess){
            User user = sqlLoader.searchUser(Integer.parseInt(idField.getText()));
            if(user.getClass().equals(Purchaser.class)){
                enterPurchaserStage(user);
            }else if(user.getClass().equals(Seller.class)){
                enterSellerStage(user);
            }else if(user.getClass().equals(Administrator.class)){
                enterAdministratorStage(user);
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("log in failed!");
            alert.showAndWait();
        }

    }
    public void enterPurchaserStage(User user) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("purchaserMainStage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        PurChaserMainStageController purChaserMainStageController = fxmlLoader.getController();
        purChaserMainStageController.init(user);
        stage.setScene(scene);
        stage.show();
    }
    public void enterSellerStage(User user) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("sellerMainStage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        SellerMainStageController sellerMainStageController = fxmlLoader.getController();
        sellerMainStageController.init(user);
        stage.setScene(scene);
        stage.show();
    }
    public void enterAdministratorStage(User user) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("administratorMainStage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        AdministratorMainStageController administratorMainStageController = fxmlLoader.getController();
        administratorMainStageController.init(user);
        stage.setScene(scene);
        stage.show();
    }
}