package com.ilearn;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppController extends Application {
    private static User currentUser;
    private static String currentClassroomInfo; //string for classroom info displayed on main page
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Connection connection = Main.connect();
    @FXML
    private ListView<String> mainPageListView; //list view element in main page
    @FXML
    private ListView<String> currClassroomListView; //list view element in classroom page
    @FXML
    private ListView<String> currGradebookListView; //list view element in gradebook page
    
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
        if(currentUser.getIsTeacher()){
            stage.setTitle("iLearn - Teacher View");
        } else {
            stage.setTitle("iLearn - Student View");
        }
        stage.setScene(scene);
        stage.show();
    }

    //back button in classroom page
    public void switchToClassroom(ActionEvent event) throws IOException {
        if(currentUser.getIsTeacher()){
            Parent root = FXMLLoader.load(getClass().getResource("/com/ilearn/views/TeacherClassroom.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("/com/ilearn/views/Classroom.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show(); 
        }
    }

    //back button in gradebook page
    public void switchToGradebook(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/ilearn/views/Gradebook.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        currGradebookToGradebookTab();
    }     

    //back button in calendar page
    public void switchToCalendar(ActionEvent event)throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/com/ilearn/views/Calendar.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    //----- Main Page -----
    //back button in messages page
    public void switchToMessages(ActionEvent event)throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/com/ilearn/views/Messaging.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    //----- Main Page -----
    //back button in main page
    public void backToMain(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/com/ilearn/views/MainPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    //----- Main Page -----
    //from user info, get classrooms from classrooms database
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

    //----- Main Page -----
    //get selected/highlighted item in current Classroom ListView element
    @FXML
    private void handleItemClick() throws IOException{
        String selectedItem = mainPageListView.getSelectionModel().getSelectedItem();
        setCurrentClass(selectedItem);
        System.out.println("Selected item: " + selectedItem);
    }

    @FXML
    private void teacherView() throws IOException{
        String selectedItem = mainPageListView.getSelectionModel().getSelectedItem();
        setCurrentClass(selectedItem);
        System.out.println("Selected item: " + selectedItem);
    }

    //----- Classroom Page -----
    //get classroom info from sqlite db into the current Classroom ListView element
    @FXML
    private void currClassInfoToClassTab(){
        List<String> classInfo = new ArrayList<>();
        try {
            String query = "SELECT class_name, class_id, discussions, teacher, meeting_time FROM classrooms WHERE class_name = ? OR class_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, currentClassroomInfo.split(", ")[0]);
            stmt.setInt(2, Integer.parseInt(currentClassroomInfo.split(", ")[1]));
            ResultSet rs = stmt.executeQuery();
            currClassroomListView.getItems().clear();
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
            currClassroomListView.getItems().addAll(classInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //----- Classroom Page -----
    //get roster info from sqlite db into the current Classroom ListView element
    @FXML
    private void currRosterToClassTab(){
        List<String> rosterArr = new ArrayList<>();
        try {
            String query = "SELECT username FROM rosters WHERE class_name = ? AND class_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, currentClassroomInfo.split(", ")[0]);
            stmt.setInt(2, Integer.parseInt(currentClassroomInfo.split(", ")[1]));
            ResultSet rs = stmt.executeQuery();
            currClassroomListView.getItems().clear();
            while (rs.next()) {
                String name = rs.getString("username");
                rosterArr.add(name);
            }
            currClassroomListView.getItems().addAll(rosterArr);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //----- Classroom Page -----
    //get gradebook info from sqlite db into the current Classroom ListView element
    @FXML
    private void currGradebookToClassTab(){
        List<String> gradebookArr = new ArrayList<>();
        try {
            String query = "SELECT class_name, assignment, grade FROM gradebooks WHERE user_id = ? AND class_name = ? AND class_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, currentUser.getId());
            stmt.setString(2, currentClassroomInfo.split(", ")[0]);
            stmt.setInt(3, Integer.parseInt(currentClassroomInfo.split(", ")[1]));
            ResultSet rs = stmt.executeQuery();
            currClassroomListView.getItems().clear();
            while (rs.next()) {
                String name = rs.getString("class_name");
                String assignment = rs.getString("assignment");
                double grade = rs.getDouble("grade");
                gradebookArr.add(name + ", " + assignment + ", " + String.valueOf(grade));
            }
            currClassroomListView.getItems().addAll(gradebookArr);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //----- Gradebook Page -----
    //get gradebook info from sqlite db into the current Classroom ListView element
    @FXML
    private void currGradebookToGradebookTab(){
        List<String> gradebookArr = new ArrayList<>();
        try {
            String query = "SELECT class_name, assignment, grade FROM gradebooks WHERE user_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, currentUser.getId());
            stmt.setString(2, currentClassroomInfo.split(", ")[0]);
            stmt.setInt(3, Integer.parseInt(currentClassroomInfo.split(", ")[1]));
            ResultSet rs = stmt.executeQuery();
            currGradebookListView.getItems().clear();
            while (rs.next()) {
                String name = rs.getString("class_name");
                String assignment = rs.getString("assignment");
                double grade = rs.getDouble("grade");
                gradebookArr.add(name + ", " + assignment + ", " + String.valueOf(grade));
            }
            currGradebookListView.getItems().addAll(gradebookArr);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch();
    }
    
}