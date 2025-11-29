module app { //used to organize, manage and keep app safer and smaller
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.xerial.sqlitejdbc;
    requires javafx.graphics;
    requires javafx.base;
    requires java.naming;
    requires jakarta.activation;
    requires jakarta.mail;    
    
    opens com.ilearn to javafx.fxml;
    exports com.ilearn;
}
