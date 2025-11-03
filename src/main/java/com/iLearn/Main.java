package com.iLearn;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.*;

import com.example.User;

public class Main{
    public static void HomePage(User user){
        AppController.main(null);
    }

    public static Connection connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:ilearn.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return conn;
    }


    public static void main(String[] args) { 
        SwingUtilities.invokeLater(() -> new LoginRegister().setVisible(true));
    }
}