package com.iLearn;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.*;

import com.example.User;



public class Main{
    public static void HomePage(User user){
        AppController.main(null);
        //get profile username - user.getName()
        //student schedule-week at a glance; idea taken from infobear
        /*
        String[] sectionsInfo = user.viewClassrooms().split(", ");
        for(String sectionInfo : sectionsInfo){
            Integer classId = Integer.parseInt(sectionInfo.replaceAll("\\D", "")); 
            Classroom course = new Classroom(classId, "Dr. Poonam Kumari", "Hello! Check syllabus", "TR 12:30PM ~ 2:00PM & T 3:15PM ~ 4:00PM");
            JTextArea sectionLabel = new JTextArea(sectionInfo + "\n" + course.getTeacher() + "\n" + course.getMeetingTime() +  "\n" + course.getDiscussions());
            calendarPanel.add(sectionLabel);
        }
        */
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