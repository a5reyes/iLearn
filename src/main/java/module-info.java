module app {
  // transitively requires javafx.base and javafx.graphics
  requires javafx.controls;
  requires javafx.fxml;
  requires java.desktop;
  // export javafx.application.Application implementation's package
  // to at least javafx.graphics
  exports com.example to javafx.graphics;
}