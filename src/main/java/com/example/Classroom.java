package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.iLearn.Main;

public class Classroom {
	private int classroomId;
	private String teacher;
	private String discussions; //set to dictionary<String, String>
	private String meetingTime;
	Connection connection = Main.connect();

	public Classroom(int classroomId, String teacher, String discussions, String meetingTime) {
		this.classroomId = classroomId;
		this.teacher = teacher;
		this.discussions = discussions;
		this.meetingTime = meetingTime;
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

	public String getDiscussions() {
		return discussions;
	}

	public void setDiscussions(String discussions) {
		this.discussions = discussions;
	}

	public String getMeetingTime() {
		return meetingTime;
	}

	public void setMeetingTime(String meetingTime) {
		this.meetingTime = meetingTime;
	}

	public void connectToDatabase() {
		/*
		try(Statement statement = connection.createStatement()){
		    String insertNewUser = "INSERT INTO users (id, name, password, isTeacher, classrooms) VALUES (?, ?, ?, ?, ?)";
		    PreparedStatement pstmtNewUser = connection.prepareStatement(insertNewUser);
		    pstmtNewUser.setInt(1, this.id);
		    pstmtNewUser.setString(2, this.name);
		    pstmtNewUser.setString(3, this.password);
		    pstmtNewUser.setInt(4, this.isTeacher ? 1 : 0);
		    pstmtNewUser.setString(5, String.join(",", this.classrooms));
		    pstmtNewUser.executeUpdate();
		} catch (SQLException er) {
		    er.printStackTrace(System.err);
		}
		
		}
		*/
	}
}
