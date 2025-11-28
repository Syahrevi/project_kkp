module harahap {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;

    opens harahap to javafx.fxml;
    exports harahap;
}
