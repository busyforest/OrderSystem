package src.ordersystem.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import src.ordersystem.SQLLoader;
import src.ordersystem.entity.OrderOverview;
import src.ordersystem.entity.Purchaser;
import src.ordersystem.entity.Seller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class RealFansStageController {
    @FXML
    public Pagination pagination;
    private int totalItems;
    public Purchaser purchaser;
    private static final int ITEMS_PER_PAGE = 5;
    public ArrayList<Purchaser> purchasers;
    public Seller seller;
    @FXML
    public void init() throws SQLException {
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        purchasers = sqlLoader.getReallyFollowers(seller.getId());
        totalItems = purchasers.size();
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
                hbox = createItemBox(i, purchasers);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            box.getChildren().add(hbox);
        }

        return box;
    }

    private HBox createItemBox(int index, ArrayList<Purchaser> purchasers) throws SQLException {
        HBox hbox = new HBox();
        Label label1 = new Label(purchasers.get(index).getName());
        label1.setFont(new javafx.scene.text.Font(20));

        Label label2 = new Label("用户ID: " + purchasers.get(index).getId());
        label2.setFont(new javafx.scene.text.Font(15));

        VBox vbox = new VBox();
        vbox.getChildren().addAll(label1, label2,new Label(),new Label(),new Label());

        Button button = new Button("查看消费分布");
        button.setOnAction(e->{
            ArrayList<String> result = new ArrayList<>();
            try {
                SQLLoader sqlLoader = new SQLLoader();
                sqlLoader.connect();
                result = sqlLoader.showReallyFollowerConsumptionDistribution(purchasers.get(index).getId(),seller.getId());
                StringBuilder stringBuilder = new StringBuilder();
                for(String s:result){
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
                throw new RuntimeException(ex);
            }
        });
        VBox vBox = new VBox();
        vBox.getChildren().addAll(new Label(), button);
        SQLLoader sqlLoader1 = new SQLLoader();
        sqlLoader1.connect();
        hbox.getChildren().addAll(new Label(), vbox,new Label(),new Label(),vBox,new Label());
        hbox.setSpacing(10);

        return hbox;
    }
}
