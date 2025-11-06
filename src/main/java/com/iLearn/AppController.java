package com.iLearn;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.User;

public class AppController extends Application {
    private static User currentUser;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Connection connection = Main.connect();
    @FXML
    private ListView<String> classListView;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }
    /**
     * Creates the GUI page, set its height/width and gives it a name
     * @param stage - the area in which the GUI is created
     * @throws IOException watches to make sure no exception gives an error
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ilearn/views/MainPage.fxml"));
        System.out.println("FXML URL: " + getClass().getResource("/com/ilearn/views/MainPage.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 650, 600);
        stage.setTitle("iLearn");
        stage.setScene(scene);
        stage.show();
    }

    public void switchToClasses(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/ilearn/views/ClassesPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void switchToGradebook(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/ilearn/views/Gradebook.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }     

    public void switchToCalender(ActionEvent event)throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/com/ilearn/views/Calender.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    

    public void switchToMessages(ActionEvent event)throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/com/ilearn/views/Messaging.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    

    public void backToMain(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/com/ilearn/views/MainPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void loadUserClasses() {
        List<String> classes = new ArrayList<>();
        try {
            String query = "SELECT class_name, class_id FROM classrooms WHERE user_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, currentUser.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("class_name");
                int id = rs.getInt("class_id");
                classes.add(name + " (ID: " + id + ")");
            }

            classListView.getItems().addAll(classes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

    
}