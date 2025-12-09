module harahap {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.apache.commons.lang3;
    requires org.apache.poi.ooxml.schemas;
    

    opens harahap to javafx.fxml;
    exports harahap;
}
