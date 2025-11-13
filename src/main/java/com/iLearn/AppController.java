package com.ilearn;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.Connection;
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
    @FXML
    private TextArea assignmentText;
    
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
    //from user info, get classrooms from classrooms database - might use getclassroomsNames instead of sqlite
    @FXML
    private void loadUserClasses() {
        mainPageListView.getItems().clear();
        List<String> userClassrooms = currentUser.viewClassroomsNamesIds();
        mainPageListView.getItems().addAll(userClassrooms);
    }

    //----- Main Page -----
    //get selected/highlighted item in current Classroom ListView element
    @FXML
    private void handleItemClick() throws IOException{
        String selectedItem = mainPageListView.getSelectionModel().getSelectedItem();
        setCurrentClass(selectedItem);
        System.out.println("Selected item: " + selectedItem);
    }

    //----- Classroom Page -----
    //get classroom info from sqlite db into the current Classroom ListView element
    @FXML
    private void currClassInfoToClassTab(){
        currClassroomListView.getItems().clear();
        List<String> classroomInfo = currentUser.viewClassroomInfo(currentClassroomInfo);
        currClassroomListView.getItems().addAll(classroomInfo);
    }

    //----- Classroom Page -----
    //get roster info from sqlite db into the current Classroom ListView element
    @FXML
    private void currRosterToClassTab(){
        currClassroomListView.getItems().clear();
        List<String> classroomRoster = currentUser.viewClassroomRoster(currentClassroomInfo);
        currClassroomListView.getItems().addAll(classroomRoster);
    }

    //----- Classroom Page -----
    //get gradebook info from sqlite db into the current Classroom ListView element
    @FXML
    private void currGradebookToClassTab(){
        currClassroomListView.getItems().clear();
        List<String> classroomGradebook = currentUser.viewClassroomGrades(currentClassroomInfo);
        currClassroomListView.getItems().addAll(classroomGradebook);
    }
    
    //----- Classroom Page -----
    @FXML
    private void addAssignmentToClassTab(){
        String assignment = assignmentText.getText();
        String[] assignmentStuff = assignment.split(", ");
        currentUser.addAssignment(currentClassroomInfo, assignmentStuff[0], assignmentStuff[1], Integer.parseInt(assignmentStuff[2]));
        System.out.println("Assignment - " + assignmentStuff[0].toString() + " added");
    }

    //----- Classroom Page -----
    @FXML
    private void currAssignmentToClassTab(){
        currClassroomListView.getItems().clear();
        List<String> assignmentList = currentUser.getAssignments(currentClassroomInfo);
        currClassroomListView.getItems().addAll(assignmentList);
    }

    //----- Gradebook Page -----
    //get total gradebook info from sqlite db into the current Gradebook ListView element
    @FXML
    private void currGradebookToGradebookTab(){
        currGradebookListView.getItems().clear();
        List<String> userGradebook = currentUser.viewGrades();
        currGradebookListView.getItems().addAll(userGradebook);
    }
    
    public static void main(String[] args) {
        launch();
    }
    
}