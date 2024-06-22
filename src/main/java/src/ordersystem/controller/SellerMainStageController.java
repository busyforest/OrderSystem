package src.ordersystem.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import src.ordersystem.MainApplication;
import src.ordersystem.SQLLoader;
import src.ordersystem.entity.Dish;
import src.ordersystem.entity.Purchaser;
import src.ordersystem.entity.Seller;
import src.ordersystem.entity.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class SellerMainStageController {
    @FXML
    public Button searchButton;
    @FXML
    public Button infoButton;
    @FXML
    public Button newDishButton;
    @FXML
    public Button checkoutButton;
    @FXML
    public Label nameLabel;

    @FXML
    private Pagination pagination;
    @FXML
    public TextField searchField;
    public Seller seller;
    private ArrayList<Dish> dishes;
    private ArrayList<Dish> resultDishes;
    private static final int ITEMS_PER_PAGE = 5;
    private int totalItems = 30;
    @FXML
    protected void handleSearch() throws SQLException {
        pagination.setPageFactory(null);
        pagination.setPageCount(0);
        resultDishes = new ArrayList<>();
        String name = searchField.getText();
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        resultDishes = sqlLoader.searchDishes(name,seller);
        totalItems = resultDishes.size();
        int pageCount = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
        pagination.setPageCount(pageCount);
        pagination.setPageFactory(this::createSearchPage);

    }

    private VBox createSearchPage(int pageIndex) {
        VBox box = new VBox(5);
        int pageStart = pageIndex * ITEMS_PER_PAGE;
        int pageEnd = Math.min(pageStart + ITEMS_PER_PAGE, totalItems);

        for (int i = pageStart; i < pageEnd; i++) {
            HBox hbox = createItemBox(i, resultDishes);
            box.getChildren().add(hbox);
        }

        return box;
    }


    public void init(User user) throws SQLException {
        this.seller = (Seller) user;
        nameLabel.setText(user.getName());
        getAllDishes();
    }
    public void getAllDishes() throws SQLException {
        dishes = new ArrayList<>();
        SQLLoader sqlLoader = new SQLLoader();
        sqlLoader.connect();
        dishes = sqlLoader.getDishesInSeller(seller);
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
            HBox hbox = createItemBox(i, dishes);
            box.getChildren().add(hbox);
        }

        return box;
    }

    private HBox createItemBox(int index, ArrayList<Dish> dishes) {
        HBox hbox = new HBox();
        ImageView imageView = new ImageView(new Image(dishes.get(index).getDishPictureUrl()));
        imageView.setFitHeight(105);
        imageView.setFitWidth(113);

        Label label1 = new Label(dishes.get(index).getDishName());
        label1.setFont(new javafx.scene.text.Font(20));

        Button changeButton = new Button("修改信息");
        Button deleteButton = new Button("删除菜品");

        changeButton.setOnAction(e ->{
            try {
                handleChangeClick(dishes.get(index));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        deleteButton.setOnAction(e->{
            SQLLoader sqlLoader = new SQLLoader();
            try {
                sqlLoader.connect();
                sqlLoader.deleteDish(dishes.get(index).getDishId());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            // 提示信息
            Label label = new Label("删除成功！");
            StackPane root = new StackPane();
            root.getChildren().add(label);
            Stage primaryStage = new Stage();
            Scene scene = new Scene(root, 250, 150);
            primaryStage.setScene(scene);
            primaryStage.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(event -> primaryStage.close());
            delay.play();
            Stage stage = (Stage) deleteButton.getScene().getWindow();
            stage.close();
            try {
                getAllDishes();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            stage.show();
        });

        VBox vbox = new VBox();
        vbox.getChildren().addAll(label1);
        VBox vbox1 = new VBox();
        vbox1.getChildren().addAll(changeButton,new Label(),deleteButton);


        hbox.getChildren().addAll(imageView, new Label(), vbox,new Label(),new Label(), new Label(),vbox1);
        hbox.setSpacing(10);

        return hbox;
    }
    private void handleChangeClick(Dish dish) throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("changeDishInformationStage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ChangeDishInformationStageController changeDishInformationStageController = fxmlLoader.getController();
        changeDishInformationStageController.dish = dish;
        changeDishInformationStageController.init();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void handleInformationClick() throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("sellerInformationStage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        SellerInformationStageController sellerInformationStageController = fxmlLoader.getController();
        sellerInformationStageController.seller = seller;
        sellerInformationStageController.init();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void handleCheckoutClick(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("确认退出");
        alert.setContentText("你确定要退出账号吗？");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                    Stage stage = (Stage) checkoutButton.getScene().getWindow();
                    stage.close();
                    Stage primaryStage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("loginStage-view.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    SQLLoader sqlLoader = new SQLLoader();
                    try {
                        sqlLoader.connect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    primaryStage.setTitle("Order System");
                    primaryStage.setScene(scene);
                    primaryStage.show();

                });
    }
    @FXML
    protected void handleNewDishClick() throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("newDishStage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        NewDishStageController newDishStageController = fxmlLoader.getController();
        newDishStageController.seller = seller;
        stage.setScene(scene);
        stage.show();
    }
}
