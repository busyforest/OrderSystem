package src.ordersystem.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
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
    private Pagination pagination;
    private ArrayList<Dish> dishes;
    private static final int ITEMS_PER_PAGE = 3;
    private int totalItems = 30;
    private Seller seller;
    public Purchaser purchaser;
    @FXML
    public void init(Seller seller) throws SQLException {
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
        ImageView imageView = new ImageView(new Image(dishes.get(index).getDishPictureUrl()));
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
//        imageView.setPreserveRatio(true);

        Label label1 = new Label(dishes.get(index).getDishName());
        label1.setFont(new javafx.scene.text.Font(20));

        Label label2 = new Label("菜品简介：" + dishes.get(index).getDishDescription());
        label2.setFont(new javafx.scene.text.Font(15));
        ArrayList<Dish> dishesPurchased = new ArrayList<>();

        Button button = new Button("添加到购物车");
        button.setOnMouseClicked(event -> {
                try {
                    handleAddDish();
                    dishesPurchased.add(dishes.get(index));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

        Button button2 = new Button("支付");
        button2.setOnMouseClicked(event -> {
                try {
                    SQLLoader sqlLoader = new SQLLoader();
                    sqlLoader.connect();
                    sqlLoader.purchaseDishList(purchaser.getId(), dishesPurchased);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        VBox vbox = new VBox();
        VBox vBox = new VBox();
        vBox.getChildren().addAll(new Label(),button,button2);
        vbox.getChildren().addAll(label1, label2);
        hbox.getChildren().addAll(imageView, new Label(), vbox,new Label(),new Label(),new Label(),vBox);
        hbox.setSpacing(10);

        return hbox;
    }
    private void handleAddDish() throws IOException, SQLException {
        // 创建一个标签来显示文本
        Label label = new Label("添加成功！");

        // 将标签放入一个 StackPane 中
        StackPane root = new StackPane();
        root.getChildren().add(label);
        Stage primaryStage = new Stage();
        // 创建一个 Scene
        Scene scene = new Scene(root, 250, 150);

        // 设置 Stage
        primaryStage.setScene(scene);

        // 显示 Stage
        primaryStage.show();

        // 设置一个暂停转换来关闭 Stage
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event -> primaryStage.close());
        delay.play();
//       Date currentTime = new Date();
//       SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//       String timeString = formatter.format(currentTime);


    }
}
