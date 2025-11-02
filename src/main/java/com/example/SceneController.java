package com.example;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SceneController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    // ---------- your existing navigation (unchanged) ----------
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
    // ----------------------------------------------------------

    // ---------- calendar hooks (these match ids already in your FXML) ----------
    @FXML private AnchorPane /* root of Calender.fxml */ anchorPane; // optional if root has no fx:id
    @FXML private Button backMonth;        // fx:id="backMonth"
    @FXML private Button forwardMonth;     // fx:id="forwardMonth"
    @FXML private Text year;               // fx:id="year"
    @FXML private Text month;              // fx:id="month"
    // There is a FlowPane in your FXML without an fx:id; we'll find it at runtime:
    private FlowPane calendarGrid;

    private YearMonth currentMonth;

    // Called whenever any FXML using this controller is loaded.
    @FXML
    private void initialize() {
        // If we're not on the calendar page, these controls will be null. Just return.
        if (backMonth == null && forwardMonth == null && month == null && year == null) {
            return;
        }

        // We *are* on the calendar page. Wire it up programmatically without changing FXML.
        currentMonth = YearMonth.now();
        calendarGrid = findFirstFlowPane(anchorPane);
        if (calendarGrid == null) return; // safety

        // Make the grid wrap to 7 columns (7 * 90 + 6 * 5 ≈ 630, matching your FXML width)
        calendarGrid.setHgap(5);
        calendarGrid.setVgap(5);
        calendarGrid.setPrefWrapLength(7 * 90 + 6 * 5);

        // Button actions
        if (backMonth != null) {
            backMonth.setOnAction(e -> {
                currentMonth = currentMonth.minusMonths(1);
                renderCalendar();
            });
        }
        if (forwardMonth != null) {
            forwardMonth.setOnAction(e -> {
                currentMonth = currentMonth.plusMonths(1);
                renderCalendar();
            });
        }

        // Initial render
        renderCalendar();
    }

    // Find the first FlowPane in the scene graph (your Calender.fxml has exactly one)
    private FlowPane findFirstFlowPane(Pane rootPane) {
        if (rootPane == null) return null;
        Deque<javafx.scene.Parent> stack = new ArrayDeque<>();
        stack.push(rootPane);
        while (!stack.isEmpty()) {
            javafx.scene.Parent p = stack.pop();
            if (p instanceof FlowPane fp) return fp;
            if (p instanceof Pane pane) {
                for (Node n : pane.getChildrenUnmodifiable()) {
                    if (n instanceof Pane child) stack.push(child);
                }
            }
        }
        return null;
    }

    private void renderCalendar() {
        calendarGrid.getChildren().clear();

        // Header texts (use your existing Text nodes: fx:id="month" and "year")
        String monthName = currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        if (month != null) month.setText(monthName);
        if (year != null)  year.setText(Integer.toString(currentMonth.getYear()));

        // Compute leading blanks so the 1st lands on the correct weekday (Sunday header)
        int firstDow = currentMonth.atDay(1).getDayOfWeek().getValue(); // 1=Mon..7=Sun
        int offset = firstDow % 7; // convert so Sunday->0, Mon->1, ... Sat->6

        for (int i = 0; i < offset; i++) {
            calendarGrid.getChildren().add(blankCell());
        }

        int days = currentMonth.lengthOfMonth();
        LocalDate today = LocalDate.now();
        for (int d = 1; d <= days; d++) {
            LocalDate date = currentMonth.atDay(d);
            calendarGrid.getChildren().add(dayCell(d, date.equals(today)));
        }
    }

    private StackPane blankCell() {
        StackPane cell = new StackPane();
        cell.setPrefSize(90, 70);
        cell.setStyle("-fx-background-color:#f5f5f5; -fx-background-radius:6;");
        return cell;
    }

    private StackPane dayCell(int dayNumber, boolean isToday) {
        StackPane cell = new StackPane();
        cell.setPrefSize(90, 70);

        Label label = new Label(Integer.toString(dayNumber));
        cell.getChildren().add(label);
        cell.setStyle(
            "-fx-background-color:white; -fx-background-radius:6; " +
            "-fx-border-color:#ddd; -fx-border-radius:6;"
        );

        if (isToday) {
            cell.setStyle(
                "-fx-background-color:#e8f0fe; -fx-background-radius:6; " +
                "-fx-border-color:#5b9aff; -fx-border-radius:6;"
            );
            label.setStyle("-fx-font-weight: bold;");
        }
        return cell;
    }
}
