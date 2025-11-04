package com.example;
import java.util.List;

import com.iLearn.Classroom;

public class Roster extends Classroom {
    private List<String> roster;

	public Roster(List<String> roster, String classroomName, int classroomId, String teacher, String[] discussions, String meetingTime) {
		super(classroomName, classroomId, teacher, discussions, meetingTime);
        this.roster = roster;
	}

    public List<String> getRoster() {
		return roster;
	}

	public void setRoster(User user, List<String> roster) {
        this.roster = roster;
	}

    /*
    public Boolean getStudent(String student) {
        //get student info - name
        boolean contains = this.roster.contains(student);
        return contains;
	}
    */

    public Boolean checkStudent(String student) {
        boolean contains = this.roster.contains(student);
        return contains;
	}

	public void addStudent(String student) {
        if(!checkStudent(student)){
            this.roster.add(student); 
        }
	}

    //accessible to only teacher
	public void removeStudent(User teacher, String student) {
        if(teacher.getIsTeacher()){
            if(checkStudent(student)){
                int idx = this.roster.indexOf(student);
                this.roster.remove(idx); 
            }
        }
	}

}
