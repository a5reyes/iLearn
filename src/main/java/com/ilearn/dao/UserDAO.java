package com.ilearn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ilearn.Main;
import com.ilearn.User;

public class UserDAO {
    Connection connection = Main.connect();
    //insert new user to users table in sqlite db
    public void connectToDatabase(User user){
        try(Statement statement = connection.createStatement()){
            String insertNewUser = "INSERT INTO users (id, name, password, isTeacher, classrooms) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmtNewUser = connection.prepareStatement(insertNewUser);
            pstmtNewUser.setInt(1, user.getId());
            pstmtNewUser.setString(2, user.getName());
            pstmtNewUser.setString(3, user.getPassword());
            pstmtNewUser.setInt(4, user.getIsTeacher() ? 1 : 0);
            pstmtNewUser.setString(5, String.join(",", user.getClassroomsNames()));
            pstmtNewUser.executeUpdate();
        } catch (SQLException er) {
            er.printStackTrace(System.err);
        }
    }

    //when logging in, get the classroom's id, isTeacher, classrooms from users table in sqlite db and set those fields to user object
    //making sure the logged in user is set to the current user object passed to the app 
    public void getFromDatabase(String username, String pw, User currUser) {
        try {
            String getUser = "SELECT id, isTeacher, classrooms FROM users WHERE name=? AND password=?";
            PreparedStatement pstmtGetUser = connection.prepareStatement(getUser);
            pstmtGetUser.setString(1, username);
            pstmtGetUser.setString(2, pw);
            ResultSet res = pstmtGetUser.executeQuery();
            while (res.next()) {
                currUser.setId(res.getInt("id"));
                currUser.setIsTeacher(res.getInt("isTeacher") == 1);

                String classroomName = res.getString("classrooms");
                currUser.setClassroomsNames(classroomName.split("[,;|\\s]+"));
            }
        } catch (SQLException er) {
            er.printStackTrace(System.err);
        }
    }

}
