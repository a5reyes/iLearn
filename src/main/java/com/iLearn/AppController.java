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
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.DayOfWeek;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.ilearn.dao.AssignmentDAO;
import com.ilearn.dao.ClassroomDAO;

import com.ilearn.dao.RosterDAO;
import com.ilearn.dao.UserDAO;

public class AppController extends Application implements Initializable {
    private static User currentUser;
    private static Classroom currentClassroom;
    private final ClassroomDAO classroomDAO;
    private final AssignmentDAO assignmentDAO;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private ZonedDateTime dateFocus;
    private ZonedDateTime today;

    public AppController() {
        this.classroomDAO = Main.classroomDAO;
        this.assignmentDAO = Main.assignmentDAO;
    }
    
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
    public static void setCurrentClassroom(Classroom classroom) { currentClassroom = classroom; }
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

    // ===========================================
    //                START
    // ===========================================
    @Override
    public void start(Stage stage) throws IOException {
        URL logoURL = getClass().getResource("/com/ilearn/logo.png");
        Image logo = new Image(logoURL.toString());
        stage.getIcons().add(logo);
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
        String selectedClass = mainPageListView.getSelectionModel().getSelectedItem();
        String selectedClassName = selectedClass.split(", ")[0];
        String selectedClassId = selectedClass.split(", ")[1];
        for(Classroom userClassroom : currentUser.getClassrooms()){
            if(Objects.equals(userClassroom.getClassroomName(), selectedClassName) && Objects.equals(userClassroom.getClassroomId(), Integer.parseInt(selectedClassId))){
                setCurrentClassroom(userClassroom);
            }
        }
        System.out.println("Selected item: " + selectedClass);
    }

    // ===========================================
    //          CLASSROOM PAGE - DEFAULT
    // ===========================================
    @FXML private void currClassInfoToClassTab() {
        currClassroomListView.getItems().setAll(classroomDAO.viewClassroomInfo(currentClassroom));
    }

    @FXML private void currRosterToClassTab() {
        currClassroomListView.getItems().setAll(classroomDAO.viewClassroomRoster(currentClassroom));
    }

    @FXML private void currGradebookToClassTab() {
        currClassroomListView.getItems().setAll(
        assignmentDAO.getAllAssignments(currentUser, currentClassroom)
                     .stream()
                     .map(Assignment::toString)
                     .collect(Collectors.toList())
    );
    }

    @FXML private void currAssignmentToClassTab() {
        currClassroomListView.getItems().setAll(assignmentDAO.getAssignments(currentUser, currentClassroom));
    }

    @FXML private void currGradebookToGradebookTab() {
        currGradebookListView.getItems().setAll(
        assignmentDAO.getAllAssignments(currentUser, currentClassroom)
                     .stream()
                     .map(Assignment::toString)
                     .collect(Collectors.toList())
    );
    }

    // ===========================================
    //        CLASSROOM PAGE - STUDENT ONLY
    // ===========================================

    @FXML private void submitAssignment() {
        String selectedAssignment = currClassroomListView.getSelectionModel().getSelectedItem();
        String work = studentWork.getText();
        assignmentDAO.submitAssignment(currentUser, currentClassroom, work, selectedAssignment.split(", ")[0], currentUser.getName());
        System.out.println("Work added to " + selectedAssignment.split(", ")[0] + " : " + work);
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
        assignmentDAO.addAssignment(currentUser, currentClassroom, assignmentNameStr, assignmentDescriptionStr, Double.parseDouble(assignmentGradeStr), assignmentStudentStr, assignmentDueDateStr);
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
    // Set header text
    year.setText(String.valueOf(dateFocus.getYear()));
    month.setText(String.valueOf(dateFocus.getMonth()));

    // Use actual width if available, otherwise prefWidth
    double width  = calendar.getWidth()  > 0 ? calendar.getWidth()  : calendar.getPrefWidth();
    double height = calendar.getHeight() > 0 ? calendar.getHeight() : calendar.getPrefHeight();
    double strokeWidth = 1;
    double spaceH = calendar.getHgap();
    double spaceV = calendar.getVgap();

    // Activities mapped by day-of-month (1..31)
    Map<Integer, List<CalendarActivity>> activityMap =
            getCalendarActivitiesMonth(dateFocus);

    // ---- DATE LOGIC ----

    // 1) First day of this month
    LocalDate firstOfMonth = dateFocus.toLocalDate().withDayOfMonth(1);
    int monthLength = firstOfMonth.lengthOfMonth();

    // 2) Find the Sunday on or before the first of the month
    LocalDate start = firstOfMonth;
    while (start.getDayOfWeek() != DayOfWeek.SUNDAY) {
        start = start.minusDays(1);
    }

    // 3) Clear old cells and walk forward one day per cell
    calendar.getChildren().clear();
    LocalDate current = start;

    // Make cells narrow enough that 7 always fit in a row
    // (using 1/8 of the width gives extra space for gaps and borders)
    double cellW = (width / 8.0);
    double cellH = (height / 6.0);

    int cellCount = 0; // debug: count boxes

    for (int row = 0; row < 6; row++) {
        for (int col = 0; col < 7; col++) {
            StackPane cell = new StackPane();

            Rectangle rect = new Rectangle();
            rect.setFill(Color.TRANSPARENT);
            rect.setStroke(Color.BLACK);
            rect.setStrokeWidth(strokeWidth);

            rect.setWidth(cellW);
            rect.setHeight(cellH);

            cell.getChildren().add(rect);

            // Only draw a day number if this date is actually in the focus month
            if (current.getMonth() == firstOfMonth.getMonth()
                    && current.getYear() == firstOfMonth.getYear()) {

                int dayOfMonth = current.getDayOfMonth();

                Text dateText = new Text(String.valueOf(dayOfMonth));
                dateText.setTranslateY(-(cellH / 2.0) * 0.75);
                cell.getChildren().add(dateText);

                // Add activities for this day if any
                List<CalendarActivity> activities = activityMap.get(dayOfMonth);
                if (activities != null) {
                    createCalendarActivity(activities, cellH, cellW, cell);
                }

                // Highlight today
                if (today.toLocalDate().equals(current)) {
                    rect.setStroke(Color.BLUE);
                }
            }

            calendar.getChildren().add(cell);
            current = current.plusDays(1); // move to next date
            cellCount++;
        }
    }

    // Debug: verify we really made 42 boxes
    System.out.println("Calendar cells created: " + cellCount);
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
        List<Assignment> allAssignments = assignmentDAO.getAllAssignments(currentUser, currentClassroom);
        for (int i = 0; i < allAssignments.size(); i++) {
            String assignmentName = allAssignments.get(i).getAssignmentName();
            String assignmentDueDateString = allAssignments.get(i).getDueDateString();
            String[] dateParts = assignmentDueDateString.split("-");
            int day = Integer.parseInt(dateParts[1]);
            ZonedDateTime t = ZonedDateTime.of(
                    y, m, day,
                    11, 59, 0, 0,
                    date.getZone()
            );
            list.add(new CalendarActivity(t, assignmentName, currentUser.getId()));
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
