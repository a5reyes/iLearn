package com.example;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.iLearn.Main;

public class MainPageController {
    @FXML
    private ListView<String> classListView;

    private User currentUser;
    private Connection connection = Main.connect();

    public void setUser(User user) {
        this.currentUser = user;
        loadUserClasses();
    }

    private void loadUserClasses() {
        List<String> classes = new ArrayList<>();

        try {
            String query = "SELECT class_name, class_id FROM classes WHERE user_id = ?";
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
    
}