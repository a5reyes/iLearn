package com.ilearn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ilearn.Classroom;
import com.ilearn.Main;
import com.ilearn.User;

public class RosterDAO {
    Connection connection = Main.connect();
    // Fetches and inserts roster data depending on the presence of exisitng data
    public void connectToDatabase(User user, Classroom classroom){
		try(Statement statement = connection.createStatement()){
            String checkIfInRoster = "SELECT * FROM rosters WHERE user_id = ? AND username = ? AND class_id = ? AND class_name = ?";
            PreparedStatement pstmtIfInRoster = connection.prepareStatement(checkIfInRoster);
			pstmtIfInRoster.setInt(1, user.getId());
            pstmtIfInRoster.setString(2, user.getName());
			pstmtIfInRoster.setInt(3, classroom.getClassroomId());
			pstmtIfInRoster.setString(4, classroom.getClassroomName());
            try (ResultSet rs = pstmtIfInRoster.executeQuery()) {
                if (!rs.next()) {
                    String insertNewUser = "INSERT INTO rosters (user_id, username, class_id, class_name) VALUES (?, ?, ?, ?)";
                    PreparedStatement pstmtNewUser = connection.prepareStatement(insertNewUser);
                    pstmtNewUser.setInt(1, user.getId());
                    pstmtNewUser.setString(2, user.getName());
			        pstmtNewUser.setInt(3, classroom.getClassroomId());
                    pstmtNewUser.setString(4, classroom.getClassroomName());
                    pstmtNewUser.executeUpdate(); 
                }
            }
        } catch (SQLException er) {
            er.printStackTrace(System.err);
        }
	}	
}
