package com.ilearn;

import java.util.ArrayList;
import java.util.List;

import com.ilearn.dao.AssignmentDAO;

public class Classroom {
	private String classroomName;
    private int classroomId;
	private String teacher;
	private String[] discussions;
	private String meetingTime;
	ArrayList<Assignment> assignments = new ArrayList<>(); 
	private Roster roster;
	
	public Classroom(String classroomName, int classroomId, String teacher, String[] discussions,  String meetingTime) {
		this.classroomName = classroomName;
		this.classroomId = classroomId;
		this.teacher = teacher;
		this.discussions = discussions;
		this.meetingTime = meetingTime;
	}
	
	public Roster getRoster() {
		return roster;
	}

	public void setRoster(Roster roster) {
		this.roster = roster;
	}

	public String getClassroomName() {
		return classroomName;
	}
	public void setClassroomName(String classroomName) {
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

    public ArrayList<Assignment> getAssignments(User user, AssignmentDAO assignmentDAO) {
		assignments = assignmentDAO.getAssignments(user, this);
		return assignments;
	}

	public Assignment findAssignment(String assignmentName) {
		for(Assignment assignment : assignments){
			if(assignmentName.equals(assignment.getAssignmentName())){
				return assignment;
			}
		}
		return null;
	}

	public void submitAssignment(AssignmentDAO assignmentDAO, Assignment assignment, String studentWork){
		assignment.updateWork(studentWork);
		assignmentDAO.submitAssignment(this, assignment);
	}

    public void addAssignment(Assignment assignment){
        this.assignments.add(assignment);
    }

	public Boolean checkAssignment(Assignment assignment) {
        boolean contains = this.assignments.contains(assignment);
        return contains;
	}

	public List<String> asInfoList() {
		return List.of(
			"Name: " + this.getClassroomName(),
			"ID: " + this.getClassroomId(),
			"Discussions: " + String.join(", ", this.getDiscussions()),
			"Teacher: " + this.getTeacher(),
			"Meeting Time: " + this.getMeetingTime()
		);
	}

	@Override
    public String toString() {
        return classroomName + ", " + classroomId;
    }
}
