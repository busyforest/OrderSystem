package src.ordersystem.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import src.ordersystem.SQLLoader;
import src.ordersystem.entity.Purchaser;
import src.ordersystem.entity.Seller;

import java.sql.SQLException;

public class CommentSellerStageController {
    @FXML
    public TextArea commentArea;
    @FXML
    public ComboBox commentBox;
    @FXML
    public Button comfirmButton;
    public Purchaser purchaser;
    public Seller seller;
    public void init(){
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "1","2","3","4","5"
                );
        commentBox.setItems(options);
        commentBox.setValue("5");
    }
    @FXML
    protected void handleConfirmClicked() throws SQLException {
        // TODO: 等待SQLLoader问题解决
//        String commentString = commentArea.getText();
//        double mark = Double.parseDouble((String) commentBox.getValue());
//        SQLLoader sqlLoader = new SQLLoader();
//        sqlLoader.connect();
//        sqlLoader.commentSeller(purchaser.getId(),seller.getId(),commentString);
//        sqlLoader.
    }
}
