package com.ilearn;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ilearn.User;

public class AppController extends Application {
    private static User currentUser;
    private static String currentClassroomInfo;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Connection connection = Main.connect();
    @FXML
    private ListView<String> mainPageListView;
    @FXML
    private ListView<String> currClassListView;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void setCurrentClass(String currentClassInfo) {
        currentClassroomInfo = currentClassInfo;
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

    public void switchToClassroom(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/ilearn/views/Classroom.fxml"));
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

    public void switchToCalendar(ActionEvent event)throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/com/ilearn/views/Calendar.fxml"));
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
            mainPageListView.getItems().clear();

            while (rs.next()) {
                String name = rs.getString("class_name");
                int id = rs.getInt("class_id");
                classes.add(name + ", " + id);

            }

            mainPageListView.getItems().addAll(classes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleItemClick() throws IOException{
        String selectedItem = mainPageListView.getSelectionModel().getSelectedItem();
        setCurrentClass(selectedItem);
        System.out.println("Selected item: " + selectedItem);
    }

    @FXML
    private void currClassToClassTab(){
        List<String> classInfo = new ArrayList<>();
        try {
            String query = "SELECT class_name, class_id, discussions, teacher, meeting_time FROM classrooms WHERE class_name = ? OR class_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, currentClassroomInfo.split(", ")[0]);
            stmt.setInt(2, Integer.parseInt(currentClassroomInfo.split(", ")[1]));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("class_name");
                int id = rs.getInt("class_id");
                String discussions = rs.getString("discussions");
                String teacher = rs.getString("teacher");
                String meetingTime = rs.getString("meeting_time");
                classInfo.add(name);
                classInfo.add(Integer.toString(id));
                classInfo.add(discussions);
                classInfo.add(teacher);
                classInfo.add(meetingTime);
                break;
            }
            currClassListView.getItems().addAll(classInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

    
}