package com.ilearn;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class User {
    private int id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
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

    //user's classrooms setter
    public void setClassrooms(String[] classrooms) {
        this.classrooms = classrooms;
    }
    
    //user's classrooms getter
    public String getClassrooms() {
        return String.join(", ", this.classrooms);
    }

    //view user's classrooms from sqlite db
    public List<String> viewClassrooms(){
        List<String> classes = new ArrayList<>();
        try {
            String query = "SELECT class_name, class_id FROM classrooms WHERE user_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, this.id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("class_name");
                int id = rs.getInt("class_id");
                classes.add(name + ", " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }

    public void submitAssignment() {
        //
        
    }

    //view user's total gradebook from sqlite db gradebook table
    public List<String> viewGrades() {
        List<String> gradebookArr = new ArrayList<>();
        try {
            String query = "SELECT class_name, assignment, grade FROM gradebooks WHERE user_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, this.id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("class_name");
                String assignment = rs.getString("assignment");
                double grade = rs.getDouble("grade");
                gradebookArr.add(name + ", " + assignment + ", " + String.valueOf(grade));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gradebookArr;
    }

    //view a classroom's info - discussions, teacher, meeting-time etc. from sqlite db classrooms table
    public List<String> viewClassroomInfo(String currentClassroomInfo) {
        List<String> classInfo = new ArrayList<>();
        try {
            String query = "SELECT class_name, class_id, discussions, teacher, meeting_time FROM classrooms WHERE class_name = ? OR class_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, currentClassroomInfo.split(", ")[0]);
            stmt.setInt(2, Integer.parseInt(currentClassroomInfo.split(", ")[1]));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("class_name");
                int id = rs.getInt("class_id");
                String discussions = rs.getString("discussions");
                String teacher = rs.getString("teacher");
                String meetingTime = rs.getString("meeting_time");
                classInfo.add(name);
                classInfo.add(Integer.toString(id));
                classInfo.add(discussions);
                classInfo.add(teacher);
                classInfo.add(meetingTime);
                break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classInfo;
    }

    //view a classroom's gradebook from sqlite db gradebook table
    public List<String> viewClassroomGrades(String currentClassroomInfo){
        List<String> gradebookArr = new ArrayList<>();
        try {
            String query = "SELECT class_name, assignment, grade FROM gradebooks WHERE user_id = ? AND class_name = ? AND class_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, this.id);
            stmt.setString(2, currentClassroomInfo.split(", ")[0]);
            stmt.setInt(3, Integer.parseInt(currentClassroomInfo.split(", ")[1]));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("class_name");
                String assignment = rs.getString("assignment");
                double grade = rs.getDouble("grade");
                gradebookArr.add(name + ", " + assignment + ", " + String.valueOf(grade));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gradebookArr;
    }

    public void sendMessage() {
        //
    }

    //view a classroom's roster from sqlite db rosters table
    public List<String> viewClassroomRoster(String currentClassroomInfo) {
        List<String> rosterArr = new ArrayList<>();
        try {
            String query = "SELECT username FROM rosters WHERE class_name = ? AND class_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, currentClassroomInfo.split(", ")[0]);
            stmt.setInt(2, Integer.parseInt(currentClassroomInfo.split(", ")[1]));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("username");
                rosterArr.add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rosterArr;
    }

    public void viewCalendar() {
        //
    }

    //insert new user to users table in sqlite db
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

    //when logging in, get the classroom's id, isTeacher, classrooms from users table in sqlite db and set those fields to user object
    //making sure the logged in user is set to the current user object passed to the app 
    public void getFromDatabase(String username, String pw, User currUser){
        try(Statement statement = connection.createStatement()){
            String getUser = "SELECT id, isTeacher, classrooms FROM users WHERE name=? AND password=?";
            PreparedStatement pstmtGetUser = connection.prepareStatement(getUser);
            pstmtGetUser.setString(1, username);
            pstmtGetUser.setString(2, pw);
            ResultSet res = pstmtGetUser.executeQuery();
            while (res.next()) {
                Integer id = res.getInt("id");
                Boolean isTeacherCheck = (res.getInt("isTeacher") == 1);
                String classrooms = res.getString("classrooms");
                currUser.setId(id);
                if(isTeacherCheck){
                    currUser.isTeacher();
                } else {
                    currUser.isStudent();
                }
                currUser.setClassrooms(classrooms.split("[,;|\\s]+"));
            }
        } catch (SQLException er) {
            er.printStackTrace(System.err);
        }
    }

    public static void main(String[] args) {
        //
    }
}