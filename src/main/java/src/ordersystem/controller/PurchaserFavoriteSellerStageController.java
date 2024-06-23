package src.ordersystem.controller;

import javafx.fxml.FXML;
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

import java.sql.SQLException;
import java.util.ArrayList;

public class PurchaserFavoriteSellerStageController {
    @FXML
    public Pagination pagination;
    public Purchaser purchaser;
    private ArrayList<Seller> sellers;
    private static final int ITEMS_PER_PAGE = 3;
    private int totalItems = 30;
    @FXML
    public void init() throws SQLException {
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        sellers = sqlLoader.getFavoriteSeller(purchaser.getId());
        totalItems = sellers.size();
        int pageCount = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
        pagination.setPageCount(pageCount);
        pagination.setPageFactory(this::createPage);
    }
    private VBox createPage(int pageIndex) {
        VBox box = new VBox(5);
        int pageStart = pageIndex * ITEMS_PER_PAGE;
        int pageEnd = Math.min(pageStart + ITEMS_PER_PAGE, totalItems);
        for (int i = pageStart; i < pageEnd; i++) {
            HBox hbox = createItemBox(i, sellers);
            box.getChildren().add(hbox);
        }

        return box;
    }

    private HBox createItemBox(int index, ArrayList<Seller> sellers) {
        HBox hbox = new HBox();
//        ImageView imageView = new ImageView(new Image(dishes.get(index).getDishPictureUrl()));
//        imageView.setFitHeight(100);
//        imageView.setFitWidth(100);

        Label label1 = new Label(sellers.get(index).getName());
        label1.setFont(new javafx.scene.text.Font(20));

        Label label2 = new Label("简介：" + sellers.get(index).getBriefInfomation());
        label2.setFont(new javafx.scene.text.Font(15));
        VBox vbox = new VBox();
        vbox.getChildren().addAll(label1, label2);
        hbox.getChildren().addAll(new Label(), vbox);
        hbox.setSpacing(10);

        return hbox;
    }

}
