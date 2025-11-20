package com.ilearn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ilearn.Classroom;
import com.ilearn.User;

public class GradebookDAO {
    private final Connection connection;
    
    public GradebookDAO(Connection connection) {
        this.connection = connection; // share same connection across all files
    }
    //view user's total gradebook from sqlite db gradebook table
    public List<String> viewGrades(User user) {
        List<String> gradebookArr = new ArrayList<>();
        String query = "SELECT class_name, assignment, grade FROM gradebooks WHERE user_id = ?";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, user.getId());
            try(ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    String name = rs.getString("class_name");
                    String assignment = rs.getString("assignment");
                    double grade = rs.getDouble("grade");
                    gradebookArr.add(name + ", " + assignment + ", " + String.valueOf(grade));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gradebookArr;
    }

    // Fetches and inserts gradebook data depending on the presence of exisitng data
    public void connectToDatabase(User user, Classroom classroom){
        String checkIfInGradebook = "SELECT * FROM gradebooks WHERE user_id = ? AND class_id = ? AND class_name = ?";
		try(PreparedStatement pstmtIfInGradeBook = connection.prepareStatement(checkIfInGradebook)){
			pstmtIfInGradeBook.setInt(1, user.getId());
			pstmtIfInGradeBook.setInt(2, classroom.getClassroomId());
			pstmtIfInGradeBook.setString(3, classroom.getClassroomName());
            try (ResultSet rs = pstmtIfInGradeBook.executeQuery()) {
                if (!rs.next()) {
                    String insertNewGradebook = "INSERT INTO gradebooks (user_id, class_id, class_name) VALUES (?, ?, ?)";
                    try(PreparedStatement pstmtNewGradebook = connection.prepareStatement(insertNewGradebook)){
                        pstmtNewGradebook.setInt(1, user.getId());
                        pstmtNewGradebook.setInt(2, classroom.getClassroomId());
                        pstmtNewGradebook.setString(3, classroom.getClassroomName());
                        pstmtNewGradebook.executeUpdate(); 
                    }
                }
            }
        } catch (SQLException er) {
            er.printStackTrace(System.err);
        }
	}	

}
