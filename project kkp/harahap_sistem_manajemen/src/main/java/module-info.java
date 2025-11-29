module harahap {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;
    requires javafx.graphics;

    opens harahap to javafx.fxml;
    exports harahap;
}
