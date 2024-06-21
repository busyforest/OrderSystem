package src.ordersystem.controller;

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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import src.ordersystem.MainApplication;
import src.ordersystem.SQLLoader;
import src.ordersystem.entity.Dish;
import src.ordersystem.entity.Purchaser;
import src.ordersystem.entity.Seller;
import src.ordersystem.entity.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class SellerMainStageController {
    @FXML
    public Button searchButton;
    @FXML
    public Label nameLabel;

    @FXML
    private Pagination pagination;
    @FXML
    public TextField searchField;
    public Seller seller;
    private ArrayList<Dish> dishes;
    private ArrayList<Dish> resultDishes;
    private static final int ITEMS_PER_PAGE = 5;
    private int totalItems = 30;
    @FXML
    protected void handleSearch() throws SQLException {
        pagination.setPageFactory(null);
        pagination.setPageCount(0);
        resultDishes = new ArrayList<>();
        String name = searchField.getText();
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        resultDishes = sqlLoader.searchDishes(name,seller);
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
            // 添加点击事件处理程序
            int finalI = i;
            hbox.setOnMouseClicked(event -> {
                try {
                    handleItemClick(resultDishes.get(finalI));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            box.getChildren().add(hbox);
        }

        return box;
    }


    public void init(User user) throws SQLException {
        this.seller = (Seller) user;
        nameLabel.setText(user.getName());
        getAllDishes();
    }
    public void getAllDishes() throws SQLException {
        dishes = new ArrayList<>();
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        dishes = sqlLoader.getDishesInSeller(seller);
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
            // 添加点击事件处理程序
            int finalI = i;
            hbox.setOnMouseClicked(event -> {
                try {
                    handleItemClick(dishes.get(finalI));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
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
        imageView.setFitHeight(105);
        imageView.setFitWidth(113);

        Label label1 = new Label(dishes.get(index).getDishName());
        label1.setFont(new javafx.scene.text.Font(20));

        Button changeButton = new Button("修改价格");
        Button deleteButton = new Button("删除菜品");

        VBox vbox = new VBox();
        vbox.getChildren().addAll(label1);
        VBox vbox1 = new VBox();
        vbox1.getChildren().addAll(changeButton,new Label(),deleteButton);


        hbox.getChildren().addAll(imageView, new Label(), vbox,new Label(),new Label(), new Label(),vbox1);
        hbox.setSpacing(10);

        return hbox;
    }
    private void handleItemClick(Dish dish) throws IOException, SQLException {
//        Stage stage = new Stage();
//        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("shoppingInSellerStage-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        ShoppingInSellerStageController shoppingStageController = fxmlLoader.getController();
//        shoppingStageController.purchaser = this.purchaser;
//        shoppingStageController.init(seller);
//        stage.setScene(scene);
//        stage.show();
    }
}
