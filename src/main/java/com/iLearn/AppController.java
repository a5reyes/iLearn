package com.ilearn;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.ilearn.dao.AssignmentDAO;
import com.ilearn.dao.ClassroomDAO;
import com.ilearn.dao.GradebookDAO;
import com.ilearn.dao.RosterDAO;
import com.ilearn.dao.UserDAO;

public class AppController extends Application implements Initializable {
    private static User currentUser;
    private static String currentClassroomInfo;
    private static String currentAssignmentString;
    private UserDAO userDAO = new UserDAO();
    private ClassroomDAO classroomDAO = new ClassroomDAO();
    private RosterDAO rosterDAO = new RosterDAO();
    private GradebookDAO gradebookDAO = new GradebookDAO();
    private AssignmentDAO assignmentDAO = new AssignmentDAO();
    private Stage stage;
    private Scene scene;
    private Parent root;
    private ZonedDateTime dateFocus;
    private ZonedDateTime today;
    
    // ---------- MAIN PAGE FXML ----------
    @FXML private ListView<String> mainPageListView;

    // ---------- GRADEBOOK FXML ----------
    @FXML private ListView<String> currGradebookListView;

    // ---------- CLASSROOM FXML ----------
    @FXML private ListView<String> currClassroomListView;

    // ---------- CLASSROOM FXML - STUDENT ----------
    @FXML private TextArea studentWork;

    // ---------- CLASSROOM FXML - TEACHER ----------
    @FXML private TextArea assignmentName;
    @FXML private TextArea assignmentDescription;
    @FXML private TextArea assignmentGrade;
    @FXML private TextArea assignmentStudent;
    @FXML private DatePicker assignmentDatePicker;

    // ---------- EMAIL LOGIN ----------
    private static String outlookEmail = null;
    private static String outlookPassword = null;

    // ---------- CALENDAR FXML ----------
    @FXML private Text year;
    @FXML private Text month;
    @FXML private FlowPane calendar;

    // ---------- MESSAGING FXML ----------
    @FXML private TextArea messageBody;
    @FXML private TextField recipientField;
    
    public static void setCurrentUser(User user) { currentUser = user; }
    public static void setCurrentClass(String currentClassInfo) { currentClassroomInfo = currentClassInfo; }
    public static void setCurrentAssignment(String currentAssignmentString) { currentAssignmentString = currentAssignmentString; }
    // ===========================================
    //                INITIALIZATION
    // ===========================================

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeCalendarIfLoaded();
        initializeMessagingIfLoaded();
    }

    private void initializeCalendarIfLoaded() {
        if (calendar != null && year != null && month != null) {
            dateFocus = ZonedDateTime.now();
            today = ZonedDateTime.now();
            drawCalendar();
            System.out.println("Calendar initialized.");
        }
    }

    private void initializeMessagingIfLoaded() {
        if (recipientField != null && messageBody != null) {
            System.out.println("Messaging system initialized.");
            if (outlookEmail == null || outlookPassword == null) {
                showEmailLoginPopup();
            }
        }
    }

    @FXML private void newMessage() {
        recipientField.clear();
        messageBody.clear();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ilearn/views/MainPage.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 650, 600);
        stage.setTitle(currentUser.getIsTeacher() ? "iLearn - Teacher View" : "iLearn - Student View");
        stage.setScene(scene);
        stage.show();
    }

    // ===========================================
    //                SCENE SWITCHING
    // ===========================================
    public void switchToClassroom(ActionEvent event) throws IOException {
        String target = currentUser.getIsTeacher() ? "/com/ilearn/views/TeacherClassroom.fxml" : "/com/ilearn/views/Classroom.fxml";
        Parent root = FXMLLoader.load(getClass().getResource(target));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void switchToGradebook(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/ilearn/views/Gradebook.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void switchToCalendar(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/ilearn/views/Calendar.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void switchToMessages(ActionEvent event) throws IOException {
        if (outlookEmail == null || outlookPassword == null) {
            showEmailLoginPopup();
        }
        Parent root = FXMLLoader.load(getClass().getResource("/com/ilearn/views/EmailSender.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void backToMain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/ilearn/views/MainPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // ===========================================
    //                MAIN PAGE
    // ===========================================
    @FXML private void loadUserClasses() {
        mainPageListView.getItems().setAll(classroomDAO.viewClassroomsNamesIds(currentUser));
    }

    @FXML private void handleItemClick() {
        String selectedItem = mainPageListView.getSelectionModel().getSelectedItem();
        setCurrentClass(selectedItem);
        System.out.println("Selected item: " + selectedItem);
    }

    // ===========================================
    //          CLASSROOM PAGE - DEFAULT
    // ===========================================
    @FXML private void currClassInfoToClassTab() {
        currClassroomListView.getItems().setAll(classroomDAO.viewClassroomInfo(currentClassroomInfo));
    }

    @FXML private void currRosterToClassTab() {
        currClassroomListView.getItems().setAll(classroomDAO.viewClassroomRoster(currentClassroomInfo));
    }

    @FXML private void currGradebookToClassTab() {
        currClassroomListView.getItems().setAll(classroomDAO.viewClassroomGrades(currentUser, currentClassroomInfo));
    }

    @FXML private void currAssignmentToClassTab() {
        currClassroomListView.getItems().setAll(assignmentDAO.getAssignments(currentUser, currentClassroomInfo));
    }

    @FXML private void currGradebookToGradebookTab() {
        currGradebookListView.getItems().setAll(gradebookDAO.viewGrades(currentUser));
    }

    // ===========================================
    //          CLASSROOM PAGE - STUDENT
    // ===========================================

    @FXML private void submitAssignment() {
        String selectedAssignment = currClassroomListView.getSelectionModel().getSelectedItem();
        setCurrentAssignment(selectedAssignment);
        String work = studentWork.getText();
        assignmentDAO.submitAssignment(currentUser, currentClassroomInfo, work, currentAssignmentString.split(", ")[0]);
        currClassroomListView.getItems().setAll("Work added to " + currentAssignmentString.split(", ")[0] + " : " + work);
    }

    // ===========================================
    //        CLASSROOM PAGE - TEACHER ONLY
    // ===========================================
    @FXML private void addAssignmentToClassTab() {
        String assignmentNameStr = assignmentName.getText();
        String assignmentDescriptionStr = assignmentDescription.getText();
        String assignmentGradeStr = assignmentGrade.getText();
        String assignmentStudentStr = assignmentStudent.getText();
        LocalDate assignmentDueDate = assignmentDatePicker.getValue();
        String assignmentDueDateStr = assignmentDueDate.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        assignmentDAO.addAssignment(currentUser, currentClassroomInfo, assignmentNameStr, assignmentDescriptionStr, Double.parseDouble(assignmentGradeStr), assignmentStudentStr, assignmentDueDateStr);
        System.out.println("Assignment added: " + assignmentNameStr);
    }

    // ===========================================
    //                 CALENDAR
    // ===========================================

    @FXML
    private void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        drawCalendar();
    }

    @FXML
    private void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        drawCalendar();
    }

    private void drawCalendar() {
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        double width = calendar.getPrefWidth();
        double height = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spaceH = calendar.getHgap();
        double spaceV = calendar.getVgap();

        Map<Integer, List<CalendarActivity>> activityMap =
                getCalendarActivitiesMonth(dateFocus);

        
        LocalDate firstOfMonth = dateFocus.toLocalDate().withDayOfMonth(1);
        int monthLength = firstOfMonth.lengthOfMonth();

        
        int dowValue = firstOfMonth.getDayOfWeek().getValue();

        
        int offset = dowValue % 7; 

        calendar.getChildren().clear();
        int day = 1;

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                StackPane cell = new StackPane();

                Rectangle rect = new Rectangle();
                rect.setFill(Color.TRANSPARENT);
                rect.setStroke(Color.BLACK);
                rect.setStrokeWidth(strokeWidth);

                double cellW = (width / 7) - strokeWidth - spaceH;
                double cellH = (height / 6) - strokeWidth - spaceV;
                rect.setWidth(cellW);
                rect.setHeight(cellH);

                cell.getChildren().add(rect);

                int cellIndex = col + row * 7;

                if (cellIndex >= offset && day <= monthLength) {
                    Text dateText = new Text(String.valueOf(day));
                    dateText.setTranslateY(-(cellH / 2) * 0.75);
                    cell.getChildren().add(dateText);

                    List<CalendarActivity> activities = activityMap.get(day);
                    if (activities != null) {
                        createCalendarActivity(activities, cellH, cellW, cell);
                    }

                    if (today.getYear() == dateFocus.getYear()
                            && today.getMonth() == dateFocus.getMonth()
                            && today.getDayOfMonth() == day) {
                        rect.setStroke(Color.BLUE);
                    }

                    day++;
                }

                calendar.getChildren().add(cell);
            }
        }
    }

    private void createCalendarActivity(List<CalendarActivity> activities,
                                        double cellH, double cellW,
                                        StackPane cell) {
        VBox box = new VBox();

        for (int i = 0; i < activities.size(); i++) {
            if (i >= 2) {
                Text more = new Text("...");
                box.getChildren().add(more);
                more.setOnMouseClicked(e -> System.out.println(activities));
                break;
            }

            CalendarActivity activity = activities.get(i);
            Text text = new Text(activity.getUserName() + ", " +
                    activity.getDate().toLocalTime());

            box.getChildren().add(text);
            text.setOnMouseClicked(e -> System.out.println(text.getText()));
        }

        box.setTranslateY((cellH / 2) * 0.20);
        box.setMaxWidth(cellW * 0.8);
        box.setMaxHeight(cellH * 0.65);
        box.setStyle("-fx-background-color:GRAY");

        cell.getChildren().add(box);
    }

    private Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> activities) {
        Map<Integer, List<CalendarActivity>> map = new HashMap<>();

        for (CalendarActivity activity : activities) {
            int date = activity.getDate().getDayOfMonth();
            map.computeIfAbsent(date, k -> new ArrayList<>()).add(activity);
        }

        return map;
    }

    private Map<Integer, List<CalendarActivity>> getCalendarActivitiesMonth(ZonedDateTime date) {
        List<CalendarActivity> list = new ArrayList<>();
        int y = date.getYear();
        int m = date.getMonthValue();
        Random r = new Random();

        for (int i = 0; i < 50; i++) {
            ZonedDateTime t = ZonedDateTime.of(
                    y, m, r.nextInt(27) + 1,
                    16, 0, 0, 0,
                    date.getZone()
            );
            list.add(new CalendarActivity(t, "user", 1234533));
        }

        return createCalendarMap(list);
    }

    // ===========================================
    //             EMAIL LOGIN + SEND
    // ===========================================

    private void showEmailLoginPopup() {
        Stage popup = new Stage();
        popup.setTitle("Outlook Login");
        popup.setWidth(350);
        popup.setHeight(200);

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20;");

        javafx.scene.control.TextField emailField = new javafx.scene.control.TextField();
        emailField.setPromptText("Outlook Email");

        javafx.scene.control.PasswordField passwordField = new javafx.scene.control.PasswordField();
        passwordField.setPromptText("Outlook Password");

        javafx.scene.control.Button saveButton = new javafx.scene.control.Button("Save");

        saveButton.setOnAction(e -> {
            outlookEmail = emailField.getText();
            outlookPassword = passwordField.getText();
            popup.close();
        });

        layout.getChildren().addAll(
                new Text("Enter Outlook Login:"),
                emailField,
                passwordField,
                saveButton
        );

        popup.setScene(new Scene(layout));
        popup.showAndWait();
    }

    @FXML
    private void sendMessage() {
        try {
            String recipient = recipientField.getText();
            String body = messageBody.getText();

            if (recipient == null || recipient.isEmpty()) {
                System.out.println("No recipient entered.");
                return;
            }

            if (body == null || body.isEmpty()) {
                System.out.println("Message body is empty.");
                return;
            }

            if (outlookEmail == null || outlookPassword == null) {
                System.out.println("Email credentials not entered.");
                showEmailLoginPopup();
                return;
            }

            EmailSender.sendEmail(
                    recipient,
                    "Message from iLearn",
                    body,
                    outlookEmail,
                    outlookPassword
            );

            System.out.println("Message sent!");
            messageBody.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===========================================
    //                 MAIN
    // ===========================================
    public static void main(String[] args) {
        launch();
    }

}
