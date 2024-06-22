package src.ordersystem.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import src.ordersystem.entity.Purchaser;

public class PurChaserInformationStageController {
    @FXML
    public Label nameHeadLabel;
    @FXML
    public Label nameLabel;
    @FXML
    public Label idLabel;
    @FXML
    public Label genderLabel;
    @FXML
    public Label studentIdLabel;

    @FXML
    public ImageView avatarImageView;
    public Purchaser purchaser;
    public void init(){
        nameHeadLabel.setText(purchaser.getName()+" ,");
        nameLabel.setText(purchaser.getName());
        idLabel.setText(String.valueOf(purchaser.getId()));
        genderLabel.setText(String.valueOf(purchaser.getGender()));
        studentIdLabel.setText(String.valueOf(purchaser.getStudentIDOrWorkID()));
        // TODO: 等数据库里面的用户插入头像之后再调用这个，现在还没有头像
//        avatarImageView = new ImageView(new Image(seller.getAvatarPath()));
        avatarImageView.setImage(new Image("https://ordersystem-images.oss-cn-shanghai.aliyuncs.com/test.jpg"));
    }
}
