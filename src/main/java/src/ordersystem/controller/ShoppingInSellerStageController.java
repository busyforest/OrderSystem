package src.ordersystem.controller;

import javafx.animation.PauseTransition;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import src.ordersystem.entity.Dish;
import src.ordersystem.entity.Message;
import src.ordersystem.entity.Purchaser;
import src.ordersystem.entity.Seller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class ShoppingInSellerStageController {
    @FXML
    public Label exLabel;
    @FXML
    public Pagination pagination;
    @FXML
    public Pagination dishCartPagination;
    @FXML
    public ComboBox methodBox;
    @FXML
    public ComboBox timeBox;
    @FXML
    public Label priceLabel;
    @FXML
    public Button payButton;
    @FXML
    public Button searchButton;
    @FXML
    public Button chooseButton;
    @FXML
    public ComboBox comboBox;
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
        comboBox.setItems(options);
        methodBox.setItems(options);
        timeBox.setItems(options1);
        comboBox.setValue("在线点餐");
        methodBox.setValue("在线点餐");
        timeBox.setValue("1 week");
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
            int finalI = i;
            hbox.setOnMouseClicked(event -> {
                try {
                    handleDishClick(dishes.get(finalI));
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
            });
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
        label1.setFont(new Font(20));

        Label label2 = new Label("菜品简介：" + dishes.get(index).getDishDescription());
        label2.setFont(new Font(15));
        Label label3 = new Label("评分："+dishes.get(index).getAvg_mark());
        Label label6 = null;
        Label label4 = new Label("在线销量："+ dishes.get(index).getOnline_sales_volume());
        Label label5 = new Label("线下销量："+ dishes.get(index).getOffline_sales_volume());
        Button button = new Button("添加到购物车");
        Button button1 = new Button("查看历史价格");
        try {
            SQLLoader sqlLoader = new SQLLoader();
            sqlLoader.connect();
            int i = sqlLoader.getFavoriteNum(dishes.get(index).getDishId());
            label6 = new Label("收藏量：" + i);
        }catch (SQLException e){

        }
        VBox vbox = new VBox();
        VBox vBox = new VBox();
        vBox.getChildren().addAll(new Label(),button,new Label(),button1);
        vbox.getChildren().addAll(label1, label2,label3,label6,label4,label5);
        hbox.getChildren().addAll(imageView, new Label(), vbox,new Label(),new Label(),new Label(),vBox);
        button.setOnMouseClicked(event -> {
            try {
                cartList.add(dishes.get(index));
                handleAddDish();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        button1.setOnAction(e->{
            try{
                ArrayList<String> history = new ArrayList<>();
                SQLLoader sqlLoader = new SQLLoader();
                sqlLoader.connect();
                history = sqlLoader.checkDishPriceHistory(dishes.get(index).getDishId());
                StringBuilder stringBuilder = new StringBuilder();
                for(String s:history){
                    stringBuilder.append(s+"\n");
                }
                // 提示信息
                Label label = new Label(stringBuilder.toString());
                StackPane root = new StackPane();
                root.getChildren().add(label);
                Stage primaryStage = new Stage();
                Scene scene = new Scene(root, 500, 300);
                primaryStage.setScene(scene);
                primaryStage.show();
            }catch (SQLException ex){

            }
        });
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
        label1.setFont(new Font(20));

        Label label2 = new Label("价格：" + cartList.get(index).getDishPrice());
        label2.setFont(new Font(15));
        VBox vbox = new VBox();
        vbox.getChildren().addAll(label1, label2);
        hbox.getChildren().addAll(imageView, new Label(), vbox,new Label(),new Label(),new Label());
        hbox.setSpacing(10);

        return hbox;
    }
    public void handlePaying() throws SQLException {
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        boolean isSuccess = sqlLoader.purchaseDishList(this.purchaser.getId(), cartList, (String) comboBox.getValue());
        Label label;
        // 提示信息
        if(isSuccess){
            Message message = new Message();
            Message message1 = new Message();
            message.setSender_id(purchaser.getId());
            message.setReceiver_id(seller.getId());
            message.setMessage("您有新订单了！ 来自"+purchaser.getName()+"的订单");
            message1.setSender_id(seller.getId());
            message1.setReceiver_id(purchaser.getId());
            message1.setMessage("您的订单已提交");
            sqlLoader.insertMessage(message1);
            sqlLoader.insertMessage(message);
            label = new Label("支付成功！");
        }
        else{
            label = new Label("购物车为空或者支付失败!");
        }
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
    public void handleDishClick(Dish dish) throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("dishDetailInformationStage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        DishDetailInformationStageController dishDetailInformationStageController = fxmlLoader.getController();
        dishDetailInformationStageController.purchaser = purchaser;
        dishDetailInformationStageController.init(dish);
        stage.setScene(scene);
        stage.show();

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
}
