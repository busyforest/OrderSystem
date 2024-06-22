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
import src.ordersystem.entity.Message;
import src.ordersystem.entity.OrderDish;
import src.ordersystem.entity.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class MessageStageController {
    @FXML
    public Pagination pagination;
    public User user;
    private ArrayList<Message> messages;
    private static final int ITEMS_PER_PAGE = 3;
    private int totalItems = 30;
    @FXML
    public void init() throws SQLException {
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        messages = sqlLoader.getMessages(user.getId());
        totalItems = messages.size();
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
                hbox = createItemBox(i, messages);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            box.getChildren().add(hbox);
        }

        return box;
    }

    private HBox createItemBox(int index, ArrayList<Message> messages) throws SQLException {
        HBox hbox = new HBox();

        Label label1 = new Label(messages.get(index).getMessage());
        label1.setFont(new javafx.scene.text.Font(20));

        Label label2 = new Label("来自："+messages.get(index).getSender_id());
        label2.setFont(new javafx.scene.text.Font(15));
        VBox vbox = new VBox();
        VBox vBox = new VBox();
        vBox.getChildren().addAll(new Label());
        vbox.getChildren().addAll(label1, label2);
        hbox.getChildren().addAll( new Label(), vbox,new Label(),new Label(),new Label(),vBox);
        hbox.setSpacing(10);
        return hbox;
    }

}
