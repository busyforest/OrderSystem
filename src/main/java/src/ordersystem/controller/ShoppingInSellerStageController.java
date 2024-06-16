package src.ordersystem.controller;

import javafx.animation.PauseTransition;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import src.ordersystem.MainApplication;
import src.ordersystem.SQLLoader;
import src.ordersystem.entity.Dish;
import src.ordersystem.entity.Purchaser;
import src.ordersystem.entity.Seller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShoppingInSellerStageController {
    @FXML
    public Label exLabel;
    @FXML
    public Pagination pagination;
    @FXML
    public Pagination dishCartPagination;
    @FXML
    public Label priceLabel;
    @FXML
    public Button payButton;
    @FXML
    public Button searchButton;
    @FXML
    public TextField searchField;
    private ArrayList<Dish> dishes;
    private ArrayList<Dish> cartList;
    private ArrayList<Dish> resultDishes;
    private static final int ITEMS_PER_PAGE = 3;
    private int totalItems = 30;
    private int totalCartItems = 0;
    private Seller seller;
    public Purchaser purchaser;
    @FXML
    public void init(Seller seller) throws SQLException {
        cartList = new ArrayList<>();
        exLabel.setText(purchaser.getName()+", 你进入了商家："+seller.getName());
        this.seller = seller;
        getDishesInSeller();
        totalItems = dishes.size();
        int pageCount = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
        pagination.setPageCount(pageCount);
        pagination.setPageFactory(this::createPage);
    }
    public void getDishesInSeller() throws SQLException {
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        dishes = sqlLoader.getDishesInSeller(this.seller);
    }

    private VBox createPage(int pageIndex) {
        VBox box = new VBox(5);
        int pageStart = pageIndex * ITEMS_PER_PAGE;
        int pageEnd = Math.min(pageStart + ITEMS_PER_PAGE, totalItems);
        for (int i = pageStart; i < pageEnd; i++) {
            HBox hbox = createItemBox(i, dishes);
//             添加点击事件处理程序
//            int finalI = i;
//            hbox.setOnMouseClicked(event -> {
//                try {
//                    handleItemClick(sellers.get(finalI));
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                } catch (SQLException e) {
//                    throw new RuntimeException(e);
//                }
//            });
            box.getChildren().add(hbox);
        }

        return box;
    }

    private HBox createItemBox(int index, ArrayList<Dish> dishes) {
        HBox hbox = new HBox();
        ImageView imageView = new ImageView(new Image(dishes.get(index).getDishPictureUrl()));
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
//        imageView.setPreserveRatio(true);

        Label label1 = new Label(dishes.get(index).getDishName());
        label1.setFont(new javafx.scene.text.Font(20));

        Label label2 = new Label("菜品简介：" + dishes.get(index).getDishDescription());
        label2.setFont(new javafx.scene.text.Font(15));
        Button button = new Button("添加到购物车");
        button.setOnMouseClicked(event -> {
                try {
                    cartList.add(dishes.get(index));
                    handleAddDish();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        VBox vbox = new VBox();
        VBox vBox = new VBox();
        vBox.getChildren().addAll(new Label(),button);
        vbox.getChildren().addAll(label1, label2);
        hbox.getChildren().addAll(imageView, new Label(), vbox,new Label(),new Label(),new Label(),vBox);
        hbox.setSpacing(10);

        return hbox;
    }
    private void handleAddDish() throws IOException{
        totalCartItems = cartList.size();
        int pageCount = (int) Math.ceil((double) totalCartItems / ITEMS_PER_PAGE);
        dishCartPagination.setPageCount(pageCount);
       dishCartPagination.setPageFactory(this::createCartPage);
        float sum = 0;
        for(Dish dish : cartList){
            sum += dish.getDishPrice();
        }
        priceLabel.setText(String.valueOf(sum));


        // 提示信息
        Label label = new Label("添加成功！");
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
    private VBox createCartPage(int pageIndex) {
        VBox box = new VBox(5);
        int pageStart = pageIndex * ITEMS_PER_PAGE;
        int pageEnd = Math.min(pageStart + ITEMS_PER_PAGE, totalCartItems);
        for (int i = pageStart; i < pageEnd; i++) {
            HBox hbox = createCartItemBox(i);
            box.getChildren().add(hbox);
        }

        return box;
    }
    private HBox createCartItemBox(int index) {
        HBox hbox = new HBox();
        ImageView imageView = new ImageView(new Image(cartList.get(index).getDishPictureUrl()));
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);

        Label label1 = new Label(cartList.get(index).getDishName());
        label1.setFont(new javafx.scene.text.Font(20));

        Label label2 = new Label("价格：" + cartList.get(index).getDishPrice());
        label2.setFont(new javafx.scene.text.Font(15));
        VBox vbox = new VBox();
        vbox.getChildren().addAll(label1, label2);
        hbox.getChildren().addAll(imageView, new Label(), vbox,new Label(),new Label(),new Label());
        hbox.setSpacing(10);

        return hbox;
    }
    public void handlePaying() throws SQLException {
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        sqlLoader.purchaseDishList(this.purchaser.getId(), cartList);
        // 提示信息
        Label label = new Label("支付成功！");
        StackPane root = new StackPane();
        root.getChildren().add(label);
        Stage primaryStage = new Stage();
        Scene scene = new Scene(root, 250, 150);
        primaryStage.setScene(scene);
        primaryStage.show();
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event -> primaryStage.close());
        delay.play();
        Stage stage = (Stage) payButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    protected void handleSearch() throws SQLException {
        pagination.setPageFactory(null);
        pagination.setPageCount(0);
        resultDishes = new ArrayList<>();
        String name = searchField.getText();
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        resultDishes = sqlLoader.searchDishes(name, this.seller);
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
