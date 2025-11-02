module app {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.xerial.sqlitejdbc;
    requires javafx.graphics;
    requires javafx.base;
    
    opens com.iLearn to javafx.fxml;
    exports com.iLearn;



    exports com.example;
}
