package src.ordersystem.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

        VBox vbox = new VBox();
        vbox.getChildren().addAll(label1, label2,new Label(),new Label(),new Label());

        hbox.getChildren().addAll(new Label(), vbox);
        hbox.setSpacing(10);

        return hbox;
    }
}
