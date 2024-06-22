package src.ordersystem.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import src.ordersystem.entity.Seller;

public class SellerInformationStageController {
    @FXML
    public Label nameHeadLabel;
    @FXML
    public Label nameLabel;
    @FXML
    public Label idLabel;
    @FXML
    public Label descriptionLabel;
    @FXML
    public Label addressLabel;
    @FXML
    public Label featuredDishLabel;
    @FXML
    public Label markLabel;
    @FXML
    public ImageView avatarImageView;
    public Seller seller;
    public void init(){
        nameHeadLabel.setText(seller.getName()+" ,");
        nameLabel.setText(seller.getName());
        idLabel.setText(String.valueOf(seller.getId()));
        descriptionLabel.setText(seller.getBriefInfomation());
        addressLabel.setText(seller.getAddress());
        featuredDishLabel.setText(seller.getFeaturedDish());
        markLabel.setText(String.valueOf(seller.getAvg_mark()));
        // TODO: 等数据库里面的用户插入头像之后再调用这个，现在还没有头像
//        avatarImageView = new ImageView(new Image(seller.getAvatarPath()));
        avatarImageView.setImage(new Image("https://ordersystem-images.oss-cn-shanghai.aliyuncs.com/test.jpg"));
    }
}
