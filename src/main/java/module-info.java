module app {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.xerial.sqlitejdbc;
    requires javafx.graphics;
    requires javafx.base;
    
    opens com.ilearn to javafx.fxml;
    exports com.ilearn;
<<<<<<< Updated upstream



    exports com.example;
=======
    //exports com.example;
>>>>>>> Stashed changes
}
