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
import src.ordersystem.entity.Dish;
import src.ordersystem.entity.OrderDish;
import src.ordersystem.entity.OrderOverview;
import src.ordersystem.entity.Seller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class HistoryOrderDetailStageController {
    @FXML
    public Pagination pagination;
    public ArrayList<Dish> dishes;
    public OrderOverview orderOverview;
    private static final int ITEMS_PER_PAGE = 3;
    private int totalItems = 30;
    @FXML
    public void init() throws SQLException {
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        dishes = sqlLoader.getDishesInOrderOverview(orderOverview);
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

            HBox hbox = null;
            try {
                hbox = createItemBox(i, dishes);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
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

    private HBox createItemBox(int index, ArrayList<Dish> dishes) throws SQLException {
        HBox hbox = new HBox();
        ImageView imageView = new ImageView(new Image(dishes.get(index).getDishPictureUrl()));
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);

        Label label1 = new Label(dishes.get(index).getDishName());
        label1.setFont(new javafx.scene.text.Font(20));

        Label label2 = new Label("数量：" + getDishQuantity(dishes.get(index)));
        label2.setFont(new javafx.scene.text.Font(15));
        VBox vbox = new VBox();
        VBox vBox = new VBox();
        Button commentButton = new Button("评价菜品");
        commentButton.setOnAction(e->{

            try {
                SQLLoader sqlLoader = new SQLLoader();
                sqlLoader.connect();
                OrderDish orderDish = sqlLoader.getOrderDishByOrderIdAndDishId(orderOverview.getOrderID(), dishes.get(index).getDishId());
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("commentDishStage-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                CommentDishStageController commentDishStageController = fxmlLoader.getController();
                commentDishStageController.orderDish = orderDish;
                commentDishStageController.init();
                stage.setScene(scene);
                stage.show();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        vBox.getChildren().addAll(new Label(),commentButton);
        vbox.getChildren().addAll(label1, label2);
        hbox.getChildren().addAll(imageView, new Label(), vbox,new Label(),new Label(),new Label(),vBox);
        hbox.setSpacing(10);
        return hbox;
    }

    public void handleDishClick(Dish dish) throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("historyDishStage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        HistoryDishStageController historyDishStageController = fxmlLoader.getController();
        historyDishStageController.dish = dish;
        historyDishStageController.init();
        stage.setScene(scene);
        stage.show();

    }
    private int getDishQuantity(Dish dish) throws SQLException {
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        ArrayList<OrderDish> orderDishes = sqlLoader.getOrderDishByOrderID(orderOverview.getOrderID());
        for(OrderDish orderDish:orderDishes){
            if (orderDish.getDishID() == dish.getDishId()){
                return orderDish.getQuantity();
            }
        }
        return 0;
    }
}
