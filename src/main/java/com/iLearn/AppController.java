package com.ilearn;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppController extends Application {
    /**
     * Creates the GUI page, set its height/width and gives it a name
     * @param stage - the area in which the GUI is created
     * @throws IOException watches to make sure no exception gives an error
     */
    @Override
    public void start(Stage stage) throws IOException {
      // Load FXML from resources
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ilearn/views/MainPage.fxml"));

        // Debug: check if FXML is found
        System.out.println("FXML URL: " + getClass().getResource("/com/ilearn/views/MainPage.fxml"));

        Parent root = loader.load();

        // Create the scene with width and height
        Scene scene = new Scene(root, 650, 600);

        // Set stage title and show
        stage.setTitle("iLearn");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    
}