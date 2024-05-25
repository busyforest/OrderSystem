package src.ordersystem.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import src.ordersystem.entity.User;

public class PurChaserMainStageController {
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public Button searchButton;
    @FXML
    public Label nameLabel;
    @FXML
    protected void handleSearch(){
        scrollPane.setContent(null);
    }

    public void init(User user){
        nameLabel.setText(user.getName());
    }
}
