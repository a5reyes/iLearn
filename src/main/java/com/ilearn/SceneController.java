package com.ilearn;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToClasses(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("ClassesPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToGradebook(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("Gradebook.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToCalender(ActionEvent event)throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("Calender.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMessages(ActionEvent event)throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("Messaging.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void backToMain(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
<<<<<<< Updated upstream:src/main/java/com/example/SceneController.java
=======

    // 🆕 Add this method
    @FXML
    private void loadUserClasses(ActionEvent event) {
        System.out.println("Loading user classes...");
        // TODO: Add your logic to populate the ListView here if needed
    }
>>>>>>> Stashed changes:src/main/java/com/ilearn/SceneController.java
}
