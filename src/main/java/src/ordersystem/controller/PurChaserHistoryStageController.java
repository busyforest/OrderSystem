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
import src.ordersystem.entity.OrderOverview;
import src.ordersystem.entity.Purchaser;
import src.ordersystem.entity.Seller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class PurChaserHistoryStageController {
    @FXML
    public Pagination pagination;
    private int totalItems;
    public Purchaser purchaser;
    private static final int ITEMS_PER_PAGE = 5;
    public ArrayList<OrderOverview> orderList;
    @FXML
    public void init() throws SQLException {
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        orderList = sqlLoader.getHistory(purchaser);
        totalItems = orderList.size();
        int pageCount = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
        pagination.setPageCount(pageCount);
        pagination.setPageFactory(this::createPage);
    }

    private VBox createPage(int pageIndex)  {
        VBox box = new VBox(5);
        int pageStart = pageIndex * ITEMS_PER_PAGE;
        int pageEnd = Math.min(pageStart + ITEMS_PER_PAGE, totalItems);

        for (int i = pageStart; i < pageEnd; i++) {
            HBox hbox = null;
            try {
                hbox = createItemBox(i, orderList);
                // 添加点击事件处理程序
                int finalI = i;
                hbox.setOnMouseClicked(event -> {
                    try {
                        handleItemClick(orderList.get(finalI));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            box.getChildren().add(hbox);
        }

        return box;
    }

    private HBox createItemBox(int index, ArrayList<OrderOverview> orderList) throws SQLException {
        HBox hbox = new HBox();
        Label label1 = new Label("订单序号："+orderList.get(index).getOrderID());
        label1.setFont(new javafx.scene.text.Font(20));

        Label label2 = new Label("下单时间: " + orderList.get(index).getOrderTime());
        label2.setFont(new javafx.scene.text.Font(20));

        Label label = new Label("订单状态：" + orderList.get(index).getDishStatus());
        label2.setFont(new javafx.scene.text.Font(20));

        VBox vbox = new VBox();
        vbox.getChildren().addAll(label1, label2,label,new Label(),new Label(),new Label());

        Button button = new Button("评价商家");
        Button button1 = new Button("收藏商家");
        SQLLoader sqlLoader1 = new SQLLoader();
        sqlLoader1.connect();
        Seller seller1 = sqlLoader1.getSellerByOrder(orderList.get(index));
        if (sqlLoader1.checkFavoriteSeller(purchaser.getId(), seller1.getId())) {
            button1.setText("取消收藏");
        }
        button.setOnAction(e -> {
            try {
                handleCommentClick(orderList.get(index));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        button1.setOnAction(e->{
            SQLLoader sqlLoader = new SQLLoader();
            // 提示信息
            Label label3 = new Label("收藏成功！");
            if (button1.getText().equals("取消收藏")) {
                label3.setText("已取消收藏！");
                button1.setText("收藏商家");
            } else {
                button1.setText("取消收藏");
            }
            try {
                sqlLoader.connect();
                Seller seller = sqlLoader1.getSellerByOrder(orderList.get(index));
                sqlLoader.favoriteSeller(purchaser.getId(), seller.getId());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            StackPane root = new StackPane();
            root.getChildren().add(label3);
            Stage primaryStage = new Stage();
            Scene scene = new Scene(root, 250, 150);
            primaryStage.setScene(scene);
            primaryStage.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(event -> primaryStage.close());
            delay.play();
        });
        label2.setFont(new javafx.scene.text.Font(20));
        hbox.getChildren().addAll(new Label(), vbox,new Label(),new Label(),button,new Label(), button1);
        hbox.setSpacing(10);

        return hbox;
    }

    private void handleCommentClick(OrderOverview orderOverview) throws IOException, SQLException {
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("commentSellerStage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        CommentSellerStageController commentSellerStageController = fxmlLoader.getController();
        commentSellerStageController.purchaser = purchaser;
        commentSellerStageController.seller = sqlLoader.getSellerByOrder(orderOverview);
        commentSellerStageController.init();
        stage.setScene(scene);
        stage.show();
    }

    public void handleItemClick(OrderOverview orderOverview) throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("historyOrderDetailStage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        HistoryOrderDetailStageController historyOrderDetailStageController = fxmlLoader.getController();
        historyOrderDetailStageController.orderOverview = orderOverview;
        historyOrderDetailStageController.init();
        stage.setScene(scene);
        stage.show();
    }
}
