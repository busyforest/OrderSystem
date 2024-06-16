package src.ordersystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import src.ordersystem.MainApplication;
import src.ordersystem.SQLLoader;
import src.ordersystem.entity.Seller;
import src.ordersystem.entity.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class PurChaserMainStageController {
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public Button searchButton;
    @FXML
    public Label nameLabel;

    @FXML
    private Pagination pagination;
    private ArrayList<Seller> sellers;

    private static final int ITEMS_PER_PAGE = 5;
    private int totalItems = 100; // 假设总共有100个项目
    @FXML
    protected void handleSearch(){
        scrollPane.setContent(null);
    }

    public void init(User user) throws SQLException {
        nameLabel.setText(user.getName());
        getAllSellers();
    }
    public void getAllSellers() throws SQLException {
        sellers = new ArrayList<>();
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        sellers = sqlLoader.getAllSellers();
        totalItems = sellers.size();
    }
    @FXML
    public void initialize() {
        int pageCount = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
        pagination.setPageCount(pageCount);
        pagination.setPageFactory(this::createPage);
    }

    private VBox createPage(int pageIndex) {
        VBox box = new VBox(5);
        int pageStart = pageIndex * ITEMS_PER_PAGE;
        int pageEnd = Math.min(pageStart + ITEMS_PER_PAGE, totalItems);

        for (int i = pageStart; i < pageEnd; i++) {
            HBox hbox = createItemBox(i);
            // 添加点击事件处理程序
            int finalI = i;
            hbox.setOnMouseClicked(event -> {
                try {
                    handleItemClick(sellers.get(finalI));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            box.getChildren().add(hbox);
        }

        return box;
    }

    private HBox createItemBox(int index) {
        HBox hbox = new HBox();
        ImageView imageView = new ImageView(new Image("D:\\IDEA\\OrderSystem\\src\\main\\resources\\src\\ordersystem\\Images\\test.jpg"));
        imageView.setFitHeight(105);
        imageView.setFitWidth(113);
        imageView.setPreserveRatio(true);

        Label label1 = new Label(sellers.get(index).getName());
        label1.setFont(new javafx.scene.text.Font(20));

        Label label2 = new Label("简介：" + sellers.get(index).getBriefInfomation());
        label2.setFont(new javafx.scene.text.Font(15));

        VBox vbox = new VBox();
        vbox.getChildren().addAll(label1, label2);

        hbox.getChildren().addAll(imageView, new Label(), vbox);
        hbox.setSpacing(10);

        return hbox;
    }
    private void handleItemClick(Seller seller) throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("shoppingInSellerStage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ShoppingInSellerStageController shoppingStageController = fxmlLoader.getController();
        shoppingStageController.init(seller);

        stage.setScene(scene);
        stage.show();
    }

}
