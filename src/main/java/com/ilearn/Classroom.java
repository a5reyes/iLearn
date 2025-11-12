package com.ilearn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Classroom {
	private String classroomName;
    private int classroomId;
	private String teacher;
	private String[] discussions;
	private String meetingTime;
	ArrayList<Assignment> assignments = new ArrayList<>();
	Connection connection = Main.connect();
	
	public Classroom(String classroomName, int classroomId, String teacher, String[] discussions,  String meetingTime) {
		this.classroomName = classroomName;
		this.classroomId = classroomId;
		this.teacher = teacher;
		this.discussions = discussions;
		this.meetingTime = meetingTime;
	}

	public String getClassroomName() {
		return classroomName;
	}
	public void setClassroomId(String classroomName) {
		this.classroomName = classroomName;
	}

	public int getClassroomId() {
		return classroomId;
	}
	public void setClassroomId(int classroomId) {
		this.classroomId = classroomId;
	}
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public String[] getDiscussions() {
		return discussions;
	}
	public void setDiscussions(String[] discussions) {
		this.discussions = discussions;
	}
	public String getMeetingTime() {
		return meetingTime;
	}
	public void setMeetingTime(String meetingTime) {
		this.meetingTime = meetingTime;
	}

    public ArrayList<Assignment> getAssignments() {
		return assignments;
	}
	public void setAssignments(ArrayList<Assignment> assignments) {
		this.assignments = assignments;
	}

    public Boolean checkAssignment(Assignment assignment) {
        boolean contains = this.assignments.contains(assignment);
        return contains;
	}

    public void addAssignment(Assignment assignment){
        this.assignments.add(assignment);
    }

    public void removeAssignment(User teacher, Assignment assignment){
        if(teacher.getIsTeacher()){
            if(checkAssignment(assignment)){
                int idx = this.assignments.indexOf(assignment);
                this.assignments.remove(idx); 
            }   
        }
    }

	public void connectToDatabase(User user){
		try(Statement statement = connection.createStatement()){
			String checkIfInClassroom = "SELECT * FROM classrooms WHERE user_id = ? AND class_id = ? AND class_name = ?";
            PreparedStatement pstmtIfInClassroom = connection.prepareStatement(checkIfInClassroom);
			pstmtIfInClassroom.setInt(1, user.getId());
			pstmtIfInClassroom.setInt(2, this.classroomId);
			pstmtIfInClassroom.setString(3, this.classroomName);
            try (ResultSet rs = pstmtIfInClassroom.executeQuery()) {
                if (!rs.next()) {
                    String insertNewClassroom = "INSERT INTO classrooms (user_id, class_id, class_name, discussions, teacher, meeting_time) VALUES (?, ?, ?, ?, ?, ?)";
					PreparedStatement pstmtNewClassroom = connection.prepareStatement(insertNewClassroom);
					pstmtNewClassroom.setInt(1, user.getId());
					pstmtNewClassroom.setInt(2, this.classroomId);
					pstmtNewClassroom.setString(3, this.classroomName);
					pstmtNewClassroom.setString(4, String.join(",", this.discussions));
					pstmtNewClassroom.setString(5, this.teacher);
					pstmtNewClassroom.setString(6, this.meetingTime);
					pstmtNewClassroom.executeUpdate();
                }
            }
        } catch (SQLException er) {
            er.printStackTrace(System.err);
        }
	}	

	
}
