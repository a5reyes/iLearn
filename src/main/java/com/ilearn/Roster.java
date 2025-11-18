package com.ilearn;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Roster {
    private List<String> roster = new ArrayList<>();
    private Classroom classroom;
    Connection connection = Main.connect();

	Roster(Classroom classroom) {
        this.classroom = classroom;
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
