package src.ordersystem.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import src.ordersystem.MainApplication;
import src.ordersystem.SQLLoader;
import src.ordersystem.entity.Administrator;
import src.ordersystem.entity.Purchaser;
import src.ordersystem.entity.Seller;
import src.ordersystem.entity.User;

import java.io.IOException;
import java.sql.SQLException;

public class AdministratorMainStageController {
    @FXML
    public Label nameLabel;
    @FXML
    public TextField idField;
    @FXML
    public Button searchButton;
    @FXML
    public Button getFeatureButton;
    @FXML
    public Button getTotalFeatureButton;
    public void init(User user){
        nameLabel.setText(user.getName());
    }
    @FXML
    protected void handleSearch() throws SQLException, IOException {
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        int id = Integer.parseInt(idField.getText());
        User user = sqlLoader.searchUser(id);
        if(user.getClass().equals(Purchaser.class)){
            enterPurchaserStage(user);
        }else if(user.getClass().equals(Seller.class)){
            enterSellerStage(user);
        }else if(user.getClass().equals(Administrator.class)){
            // 提示信息
            Label label = new Label("该用户为管理员！");
            StackPane root = new StackPane();
            root.getChildren().add(label);
            Stage primaryStage = new Stage();
            Scene scene = new Scene(root, 250, 150);
            primaryStage.setScene(scene);
            primaryStage.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(event -> primaryStage.close());
            delay.play();
        }else{
            // 提示信息
            Label label = new Label("该用户不存在！");
            StackPane root = new StackPane();
            root.getChildren().add(label);
            Stage primaryStage = new Stage();
            Scene scene = new Scene(root, 250, 150);
            primaryStage.setScene(scene);
            primaryStage.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(event -> primaryStage.close());
            delay.play();
        }
    }
    public void enterPurchaserStage(User user) throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("managePurchaserStage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ManagePurchaserStageController managePurchaserStageController = fxmlLoader.getController();
        managePurchaserStageController.purchaser = (Purchaser) user;
        managePurchaserStageController.init();
        stage.setScene(scene);
        stage.show();
    }
    public void enterSellerStage(User user) throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("manageSellerStage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ManageSellerStageController manageSellerStageController = fxmlLoader.getController();
        manageSellerStageController.seller = (Seller)user;
        manageSellerStageController.init();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void handleGetFeatureClick() throws SQLException {
        int purchaserID = Integer.parseInt(idField.getText());
        String s;
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        s = sqlLoader.analyzeUserActivityPattern(purchaserID);
        Label label = new Label(s);
        StackPane root = new StackPane();
        root.getChildren().add(label);
        Stage primaryStage = new Stage();
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    protected void handleGetTotalFeatureClick() throws SQLException {
        String s;
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        s = sqlLoader.analyzeUserGroup();
        Label label = new Label(s);
        StackPane root = new StackPane();
        root.getChildren().add(label);
        Stage primaryStage = new Stage();
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
