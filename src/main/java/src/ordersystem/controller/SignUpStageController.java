package src.ordersystem.controller;

import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SignUpStageController {
    @FXML
    public ComboBox comboBox;
    @FXML
    public TextField nickNameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField confirmPasswordField;
    @FXML
    public TextField avatarField;
    @FXML
    public VBox vBox;
    @FXML
    public Button previewButton;
    @FXML
    public Button confirmButton;
    public void init(){
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "学生/老师注册",
                        "商户注册",
                        "管理员注册"
                );
        comboBox.setItems(options);
        // 添加值改变监听器
        comboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                switch (newValue){
                    case "学生/老师注册":
                        drawPurchaser();
                        break;
                    case "商户注册":
                        drawSeller();
                        break;
                    case "管理员注册":
                        drawAdministrator();
                        break;
                }
            }
        });
        comboBox.setValue("学生/老师注册");
    }
    public void drawPurchaser(){
        vBox.getChildren().clear();
        HBox genderBox = new HBox();
        Label genderLabel = new Label("     性别：");
        genderLabel.setFont(Font.font(20.0));
        TextField genderField = new TextField();
        genderBox.getChildren().addAll(genderLabel,new Label(),genderField);
        HBox studentIDBox = new HBox();
        Label studentIDLabel = new Label("学/工号：");
        studentIDLabel.setFont(Font.font(20.0));
        TextField idField = new TextField();
        studentIDBox.getChildren().addAll(studentIDLabel, new Label(),idField);
        vBox.getChildren().addAll(genderBox,new Label(),studentIDBox);
    }
    public void drawSeller(){
        vBox.getChildren().clear();
        HBox infoBox = new HBox();
        Label infoLabel = new Label("       简介：");
        infoLabel.setFont(Font.font(20.0));
        TextField infoField = new TextField();
        infoBox.getChildren().addAll(infoLabel,new Label(),infoField);
        HBox addressBox = new HBox();
        Label addressLabel = new Label("       地址：");
        addressLabel.setFont(Font.font(20.0));
        TextField addressField = new TextField();
        addressBox.getChildren().addAll(addressLabel, new Label(),addressField);
        HBox featureDishBox = new HBox();
        Label featureDishLabel = new Label("特色菜品：");
        featureDishLabel.setFont(Font.font(20.0));
        TextField featureDishField = new TextField();
        featureDishBox.getChildren().addAll(featureDishLabel, new Label(), featureDishField);
        vBox.getChildren().addAll(infoBox,new Label(),addressBox,new Label(),featureDishBox);

    }
    public void drawAdministrator(){
        vBox.getChildren().clear();
        HBox validBox = new HBox();
        Label validLabel = new Label("请输入验证秘钥：");
        validLabel.setFont(Font.font(20.0));
        TextField validField = new TextField();
        validBox.getChildren().addAll(validLabel,new Label(),validField);
        vBox.getChildren().addAll(validBox);
    }
    @FXML
    protected void handlePreviewClick(){
        String imageURL = avatarField.getText();
        Stage stage = new Stage();
        try {
            ImageView imageView = new ImageView(new Image(imageURL));
            imageView.setFitWidth(200);
            imageView.setFitHeight(200);
            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(imageView);
            Scene scene = new Scene(stackPane);
            stage.setScene(scene);
            stage.show();
        }catch (IllegalArgumentException e){
            // 提示信息
            Label label = new Label("路径不正确！");
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
}
