package src.ordersystem.controller;

import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
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
        String commentString = commentArea.getText();
        //TODO: 评价星级
        int mark = 0;
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        sqlLoader.commentSeller(purchaser.getId(),seller.getId(),commentString, mark);
        //提示信息
        Label label = new Label("评价成功！");
        StackPane root = new StackPane();
        root.getChildren().add(label);
        Stage primaryStage = new Stage();
        Scene scene = new Scene(root, 250, 150);
        primaryStage.setScene(scene);
        primaryStage.show();
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event -> primaryStage.close());
        delay.play();
        Stage stage = (Stage) comfirmButton.getScene().getWindow();
        stage.close();
    }
}
