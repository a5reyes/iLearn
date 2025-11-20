package com.ilearn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ilearn.Classroom;
import com.ilearn.Main;
import com.ilearn.User;

public class AssignmentDAO {
    private UserDAO userDAO = new UserDAO();
    Connection connection = Main.connect();
    public void addAssignment(User user, String currentClassroomInfo, String assignmentName, String description, double grade, String student, String dueDate){
        //for(Classroom classroomObj: classroomArr){
            //if(classroomObj.getClassroomName().equals(currentClassroomName)){
                //Assignment assignment = new Assignment(name, description, grade);
                //classroomObj.addAssignment(assignment);
        try(Statement statement = connection.createStatement()){
            String insertNewAssignment = "INSERT INTO assignments (user_id, class_id, class_name, name, description, grade, student, due_date, teacher) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmtNewAssignment = connection.prepareStatement(insertNewAssignment);
            pstmtNewAssignment.setInt(1, userDAO.getStudentIdFromDatabase(student));
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
        try {
            String query = user.getIsTeacher() ? "SELECT * FROM assignments WHERE teacher = ? AND class_name = ? AND class_id = ?" : "SELECT * FROM assignments WHERE student = ? AND class_name = ? AND class_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, user.getName());
            stmt.setString(2, currentClassroomInfo.split(", ")[0]);
            stmt.setInt(3, Integer.parseInt(currentClassroomInfo.split(", ")[1]));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");
                double grade = rs.getDouble("grade");
                String dueDateString = rs.getString("due_date");
                String work = rs.getString("work");
                assignmentNameList.add(name + ", " + description + ", " + String.valueOf(grade) + ", " + dueDateString + ", " + work);
            }
            System.out.println(assignmentNameList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignmentNameList;
            //}
        //}
    }

    public void submitAssignment(User user, String currentClassroomInfo, String work, String assignmentName){
		try(Statement statement = connection.createStatement()) {
            String updateAssignment = "UPDATE assignments SET work = ? WHERE user_id = ? AND class_name = ? AND class_id = ? AND name = ?";
            PreparedStatement pstmt = connection.prepareStatement(updateAssignment);
            pstmt.setString(1, work);
            pstmt.setInt(2, user.getId());
            pstmt.setString(3, currentClassroomInfo.split(", ")[0]);
            pstmt.setInt(4, Integer.parseInt(currentClassroomInfo.split(", ")[1]));
            pstmt.setString(5, assignmentName);
            pstmt.executeUpdate();      
        } catch (SQLException er) {
            er.printStackTrace(System.err);
        }
	}	
}
