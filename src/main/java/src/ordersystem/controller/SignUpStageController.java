package src.ordersystem.controller;

import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import src.ordersystem.MainApplication;
import src.ordersystem.SQLLoader;
import src.ordersystem.entity.Administrator;
import src.ordersystem.entity.Purchaser;
import src.ordersystem.entity.Seller;

import java.io.IOException;
import java.sql.SQLException;

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
        HBox ageBox = new HBox();
        Label ageLabel = new Label("年龄：");
        studentIDLabel.setFont(Font.font(20.0));
        TextField ageField = new TextField();
        ageBox.getChildren().addAll(ageLabel,new Label(), ageField);
        Button confirmButton = new Button("确认注册");
        confirmButton.setFont(Font.font(20.0));
        confirmButton.setOnAction(e->{
            if(nickNameField.getText().isEmpty()||passwordField.getText().isEmpty()||confirmPasswordField.getText().isEmpty()||avatarField.getText().isEmpty()
            || genderField.getText().isEmpty()||idField.getText().isEmpty()||ageField.getText().isEmpty()){
                // 提示信息
                Label label = new Label("请填写完整信息！");
                StackPane root = new StackPane();
                root.getChildren().add(label);
                Stage primaryStage = new Stage();
                Scene scene = new Scene(root, 250, 150);
                primaryStage.setScene(scene);
                primaryStage.show();
                PauseTransition delay = new PauseTransition(Duration.seconds(1));
                delay.setOnFinished(event -> primaryStage.close());
                delay.play();
            } else if (!passwordField.getText().equals(confirmPasswordField.getText())) {
                // 提示信息
                Label label = new Label("密码不一致！");
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
                Purchaser purchaser = new Purchaser();
                purchaser.setName(nickNameField.getText());
                purchaser.setStudentIDOrWorkID(Integer.parseInt(idField.getText()));
                purchaser.setGender(genderField.getText().charAt(0));
                purchaser.setAvatarPath(avatarField.getText());
                purchaser.setAge(Integer.parseInt(ageField.getText()));
                SQLLoader sqlLoader = new SQLLoader();
                try {
                    sqlLoader.connect();
                    sqlLoader.addPurchaser(purchaser,passwordField.getText());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                // 提示信息
                Label label = new Label("注册成功！");
                StackPane root = new StackPane();
                root.getChildren().add(label);
                Stage primaryStage = new Stage();
                Scene scene = new Scene(root, 250, 150);
                primaryStage.setScene(scene);
                primaryStage.show();
                PauseTransition delay = new PauseTransition(Duration.seconds(1));
                delay.setOnFinished(event -> primaryStage.close());
                delay.play();
                Stage stage = (Stage) confirmButton.getScene().getWindow();
                stage.close();
                Stage stage1 = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("purchaserInformationStage-view.fxml"));
                Scene scene1 = null;
                try {
                    scene1 = new Scene(fxmlLoader.load());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                PurChaserInformationStageController purChaserInformationStageController = fxmlLoader.getController();
                purChaserInformationStageController.purchaser = purchaser;
                purChaserInformationStageController.init();
                stage1.setScene(scene1);
                stage1.show();
            }
        });
        vBox.getChildren().addAll(genderBox,new Label(),studentIDBox, new Label(),ageBox,new Label(),confirmButton);
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
        Button confirmButton = new Button("确认注册");
        confirmButton.setFont(Font.font(20.0));
        confirmButton.setOnAction(e->{
            if(nickNameField.getText().isEmpty()||passwordField.getText().isEmpty()||confirmPasswordField.getText().isEmpty()||avatarField.getText().isEmpty()
                    || infoField.getText().isEmpty()||addressField.getText().isEmpty()||featureDishField.getText().isEmpty()){
                // 提示信息
                Label label = new Label("请填写完整信息！");
                StackPane root = new StackPane();
                root.getChildren().add(label);
                Stage primaryStage = new Stage();
                Scene scene = new Scene(root, 250, 150);
                primaryStage.setScene(scene);
                primaryStage.show();
                PauseTransition delay = new PauseTransition(Duration.seconds(1));
                delay.setOnFinished(event -> primaryStage.close());
                delay.play();
            } else if (!passwordField.getText().equals(confirmPasswordField.getText())) {
                // 提示信息
                Label label = new Label("密码不一致！");
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
                Seller seller = new Seller();
                seller.setName(nickNameField.getText());
                seller.setAvatarPath(avatarField.getText());
                seller.setBriefInfomation(infoField.getText());
                seller.setAddress(addressField.getText());
                seller.setFeaturedDish(featureDishField.getText());

                SQLLoader sqlLoader = new SQLLoader();
                try {
                    sqlLoader.connect();
                    sqlLoader.addSeller(seller, passwordField.getText());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                // 提示信息
                Label label = new Label("注册成功！");
                StackPane root = new StackPane();
                root.getChildren().add(label);
                Stage primaryStage = new Stage();
                Scene scene = new Scene(root, 250, 150);
                primaryStage.setScene(scene);
                primaryStage.show();
                PauseTransition delay = new PauseTransition(Duration.seconds(1));
                delay.setOnFinished(event -> primaryStage.close());
                delay.play();
                Stage stage = (Stage) confirmButton.getScene().getWindow();
                stage.close();
                Stage stage1 = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("sellerInformationStage-view.fxml"));
                Scene scene1 = null;
                try {
                    scene1 = new Scene(fxmlLoader.load());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                SellerInformationStageController sellerInformationStageController = fxmlLoader.getController();
                sellerInformationStageController.seller = seller;
                sellerInformationStageController.init();
                stage1.setScene(scene1);
                stage1.show();

            }
        });
        vBox.getChildren().addAll(infoBox,new Label(),addressBox,new Label(),featureDishBox, new Label(), confirmButton);

    }
    public void drawAdministrator(){
        vBox.getChildren().clear();
        HBox validBox = new HBox();
        Label validLabel = new Label("请输入验证秘钥：");
        validLabel.setFont(Font.font(20.0));
        TextField validField = new TextField();
        validBox.getChildren().addAll(validLabel,new Label(),validField);
        Button confirmButton = new Button("确认注册");
        confirmButton.setFont(Font.font(20.0));
        confirmButton.setOnAction(e->{
            if(nickNameField.getText().isEmpty()||passwordField.getText().isEmpty()||confirmPasswordField.getText().isEmpty()||avatarField.getText().isEmpty()
                    || validField.getText().isEmpty()){
                // 提示信息
                Label label = new Label("请填写完整信息！");
                StackPane root = new StackPane();
                root.getChildren().add(label);
                Stage primaryStage = new Stage();
                Scene scene = new Scene(root, 250, 150);
                primaryStage.setScene(scene);
                primaryStage.show();
                PauseTransition delay = new PauseTransition(Duration.seconds(1));
                delay.setOnFinished(event -> primaryStage.close());
                delay.play();
            } else if (!passwordField.getText().equals(confirmPasswordField.getText())) {
                // 提示信息
                Label label = new Label("密码不一致！");
                StackPane root = new StackPane();
                root.getChildren().add(label);
                Stage primaryStage = new Stage();
                Scene scene = new Scene(root, 250, 150);
                primaryStage.setScene(scene);
                primaryStage.show();
                PauseTransition delay = new PauseTransition(Duration.seconds(1));
                delay.setOnFinished(event -> primaryStage.close());
                delay.play();
            } else if (!validField.getText().equals("123456")) {
                // 提示信息
                Label label = new Label("秘钥不正确！");
                StackPane root = new StackPane();
                root.getChildren().add(label);
                Stage primaryStage = new Stage();
                Scene scene = new Scene(root, 250, 150);
                primaryStage.setScene(scene);
                primaryStage.show();
                PauseTransition delay = new PauseTransition(Duration.seconds(1));
                delay.setOnFinished(event -> primaryStage.close());
                delay.play();
            } else{
                Administrator administrator = new Administrator();
                administrator.setName(nickNameField.getText());
                administrator.setAvatarPath(avatarField.getText());
                SQLLoader sqlLoader = new SQLLoader();
                try {
                    sqlLoader.connect();
                    sqlLoader.addAdministrator(administrator, passwordField.getText());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                Label label = new Label("注册成功！");
                StackPane root = new StackPane();
                root.getChildren().add(label);
                Stage primaryStage = new Stage();
                Scene scene = new Scene(root, 250, 150);
                primaryStage.setScene(scene);
                primaryStage.show();
                PauseTransition delay = new PauseTransition(Duration.seconds(1));
                delay.setOnFinished(event -> primaryStage.close());
                delay.play();
                Stage stage = (Stage) confirmButton.getScene().getWindow();
                stage.close();
            }
        });
        vBox.getChildren().addAll(validBox, new Label(),new Label(),new Label(),confirmButton);
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
