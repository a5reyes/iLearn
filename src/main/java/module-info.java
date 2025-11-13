module app {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.xerial.sqlitejdbc;
    requires javafx.graphics;
    requires javafx.base;
    requires java.naming;
    requires jakarta.activation;
    
    opens com.iLearn to javafx.fxml;
    exports com.iLearn;
}
