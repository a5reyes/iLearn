package com.example;
import java.util.List;

import com.ilearn.Classroom;

public class Assignments extends Classroom {
    private List<String> assignments;
     
	public Assignments(List<String> assignments, int classroomId, String teacher, String discussions, String meetingTime) {
        super(classroomId, teacher, discussions, meetingTime);
        this.assignments = assignments;
    }

    public List<String> getAssignments() {
		return assignments;
	}
	public void setAssignments(List<String> assignments) {
		this.assignments = assignments;
	}

    public Boolean checkAssignment(String assignment) {
        boolean contains = this.assignments.contains(assignment);
        return contains;
	}

    public void addAssignment(String assignment){
        this.assignments.add(assignment);
    }

    public void removeAssignment(User teacher, String assignment){
        if(teacher.getIsTeacher()){
            if(checkAssignment(assignment)){
                int idx = this.assignments.indexOf(assignment);
                this.assignments.remove(idx); 
            }   
        }
    }

}
