package com.ilearn;
import java.util.List;

public class Assignments extends Classroom {
    private List<String> assignments;
     
	public Assignments(List<String> assignments, String classroomName, int classroomId, String teacher,
        String[] discussions, String meetingTime) {
        super(classroomName, classroomId, teacher, discussions, meetingTime);
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
