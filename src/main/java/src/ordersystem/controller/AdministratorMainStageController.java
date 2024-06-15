package src.ordersystem.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import src.ordersystem.entity.User;

public class AdministratorMainStageController {
    @FXML
    public Label nameLabel;
    public void init(User user){
        nameLabel.setText(user.getName());
    }
}
