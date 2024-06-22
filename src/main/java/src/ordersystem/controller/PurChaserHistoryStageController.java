package src.ordersystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
        button.setOnAction(e -> {
            try {
                handleCommentClick();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        label2.setFont(new javafx.scene.text.Font(20));
        hbox.getChildren().addAll(new Label(), vbox,new Label(),new Label(),button);
        hbox.setSpacing(10);

        return hbox;
    }

    private void handleCommentClick() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("commentSellerStage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        CommentSellerStageController commentSellerStageController = fxmlLoader.getController();
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
