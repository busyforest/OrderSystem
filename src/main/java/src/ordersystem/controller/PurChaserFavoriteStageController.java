package src.ordersystem.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import src.ordersystem.SQLLoader;
import src.ordersystem.entity.Dish;
import src.ordersystem.entity.Purchaser;
import src.ordersystem.entity.Seller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class PurChaserFavoriteStageController {
    @FXML
    public Pagination pagination;
    @FXML
    public ComboBox methodBox;
    @FXML
    public ComboBox timeBox;
    @FXML
    public Button chooseButton;
    public Purchaser purchaser;
    private ArrayList<Dish> dishes;
    private ArrayList<Dish> resultDishes;
    private static final int ITEMS_PER_PAGE = 3;
    private int totalItems = 30;
    @FXML
    public void init() throws SQLException {
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "在线点餐",
                        "排队点餐"
                );
        ObservableList<String> options1 =
                FXCollections.observableArrayList(
                        "1 week",
                        "1 month",
                        "1 year"
                );
        methodBox.setItems(options);
        timeBox.setItems(options1);
        methodBox.setValue("在线点餐");
        timeBox.setValue("1 week");
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        dishes = sqlLoader.getFavoriteDish(purchaser.getId());
        totalItems = dishes.size();
        int pageCount = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
        pagination.setPageCount(pageCount);
        pagination.setPageFactory(this::createPage);
    }
    private VBox createPage(int pageIndex) {
        VBox box = new VBox(5);
        int pageStart = pageIndex * ITEMS_PER_PAGE;
        int pageEnd = Math.min(pageStart + ITEMS_PER_PAGE, totalItems);
        for (int i = pageStart; i < pageEnd; i++) {
            HBox hbox = createItemBox(i, dishes);
            box.getChildren().add(hbox);
        }

        return box;
    }

    private HBox createItemBox(int index, ArrayList<Dish> dishes) {
        HBox hbox = new HBox();
        ImageView imageView = new ImageView(new Image(dishes.get(index).getDishPictureUrl()));
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);

        Label label1 = new Label(dishes.get(index).getDishName());
        label1.setFont(new javafx.scene.text.Font(20));

        Label label2 = new Label("菜品简介：" + dishes.get(index).getDishDescription());
        label2.setFont(new javafx.scene.text.Font(15));

        Label label3 = new Label("线上菜品销量："+dishes.get(index).getOnline_sales_volume());
        Label label4 = new Label("线下菜品销量："+dishes.get(index).getOffline_sales_volume());

        VBox vbox = new VBox();
        vbox.getChildren().addAll(label1, label2, label3,   label4);
        hbox.getChildren().addAll(imageView, new Label(), vbox);
        hbox.setSpacing(10);

        return hbox;
    }
    @FXML
    protected void handleChooseClick() throws SQLException {
        pagination.setPageFactory(null);
        pagination.setPageCount(0);
        resultDishes = new ArrayList<>();
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        resultDishes = sqlLoader.checkDishSalesByPurchaseMethod(dishes, (String) methodBox.getValue(), (String) timeBox.getValue());
        totalItems = resultDishes.size();
        int pageCount = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
        pagination.setPageCount(pageCount);
        pagination.setPageFactory(this::createSearchPage);

    }
    private VBox createSearchPage(int pageIndex) {
        VBox box = new VBox(5);
        int pageStart = pageIndex * ITEMS_PER_PAGE;
        int pageEnd = Math.min(pageStart + ITEMS_PER_PAGE, totalItems);

        for (int i = pageStart; i < pageEnd; i++) {
            HBox hbox = createItemBox(i, resultDishes);
            box.getChildren().add(hbox);
        }

        return box;
    }

}
