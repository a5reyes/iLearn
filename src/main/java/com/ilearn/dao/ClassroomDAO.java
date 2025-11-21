package com.ilearn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ilearn.Classroom;
import com.ilearn.User;

public class ClassroomDAO {
    private final Connection connection;
    
    public ClassroomDAO(Connection connection) {
        this.connection = connection; // share same connection across all files
    }
    //classroom objects are only created when a user is registered so if user logins, 
    //here we get their classrooms from classrooms table in sqlite db and then create a classroom object for each
    //then add to classroomsArr
    public void getClassrooms(User user){
        String query = "SELECT * FROM classrooms WHERE user_id = ?";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, user.getId());
            try(ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    String name = rs.getString("class_name");
                    int id = rs.getInt("class_id");
                    String teacher = rs.getString("teacher");
                    String discussions = rs.getString("discussions");
                    String meetingTime = rs.getString("meeting_time");
                    Classroom course = new Classroom(name, id, teacher, discussions.split(","), meetingTime);
                    user.addClassroom(course);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //view user's classrooms names & ids from sqlite db
    public List<String> viewClassroomsNamesIds(User user){
        List<String> classes = new ArrayList<>();
        String query = "SELECT class_name, class_id FROM classrooms WHERE user_id = ?";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, user.getId());
            try(ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    String name = rs.getString("class_name");
                    int id = rs.getInt("class_id");
                    classes.add(name + ", " + id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }

    //view a classroom's info - discussions, teacher, meeting-time etc. from sqlite db classrooms table
    public List<String> viewClassroomInfo(Classroom currentClassroom) {
        List<String> classInfo = new ArrayList<>();
        String query = "SELECT class_name, class_id, discussions, teacher, meeting_time FROM classrooms WHERE class_name = ? OR class_id = ?";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, currentClassroom.getClassroomName());
            stmt.setInt(2, currentClassroom.getClassroomId());
            try(ResultSet rs = stmt.executeQuery()){
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classInfo;
    }


    //view a classroom's roster from sqlite db rosters table
    public List<String> viewClassroomRoster(Classroom currentClassroom) {
        List<String> rosterArr = new ArrayList<>();
        String query = "SELECT username FROM rosters WHERE class_name = ? AND class_id = ?";
        try(PreparedStatement stmt = connection.prepareStatement(query)) {  
            stmt.setString(1, currentClassroom.getClassroomName());
            stmt.setInt(2, currentClassroom.getClassroomId());
            try(ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    String name = rs.getString("username");
                    rosterArr.add(name);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rosterArr;
    }

    // Fetches and inserts classroom data depending on the presence of exisitng data
	public void connectToDatabase(User user, Classroom classroom){
        String checkIfInClassroom = "SELECT * FROM classrooms WHERE user_id = ? AND class_id = ? AND class_name = ?";
		try(PreparedStatement pstmtIfInClassroom = connection.prepareStatement(checkIfInClassroom)){
			pstmtIfInClassroom.setInt(1, user.getId());
			pstmtIfInClassroom.setInt(2, classroom.getClassroomId());
			pstmtIfInClassroom.setString(3, classroom.getClassroomName());
            try (ResultSet rs = pstmtIfInClassroom.executeQuery()) {
                if (!rs.next()) {
                    String insertNewClassroom = "INSERT INTO classrooms (user_id, class_id, class_name, discussions, teacher, meeting_time) VALUES (?, ?, ?, ?, ?, ?)";
					try(PreparedStatement pstmtNewClassroom = connection.prepareStatement(insertNewClassroom)){
                        pstmtNewClassroom.setInt(1, user.getId());
                        pstmtNewClassroom.setInt(2, classroom.getClassroomId());
                        pstmtNewClassroom.setString(3, classroom.getClassroomName());
                        pstmtNewClassroom.setString(4, String.join(",", classroom.getDiscussions()));
                        pstmtNewClassroom.setString(5, classroom.getTeacher());
                        pstmtNewClassroom.setString(6, classroom.getMeetingTime());
                        pstmtNewClassroom.executeUpdate();
                    }
                }
            }
        } catch (SQLException er) {
            er.printStackTrace(System.err);
        }
	}	

}
