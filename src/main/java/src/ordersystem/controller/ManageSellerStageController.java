package src.ordersystem.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import src.ordersystem.SQLLoader;
import src.ordersystem.entity.Seller;

import java.sql.SQLException;

public class ManageSellerStageController {
    @FXML
    public Label idLabel;
    @FXML
    public TextField nameField;
    @FXML
    public TextField descriptionField;
    @FXML
    public TextField addressField;
    @FXML
    public TextField featureDishField;
    @FXML
    public TextField markField;
    @FXML
    public Button deleteButton;
    @FXML
    public Button confirmButton;
    public Seller seller;
    @FXML
    protected void handleDeleteClick() throws SQLException {
        Seller seller1 = seller;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("确认删除");
        alert.setContentText("你确定要删除该用户吗？");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    Stage stage = (Stage) confirmButton.getScene().getWindow();
                    stage.close();
                });
        SQLLoader sqlLoader1 = new SQLLoader();
        sqlLoader1.connect();
        sqlLoader1.deleteSeller(seller1.getId());

        // 提示信息
        Label label = new Label("删除成功！");
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
    @FXML
    protected void handleConfirmClick() throws SQLException {
        Seller seller1 = seller;
        if(nameField.getText().isEmpty()){
            seller1.setName(nameField.getPromptText());
        }else{
            seller1.setName(nameField.getText());
        }
        if (descriptionField.getText().isEmpty()){
            seller1.setBriefInfomation(descriptionField.getPromptText());
        }else {
            seller1.setBriefInfomation(descriptionField.getText());
        }
        if(addressField.getText().isEmpty()){
            seller1.setAddress(addressField.getPromptText());
        }else {
            seller1.setAddress(addressField.getText());
        }
        if (featureDishField.getText().isEmpty()){
            seller1.setFeaturedDish(featureDishField.getPromptText());
        }else {
            seller1.setFeaturedDish(featureDishField.getText());
        }
        if (markField.getText().isEmpty()){
            seller1.setAvg_mark(Float.parseFloat(markField.getPromptText()));
        }else {
            seller1.setAvg_mark(Float.parseFloat(markField.getText()));
        }
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        sqlLoader.updateSeller(seller1);
        // 提示信息
        Label label = new Label("修改成功！");
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
    public void init(){
        idLabel.setText(String.valueOf(seller.getId()));
        nameField.setPromptText(seller.getName());
        descriptionField.setPromptText(seller.getBriefInfomation());
        addressField.setPromptText(seller.getAddress());
        featureDishField.setPromptText(seller.getFeaturedDish());
        markField.setPromptText(String.valueOf(seller.getAvg_mark()));
    }
}
