package src.ordersystem.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import src.ordersystem.SQLLoader;
import src.ordersystem.entity.Seller;

import java.sql.SQLException;

public class NewDishStageController {
    @FXML
    public TextField nameField;
    @FXML
    public TextField descriptionField;
    @FXML
    public TextField ingredientField;
    @FXML
    public TextField nutritionField;
    @FXML
    public TextField allergenField;
    @FXML
    public TextField priceField;
    @FXML
    public TextField imageField;
    @FXML
    public Button previewButton;
    @FXML
    public Button confirmButton;
    public Seller seller;
    @FXML
    protected void handleConfirmClick() throws SQLException {
        String name = nameField.getText();
        String description = descriptionField.getText();
        String ingredient = ingredientField.getText();
        String nutrition = nutritionField.getText();
        String allergen = allergenField.getText();
        double price = Double.parseDouble(priceField.getText());
        String imagePath = imageField.getText();

        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        sqlLoader.addDish(seller.getId(),name,description,price,imagePath,ingredient,nutrition,allergen);

        // 提示信息
        Label label = new Label("发布成功！");
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
    @FXML
    protected void handlePreviewClick(){
        String imageURL = imageField.getText();
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
