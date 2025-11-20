//iLearn - COMP390-002 - Richard Simison, Matthew Charette, Saurav Niroula, Albert Reyes, Charles McNamara
package com.ilearn;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.*;

public class Main{
    private static Connection conn;
    public static void HomePage(SetUser currUser){
        User user = currUser.getUser();
        AppController.setCurrentUser(user);
        AppController.main(null);
    }

    // Connects application to SQL database
    public static Connection connect() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection("jdbc:sqlite:ilearn.db");
                conn.createStatement().execute("PRAGMA busy_timeout = 5000;");
                conn.createStatement().execute("PRAGMA journal_mode = WAL;");
                System.out.println("Connected to SQLite.");
                System.out.println(">>> Opening connection: " + System.identityHashCode(conn));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    public static void main(String[] args) { 
        SwingUtilities.invokeLater(() -> new LoginRegister().setVisible(true));
    }
}