module src.ordersystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens src.ordersystem to javafx.fxml;
    exports src.ordersystem;
}