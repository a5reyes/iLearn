package com.ilearn;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

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

    public class CalenderController implements Initializable{
        ZonedDateTime dateFocus;
        ZonedDateTime today;

        @FXML
        private Text year;

        @FXML
        private Text month;

        @FXML 
        private FlowPane calendar;

        @Overide
        public void initialize(URL url, ResourceBundle resourceBundle){
            dateFocus = ZonedDateTime.now();
            today = ZonedDateTime.now();
            drawCalendar();

        }

        @FXML
        void backOneMonth(ActionEvent event) {
            dateFocus = dateFocus.minusMonths(1);
            calendar.getChildren().clear();
            drawCalendar();
        }

        @FXML
        void forwardOneMonth(ActionEvent event) {
            dateFocus = dateFocus.plusMonths(1);
            calendar.getChildren().clear();
            drawCalendar();
        }

        private void drawCalendar(){
            year.setText(String.valueOf(dateFocus.getYear()));
            month.setText(String.valueOf(dateFocus.getMonth()));

            double calendarWidth = calendar.getPrefWidth();
            double calendarHeight = calendar.getPrefHeight();
            double strokeWidth = 1;
            double spacingH = calendar.getHgap();
            double spacingV = calendar.getVgap();

            Map<Integer, List<CalendarActivity>> calendarActivityMap = getCalendarActivitiesMonth(dateFocus);

            int monthMaxDate = dateFocus.getMonth().maxLength();
            //Check for leap year
            if(dateFocus.getYear() % 4 != 0 && monthMaxDate == 29){
                monthMaxDate = 28;
            }
            int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1,0,0,0,0,dateFocus.getZone()).getDayOfWeek().getValue();

            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    StackPane stackPane = new StackPane();

                    Rectangle rectangle = new Rectangle();
                    rectangle.setFill(Color.TRANSPARENT);
                    rectangle.setStroke(Color.BLACK);
                    rectangle.setStrokeWidth(strokeWidth);
                    double rectangleWidth =(calendarWidth/7) - strokeWidth - spacingH;
                    rectangle.setWidth(rectangleWidth);
                    double rectangleHeight = (calendarHeight/6) - strokeWidth - spacingV;
                    rectangle.setHeight(rectangleHeight);
                    stackPane.getChildren().add(rectangle);

                    int calculatedDate = (j+1)+(7*i);
                    if(calculatedDate > dateOffset){
                        int currentDate = calculatedDate - dateOffset;
                        if(currentDate <= monthMaxDate){
                            Text date = new Text(String.valueOf(currentDate));
                            double textTranslationY = - (rectangleHeight / 2) * 0.75;
                            date.setTranslateY(textTranslationY);
                            stackPane.getChildren().add(date);

                            List<CalendarActivity> calendarActivities = calendarActivityMap.get(currentDate);
                            if(calendarActivities != null){
                                createCalendarActivity(calendarActivities, rectangleHeight, rectangleWidth, stackPane);
                            }
                        }
                        if(today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate){
                            rectangle.setStroke(Color.BLUE);
                        }
                    }
                    calendar.getChildren().add(stackPane);
                }
            }
        }

        private void createCalendarActivity(List<CalendarActivity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
            VBox calendarActivityBox = new VBox();
            for (int k = 0; k < calendarActivities.size(); k++) {
                if(k >= 2) {
                    Text moreActivities = new Text("...");
                    calendarActivityBox.getChildren().add(moreActivities);
                    moreActivities.setOnMouseClicked(mouseEvent -> {
                        //On ... click print all activities for given date
                        System.out.println(calendarActivities);
                    });
                    break;
                }
                Text text = new Text(calendarActivities.get(k).getUserName() + ", " + calendarActivities.get(k).getDate().toLocalTime());
                calendarActivityBox.getChildren().add(text);
                text.setOnMouseClicked(mouseEvent -> {
                    //On Text clicked
                    System.out.println(text.getText());
                });
            }
            calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
            calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
            calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
            calendarActivityBox.setStyle("-fx-background-color:GRAY");
            stackPane.getChildren().add(calendarActivityBox);
        }

        private Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities) {
            Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>();

            for (CalendarActivity activity: calendarActivities) {
                int activityDate = activity.getDate().getDayOfMonth();
                if(!calendarActivityMap.containsKey(activityDate)){
                    calendarActivityMap.put(activityDate, List.of(activity));
                } else {
                    List<CalendarActivity> OldListByDate = calendarActivityMap.get(activityDate);

                    List<CalendarActivity> newList = new ArrayList<>(OldListByDate);
                    newList.add(activity);
                    calendarActivityMap.put(activityDate, newList);
                }
            }
            return  calendarActivityMap;
        }

        private Map<Integer, List<CalendarActivity>> getCalendarActivitiesMonth(ZonedDateTime dateFocus) {
            List<CalendarActivity> calendarActivities = new ArrayList<>();
            int year = dateFocus.getYear();
            int month = dateFocus.getMonth().getValue();

            Random random = new Random();
            for (int i = 0; i < 50; i++) {
                ZonedDateTime time = ZonedDateTime.of(year, month, random.nextInt(27)+1, 16,0,0,0,dateFocus.getZone());
                calendarActivities.add(new CalendarActivity(time , "user", 1234533));
            }

            return createCalendarMap(calendarActivities);
        }
    }
    
}