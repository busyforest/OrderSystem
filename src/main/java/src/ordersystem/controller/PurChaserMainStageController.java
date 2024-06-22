package src.ordersystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import src.ordersystem.MainApplication;
import src.ordersystem.SQLLoader;
import src.ordersystem.entity.Purchaser;
import src.ordersystem.entity.Seller;
import src.ordersystem.entity.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class PurChaserMainStageController {
    @FXML
    public Button searchButton;
    @FXML
    public Button historyButton;
    @FXML
    public Button infoButton;
    @FXML
    public Label nameLabel;

    @FXML
    private Pagination pagination;
    @FXML
    public TextField searchField;
    private ArrayList<Seller> sellers;
    private ArrayList<Seller> resultSellers;
    private static final int ITEMS_PER_PAGE = 5;
    private int totalItems = 30;
    public Purchaser purchaser;
    @FXML
    protected void handleSearch() throws SQLException {
        pagination.setPageFactory(null);
        pagination.setPageCount(0);
        resultSellers = new ArrayList<>();
        String name = searchField.getText();
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        resultSellers = sqlLoader.searchSeller(name);
        totalItems = resultSellers.size();
        int pageCount = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
        pagination.setPageCount(pageCount);
        pagination.setPageFactory(this::createSearchPage);

    }

    private VBox createSearchPage(int pageIndex) {
        VBox box = new VBox(5);
        int pageStart = pageIndex * ITEMS_PER_PAGE;
        int pageEnd = Math.min(pageStart + ITEMS_PER_PAGE, totalItems);

        for (int i = pageStart; i < pageEnd; i++) {
            HBox hbox = createItemBox(i, resultSellers);
            // 添加点击事件处理程序
            int finalI = i;
            hbox.setOnMouseClicked(event -> {
                try {
                    handleItemClick(resultSellers.get(finalI));
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


    public void init(User user) throws SQLException {
        this.purchaser = (Purchaser) user;
        nameLabel.setText(user.getName());
        getAllSellers();

    }
    public void getAllSellers() throws SQLException {
        sellers = new ArrayList<>();
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        sellers = sqlLoader.getAllSellers();
        totalItems = sellers.size();
        int pageCount = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
        pagination.setPageCount(pageCount);
        pagination.setPageFactory(this::createPage);
    }

    private VBox createPage(int pageIndex) {
        VBox box = new VBox(5);
        int pageStart = pageIndex * ITEMS_PER_PAGE;
        int pageEnd = Math.min(pageStart + ITEMS_PER_PAGE, totalItems);

        for (int i = pageStart; i < pageEnd; i++) {
            HBox hbox = createItemBox(i, sellers);
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

    private HBox createItemBox(int index, ArrayList<Seller> sellers) {
        HBox hbox = new HBox();
        ImageView imageView = new ImageView(new Image("https://ordersystem-images.oss-cn-shanghai.aliyuncs.com/test.jpg"));
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
        shoppingStageController.purchaser = this.purchaser;
        shoppingStageController.init(seller);
        stage.setScene(scene);
        stage.show();
    }
    public void handleHistoryClick() throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("purchaserHistoryStage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        PurChaserHistoryStageController purChaserHistoryStageController = fxmlLoader.getController();
        purChaserHistoryStageController.purchaser = this.purchaser;
        purChaserHistoryStageController.init();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void handleInformationClick() throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("purchaserInformationStage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        PurChaserInformationStageController purChaserInformationStageController = fxmlLoader.getController();
        purChaserInformationStageController.purchaser = purchaser;
        purChaserInformationStageController.init();
        stage.setScene(scene);
        stage.show();
    }
}
