package com.example;
public class Classroom {
    private int classroomId;
	private String teacher;
	private String discussions;
	private String meetingTime;
	
	public Classroom(int classroomId, String teacher, String discussions,  String meetingTime) {
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
	
	
}
