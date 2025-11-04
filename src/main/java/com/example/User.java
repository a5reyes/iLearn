package com.example;
import java.sql.*;

import com.ilearn.Main;

public class User {
    private int id;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String password;
    private Boolean isTeacher;
    private String name;
    private String[] classrooms;
    Connection connection = Main.connect();

    public User(int id, String password, Boolean isTeacher, String name, String[] classrooms) {
        this.id = id;
        this.password = password;
        this.isTeacher = isTeacher;
        this.name = name;
        this.classrooms = classrooms;
    }

    public String getName() {
        return this.name;
    }

    public Boolean getIsTeacher() {
        return this.isTeacher;
    }

    public void isStudent(){
        this.isTeacher = false;
    }

    public void isTeacher(){
        this.isTeacher = true;
    }
    
    public String viewClassrooms() {
        return String.join(", ", this.classrooms);
    }

    public void submitAssignment() {
        //
        
    }

    public void viewGrades() {
        //
    }

    public void sendMessage() {
        //
    }

    public void viewClassroomRoster() {
        //
    }

    public void viewCalendar() {
        //
    }

    public void connectToDatabase(){
        try(Statement statement = connection.createStatement()){
            String insertNewUser = "INSERT INTO users (id, name, password, isTeacher, classrooms) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmtNewUser = connection.prepareStatement(insertNewUser);
            pstmtNewUser.setInt(1, this.id);
            pstmtNewUser.setString(2, this.name);
            pstmtNewUser.setString(3, this.password);
            pstmtNewUser.setInt(4, this.isTeacher ? 1 : 0);
            pstmtNewUser.setString(5, String.join(",", this.classrooms));
            pstmtNewUser.executeUpdate();
        } catch (SQLException er) {
            er.printStackTrace(System.err);
        }
    }

    public static void main(String[] args) {
        //
    }
}
