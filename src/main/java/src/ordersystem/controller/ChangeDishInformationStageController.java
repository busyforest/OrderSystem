package src.ordersystem.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import src.ordersystem.SQLLoader;
import src.ordersystem.entity.Dish;

import java.sql.SQLException;

public class ChangeDishInformationStageController {
    @FXML
    public Label dishName;
    @FXML
    public TextField descriptionField;
    @FXML
    public TextField ingredientField;
    @FXML
    public TextField allergensField;
    @FXML
    public TextField priceField;
    @FXML
    public Button cancelButton;
    @FXML
    public Button confirmButton;
    public Dish dish;

    @FXML
    public void handleConfirmClick() throws SQLException {
        String descriptionString;
        double price;
        String ingredientString;
        String allergensString;
        if(descriptionField.getText().isEmpty()){
            descriptionString = descriptionField.getPromptText();
        }else{
            descriptionString = descriptionField.getText();
        }
        if(ingredientField.getText().isEmpty()){
            ingredientString = ingredientField.getPromptText();
        }else{
            ingredientString = ingredientField.getText();
        }
        if(allergensField.getText().isEmpty()){
            allergensString = allergensField.getPromptText();
        }else {
            allergensString = allergensField.getText();
        }
        if(priceField.getText().isEmpty()) {
            price = Double.parseDouble(priceField.getPromptText());
        }else {
            price = Double.parseDouble(priceField.getText());
        }
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        sqlLoader.updateDish(dish.getDishId(), dish.getDishName(), descriptionString, price, dish.getDishPictureUrl(), ingredientString, dish.getNutritionInfo(), allergensString);
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
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleCancelClick() {
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    public void init() {
        dishName.setText(dish.getDishName());
        descriptionField.setPromptText(dish.getDishDescription());
        ingredientField.setPromptText(dish.getIngredients());
        allergensField.setPromptText(dish.getPossibleAllergens());
        priceField.setPromptText(String.valueOf(dish.getDishPrice()));
    }
}
