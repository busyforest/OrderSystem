package src.ordersystem.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import src.ordersystem.entity.Dish;

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
    public void handleConfirmClick(){

    }
    @FXML
    public void handleCancelClick(){

    }
    public void init(){
        dishName.setText(dish.getDishName());
        descriptionField.setPromptText(dish.getDishDescription());
        ingredientField.setPromptText(dish.getIngredients());
        allergensField.setPromptText(dish.getPossibleAllergens());
        priceField.setPromptText(String.valueOf(dish.getDishPrice()));
    }
}
