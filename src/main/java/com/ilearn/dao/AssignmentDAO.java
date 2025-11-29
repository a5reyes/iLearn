package com.ilearn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.ilearn.Assignment;
import com.ilearn.Classroom;
import com.ilearn.User;

public class AssignmentDAO {
    private final Connection connection;
    
    public AssignmentDAO(Connection connection) {
        this.connection = connection; // share same connection across all files
    }

    //this is where a teacher can assign assignments for classroom
    public void addAssignment(User user, Classroom currentClassroom, String assignmentName, String description, double grade, String student, String dueDate){
        String insertNewAssignment = "INSERT INTO assignments (user_id, class_id, class_name, name, description, grade, student, due_date, teacher) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement pstmtNewAssignment = connection.prepareStatement(insertNewAssignment);){
            pstmtNewAssignment.setInt(1, user.getId());
            pstmtNewAssignment.setInt(2, currentClassroom.getClassroomId());
            pstmtNewAssignment.setString(3, currentClassroom.getClassroomName());
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
    }

    //where all assignments from a specific classroom are returned
    public List<String> getAssignments(User user, Classroom currentClassroom){
        List<String> assignmentNameList = new ArrayList<>();
        ArrayList<Assignment> assignmentsList = new ArrayList<>();
        String query = user.getIsTeacher() ? "SELECT * FROM assignments WHERE teacher = ? AND class_name = ? AND class_id = ?" : "SELECT * FROM assignments WHERE student = ? AND class_name = ? AND class_id = ?";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, currentClassroom.getClassroomName());
            stmt.setInt(3, currentClassroom.getClassroomId());
            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    String assignmentName = rs.getString("name");
                    String description = rs.getString("description");
                    double grade = rs.getDouble("grade");
                    String name = user.getIsTeacher() ? rs.getString("student") : rs.getString("teacher");
                    String dueDateString = rs.getString("due_date");
                    String work = rs.getString("work");
                    assignmentNameList.add(assignmentName + ", " + description + ", " + name + ", " + String.valueOf(grade) + ", " + dueDateString + ", " + work);
                    Assignment assignment = new Assignment(assignmentName, description, grade, name, dueDateString, work);
                    assignmentsList.add(assignment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        currentClassroom.setAssignments(assignmentsList);
        return assignmentNameList;
    }

    //where all assignments no matter the classroom are returned
    public List<Assignment> getAllAssignments(User user, Classroom currentClassroom){
        List<Assignment> assignmentsList = new ArrayList<>();
        String query = Objects.equals(null, currentClassroom) ? 
            (user.getIsTeacher() ? "SELECT * FROM assignments WHERE teacher = ?" : "SELECT * FROM assignments WHERE student = ?") 
            : (user.getIsTeacher() ? "SELECT * FROM assignments WHERE teacher = ? AND class_name = ? AND class_id = ?" : "SELECT * FROM assignments WHERE student = ? AND class_name = ? AND class_id = ?") ;
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            if(!Objects.equals(currentClassroom, null)){
                stmt.setString(2, currentClassroom.getClassroomName());
                stmt.setInt(3, currentClassroom.getClassroomId());
            }
            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    String assignmentName = rs.getString("name");
                    String description = rs.getString("description");
                    double grade = rs.getDouble("grade");
                    String name = user.getIsTeacher() ? rs.getString("student") : rs.getString("teacher");
                    String dueDateString = rs.getString("due_date");
                    String work = rs.getString("work");
                    Assignment assignment = new Assignment(assignmentName, description, grade, name, dueDateString, work);
                    assignmentsList.add(assignment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignmentsList;
    }

    //where a student can submit their work to an assignment 
    public void submitAssignment(User user, Classroom currentClassroom, String work, String assignmentName, String studentName){
		String updateAssignment = "UPDATE assignments SET work = ? WHERE class_name = ? AND class_id = ? AND name = ? AND student = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(updateAssignment)) {
            pstmt.setString(1, work);
            pstmt.setString(2, currentClassroom.getClassroomName());
            pstmt.setInt(3, currentClassroom.getClassroomId());
            pstmt.setString(4, assignmentName);
            pstmt.setString(5, studentName);
            pstmt.executeUpdate();      
        } catch (SQLException er) {
            er.printStackTrace(System.err);
        }
	}	
}
