package src.ordersystem.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import src.ordersystem.SQLLoader;
import src.ordersystem.entity.Purchaser;

import java.sql.SQLException;

public class ManagePurchaserStageController {
    @FXML
    public Label idLabel;
    @FXML
    public TextField nameField;
    @FXML
    public TextField genderField;
    @FXML
    public TextField ageField;
    @FXML
    public TextField studentIDField;
    @FXML
    public Button deleteButton;
    @FXML
    public Button confirmButton;
    public Purchaser purchaser;
    @FXML
    protected void handleDeleteClick() throws SQLException {
        Purchaser purchaser1 = purchaser;
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
        sqlLoader1.deletePurchaser(purchaser1.getId());

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
        Purchaser purchaser1 = purchaser;
        if(nameField.getText().isEmpty()){
            purchaser1.setName(nameField.getPromptText());
        }else{
            purchaser1.setName(nameField.getText());
        }
        if (genderField.getText().isEmpty()){
            purchaser1.setGender(genderField.getPromptText().charAt(0));
        }else {
            purchaser1.setGender(genderField.getText().charAt(0));
        }
        if(studentIDField.getText().isEmpty()){
            purchaser1.setStudentIDOrWorkID(Integer.parseInt(studentIDField.getPromptText()));
        }else {
            purchaser1.setStudentIDOrWorkID(Integer.parseInt(studentIDField.getText()));
        }
        if (ageField.getText().isEmpty()){
            purchaser1.setAge(Integer.parseInt(ageField.getPromptText()));
        }else{
            purchaser1.setAge(Integer.parseInt(ageField.getText()));
        }
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        sqlLoader.updatePurchaser(purchaser1);
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
        idLabel.setText(String.valueOf(purchaser.getId()));
        nameField.setPromptText(purchaser.getName());
        genderField.setPromptText(String.valueOf(purchaser.getGender()));
        studentIDField.setPromptText(String.valueOf(purchaser.getStudentIDOrWorkID()));
        ageField.setPromptText(String.valueOf(purchaser.getAge()));
    }
}
