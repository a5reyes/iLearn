package com.ilearn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public void addAssignment(User teacher, Classroom currentClassroom, String assignmentName, String description, double grade, String student, String dueDate) {
        String sql = "INSERT INTO assignments (teacher_id, class_id, class_name, name, description, grade, student, due_date, teacher) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, teacher.getId());
            pstmt.setInt(2, currentClassroom.getClassroomId());
            pstmt.setString(3, currentClassroom.getClassroomName());
            pstmt.setString(4, assignmentName);
            pstmt.setString(5, description);
            pstmt.setDouble(6, grade);
            pstmt.setString(7, student);
            pstmt.setString(8, dueDate);
            pstmt.setString(9, teacher.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    //this is where a teacher can assign assignments for classroom
    public void updateAssignment(User teacher, Classroom currentClassroom, Integer assignmentId, String assignmentName, String description, double grade, String student, String dueDate) {
        // update existing assignment
        String sql = "UPDATE assignments SET " +
                    "teacher_id = ?, class_id = ?, class_name = ?, name = ?, description = ?, grade = ?, student = ?, due_date = ?, teacher = ? " +
                    "WHERE assignment_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, teacher.getId());
            pstmt.setInt(2, currentClassroom.getClassroomId());
            pstmt.setString(3, currentClassroom.getClassroomName());
            pstmt.setString(4, assignmentName);
            pstmt.setString(5, description);
            pstmt.setDouble(6, grade);
            pstmt.setString(7, student);
            pstmt.setString(8, dueDate);
            pstmt.setString(9, teacher.getName());
            pstmt.setInt(10, assignmentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        
    }


    //where all assignments from a specific classroom are returned
    public ArrayList<Assignment> getAssignments(User user, Classroom currentClassroom){
        ArrayList<Assignment> assignmentsList = new ArrayList<>();
        String query = Objects.equals(null, currentClassroom) ? 
            (user.getIsTeacher() ? "SELECT * FROM assignments WHERE teacher = ?" : "SELECT * FROM assignments WHERE student = ?") 
            : (user.getIsTeacher() ? "SELECT * FROM assignments WHERE teacher = ? AND class_name = ? AND class_id = ?" : "SELECT * FROM assignments WHERE student = ? AND class_name = ? AND class_id = ?");
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            if(!Objects.equals(currentClassroom, null)){
                stmt.setString(2, currentClassroom.getClassroomName());
                stmt.setInt(3, currentClassroom.getClassroomId());
            }
            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    String name = user.getIsTeacher() ? rs.getString("student") : rs.getString("teacher");
                    String dueDateString = rs.getString("due_date");
                    String work = rs.getString("work");
                    Assignment assignment = new Assignment(rs.getInt("assignment_id"), rs.getInt("teacher_id"), rs.getString("name"), rs.getString("description"), rs.getDouble("grade"), name, dueDateString, work);
                    assignmentsList.add(assignment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignmentsList;
    }

    //where a student can submit their work to an assignment 
    public void submitAssignment(Classroom currentClassroom, Assignment assignment){
		String sql = "UPDATE assignments SET work = ? WHERE assignment_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, assignment.getWork());
            pstmt.setInt(2, assignment.getAssignmentId());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
	}	
}
