module src.ordersystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires mysql.connector.j;
    requires java.sql;
    requires static lombok;

    opens src.ordersystem to javafx.fxml;
    exports src.ordersystem;
    exports src.ordersystem.controller;
    opens src.ordersystem.controller to javafx.fxml;
}