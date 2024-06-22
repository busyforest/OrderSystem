package src.ordersystem.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import src.ordersystem.SQLLoader;
import src.ordersystem.entity.Dish;
import src.ordersystem.entity.Purchaser;

import java.sql.SQLException;

public class DishDetailInformationStageController {
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
    @FXML
    public Button favButton;
    public Purchaser purchaser;
    public Dish dish;
    public void init(Dish dish) throws SQLException {
        this.dish = dish;
        dishImageview.setImage(new Image(dish.getDishPictureUrl()));
        nameLabel.setText(dish.getDishName());
        priceLabel.setText("￥ "+ dish.getDishPrice());
        detailLabel.setText(dish.getDishDescription());
        ingredientsLabel.setText(dish.getIngredients());
        nutritionLabel.setText(dish.getNutritionInfo());
        allergensLabel.setText(dish.getPossibleAllergens());
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        if(sqlLoader.checkFavoriteDish(purchaser.getId(), dish.getDishId())){
            favButton.setText("已收藏");
        }

    }
    @FXML
    protected void handleFavButtonClicked() throws SQLException {
        // 提示信息
        Label label = new Label("添加成功！");
        if(favButton.getText().equals("取消收藏")){
            label.setText("已取消收藏！");
            favButton.setText("收藏这道菜");
        }else{
            favButton.setText("取消收藏");
        }
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        sqlLoader.favoriteDish(purchaser.getId(), dish.getDishId());
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
