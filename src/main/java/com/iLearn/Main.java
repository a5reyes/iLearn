//iLearn - COMP390-002 - Richard Simison, Matthew Charette, Saurav Niroula, Albert Reyes, Charles McNamara
package com.ilearn;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.*;
import com.ilearn.dao.AssignmentDAO;
import com.ilearn.dao.ClassroomDAO;
import com.ilearn.dao.RosterDAO;
import com.ilearn.dao.UserDAO;

public class Main{
    //variables for daos
    public static Connection connection;
    public static UserDAO userDAO;
    public static ClassroomDAO classroomDAO;
    public static RosterDAO rosterDAO;
    public static AssignmentDAO assignmentDAO;

    //passing setuser object from loginRegister JFRAME into appcontroller
    public static void HomePage(SetUser currUser){
        User user = currUser.getUser();
        AppController.setCurrentUser(user);
        AppController.main(null);
    }

    //initializes db; if db locked cause its busy, wait 5ms; foreign_keys constraints off
    public static void initDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:ilearn.db");
        connection.createStatement().execute("PRAGMA busy_timeout = 5000;");
        connection.createStatement().execute("PRAGMA foreign_keys = ON;");

        userDAO = new UserDAO(connection);
        classroomDAO = new ClassroomDAO(connection);
        rosterDAO = new RosterDAO(connection);
        assignmentDAO = new AssignmentDAO(connection);
    }

    public static void main(String[] args) { 
        try {
            initDatabase(); //initialize db connection all at once
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new LoginRegister().setVisible(true)); //start loginRegister JFrame
    }
}