module src.ordersystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires mysql.connector.j;
    requires java.sql;

    opens src.ordersystem to javafx.fxml;
    exports src.ordersystem;
}