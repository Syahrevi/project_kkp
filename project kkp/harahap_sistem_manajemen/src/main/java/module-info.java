module harahap {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;
    requires javafx.graphics;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.apache.commons.compress;
    requires org.apache.commons.collections4;

    opens harahap to javafx.fxml;
    exports harahap;
}
