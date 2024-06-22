package src.ordersystem.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import src.ordersystem.entity.Dish;
import src.ordersystem.entity.Purchaser;

import java.sql.SQLException;

public class HistoryDishStageController {
    @FXML
    public Label nameLabel;
    @FXML
    public Label priceLabel;
    @FXML
    public Label detailLabel;
    @FXML
    public Label ingredientsLabel;
    @FXML
    public Label nutritionLabel;
    @FXML
    public Label allergensLabel;
    @FXML
    public ImageView dishImageview;
    public Purchaser purchaser;
    public Dish dish;
    public void init() throws SQLException {
        dishImageview.setImage(new Image(dish.getDishPictureUrl()));
        nameLabel.setText(dish.getDishName());
        priceLabel.setText("￥ " + dish.getDishPrice());
        detailLabel.setText(dish.getDishDescription());
        ingredientsLabel.setText(dish.getIngredients());
        nutritionLabel.setText(dish.getNutritionInfo());
        allergensLabel.setText(dish.getPossibleAllergens());
    }
}
