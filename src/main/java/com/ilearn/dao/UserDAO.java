package com.ilearn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.ilearn.User;

public class UserDAO {
    private final Connection connection;
    
    public UserDAO(Connection connection) {
        this.connection = connection; // share same connection across all files
    }
    //insert new user to users table in sqlite db
    public void connectToDatabase(User user){
        String insertNewUser = "INSERT INTO users (id, name, password, isTeacher, classrooms) VALUES (?, ?, ?, ?, ?)";
        try(PreparedStatement pstmtNewUser = connection.prepareStatement(insertNewUser)){
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

    //when logging in, set the user's id, isTeacher, classrooms from users table in sqlite db
    //making sure the logged in user is set to the current user object passed to the app 
    public void setFromDatabase(String username, String pw, User currUser) {
        String getUser = "SELECT id, isTeacher, classrooms FROM users WHERE name=? AND password=?";
        try(PreparedStatement pstmtGetUser = connection.prepareStatement(getUser)) {
            pstmtGetUser.setString(1, username);
            pstmtGetUser.setString(2, pw);
            try(ResultSet res = pstmtGetUser.executeQuery()){
                while (res.next()) {
                    currUser.setId(res.getInt("id"));
                    currUser.setIsTeacher(res.getInt("isTeacher") == 1);
                    String classroomName = res.getString("classrooms");
                    currUser.setClassroomsNames(classroomName.split("[,;|\\s]+"));
                }
            }
        } catch (SQLException er) {
            er.printStackTrace(System.err);
        }
    }

    // Checks if a user is registered
    public boolean isRegistered(String username, String pw) {
        String findUser = "SELECT * FROM users WHERE name = ? AND password = ?";
        try(PreparedStatement pstmtFindUser = connection.prepareStatement(findUser)){
            pstmtFindUser.setString(1, username);
            pstmtFindUser.setString(2, pw);
            try (ResultSet res = pstmtFindUser.executeQuery()){
                while (res.next()){
                    String name = res.getString("name");
                    String password = res.getString("password");
                    return (username.equals(name) && password.equals(pw));
                }
            }
        } catch (SQLException er) {
            er.printStackTrace(System.err);
        }
        return false;
    }

}
