package src.ordersystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import src.ordersystem.MainApplication;
import src.ordersystem.SQLLoader;
import src.ordersystem.entity.Dish;
import src.ordersystem.entity.Seller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ShoppingInSellerStageController {
    @FXML
    public Label exLabel;
    @FXML
    private Pagination pagination;
    private ArrayList<Dish> dishes;
    private static final int ITEMS_PER_PAGE = 3;
    private int totalItems = 30;
    private Seller seller;
    @FXML
    public void init(Seller seller) throws SQLException {
        exLabel.setText("你进入了商家："+seller.getName());
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
            HBox hbox = createItemBox(i);
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

    private HBox createItemBox(int index) {
        HBox hbox = new HBox();
        ImageView imageView = new ImageView(new Image("https://ordersystem-images.oss-cn-shanghai.aliyuncs.com/nahida.jpg"));
        imageView.setFitHeight(105);
        imageView.setFitWidth(113);
        imageView.setPreserveRatio(true);

        Label label1 = new Label(dishes.get(index).getDishName());
        label1.setFont(new javafx.scene.text.Font(20));

        Label label2 = new Label("菜品简介：" + dishes.get(index).getDishDescription());
        label2.setFont(new javafx.scene.text.Font(15));

        VBox vbox = new VBox();
        vbox.getChildren().addAll(label1, label2);
        hbox.getChildren().addAll(imageView, new Label(), vbox);
        hbox.setSpacing(10);

        return hbox;
    }
    private void handleItemClick(Seller seller) throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("shoppingInSellerStage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ShoppingInSellerStageController shoppingStageController = fxmlLoader.getController();
        shoppingStageController.init(seller);
        stage.setScene(scene);
        stage.show();
    }
}
