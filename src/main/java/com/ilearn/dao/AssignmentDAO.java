package com.ilearn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.ilearn.User;

public class AssignmentDAO {
    private final Connection connection;
    
    public AssignmentDAO(Connection connection) {
        this.connection = connection; // share same connection across all files
    }

    public void addAssignment(User user, String currentClassroomInfo, String assignmentName, String description, double grade, String student, String dueDate){
        //for(Classroom classroomObj: classroomArr){
            //if(classroomObj.getClassroomName().equals(currentClassroomName)){
                //Assignment assignment = new Assignment(name, description, grade);
                //classroomObj.addAssignment(assignment);
        String insertNewAssignment = "INSERT INTO assignments (user_id, class_id, class_name, name, description, grade, student, due_date, teacher) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement pstmtNewAssignment = connection.prepareStatement(insertNewAssignment);){
            pstmtNewAssignment.setInt(1, user.getId());
            pstmtNewAssignment.setInt(2, Integer.parseInt(currentClassroomInfo.split(", ")[1]));
            pstmtNewAssignment.setString(3, currentClassroomInfo.split(", ")[0]);
            pstmtNewAssignment.setString(4, assignmentName);
            pstmtNewAssignment.setString(5, description);
            pstmtNewAssignment.setDouble(6, grade);
            pstmtNewAssignment.setString(7, student);
            pstmtNewAssignment.setString(8, dueDate);
            pstmtNewAssignment.setString(9, user.getName());
            pstmtNewAssignment.executeUpdate();
        } catch (SQLException er) {
            er.printStackTrace(System.err);
        }
            //}
        //}
    }

    public List<String> getAssignments(User user, String currentClassroomInfo){
        //ArrayList<Assignment> assignmentList = new ArrayList<>();
        List<String> assignmentNameList = new ArrayList<>();
        //for(Classroom classroomObj: classroomArr){
            //if(classroomObj.getClassroomName().equals(currentClassroomName)){
                //assignmentList = classroomObj.getAssignments();
        String query = user.getIsTeacher() ? "SELECT * FROM assignments WHERE teacher = ? AND class_name = ? AND class_id = ?" : "SELECT * FROM assignments WHERE student = ? AND class_name = ? AND class_id = ?";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, currentClassroomInfo.split(", ")[0]);
            stmt.setInt(3, Integer.parseInt(currentClassroomInfo.split(", ")[1]));
            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    String assignmentName = rs.getString("name");
                    String description = rs.getString("description");
                    double grade = rs.getDouble("grade");
                    String name = user.getIsTeacher() ? rs.getString("student") : rs.getString("teacher");
                    String dueDateString = rs.getString("due_date");
                    String work = rs.getString("work");
                    assignmentNameList.add(assignmentName + ", " + description + ", " + name + ", " + String.valueOf(grade) + ", " + dueDateString + ", " + work);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignmentNameList;
            //}
        //}
    }

    public void submitAssignment(User user, String currentClassroomInfo, String work, String assignmentName, String studentName){
		String updateAssignment = "UPDATE assignments SET work = ? WHERE class_name = ? AND class_id = ? AND name = ? AND student = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(updateAssignment)) {
            pstmt.setString(1, work);
            pstmt.setString(2, currentClassroomInfo.split(", ")[0]);
            pstmt.setInt(3, Integer.parseInt(currentClassroomInfo.split(", ")[1]));
            pstmt.setString(4, assignmentName);
            pstmt.setString(5, studentName);
            pstmt.executeUpdate();      
        } catch (SQLException er) {
            er.printStackTrace(System.err);
        }
	}	
}
